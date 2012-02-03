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
