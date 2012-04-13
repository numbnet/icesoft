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

import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.PageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Cookie;

import java.util.Arrays;
import java.io.Writer;
import java.io.IOException;

public class CameraTag extends SimpleTagSupport {

    public void doTag() throws IOException {
        PageContext pageContext = (PageContext) getJspContext();
        boolean isEnhanced = isEnhancedBrowser(pageContext);
        Writer out = pageContext.getOut();
        if (isEnhanced)  {
            out.write("<input type='button' id='camera' class='camera' onclick='ice.camera(\"camera\");' value='camera'>");
        } else {
            out.write("<input id='camera' type='file' name='camera' />");
        }
    }

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
}