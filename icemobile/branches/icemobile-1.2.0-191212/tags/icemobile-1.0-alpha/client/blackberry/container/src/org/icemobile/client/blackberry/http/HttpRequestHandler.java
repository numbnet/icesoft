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
package org.icemobile.client.blackberry.http;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.microedition.io.HttpConnection;
import javax.microedition.io.InputConnection;

import org.icemobile.client.blackberry.ICEmobileContainer;

import net.rim.device.api.io.Base64OutputStream;
import net.rim.device.api.io.http.HttpHeaders;

public class HttpRequestHandler {

    private ICEmobileContainer mContainer; 

    public HttpRequestHandler (ICEmobileContainer container) { 
        mContainer = container;
    }

    /**
     * Form up some details and forward a request to the container. 
     * The Container (or more accurately, the original BrowserField) must 
     * make the request (rather than our doing so using HttpConnection 
     * directly) in order to maintain the JSessionId cookies during 
     * upload and to allow the BrowserField to properly utilize the 
     * Network transport infrastructure on the device. 
     * 
     * @param authToken An optional authentication token
     * @param requestData A MultipartPostData object containing a series of 
     *        Parts for the upload
     * @return returns the result string read from the input stream 
     */
    public String processAJAXRequest(String url,  
            String authToken, 
            MultipartPostData request) { 

        HttpConnection hc = null ;
        OutputStream os = null;

        String type = request.getContentType();		

        try {

            HttpHeaders headers = new HttpHeaders();
            headers.addProperty("Content-Type", type );
            headers.addProperty("Connection", " close" );
            headers.addProperty("Faces-Request", "partial/ajax" );
            //			
            if (authToken != null && authToken.length()>0) { 

                byte[] ByteArray=authToken.getBytes("UTF-8");
                String Encoded = 
                    Base64OutputStream.encodeAsString(
                            ByteArray,
                            0, 
                            ByteArray.length, 
                            false, 
                            false);
                headers.addProperty("Authorization",Encoded);
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();	
            request.write(bos);

            bos.flush();
            bos.close();

            // This doesn't lend itself very well to the progress meter. 
            InputConnection ic = 
                mContainer.postRequest( url, bos.toString(), headers);
            InputStream inputStream = ic.openInputStream();
            StringBuffer sb = new StringBuffer();

            int C;
            while( -1 != (C = inputStream.read())) { 
                sb.append((char)C);
            }
            return sb.toString();
        } catch (Exception e) {
            ICEmobileContainer.ERROR("Exception in HTTPRequest processing: " + e.getMessage());
        }
        return null;
    }
}
