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
package org.icemobile.client.blackberry.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import org.icemobile.client.blackberry.ICEmobileContainer;

public class UploadUtilities {


    /**
     * Get the name value pairs from an URLEncoded string like aaa=bbb&ccc=ddd
     * @param data The string to parse. 
     * @param delim1 The first delimiter, typically "=" 
     * @param delim2 The second delimiter, typically "&" 
     * @return Array of NameValuePair objects from parsed string
     */
    public static  NameValuePair[] getNameValuePairs(String data, String delim1, String delim2) {

        NameValuePair[] returnVal = new NameValuePair[0];
        if (!(data.indexOf(delim1) > -1) ) {
            return returnVal;
        } else {

            // break namevalue pairs up first
            String nameValuePairs[] = split(data, delim2);

            NameValuePair[] res = new NameValuePair[nameValuePairs.length];
            for (int i=0; i<nameValuePairs.length; i++) {

                String [] nameValue = split( nameValuePairs[i], delim1);

                if (nameValue.length == 2) {
                    res[i] = new NameValuePair(nameValue[0],nameValue[1]);
                } else {
                    res[i] = new NameValuePair(nameValue[0],"");
                }
            }		
            return res;
        }
    }
    /**
     * Split a string into a number of tokens based on a single delimiter
     * @param str String to parse 
     * @param delim The delimiter
     * @return Array of Strings 
     */
    public static String[] split (String str, String delim) { 

        Vector v = new Vector();
        int spos = 0; 

        int epos = str.indexOf( delim ); 
        String token;
        while (true) {
            // Catch the end case. Might be zero length, or might have to catch case when str= "aaa="
            if (epos == -1) { 
                if (str.length() > spos) { 
                    v.addElement(str.substring(spos));
                }
                break;
            }

            v.addElement(  str.substring(spos, epos) );			

            spos = epos + delim.length();
            epos = str.indexOf( delim, spos + 1);

        }		
        String[] returnVal = new String[ v.size() ]; 
        v.copyInto(returnVal);
        return returnVal;
    }
    
    /**
     * Get the set of name value pairs from an URLEncoded string like aaa=bbb&ccc=ddd, but 
     * in handy Hashtable form. NameValuePairs are added to the table using the 
     * name property
     * 
     * @param data The string to parse. 
     * @param delim1 The first delimiter, typically "=" 
     * @param delim2 The second delimiter, typically "&" 
     * @return Hashtable of NameValuePair objects from parsed string, keyed by pair name
     */
    public static Hashtable getNameValuePairHash(String data, String delim1, String delim2) {
        NameValuePair[] array = getNameValuePairs(data, delim1, delim2);
        Hashtable returnVal = new Hashtable();
        NameValuePair item; 
        for (int idx = 0; idx < array.length; idx ++ ) { 
            item = array[idx];
            returnVal.put( item.getName(), item);
        }
        return returnVal;        
    }

    /**
     * BlackBerry doesn't have a String.replace(str, str) function? 
     * @param original original String 
     * @param toReplace String to replace
     * @param replaceWith String to replace with
     * @return Amended string
     */
    public static String replace (String original, String toReplace, char[] replaceWith) {

        StringBuffer retVal = new StringBuffer();
        int epos = original.indexOf(toReplace);
        int spos = 0;

        while (epos > -1) {
            retVal.append( original.substring(spos, epos));
            for (int idx = 0; idx < replaceWith.length; idx ++) {
                retVal.append(replaceWith[idx] );
            }
            spos = epos + toReplace.length();
            epos = original.indexOf(toReplace, epos + toReplace.length() );
        }
        if (original.length() > spos) {
            retVal.append( original.substring(spos ));
        }

        return retVal.toString();
    }

    /**
     * Read a local file 
     * @param filename
     * @return The contents of said file
     */
    private String readFile( String filename ) throws IOException { 

        InputStream is = null; 
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        FileConnection fconn = null;

        try { 
            fconn = (FileConnection) Connector.open(filename);
            if (fconn.exists()) {
                is = fconn.openInputStream(); 
                byte buf[] = new byte[1024];
                int len; 
                while ((len = is.read(buf)) != -1) { 
                    outputStream.write(buf, 0, len); 
                } 

            }
        } catch (IOException ioe) { 
            ICEmobileContainer.ERROR("readFile - Exception opening file: " + ioe + ", path of file: " +
                    fconn.getPath());


        } finally { 
            if (is != null) { 
                is.close(); 
            }
            if (outputStream != null) { 
                outputStream.close();
            }
        }

        return outputStream.toString();
    }
}
