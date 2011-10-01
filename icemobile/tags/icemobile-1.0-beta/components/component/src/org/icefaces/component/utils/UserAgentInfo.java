/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */
package org.icefaces.component.utils;

/**
 * backbone of deviceStylesheet as it requires these
 * objects for device detection.
 */
public class UserAgentInfo {

    //info about browser and device
    private String userAgentString;

    //what browser can display
    private String httpAccepted;

    //each device has list of info about device and capabilities
    public static final String deviceIphone = "iphone";
    public static final String deviceIpad = "ipad";
    public static final String deviceIpod = "ipod";
    public static final String deviceMac = "macintosh"; //test laptop
    public static final String deviceAndroid = "android";
    public static final String deviceBlackB = "blackberry";
    public static final String deviceBBCurve = "blackberry89"; //curve2
    public static final String deviceBBTorch = "blackberry 98"; //torch
    public static final String vndRIM = "vnd.rim";  //found when emulating IE or FF on BB

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
    }

    public boolean sniffIpod() {
        return userAgentString.indexOf(deviceIpod) != -1;
    }


    //don't get iPhone confused with iPod touch
    public boolean sniffIphone() {
        return userAgentString.indexOf(deviceIphone) != -1 && !sniffIpod()
                && !sniffIpad();
    }

    public boolean sniffIpad() {
        return userAgentString.indexOf(deviceIpad) != -1;
    }

    public boolean sniffAndroid() {
        return userAgentString.contains(deviceAndroid);
    }


    public boolean sniffBlackberry() {
        return userAgentString.contains(deviceBlackB) || httpAccepted.contains(vndRIM);
    }

}
