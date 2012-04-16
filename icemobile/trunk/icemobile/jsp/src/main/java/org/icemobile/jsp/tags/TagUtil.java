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
 *
 */
 
package org.icemobile.jsp.tags;

import javax.servlet.jsp.PageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;

import java.util.Arrays;
import java.io.Writer;
import java.io.IOException;
import java.net.URLEncoder;

public class TagUtil  {
    private static String USER_AGENT_COOKIE = "com.icesoft.user-agent";
    private static String HYPERBROWSER = "HyperBrowser";

    public static boolean isEnhancedBrowser(PageContext pageContext) {
        HttpServletRequest request = (HttpServletRequest) 
                pageContext.getRequest();
        Cookie[] cookies = request.getCookies();
        if (null == cookies)  {
            return false;
        }
        for (int i = 0; i < cookies.length; i++)  {
            if ( USER_AGENT_COOKIE.equals(cookies[i].getName()) &&
                 cookies[i].getValue().startsWith(HYPERBROWSER)) {
            return true;
            }
        }
        return false;
    }
    
    public static String getICEmobileSXScript(PageContext pageContext,
            String command, String id) {
        HttpServletRequest request = (HttpServletRequest) 
                pageContext.getRequest();
        String sessionID = null;
        HttpSession httpSession = pageContext.getSession();
        if (null != httpSession)  {
            sessionID = httpSession.getId();
        }
        String uploadURL = getUploadURL(request);
        String fullCommand = command + "?id=" + id;
        String script = "window.location=\"icemobile://c=" +
            URLEncoder.encode(fullCommand) +
            "&r=\"+escape(window.location)+\"";
        if (null != sessionID)  { 
            script += "&JSESSIONID=" + getSessionIdCookie(sessionID);
        }
        script += "&u=" + URLEncoder.encode(uploadURL) + "\"";
        return script;
    }

    public static String getSessionIdCookie(String sessionID)  {
        return sessionID;
//        String cookieFormat = (String) facesContext.getExternalContext()
//            .getInitParameterMap().get(COOKIE_FORMAT);
//        if (null == cookieFormat)  {
//            //if we have more of these, implement EnvUtils for ICEmobile
//            return sessionID;
//        }
//        StringBuilder out = new StringBuilder();
//        Formatter cookieFormatter = new Formatter(out);
//        cookieFormatter.format(cookieFormat, sessionID);
//        return out.toString();
    }

    public static String getUploadURL(HttpServletRequest request)  {
        String serverName = request.getHeader("x-forwarded-host");
        if (null == serverName)  {
            serverName = request.getServerName() + ":" +
                    request.getServerPort();
        }
        return "http://" + serverName + 
                getUploadPath(request);
    }

    public static String getUploadPath(HttpServletRequest request)  {
        String upPath = request.getContextPath() + "/icemobile";
System.out.println("fix hardcoded " + upPath);
        return upPath;
    }
}