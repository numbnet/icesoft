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
package org.icemobile.client.blackberry.http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.microedition.io.InputConnection;

import org.icemobile.client.blackberry.ContainerController;
import org.icemobile.client.blackberry.Logger;

import net.rim.device.api.io.Base64OutputStream;
import net.rim.device.api.io.http.HttpHeaders;

public class HttpRequestHandler {

    private ContainerController mController;

    public HttpRequestHandler(ContainerController controller) {
        mController = controller;
    }

    /**
     * Form up some details and forward a request to the server.
     * The  original BrowserField) must
     * make the request (rather than our doing so using HttpConnection
     * directly) in order to maintain the JSessionId cookies during
     * upload and to allow the BrowserField to properly utilize the
     * configured Network transport infrastructure on the device.
     *
     * @param authToken   An optional authentication token of the format 'username:password'
     * @param requestData A MultipartPostData object containing a series of
     *                    Parts for the upload
     * @return returns the result string read from the input stream
     */
    public String processAJAXRequest(String actionMethod, String authToken,
                                     MultipartPostData request) {

        String type = request.getContentType();

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.addProperty("Content-Type", type);
            headers.addProperty("Connection", " close");
            headers.addProperty("Faces-Request", "partial/ajax");
            //			
            if (authToken != null && authToken.length() > 0) {

                byte[] ByteArray = authToken.getBytes("UTF-8");
                String Encoded =
                    Base64OutputStream.encodeAsString(
                        ByteArray,
                        0,
                        ByteArray.length,
                        false,
                        false);
                headers.addProperty("Authorization", Encoded);
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            request.write(bos);

            bos.flush();
            bos.close();

            // This doesn't lend itself very well to the progress meter. 
            InputConnection ic =
                mController.postRequest(actionMethod, bos.toString(), headers);
            InputStream inputStream = ic.openInputStream();
            StringBuffer sb = new StringBuffer();

            int C;
            while (-1 != (C = inputStream.read())) {
                sb.append((char) C);
            }
            return sb.toString();
        } catch (Exception e) {
            Logger.ERROR("Exception in HTTPRequest processing: " + e.getMessage());
        }
        return null;
    }
}
