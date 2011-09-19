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
