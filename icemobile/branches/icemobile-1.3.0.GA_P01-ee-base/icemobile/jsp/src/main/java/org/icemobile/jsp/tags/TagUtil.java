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

package org.icemobile.jsp.tags;


import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;

import org.icemobile.util.ClientDescriptor;

public class TagUtil {
    

    public static final String USER_AGENT = "User-Agent";
    public static final String USER_AGENT_COOKIE = "com.icesoft.user-agent";
    public static final String SX_USER_AGENT = "icemobile-sx";
    public static final String CLOUD_PUSH_KEY = "iceCloudPushId";

    public static String A_TAG = "<a";
    public static String A_TAG_END = "</a>";

    public static String SPAN_TAG = "<span";
    public static String SPAN_TAG_END = "</span>";

    public static String DIV_TAG = "<div";
    public static String DIV_TAG_END = "</div>";

    public static String UL_TAG = "<ul";
    public static String UL_TAG_END = "</ul>";

    public static String LI_TAG = "<li";
    public static String LI_TAG_END = "</li>";

    public static String INPUT_TAG = "<input";
    public static String INPUT_TAG_END = "</input>";

    public static String HEAD_TAG = "<head";
    public static String HEAD_TAG_END = "</head>";

    public static String SCRIPT_TAG = "<script";
    public static String SCRIPT_TAG_END = "</script>";

    public static String SECTION_TAG = "<section";
    public static String SECTION_TAG_END = "</section>";

    public static String JS_BOILER = " type=\"text/javascript\"";

    static Logger log = Logger.getLogger(
        TagUtil.class.getName());

    public static final String VIEW_TYPE_SMALL = "small";
    public static final String VIEW_TYPE_LARGE = "large";

    public static boolean isTouchEventEnabled(PageContext pageContext) {
        ClientDescriptor client = ClientDescriptor
                .getInstance((HttpServletRequest)pageContext.getRequest());
        if (client.isAndroidOS() && client.isTabletBrowser()) {
            return false;
        }
        if (client.isIOS() || client.isAndroidOS()) {
            return true;
        }
        return false;
    }

    public static boolean useNative(PageContext pageContext) {
        ClientDescriptor client = ClientDescriptor
                .getInstance((HttpServletRequest)pageContext.getRequest());
        return client.isIOS5()|| client.isIOS6() || client.isBlackBerryOS();
    }
    
    public void writeAttribute(Writer out, String name, String value) throws
        IOException {
        out.write(" " + name + "=\"" + value + "\"");
    }
    

    public boolean isValueBlank(String value) {
        if (value == null) {
            return true;
        }
        return value.trim().equals("");
    }

}
