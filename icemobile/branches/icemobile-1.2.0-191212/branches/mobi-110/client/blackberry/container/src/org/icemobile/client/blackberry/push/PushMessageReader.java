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

import java.io.ByteArrayInputStream;

import javax.microedition.io.Connection;

import org.icemobile.client.blackberry.ICEmobileContainer;

import net.rim.device.api.io.Base64InputStream;
import net.rim.device.api.io.http.HttpServerConnection;
import net.rim.device.api.io.http.PushInputStream;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.util.Arrays;

/**
 * Reads incoming push messages and extracts texts and images.
 */
public final class PushMessageReader {

    // HTTP header property that carries unique push message ID
    private static final String MESSAGE_ID_HEADER = "Push-Message-ID";
    // content type constant for text messages
    private static final String MESSAGE_TYPE_TEXT = "text";
    // content type constant for image messages
    private static final String MESSAGE_TYPE_IMAGE = "image";

    private static final int MESSAGE_ID_HISTORY_LENGTH = 10;
    private static String[] messageIdHistory = new String[MESSAGE_ID_HISTORY_LENGTH];
    private static byte historyIndex;

    private static byte[] buffer = new byte[15 * 1024];
    private static byte[] imageBuffer = new byte[10 * 1024];

    /**
     * Utility classes should have a private constructor.
     */
    private PushMessageReader() {
    }

    /**
     * Reads the incoming push message from the given streams in the current thread and notifies controller to display the information.
     * 
     * @param piStream the Push Input Stream
     *            
     * @param conn The Push Connection
     */
    public static void process(PushInputStream piStream, Connection conn) {

        try {

            HttpServerConnection httpConn;
            if (conn instanceof HttpServerConnection) {
                httpConn = (HttpServerConnection) conn;
            } else {
                throw new IllegalArgumentException("Can not process non-http pushes, expected HttpServerConnection but have "
                        + conn.getClass().getName());
            }

            String msgId = httpConn.getHeaderField(MESSAGE_ID_HEADER);
            String msgType = httpConn.getType();
            String encoding = httpConn.getEncoding();

//            ICEmobileContainer.DEBUG("Message props: ID=" + msgId + ", Type=" + msgType + ", Encoding=" + encoding);
            ICEmobileContainer.showNotificationIcon(true);

            boolean accept = true;
            if (!alreadyReceived(msgId)) {
                byte[] binaryData;

                if (msgId == null) {
                    msgId = String.valueOf(System.currentTimeMillis());
                }

                if (msgType == null) {
                    ICEmobileContainer.DEBUG("Message content type is NULL");
                    accept = false;
                } else if (msgType.indexOf(MESSAGE_TYPE_TEXT) >= 0) {
                    
                    // a string 
                    int size = piStream.read(buffer);
                    binaryData = new byte[size];
                    System.arraycopy(buffer, 0, binaryData, 0, size);					
                    ICEmobileContainer.DEBUG("icePush - Text message received: " + new String (binaryData));
                    
                } else if (msgType.indexOf(MESSAGE_TYPE_IMAGE) >= 0) {
                    // an image in binary or Base64 encoding
                    int size = piStream.read(buffer);
                    ICEmobileContainer.DEBUG("icePush - image message received");
                    if (encoding != null && encoding.equalsIgnoreCase("base64")) {
                        // image is in Base64 encoding, decode it
                        Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(buffer, 0, size));
                        size = bis.read(imageBuffer);
                    }
                    binaryData = new byte[size];
                    System.arraycopy(buffer, 0, binaryData, 0, size);					
                    // TODO report message
                } else {
                    ICEmobileContainer.DEBUG("Unknown message type " + msgType);
                    accept = false;
                }
            } else {
                ICEmobileContainer.DEBUG("Received duplicate message with ID " + msgId);
            }
            piStream.accept();
            
        } catch (Exception e) {
            ICEmobileContainer.DEBUG("Failed to process push message: " + e);
        } finally {
            PushAgent.close(conn, piStream, null);
        }
    }

    /**
     * Check whether the message with this ID has been already received.
     * 
     * @param id
     *            the id
     * @return true, if successful
     */
    private static boolean alreadyReceived(String id) {
        if (id == null) {
            return false;
        }

        if (Arrays.contains(messageIdHistory, id)) {
            return true;
        }

        // new ID, append to the history (oldest element will be eliminated)
        messageIdHistory[historyIndex++] = id;
        if (historyIndex >= MESSAGE_ID_HISTORY_LENGTH) {
            historyIndex = 0;
        }
        return false;
    }

}