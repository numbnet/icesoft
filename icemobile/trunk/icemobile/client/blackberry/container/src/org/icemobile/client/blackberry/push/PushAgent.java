/*
 * Copyright (c) 2011, ICEsoft Technologies Canada Corp.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, 
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, 
 * this list of conditions and the following disclaimer in the documentation 
 * and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, 
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR 
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 */ 
package org.icemobile.client.blackberry.push;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.ServerSocketConnection;

import org.icemobile.client.blackberry.ICEmobileContainer;

import net.rim.device.api.io.IOUtilities;
import net.rim.device.api.io.http.HttpServerConnection;
import net.rim.device.api.io.http.MDSPushInputStream;
import net.rim.device.api.io.http.PushInputStream;
import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

/**
 * Registers for and receives bis push messages.<br/>
 * 
 */
public class PushAgent {

    private static final String REGISTER_SUCCESSFUL = "rc=200";
    private static final String DEREGISTER_SUCCESSFUL = REGISTER_SUCCESSFUL;
    private static final String USER_ALREADY_SUBSCRIBED = "rc=10003";
    private static final String ALREADY_UNSUSCRIBED_BY_USER = "rc=10004";
    private static final String ALREADY_UNSUSCRIBED_BY_PROVIDER = "rc=10005";

    private static final String PUSH_PORT = "29785";
    private static final String BPAS_URL = "http://pushapi.eval.blackberry.com";
    private static final String APP_ID = "1868-72162e8r2m8tr292r9D3o2i7890921k9s66";
    private static final String WAP2_SUFFIX = ";deviceside=true;ConnectionUID=";
    private static final String HTTP_SUFFIX = ";deviceside=true";
    private static final String BIS_SUFFIX = ";deviceside=true;ConnectionUID=";
    private static final String MDS_SUFFIX = ";deviceside=false;";
    private static final String WIFI_SUFFIX = ";deviceside=true;interface=wifi";
    private static final String TEST_FREENESS_SUFFIX = ";deviceside=false;ConnectionType=mds-public";
    private String mConnectionSuffix;

    private boolean isWAP2 = false; 

    private MessageReadingThread messageReadingThread;
    private boolean isRegistered;

    private String mUID; 

    /**
     * Instantiates a new push agent.
     * 
     */
    public PushAgent() {
        // remove the coverage check if compiling for OS < 4.6
        if (!CoverageInfo.isCoverageSufficient(CoverageInfo.COVERAGE_BIS_B)) {
            ICEmobileContainer.ERROR("BIS-B Push Coverage insufficient for registration");
            return;
        }
        if (DeviceInfo.isSimulator()) {
            ICEmobileContainer.ERROR("Push Registration can only be done for real devices");
            return;
        }
        //		mConnectionSuffix = WAP2_SUFFIX;
        mConnectionSuffix = TEST_FREENESS_SUFFIX;

        ServiceBook sb = ServiceBook.getSB(); 
        ServiceRecord[] records = sb.findRecordsByCid("WPTCP"); 

        for (int i = 0; i < records.length; i++) { 

            // Search through all service records to find the valid 
            // non WI-FI and non-mms 
            // WAP 2.0 Gateway service records 
            if (records[i].isValid() && !records[i].isDisabled()) { 
                //				BlackberryContainer.DEBUG("Service Book entry: " + records[i].getDescription());
                String checkUID = records[i].getUid(); 
                if ( checkUID != null && checkUID.length() > 0 ) { 

                    if ( (checkUID.toLowerCase().indexOf("wifi") == -1) && 
                            (checkUID.toLowerCase().indexOf("mms") == -1)) { 
                        //						mConnectionSuffix += checkUID; 
//                        ICEmobileContainer.DEBUG("WAP2 Book entry found: " + checkUID);
                        break;
                    }
                }					
            }			
        }
        messageReadingThread = new MessageReadingThread();
        messageReadingThread.start();
        registerBpas();
    }

    public void shutdown() { 

        if (isRegistered) { 
            deregisterBpas(); 
        }

        if (messageReadingThread != null && messageReadingThread.running) { 
            messageReadingThread.stopRunning(); 
        }

        messageReadingThread = null;		
    }

    /**
     * Thread that processes incoming connections through {@link PushMessageReader}.
     */
    private class MessageReadingThread extends Thread {

        private boolean running = true;
        private ServerSocketConnection socket;
        private HttpServerConnection conn;
        private InputStream inputStream;
        private PushInputStream pushInputStream;

        /**
         * Instantiates a new message reading thread.
         */
        public MessageReadingThread() {
        }

        /**
         * {@inheritDoc}
         * 
         * @see java.lang.Thread#run()
         */
        public void run() {
            String url = "http://:" + PUSH_PORT  + mConnectionSuffix;
//            ICEmobileContainer.DEBUG("Listening for push messages through '" + url + "'");

            try {
                socket = (ServerSocketConnection) Connector.open(url);
            } catch (IOException ex) {
                // can't open the port, probably taken by another application
                onListenError(ex);
            }

            ICEmobileContainer.DEBUG("Push Reader ready for connection ");
            while (running) {
                try {
                    
                    Object o = socket.acceptAndOpen();                    
                    conn = (HttpServerConnection) o;
                    inputStream = conn.openInputStream();
                    pushInputStream = new MDSPushInputStream(conn, inputStream);
                    PushMessageReader.process(pushInputStream, conn);
                    ICEmobileContainer.DEBUG("Message processed");
                } catch (Exception e) {
                    ICEmobileContainer.ERROR("Exception reading push message: " + e);
                    if (running) {
                        running = false;
                    }
                } finally {
                    close(conn, pushInputStream, null);
                }
            }
            ICEmobileContainer.DEBUG("Stopped listening for push messages");
        }

        /**
         * Stop running.
         */
        public void stopRunning() {
            
            ICEmobileContainer.DEBUG("pushAgent - Stop called");
            running = false;
            close(socket, null, null);
        }

        /**
         * On listen error.
         * 
         * @param ex
         */
        private void onListenError(final Exception ex) {
            ICEmobileContainer.ERROR("pushAgent - Exception opening local port: " + ex);
        }
    }

    /**
     * Safely closes connection and streams.
     *
     */
    public static void close(Connection conn, InputStream is, OutputStream os) {
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
            }
        }
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Form a register request.
     */
    private String formRegisterRequest(String bpasUrl, String appId, String token) {
        StringBuffer sb = new StringBuffer(bpasUrl);
        sb.append("/mss/PD_subReg?");
        sb.append("serviceid=").append(appId);
        sb.append("&osversion=").append(DeviceInfo.getSoftwareVersion());
        sb.append("&model=").append(DeviceInfo.getDeviceName());
        if (token != null && token.length() > 0) {
            sb.append("&").append(token);
        }
        return sb.toString();
    }

    /**
     * Form an unregister request.
     *
     */
    private String formUnRegisterRequest(String bpasUrl, String appId, String token) {
        StringBuffer sb = new StringBuffer(bpasUrl);
        sb.append("/mss/PD_subDereg?");
        sb.append("serviceid=").append(appId);
        if (token != null && token.length() > 0) {
            sb.append("&").append(token);
        }
        return sb.toString();
    }

    /**
     * Register to the BPAS.
     */
    private void registerBpas() {

        String url = null;

        url = formRegisterRequest(BPAS_URL, APP_ID, null)  + mConnectionSuffix;


        //		final String registerUrl = formRegisterRequest(BPAS_URL, APP_ID, null) + mConnectionSuffix;
        final String registerUrl = url;
        ICEmobileContainer.DEBUG("Opening initial URL: " + registerUrl);
        /**
         * As the connection suffix is fixed I just use a Thread to call the connection code
         * 
         **/
        new Thread() {
            public void run() {
                try {
                    HttpConnection httpConnection = (HttpConnection) Connector.open(registerUrl);
                    InputStream is = httpConnection.openInputStream();
                    String response = new String(IOUtilities.streamToBytes(is));
                    close(httpConnection, is, null);
                    String nextUrl = formRegisterRequest(BPAS_URL, APP_ID, response) + mConnectionSuffix;
                    ICEmobileContainer.DEBUG("Subsequent URL for registration: " + nextUrl);
                    HttpConnection nextHttpConnection = (HttpConnection) Connector.open(nextUrl);
                    InputStream nextInputStream = nextHttpConnection.openInputStream();
                    response = new String(IOUtilities.streamToBytes(nextInputStream));
                    close(nextHttpConnection, is, null);
                    if (REGISTER_SUCCESSFUL.equals(response)) {
                        ICEmobileContainer.DEBUG("Successfully registered to BIS push: " + response);

                    } else if ( USER_ALREADY_SUBSCRIBED.equals(response)) {					
                        ICEmobileContainer.DEBUG("Welcome back to BIS push: " + response);
                        isRegistered = true;
                    } else { 
                        ICEmobileContainer.DEBUG("BPAS rejected registration: " + response);
                    }
                } catch (IOException e) {
                    ICEmobileContainer.DEBUG("IOException on register() " + e );
                }
            }
        }.start();
    }

    /**
     * Register to the BPAS.
     */
    private void deregisterBpas() {
        final String deregisterUrl = formRegisterRequest(BPAS_URL, APP_ID, null) + mConnectionSuffix;
        /**
         * As the connection suffix is fixed I just use a Thread to call the connection code
         * 
         **/
        new Thread() {
            public void run() {
                try {
                    HttpConnection httpConnection = (HttpConnection) Connector.open(deregisterUrl);
                    InputStream is = httpConnection.openInputStream();
                    String response = new String(IOUtilities.streamToBytes(is));
                    close(httpConnection, is, null);
                    String nextUrl = formUnRegisterRequest(BPAS_URL, APP_ID, response) + mConnectionSuffix;
                    HttpConnection nextHttpConnection = (HttpConnection) Connector.open(nextUrl);
                    InputStream nextInputStream = nextHttpConnection.openInputStream();
                    response = new String(IOUtilities.streamToBytes(nextInputStream));
                    close(nextHttpConnection, is, null);
                    if (DEREGISTER_SUCCESSFUL.equals(response) || 
                            ALREADY_UNSUSCRIBED_BY_PROVIDER.equals(response) || 
                            ALREADY_UNSUSCRIBED_BY_USER.equals(response)) {
                        ICEmobileContainer.DEBUG("UNRegistered successfully from BIS pushservice");
                        isRegistered = false;
                    } else {
                        ICEmobileContainer.DEBUG("BPAS rejected DE-registration");
                    }
                } catch (IOException e) {
                    ICEmobileContainer.DEBUG("IOException on DEregister() " + e );
                }
            }
        }.start();
    }
}
