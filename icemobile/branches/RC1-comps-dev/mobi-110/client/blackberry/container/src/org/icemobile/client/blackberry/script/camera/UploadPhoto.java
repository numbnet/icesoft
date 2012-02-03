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
package org.icemobile.client.blackberry.script.camera;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import org.icemobile.client.blackberry.ICEmobileContainer;
import org.icemobile.client.blackberry.http.HttpRequestHandler;
import org.icemobile.client.blackberry.http.MultipartPostData;
import org.icemobile.client.blackberry.utils.NameValuePair;
import org.icemobile.client.blackberry.utils.HttpUtils;
import org.icemobile.client.blackberry.utils.UploadUtilities;

import net.rim.device.api.script.ScriptableFunction;

public class UploadPhoto extends ScriptableFunction {

    private String photoFile; 
    private ICEmobileContainer _container; 

    public UploadPhoto( ICEmobileContainer container) { 

        _container = container; 
    }



    /**
     * The args  here should come from javascript
     */
    public  Object invoke( Object thiz, Object[] args) {


        /*
		// This argument code is working
		String serializedForm = (String) args[0];
//		boolean gotValue = true;
//		String[] result;

		//photoFile = _container.getTakenPhotoName();

		MultipartPostData content2 = new MultipartPostData(); 

		try {

			BasicNameValuePair[] params = UploadUtilities.getNameValuePairs(serializedForm, "=", "&");
			//        Log.e("PhotoShare", "NVP=" + params.length);
			for (int i=0; i<params.length; i++) {
				if (params[i].getName().indexOf("file") > -1) {
					String filename = HttpUtils.URLdecode(params[i].getValue()); 
					InputStream theImage;
					byte[] imageBytes;
					//Base64OutputStream base64OutputStream = null;
					FileConnection fconn = (FileConnection) Connector.open("file://" + filename);
//					FileConnection fconn = (FileConnection)Connector.open("file://" + photoPath);
					imageBytes = new byte[(int) fconn.fileSize()];
					theImage = fconn.openInputStream();
					theImage.read(imageBytes);
					ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream( imageBytes.length );
					byteArrayOutputStream.write( imageBytes );

			    	byteArrayOutputStream.flush();
			    	byteArrayOutputStream.close();					

			    	String name = HttpUtils.URLdecode(params[i].getName()); 
			    	String imageVar = byteArrayOutputStream.toString(); 
			    	content2.appendImagePart(name,
			    			    			filename, 
			    				        	imageVar );
			    	//content2.append(HttpUtils.URLdecode(params[i].getValue()), byteArrayOutputStream.toString() ); 

				} else {
					if (params[i].getName() == null || params[i].getName().equals("")) { 
						continue;
					}
					content2.append( HttpUtils.URLdecode(params[i].getName()), HttpUtils.URLdecode(params[i].getValue()));
				}
			}			

			// Todo: At some point, I'll do this in a thread to isolate the UI thread. 
//			HttpRequestHandler.ProcessPOSTRequest(mUrl, null, content2 ); 


		} catch (Throwable e) {
			int i = 0;
		}
         */ 
        return Boolean.TRUE;

    }

}
