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

import java.io.OutputStream;
import org.icemobile.client.blackberry.utils.NameValuePair;

public class MultipartPostData {

    private Part [] mParts = new Part[4]; 
    private NameValuePair[] mHeaders;
    private int headerIdx;
    private int partIdx;
    private String contentType; 
    private String boundary = "----WebKitFormBoundarymviarLhC7ENfxuAE";

    public MultipartPostData() { 

        contentType = "multipart/form-data; boundary="+ boundary;
    }

    /**
     * Append a name-value pair part 
     * @param name
     * @param value
     */
    public void append (String name, String value) { 
        if (name == null) { 
            throw new IllegalArgumentException ("Name cannot be null"); 
        }
        if (value == null) { 
            throw new IllegalArgumentException ("Value cannot be null"); 
        }
        Part newPart = new NameValuePart(name, value); 
        appendPart(newPart);
    }

    public Part[] getParts() { 
        return mParts;
    }

    /**
     * Convenience method to construct a new Image part for multipart posting 
     * and append it to the buffer of Parts
     * 
     * @param name 
     * @param filename
     * @param encodedImage
     */
    public void appendImagePart(String name, String filename, String encodedImage) { 
        if (name == null) { 
            throw new IllegalArgumentException ("Name cannot be null"); 
        }
        if (filename == null) { 
            throw new IllegalArgumentException ("Value cannot be null"); 
        }

        Part newPart = new ImagePart(name, filename, encodedImage ); 		
        appendPart( newPart );

    }

    /**
     * Convenience method that constructs a new video part for a multipart POST message 
     * and adds it to the list of Parts 
     */
    public void appendVideoPart(String name, String filename, String encodedVideo) { 
        if (name == null) { 
            throw new IllegalArgumentException ("Name cannot be null"); 
        }
        if (filename == null) { 
            throw new IllegalArgumentException ("Value cannot be null"); 
        }

        Part newPart = new VideoPart(name, filename, encodedVideo ); 		
        appendPart( newPart );

    }

    /**
     * Add a new Audio clip to a multi-part form
     * @param fieldName id of hidden field containing the data
     * @param filename local filename of audio file 
     * @param encodedClip Audio clip itself. 
     */
    public void appendAudioPart(String name, String filename, String encodedClip ) { 
        if (name == null) { 
            throw new IllegalArgumentException ("Name cannot be null"); 
        }
        if (filename == null) { 
            throw new IllegalArgumentException ("Value cannot be null"); 
        }

        Part newPart = new AudioPart(name, filename, encodedClip ); 		
        appendPart( newPart );	

    }

    private void append( NameValuePair pair ) { 
        if (headerIdx == mHeaders.length-1) { 
            NameValuePair [] temp = mHeaders; 
            mHeaders = new NameValuePair[temp.length *2]; 
            System.arraycopy(temp, 0, mHeaders, 0, temp.length);
        }
        mHeaders[headerIdx++] = pair;
    }

    public String getContentType() { 
        return contentType; 
    }

    private void appendPart( Part part ) { 
        if (partIdx == mParts.length-1) { 
            Part [] temp = mParts; 
            mParts = new Part[temp.length *2]; 
            System.arraycopy(temp, 0, mParts, 0, temp.length);
        }
        mParts[partIdx++] = part;
    }

    public void write(OutputStream os) { 
        try {
            //os.write( "Connection:close\r\n".getBytes());
            //os.write( "Accept:*/*\r\n".getBytes());
            //os.write(  "Faces-Request:partial/ajax\r\n".getBytes());
            //os.write( contentType.getBytes() );
            byte[] paddedB = ("\r\n--" + boundary + "\r\n").getBytes();
            for (int pdx = 0; pdx < mParts.length; pdx ++ ) { 
                if (mParts[pdx] != null) { 
                    os.write(paddedB);
                    mParts[pdx].write(os);							
                }		
            }	
            paddedB = ("\r\n--" + boundary + "--\r\n").getBytes();
            os.write(paddedB);


        } catch (Exception e) { 

        }		
    }
}
