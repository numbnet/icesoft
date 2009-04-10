/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */
package com.icesoft.faces.push.server.arp;

import com.icesoft.faces.push.server.MessageService;
import com.icesoft.faces.push.server.ProductInfo;
import com.icesoft.net.HttpConnectionAcceptor;
import com.icesoft.net.IoHttpConnectionAcceptor;
import com.icesoft.net.NioHttpConnectionAcceptor;
import com.icesoft.net.HttpConnection;
import com.icesoft.net.messaging.MessageServiceClient;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.icefaces.push.server.ExecuteQueue;
import org.icefaces.push.server.SessionManager;

/**
 * <p>
 *   The <code>PushServer</code> class represents the "Push Server", which is an
 *   HTTP Server specifically tailored to handle the blocking asynchronous
 *   <code>XMLHttpRequest</code>s sent using the ICEfaces Bridge.
 * </p>
 */
public class PushServer {
    private static final String ANNOUNCEMENT_MESSAGE_TYPE = "Announcement";
    private static final Log LOG = LogFactory.getLog(PushServer.class);

    private final Map httpConnectionAcceptorMap = new HashMap();

    private boolean blocking;
    private boolean compression;
    private boolean persistent;
    private int port;

    private SessionManager sessionManager;
    private MessageService messageService;

    /**
     * <p>
     *   Constructs a <code>PushServer</code> with the specified
     *   <code>pushServerSettings</code>.
     * </p>
     *
     * @param      pushServerSettings
     *                 the settings for the <code>PushServer</code> to be
     *                 created.
     */
    public PushServer(
        final PushServerSettings pushServerSettings,
        final SessionManager sessionManager,
        final MessageService messageService) {

        setBlocking(pushServerSettings.isBlocking());
        setCompression(pushServerSettings.useCompression());
        setPersistent(pushServerSettings.isPersistent());
        setPort(pushServerSettings.getPort());
        this.sessionManager = sessionManager;
        this.messageService = messageService;
        HttpConnectionAcceptor.ReadHandler _readHandler =
            new HttpConnectionAcceptor.ReadHandler() {
                private final ExecuteQueue executeQueue = new ExecuteQueue();
                private final Map pendingReadHandlerMap = new HashMap();

                public void read(final HttpConnection httpConnection) {
                    if (pendingReadHandlerMap.containsKey(httpConnection)) {
                        ((ReadHandler)
                            pendingReadHandlerMap.remove(httpConnection)).
                                handle();
                    } else {
                        ReadHandler _readHandler =
                            new ReadHandler(executeQueue, PushServer.this) {
                                protected void park() {
                                    pendingReadHandlerMap.put(
                                        httpConnection, this);
                                    httpConnection.doneReading();
                                }
                            };
                        _readHandler.setHttpConnection(httpConnection);
                        _readHandler.handle();
                    }
                }
            };
        if (!blocking) {
            httpConnectionAcceptorMap.put(
                new Integer(port),
                new NioHttpConnectionAcceptor(port, _readHandler));
        } else {
            httpConnectionAcceptorMap.put(
                new Integer(port),
                new IoHttpConnectionAcceptor(port, _readHandler));
        }
    }

    public void cancelRequest(final Set iceFacesIdSet) {
        sessionManager.getRequestManager().pull(iceFacesIdSet);
    }

    /**
     * <p>
     *   Gets the HTTP connection acceptor of this <code>PushServer</code> that
     *   accepts HTTP connections from the specified <code>port</code>.
     * </p>
     *
     * @param      port
     *                 the port for which the HTTP connection acceptor is
     *                 requested.
     * @return     the HTTP connection acceptor or <code>null</code> if there is
     *             no HTTP connection acceptor for the specified
     *             <code>port</code>.
     */
    public HttpConnectionAcceptor getHttpConnectionAcceptor(final int port) {
        return
            (HttpConnectionAcceptor)
                httpConnectionAcceptorMap.get(new Integer(port));
    }

    /**
     * <p>
     *   Gets the port of this <code>PushServer</code>.
     * </p>
     *
     * @return     the port.
     * @see        #setPort(int)
     */
    public int getPort() {
        return port;
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    /**
     * <p>
     *   Checks to see if this <code>PushServer</code> is configured in
     *   blocking-mode.
     * </p>
     *
     * @return     <code>true</code> if configured in blocking-mode,
     *             <code>false</code> if not.
     * @see        #setBlocking(boolean)
     */
    public boolean isBlocking() {
        return blocking;
    }

    /**
     * <p>
     *   Checks to see if this <code>PushServer</code> is configured in
     *   persistent-mode.
     * </p>
     *
     * @return     <code>true</code> if configured in persistent-mode,
     *             <code>false</code> if not.
     * @see        #setPersistent(boolean)
     */
    public boolean isPersistent() {
        return persistent;
    }

    public boolean receiveBufferedEvents() {
        return true;
    }

    /**
     * <p>
     *   Sets the blocking-mode of this <code>PushServer</code> to the specified
     *   <code>blocking</code>.
     * </p>
     * <p>
     *   Please note that a restart of this <code>PushServer</code> is required
     *   in order for it to be in blocking-mode.
     * </p>
     *
     * @param      blocking
     *                 the new blocking-mode value.
     * @see        #isBlocking()
     * @see        #stop()
     * @see        #start()
     */
    public void setBlocking(final boolean blocking) {
        this.blocking = blocking;
    }

    /**
     * <p>
     *   Sets the compression-mode of this <code>PushServer</code> to the
     *   specified <code>compression</code>.
     * </p>
     *
     * @param      compression
     *                 the new compression-mode value.
     * @see        #useCompression()
     */
    public void setCompression(final boolean compression) {
        this.compression = compression;
    }

    /**
     * <p>
     *   Sets the persistent-mode of this <code>PushServer</code> to the
     *   specified <code>persistent</code>.
     * </p>
     *
     * @param      persistent
     *                 the new persistent-mode value.
     * @see        #isPersistent()
     */
    public void setPersistent(final boolean persistent) {
        this.persistent = persistent;
    }

    /**
     * <p>
     *   Sets the port of this <code>PushServer</code> to the specified
     *   <code>port</code>.
     * </p>
     * <p>
     *   Please note that a restart of this <code>PushServer</code> is required
     *   in order for it to start accepting HTTP connections on the new port.
     * </p>
     *
     * @param      port
     *                 the new port.
     * @throws     IllegalArgumentException
     *                 if the specified <code>port</code> is lesser than
     *                 <code>0</code> or greater than <code>65535</code>.
     * @see        #getPort()
     * @see        #stop()
     * @see        #start()
     */
    public void setPort(final int port)
    throws IllegalArgumentException {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Illegal port: " + port);
        }
        this.port = port;
    }

    /**
     * <p>
     *   Starts this <code>PushServer</code>.
     * </p>
     *
     * @see        #stop()
     */
    public void start() {
        if (LOG.isInfoEnabled()) {
            LOG.info("[1] Sending announcements...");
        }
        messageService.publish(
            PushServer.class.getName(),
            null,
            ANNOUNCEMENT_MESSAGE_TYPE,
            MessageServiceClient.PUSH_TOPIC_NAME);
        if (LOG.isInfoEnabled()) {
            LOG.info("    done.");
            LOG.info("[2] Waiting...");
        }
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException exception) {
            // do nothing.
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("    done.");
            LOG.info("[3] Start listening...");
        }
        HttpConnectionAcceptor[] _httpConnectionAcceptors =
            (HttpConnectionAcceptor[])
                httpConnectionAcceptorMap.values().
                    toArray(
                        new HttpConnectionAcceptor[
                            httpConnectionAcceptorMap.size()]);
        for (int i = 0; i < _httpConnectionAcceptors.length; i++) {
            _httpConnectionAcceptors[i].start();
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("    done.");
            LOG.info(
                ProductInfo.PRODUCT + " started!\r\n" +
                "        blocking-mode          : " + blocking + "\r\n" +
                "        persistent connections : " + persistent + "\r\n" +
                "        HTTP compression       : " + compression);
        }
    }

    /**
     * <p>
     *   Stops this <code>PushServer</code>.
     * </p>
     *
     * @see        #stop()
     */
    public void stop() {
        try {
            if (LOG.isInfoEnabled()) {
                LOG.info("Stopping " + ProductInfo.PRODUCT + "...");
            }
            HttpConnectionAcceptor[] _httpConnectionAcceptors =
                (HttpConnectionAcceptor[])
                    httpConnectionAcceptorMap.values().
                        toArray(
                            new HttpConnectionAcceptor[
                                httpConnectionAcceptorMap.size()]);
            for (int i = 0; i < _httpConnectionAcceptors.length; i++) {
                _httpConnectionAcceptors[i].requestStop();
            }
            httpConnectionAcceptorMap.clear();
            if (LOG.isInfoEnabled()) {
                LOG.info(ProductInfo.PRODUCT + " stopped!");
            }
        } catch (Throwable throwable) {
            LOG.error("ERROR!", throwable);
        }
    }

    /**
     * <p>
     *   Checks to see if this <code>PushServer</code> is configured in
     *   compression-mode.
     * </p>
     *
     * @return     <code>true</code> if configured in compression-mode,
     *             <code>false</code> if not.
     * @see        #setCompression(boolean)
     */
    public boolean useCompression() {
        return compression;
    }
}