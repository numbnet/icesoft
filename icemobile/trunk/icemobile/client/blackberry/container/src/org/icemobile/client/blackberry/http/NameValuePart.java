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

import org.icemobile.client.blackberry.utils.NameValuePair;

public class NameValuePart extends Part {

    private NameValuePair value; 
    private int partLength;

    public NameValuePart(String name, String value ) {

        super();
        super.setDisposition( "Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n");
        this.value = new NameValuePair( name, value );
    }


    public void write( OutputStream os ) throws Exception { 

        super.write(os);	

        ByteArrayOutputStream bos = new ByteArrayOutputStream();		
        bos.write(value.getValue().getBytes() );
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
