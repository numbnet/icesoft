/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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
package org.icemobile.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class Utils {
    
    private static Logger logger = Logger.getLogger(Utils.class.getName());
    
    public static final DateFormat HTTP_DATE =
            new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);


    public static Cookie getCookie(String name, HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        for( int i=0 ; i < cookies.length ; i++ ){
            if( cookies[i].getName().equalsIgnoreCase(name)){
                return cookies[i];
            }
        }
        return null;
    }
    
    public static void copyStream(InputStream in, OutputStream out) throws IOException {
        byte[] buf = new byte[1000];
        int l = 1;
        while (l > 0) {
            l = in.read(buf);
            if (l > 0) {
                out.write(buf, 0, l);
            }
        }
    }
    public static int copyStream(InputStream in, OutputStream out,
            int start, int end) throws IOException {
        long skipped = in.skip((long) start);
        if (start != skipped)  {
            throw new IOException("copyStream failed range start " + start);
        }
        byte[] buf = new byte[1000];
        int pos = start - 1;
        int count = 0;
        int l = 1;
        while (l > 0) {
            l = in.read(buf);
            if (l > 0) {
                pos = pos + l;
                if (pos > end)  {
                    l = l - (pos - end);
                    out.write(buf, 0, l);
                    count += l;
                    break;
                }
                out.write(buf, 0, l);
                count += l;
            }
        }
        return count;
    }
    
    public static String getUserAgent(HttpServletRequest request){
        return request.getHeader("User-Agent");
    }

    /**
     * Get the base URL for the request. 
     * 
     * The base URL will include the scheme, server name, port, and 
     * application context, but not the page, or servlet path, 
     * or query string. The returned URL will include a trailing slash.
     * eg. http://server:8080/myapp/
     * 
     * @param request The ServletRequest
     * @return The base URL.
     */
    public static String getBaseURL(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String serverName = httpRequest.getHeader("x-forwarded-host");
        if (null == serverName) {
            serverName = httpRequest.getServerName() + ":" +
                httpRequest.getServerPort();
        }
        return httpRequest.getScheme() + "://" + serverName +
            httpRequest.getContextPath() + "/";
    }



}
