/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletRequest;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class Utils {

    private static Logger logger = Logger.getLogger(Utils.class.getName());

    public static final Map<String, String> FILE_EXT_BY_CONTENT_TYPE = new HashMap<String, String>() {
        private static final long serialVersionUID = -8905491307471581114L;

        {
            put("video/mp4", ".mp4");
            put("audio/mp4", ".mp4");
            put("video/mpeg", ".mpg");
            put("video/mov", ".mov");
            put("video/3gpp", ".3gp");
            put("audio/wav", ".wav");
            put("audio/x-wav", ".wav");
            put("audio/x-m4a", ".m4a");
            put("audio/mpeg", ".mp3");
            put("audio/amr", ".amr");
            put("image/jpeg", ".jpg");
            put("image/jpg", ".jpg");
            put("image/png", ".png");
        }
    };
    
    private static final String DATE_FORMAT = "EEE, dd MMM yyyy HH:mm:ss zzz";

    public static final Map<String, String> CONTENT_TYPE_BY_FILE_EXT = new HashMap<String, String>() {
        {
            put(".mp4", "video/mp4");
            put(".mp4", "audio/mp4");
            put(".mpg", "video/mpeg");
            put(".mov", "video/mov");
            put(".3gp", "video/3gpp");
            put(".wav", "audio/wav");
            put(".wav", "audio/x-wav");
            put(".m4a", "audio/x-m4a");
            put(".mp3", "audio/mpeg");
            put(".amr", "audio/amr");
            put(".jpg", "image/jpeg");
            put(".jpg", "image/jpg");
            put(".png", "image/png");
        }
    };

    public static Cookie getCookie(String name, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equalsIgnoreCase(name)) {
                    return cookies[i];
                }
            }
        }
        return null;
    }

    public static void copyStream(InputStream in, OutputStream out)
            throws IOException {
        if( in != null && out != null ){
            byte[] buf = new byte[1000];
            int l = 1;
            while (l > 0) {
                l = in.read(buf);
                if (l > 0) {
                    out.write(buf, 0, l);
                }
            }
        }
        
    }

    public static int copyStream(InputStream in, OutputStream out, int start,
            int end) throws IOException {
        long skipped = in.skip((long) start);
        if (start != skipped) {
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
                if (pos > end) {
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

    public static String getUserAgent(HttpServletRequest request) {
        return request.getHeader("User-Agent");
    }

    /**
     * Method to simply check for the Asus transformer prime.
     * @param client ClientDescriptor for request
     * @return true if the Asus was detected
     */
    public static boolean isTransformerHack(ClientDescriptor client ) {
         String ua = client.getUserAgent();
         if (ua != null) {
             ua = ua.toLowerCase();
             return ua.contains( UserAgentInfo.TABLET_TRANSORMER_PRIME );
         }
         return false;
     }
    /**
     * Get the base URL for the request.
     * 
     * The base URL will include the scheme, server name, port, and application
     * context, but not the page, or servlet path, or query string. The returned
     * URL will include a trailing slash. eg. http://server:8080/myapp/
     * 
     * @param request
     *            The ServletRequest
     * @return The base URL.
     */
    public static String getBaseURL(ServletRequest request) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String serverName = httpRequest.getHeader("x-forwarded-host");
        if (null == serverName) {
            serverName = httpRequest.getServerName() + ":"
                    + httpRequest.getServerPort();
        }
        return httpRequest.getScheme() + "://" + serverName
                + httpRequest.getContextPath() + "/";
    }

    public static String getCloudPushId(HttpServletRequest request) {
        String cloudPushId = null;
        cloudPushId = (String) request.getSession().getAttribute(
                Constants.CLOUD_PUSH_KEY);
        return cloudPushId;
    }

    public static String getAcceptHeader(HttpServletRequest request) {
        String accept = request.getHeader(Constants.HEADER_ACCEPT);
        return accept == null ? accept : accept.toLowerCase();
    }

    public static boolean acceptContains(HttpServletRequest request,
            String contains) {
        boolean result = false;
        String accept = getAcceptHeader(request);
        if (accept != null) {
            result = accept.contains(contains);
        }
        return result;
    }

    public static void concatenateStyleClass(StringBuilder sb,
            String styleClass, boolean disabled, String disabledStr) {
        if (sb.length() > 0) {
            sb.append(' ');
        }
        sb.append(styleClass);
        if (disabled) {
            sb.append(' ');
            sb.append(styleClass);
            sb.append(disabledStr);
        }
    }

    public static int generateHashCode(Object value) {
        int hashCode = 0;
        if (value != null) {
            hashCode = value.toString().hashCode();
        }
        return hashCode;
    }
    
    public static DateFormat getHttpDateFormat(){
        return new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
    }

}
