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

package org.icemobile.jsp.tags;


import javax.servlet.jsp.PageContext;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.Cookie;
import java.io.IOException;

import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.Writer;

public class TagUtil {
    private static String USER_AGENT = "User-Agent";
    private static String ACCEPT = "Accept";
    private static String USER_AGENT_COOKIE = "com.icesoft.user-agent";
    private static String HYPERBROWSER = "HyperBrowser";


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


    public enum DeviceType {
        android,
        honeycomb,
        bberry,
        iphone,
        ipad;
        public static final DeviceType DEFAULT = DeviceType.ipad;

        public boolean equals(String deviceName) {
            return this.name().equals(deviceName);
        }
    }

    /**
     * Check to see if the browser is a enhanced container browser
     *
     * @param pageContext
     * @return true if hyperbrowser cookie detected.
     */
    public static boolean isEnhancedBrowser(PageContext pageContext) {
        HttpServletRequest request = (HttpServletRequest)
            pageContext.getRequest();
        Cookie[] cookies = request.getCookies();
        if (null == cookies) {
            return false;
        }
        for (int i = 0; i < cookies.length; i++) {
            if (USER_AGENT_COOKIE.equals(cookies[i].getName()) &&
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
        if (null != httpSession) {
            sessionID = httpSession.getId();
        }
        String uploadURL = getUploadURL(request);
        String fullCommand = command + "?id=" + id;
        String script = "window.location=\"icemobile://c=" +
            URLEncoder.encode(fullCommand) +
            "&r=\"+escape(window.location)+\"";
        if (null != sessionID) {
            script += "&JSESSIONID=" + getSessionIdCookie(sessionID);
        }
        script += "&u=" + URLEncoder.encode(uploadURL) + "\"";
        return script;
    }

    public static String getSessionIdCookie(String sessionID) {
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

    public static String getUploadURL(HttpServletRequest request) {
        String serverName = request.getHeader("x-forwarded-host");
        if (null == serverName) {
            serverName = request.getServerName() + ":" +
                request.getServerPort();
        }
        return "http://" + serverName +
            getUploadPath(request);
    }

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

    public static String getUploadPath(HttpServletRequest request) {
        String upPath = request.getContextPath() + "/icemobile";
        System.out.println("fix hardcoded " + upPath);
        return upPath;
    }


    static Logger log = Logger.getLogger(
        TagUtil.class.getName());

    //each device has list of info about device and capabilities
    public static final String DEVICE_IPHONE = "iphone";
    public static final String DEVICE_IPAD = "ipad";
    public static final String DEVICE_IPOD = "ipod";
    public static final String DEVICE_MAC = "macintosh"; //test laptop
    public static final String DEVICE_ANDROID = "android";
    public static final String DEVICE_HONEYCOMB = "android 3.";
    public static final String DEVICE_BLACKB = "blackberry";
    public static final String DEVICE_BB_CURVE = "blackberry89"; //curve2
    public static final String DEVICE_BB_TORCH = "blackberry 98"; //torch
    public static final String VND_RIM = "vnd.rim";  //found when emulating IE or FF on BB
    public static final String DEVICE_IOSS = " os 5_";
    public static final String DEVICE_IOS6 = " os 6_";
    public static final String DEVICE_MOBILE = "mobile";
    public static final String DEVICE_TABLET = "tablet";
    public static final String DEVICE_GALAXY_TABLET = "gt-p1000";

    public static final String VIEW_TYPE_SMALL = "small";
    public static final String VIEW_TYPE_LARGE = "large";

    public static boolean isTouchEventEnabled(PageContext pageContext) {
        HttpServletRequest request = (HttpServletRequest)
            pageContext.getRequest();
        String userAgent = request.getHeader(USER_AGENT);
        if (sniffAndroidTablet(userAgent)) {
            return false;
        }
        if (isIOS(pageContext) || isAndroid(pageContext)) {
            return true;
        }
        return false;
    }

    public static boolean useNative(PageContext pageContext) {
        return isIOS5orHigher(pageContext) || isBlackBerry(pageContext);
    }

    public static boolean isIOS5orHigher(PageContext pageContext) {
        HttpServletRequest request = (HttpServletRequest)
            pageContext.getRequest();
        String userAgent = request.getHeader(USER_AGENT);
        return sniffIOS5(userAgent) || sniffIOS6(userAgent);
    }

    public static boolean isIOS(PageContext pageContext) {
        HttpServletRequest request = (HttpServletRequest)
            pageContext.getRequest();
        String userAgent = request.getHeader(USER_AGENT);
        return sniffIOS(userAgent);
    }

    public static boolean isAndroid(PageContext pageContext) {
        HttpServletRequest request = (HttpServletRequest)
            pageContext.getRequest();
        String userAgent = request.getHeader(USER_AGENT);
        return sniffAndroid(userAgent);
    }

    public static boolean isBlackBerry(PageContext pageContext) {
        HttpServletRequest request = (HttpServletRequest)
            pageContext.getRequest();
        String userAgent = request.getHeader(USER_AGENT);
        String accept = request.getHeader(ACCEPT);
        return sniffBlackberry(userAgent, accept);
    }

    static boolean sniffIpod(String uaString) {
        boolean result = uaString.contains(DEVICE_IPOD);
        logSniff(result, "iPod", uaString);
        return result;
    }

    //don't get iPhone confused with iPod touch
    static boolean sniffIphone(String uaString) {
        boolean result = uaString.contains(DEVICE_IPHONE)
            && !sniffIpod(uaString)
            && !sniffIpad(uaString);
        logSniff(result, "iPod", uaString);
        return result;
    }

    static boolean sniffIOS(String uaString) {
        boolean result = sniffIphone(uaString) || sniffIpod(uaString) || sniffIpad(uaString);
        logSniff(result, "iOS", uaString);
        return result;
    }

    static boolean sniffIOS5(String uaString) {
        boolean result = uaString.contains(DEVICE_IOSS);
        logSniff(result, "iOS5", uaString);
        return result;
    }
    
    static boolean sniffIOS6(String uaString) {
        boolean result = uaString.contains(DEVICE_IOS6);
        logSniff(result, "iOS6", uaString);
        return result;
    }

    static boolean sniffIpad(String uaString) {
        boolean result = uaString.contains(DEVICE_IPAD);
        logSniff(result, "iPad", uaString);
        return result;
    }

    static boolean sniffAndroid(String uaString) {

        boolean foundAndroid = uaString.contains(DEVICE_ANDROID) && 
        		uaString.contains(DEVICE_MOBILE) && !uaString.contains(DEVICE_GALAXY_TABLET) 
        		&& !uaString.contains(DEVICE_TABLET);
        logSniff(foundAndroid, "Android Mobile", uaString);
        return foundAndroid;
    }

    static boolean sniffAndroidTablet(String uaString) {
    	 boolean result = uaString.contains(DEVICE_ANDROID) && 
    	        	(!uaString.contains(DEVICE_MOBILE) || uaString.contains(DEVICE_GALAXY_TABLET) 
    	        	|| uaString.contains(DEVICE_TABLET));
        logSniff(result, "Android Tablet", uaString);
        return result;
    }

    /**
     * Check for blackberry. This method is called from all paths now and
     * Android devices on POST appear to have no accept header.
     *
     * @param uaString
     * @param httpAccept
     * @return true if Blackberry device detected
     */
    static boolean sniffBlackberry(String uaString, String httpAccept) {
        if (httpAccept == null) {
            httpAccept = "";
        }
        boolean result = uaString.contains(DEVICE_BLACKB)
            || httpAccept.contains(VND_RIM);
        logSniff(result, "BlackBerry", uaString);
        return result;
    }

    private static void logSniff(boolean result, String device,
                                 String userAgent) {
        if (log.isLoggable(Level.FINEST)) {
            if (result) {
                log.finest("Detected " + device + " " + userAgent);
            }
        }
    }

    static DeviceType getDeviceType(String userAgent) {
        DeviceType device = checkUserAgentInfo(userAgent, "");
        return device;
    }

    static DeviceType getDeviceType(String userAgent, String accepts) {
        DeviceType device = checkUserAgentInfo(userAgent, accepts);
        return device;
    }

    private static DeviceType checkUserAgentInfo(String userAgent,
                                                 String accepts) {
    	userAgent = userAgent.toLowerCase();
        if (sniffIphone(userAgent) || sniffIpod(userAgent)) {
            return DeviceType.iphone;
        }
        if (sniffAndroidTablet(userAgent)) {
            return DeviceType.honeycomb;
        }
        if (sniffAndroid(userAgent)) {
            return DeviceType.android;
        }
        if (sniffBlackberry(userAgent, accepts)) {
            return DeviceType.bberry;
        }
        if (sniffIpad(userAgent)) {
            return DeviceType.ipad;
        }
        return DeviceType.DEFAULT;
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

    /*    protected void writeJavascriptFile(FacesContext facesContext, 
            UIComponent component, String JS_NAME, String JS_MIN_NAME, 
            String JS_LIBRARY) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = component.getClientId(facesContext);
        writer.startElement(HTML.SPAN_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_libJS", HTML.ID_ATTR);
        if (!isScriptLoaded(facesContext, JS_NAME)) {
            String jsFname = JS_NAME;
            if (facesContext.isProjectStage(ProjectStage.Production)){
                jsFname = JS_MIN_NAME;
            }
            //set jsFname to min if development stage
            Resource jsFile = facesContext.getApplication().getResourceHandler().createResource(jsFname, JS_LIBRARY);
            String src = jsFile.getRequestPath();
            writer.startElement("script", component);
            writer.writeAttribute("text", "text/javascript", null);
            writer.writeAttribute("src", src, null);
            writer.endElement("script");
            setScriptLoaded(facesContext, JS_NAME);
        } 
        writer.endElement(HTML.SPAN_ELEM);
	} */

}