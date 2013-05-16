/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */ 
package org.icemobile.client.blackberry.push;

import java.io.ByteArrayInputStream;
import javax.microedition.io.Connection;
import org.icemobile.client.blackberry.ICEmobileContainer;
import org.icemobile.client.blackberry.Logger;
import net.rim.device.api.io.Base64InputStream;
import net.rim.device.api.io.http.HttpServerConnection;
import net.rim.device.api.io.http.PushInputStream;
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

            ICEmobileContainer.showNotificationIcon(true);

            if (!alreadyReceived(msgId)) {
                byte[] binaryData;

                if (msgId == null) {
                    msgId = String.valueOf(System.currentTimeMillis());
                }

                if (msgType == null) {
                    Logger.DEBUG("icePush - Message content type is NULL");
                } else if (msgType.indexOf(MESSAGE_TYPE_TEXT) >= 0) {
                    
                    // a string 
                    int size = piStream.read(buffer);
                    binaryData = new byte[size];
                    System.arraycopy(buffer, 0, binaryData, 0, size);					
                    Logger.DEBUG("icePush - Text message received: " + new String (binaryData));
                    
                } else if (msgType.indexOf(MESSAGE_TYPE_IMAGE) >= 0) {
                    // an image in binary or Base64 encoding
                    int size = piStream.read(buffer);
                    Logger.DEBUG("icePush - image message received");
                    if (encoding != null && encoding.equalsIgnoreCase("base64")) {
                        // image is in Base64 encoding, decode it
                        Base64InputStream bis = new Base64InputStream(new ByteArrayInputStream(buffer, 0, size));
                        size = bis.read(imageBuffer);
                    }
                    binaryData = new byte[size];
                    System.arraycopy(buffer, 0, binaryData, 0, size);					
                    // TODO report message
                } else {
                    Logger.DEBUG("icePush - Unknown message type " + msgType);
                }
            } else {
                Logger.DEBUG("icePush - Received duplicate message with ID " + msgId);
            }
            piStream.accept();
            
        } catch (Exception e) {
            Logger.DEBUG("Failed to process push message: " + e);
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