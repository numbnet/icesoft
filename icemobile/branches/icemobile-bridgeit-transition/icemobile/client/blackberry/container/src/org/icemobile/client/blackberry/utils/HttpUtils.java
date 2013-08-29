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


public class HttpUtils {
    

    /*
     * Decode a url-encoded string.
     */
    public static String URLdecode( String str ) {
        
        StringBuffer result = new StringBuffer();
        int len = str.length();
        char c1, c2; 
        for ( int i = 0; i < len; ++i ) { 
            char c = str.charAt( i );
            if ( c == '%' && i + 2 < len ) { 
                c1 = str.charAt( i + 1 );
                c2 = str.charAt( i + 2 );
                if ( isHexEntity( c1 ) && isHexEntity( c2 ) ) { 
                    result.append( (char) ( intValue( c1 ) * 16 + intValue( c2 ) ) );
                    i += 2;
                } else { 
                    result.append( c );
                } 
            } else { 
                result.append( c );
            }
        }
        return result.toString();
    }

    private static  boolean isHexEntity( char chr ) {
        String legalChars = "0123456789abcdefABCDEF";
        return ( legalChars.indexOf( chr ) != -1 );
    }

    private static int intValue( char chr ) {
        if ( chr >= '0' && chr <= '9' ) { 
            return chr - '0';
        } 
        if ( chr >= 'a' && chr <= 'f' ) { 
            return chr - 'a' + 10;
        } 
        if ( chr >= 'A' && chr <= 'F' ) { 
            return chr - 'A' + 10;
        } 
        return 0; 
        
    }

    
    /**
     * Encode a string
     *
     * @param original The string to be encoded
     * @return The encoded string
     */
    public static String URLencode (String original) { 

        StringBuffer returnVal = new StringBuffer();
        int len = original.length();
        int chr; 
        for (int i = 0; i < len; i++) {
            
            chr = original.charAt(i);
            if ('A' <= chr && chr <= 'Z') {               
                returnVal.append((char)chr);
            } else if ('a' <= chr && chr <= 'z') {        
                returnVal.append((char)chr);
            } else if ('0' <= chr && chr <= '9') {        
                returnVal.append((char)chr);
            } else if (chr == ' ') {                  
                returnVal.append("%20");
            } else if (chr <= 0x007f) {             
                returnVal.append(hexChars[chr]);
            } else if (chr <= 0x07FF) {             
                // non-ASCII <= 0x7FF
                returnVal.append(hexChars[0xc0 | (chr >> 6)]);
                returnVal.append(hexChars[0x80 | (chr & 0x3F)]);
            } else {                                  
                // 0x7FF < ch <= 0xFFFF
                returnVal.append(hexChars[0xe0 | (chr >> 12)]);
                returnVal.append(hexChars[0x80 | ((chr >> 6) & 0x3F)]);
                returnVal.append(hexChars[0x80 | (chr & 0x3F)]);
            }
        }
        return returnVal.toString();
    }
    
    final static String[] hexChars = {
        "%00", "%01", "%02", "%03", "%04", "%05", "%06", "%07",
        "%08", "%09", "%0a", "%0b", "%0c", "%0d", "%0e", "%0f",
        "%10", "%11", "%12", "%13", "%14", "%15", "%16", "%17",
        "%18", "%19", "%1a", "%1b", "%1c", "%1d", "%1e", "%1f",
        "%20", "%21", "%22", "%23", "%24", "%25", "%26", "%27",
        "%28", "%29", "%2a", "%2b", "%2c", "%2d", "%2e", "%2f",
        "%30", "%31", "%32", "%33", "%34", "%35", "%36", "%37",
        "%38", "%39", "%3a", "%3b", "%3c", "%3d", "%3e", "%3f",
        "%40", "%41", "%42", "%43", "%44", "%45", "%46", "%47",
        "%48", "%49", "%4a", "%4b", "%4c", "%4d", "%4e", "%4f",
        "%50", "%51", "%52", "%53", "%54", "%55", "%56", "%57",
        "%58", "%59", "%5a", "%5b", "%5c", "%5d", "%5e", "%5f",
        "%60", "%61", "%62", "%63", "%64", "%65", "%66", "%67",
        "%68", "%69", "%6a", "%6b", "%6c", "%6d", "%6e", "%6f",
        "%70", "%71", "%72", "%73", "%74", "%75", "%76", "%77",
        "%78", "%79", "%7a", "%7b", "%7c", "%7d", "%7e", "%7f",
        "%80", "%81", "%82", "%83", "%84", "%85", "%86", "%87",
        "%88", "%89", "%8a", "%8b", "%8c", "%8d", "%8e", "%8f",
        "%90", "%91", "%92", "%93", "%94", "%95", "%96", "%97",
        "%98", "%99", "%9a", "%9b", "%9c", "%9d", "%9e", "%9f",
        "%a0", "%a1", "%a2", "%a3", "%a4", "%a5", "%a6", "%a7",
        "%a8", "%a9", "%aa", "%ab", "%ac", "%ad", "%ae", "%af",
        "%b0", "%b1", "%b2", "%b3", "%b4", "%b5", "%b6", "%b7",
        "%b8", "%b9", "%ba", "%bb", "%bc", "%bd", "%be", "%bf",
        "%c0", "%c1", "%c2", "%c3", "%c4", "%c5", "%c6", "%c7",
        "%c8", "%c9", "%ca", "%cb", "%cc", "%cd", "%ce", "%cf",
        "%d0", "%d1", "%d2", "%d3", "%d4", "%d5", "%d6", "%d7",
        "%d8", "%d9", "%da", "%db", "%dc", "%dd", "%de", "%df",
        "%e0", "%e1", "%e2", "%e3", "%e4", "%e5", "%e6", "%e7",
        "%e8", "%e9", "%ea", "%eb", "%ec", "%ed", "%ee", "%ef",
        "%f0", "%f1", "%f2", "%f3", "%f4", "%f5", "%f6", "%f7",
        "%f8", "%f9", "%fa", "%fb", "%fc", "%fd", "%fe", "%ff"
    };

}
