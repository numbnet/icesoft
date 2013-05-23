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
import java.io.OutputStream;


public class AudioPart extends Part {

    private String audioData;


    /**
     * 
     * @param fieldName Name of the field containing the image data
     * @param filename name of file on the device 
     * @param imageData The encoded audio data 
     
     */
    public AudioPart(String fieldName, String filename, String audioData ) { 
        super();
        super.setDisposition( "Content-Disposition: form-data; name=\"" + fieldName + "\"; filename=\"" + filename + "\"\r\n");
        super.setContentType( "Content-Type: audio/amr\r\n");
        super.setTransferEncoding("Content-Transfer-Encoding: binary\r\n\r\n");
        this.audioData = audioData;
    }

    /**
     * Write this audioPart to an outputStream
     */
    public void write( OutputStream os ) throws Exception { 

        super.write(os);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(); 

        if (audioData != null) { 
            bos.write (audioData.getBytes());
        }
        os.write( bos.toByteArray() );
    }
}
