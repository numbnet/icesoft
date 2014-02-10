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
