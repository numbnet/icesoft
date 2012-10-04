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

import javax.servlet.http.HttpServletRequest;

public class SXUtils {
    
    /**
     * Get the SX Register URL. 
     * 
     * Format is icemobile://c=register&r=<current-url>&JSESSIONID=<session-id>&u=<upload-url>
     * @param request The servlet request
     * @return The escaped SX register URL.
     */
    public static String getRegisterSXURL(HttpServletRequest request){
        String redirectParm = "&r=" + Utils.getBaseURL(request);
        String forward = (String)request.getAttribute("javax.servlet.forward.servlet_path");
        if( forward == null ){
            forward = "";
        }
        else if( forward.startsWith("/")){
            forward = forward.substring(1);
        }
        String params = "";
        if( request.getQueryString() != null ){
            params = "?"+request.getQueryString();
        }
        redirectParm += forward + params;
        String jsessionParam = "&JSESSIONID="+request.getSession().getId();
        String uploadParam = "&u="+ getUploadURL(request);
        String url = "icemobile://c=register"+redirectParm+jsessionParam+uploadParam;
        return url;
    }
 
    private static String getUploadPath(HttpServletRequest request) {
        String upPath = request.getContextPath() + "/icemobile";
        return upPath;
    }

    private static String getUploadURL(HttpServletRequest request) {
        String serverName = request.getHeader("x-forwarded-host");
        if (null == serverName) {
            serverName = request.getServerName() + ":" +
                request.getServerPort();
        }
        return "http://" + serverName +
            getUploadPath(request);
    }


}
