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


/**
 * Part class is a baseclass for concrete instances of media that may 
 * be added to a multipart form POST request
 */
public class Part {


    protected String contentDisposition; 
    protected String contentType; 
    protected String contentTransferEncoding; 

    private int partLength;



    public void setDisposition(String disposition) { 
        this.contentDisposition = disposition; 
    }

    public void setContentType (String contentType) { 
        this.contentType = contentType; 
    }

    public void setTransferEncoding(String transferEncoding) { 
        this.contentTransferEncoding = transferEncoding; 
    }


    /**
     * Write out the common fields of all multipart types. 
     * @param os OutputStream to write data to
     * @throws Exception 
     */
    public void write( OutputStream os ) throws Exception { 

        if (contentDisposition == null) { 
            throw new IllegalArgumentException("Disposition can't be null");
        }
        //		if (contentType == null) { 
        //			throw new IllegalArgumentException ("Content Type can't be null");
        //		}


        ByteArrayOutputStream bos = new ByteArrayOutputStream(); 

        bos.write(contentDisposition.getBytes());
        if (contentType != null) { 
            bos.write(contentType.getBytes() );
        }

        if (contentTransferEncoding != null) { 
            bos.write(contentTransferEncoding.getBytes());
        }

        partLength = bos.size();
        os.write( bos.toByteArray() );	

    }

    /**
     * Get size of part. Not calculated until part has been written
     * @return
     */
    public int getLength() { 
        return partLength; 
    }
}
