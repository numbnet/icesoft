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
package org.icefaces.mobi.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * backbone of deviceStylesheet as it requires these
 * objects for device detection.
 */
public class UserAgentInfo {
    private static Logger log = Logger.getLogger(
            UserAgentInfo.class.getName());

    //info about browser and device
    private String userAgentString;

    //what browser can display
    private String httpAccepted;

    //each device has list of info about device and capabilities
    public static final String IPHONE = "iphone";
    public static final String IPAD = "ipad";
    public static final String IPOD = "ipod";
    public static final String MAC = "macintosh"; //test laptop
    public static final String ANDROID = "android";
    public static final String MOBILE = "mobile";
    public static final String BLACKBERRY = "blackberry";
    public static final String BLACKBERRY_CURVE = "blackberry89"; //curve2
    public static final String BLACKBERRY_TORCH = "blackberry 98"; //torch
    public static final String BLACKBERRY_EMUL = "vnd.rim";  //found when emulating IE or FF on BB
    public static final String IOS5 = " os 5_";
    public static final String IOS6 = " os 6_";
    public static final String TABLET = "tablet";
    public static final String GALAXY_TABLET = "gt-p1000";

    public UserAgentInfo(String userAgent, String httpAcc) {
        if (userAgent != null) {
            this.userAgentString = userAgent.toLowerCase();
        }
        if (httpAcc != null) {
            this.httpAccepted = httpAcc.toLowerCase();
        }
    }

    public UserAgentInfo(String userAgent) {
        if (userAgent != null) {
            this.userAgentString = userAgent.toLowerCase();
        }
        this.httpAccepted = "*/*";
    }

    public boolean sniffIpod() {
        boolean result = userAgentString.indexOf(IPOD) != -1;
        logSniff(result, "iPod", userAgentString);
        return result;
    }

    //don't get iPhone confused with iPod touch
    public boolean sniffIphone() {
        boolean result = userAgentString.indexOf(IPHONE) != -1
                && !sniffIpod()
                && !sniffIpad();
        logSniff(result, "iPod", userAgentString);
        return result;
    }

    public boolean sniffIOS(){
        boolean result = sniffIphone() || sniffIpod() ||  sniffIpad();
        logSniff(result, "iOS", userAgentString);
        return result;
    }

    public boolean sniffIOS5(){
        boolean result = userAgentString.indexOf(IOS5) != -1;
        logSniff(result, "iOS5", userAgentString);
        return result;
    }
    
    public boolean sniffIOS6(){
        boolean result = userAgentString.indexOf(IOS6) != -1;
        logSniff(result, "iOS6", userAgentString);
        return result;
    }

    public boolean sniffIpad() {
        boolean result = userAgentString.indexOf(IPAD) != -1;
        logSniff(result, "iPad", userAgentString);
        return result;
    }

    public boolean sniffAndroidPhone() {
        boolean foundAndroid = userAgentString.contains(ANDROID) && 
        		userAgentString.contains(MOBILE) && !userAgentString.contains(GALAXY_TABLET) 
        		&& !userAgentString.contains(TABLET);
        logSniff(foundAndroid, "Android Mobile", userAgentString);
        return foundAndroid;
    }

    public boolean sniffAndroidTablet() {
        boolean result = userAgentString.contains(ANDROID) && 
        	(!userAgentString.contains(MOBILE) || userAgentString.contains(GALAXY_TABLET) 
        	|| userAgentString.contains(TABLET));
        logSniff(result, "Android Tablet", userAgentString);
        return result;
    }

    public boolean sniffBlackberry() {
        boolean result = userAgentString.contains(BLACKBERRY) 
            || httpAccepted.contains(BLACKBERRY_EMUL);
        logSniff(result, "BlackBerry", userAgentString);
        return result;
    }

    private void logSniff(boolean result, String device, 
                          String userAgent)  {
        if (log.isLoggable(Level.FINEST))  {
            if (result)  {
                log.finest("Detected " + device + " " + userAgentString);
            }
        }
    }
}
