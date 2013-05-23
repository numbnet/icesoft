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
package org.icemobile.client.blackberry.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import org.icemobile.client.blackberry.Logger;

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
        while (true) {
            // Catch the end case. Might be zero length, or might have to catch case when str= "aaa="
            if (epos == -1) { 
                if (str.length() > spos) { 
                    v.addElement(str.substring(spos).trim());
                }
                break;
            }

            v.addElement(  str.substring(spos, epos).trim() );			

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
        	Logger.ERROR("readFile - Exception opening file: " + ioe + ", path of file: " +
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
