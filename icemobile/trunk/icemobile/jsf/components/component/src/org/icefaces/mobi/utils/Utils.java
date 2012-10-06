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

import java.util.logging.Logger;

import javax.faces.context.FacesContext;

import org.icemobile.util.ClientDescriptor;

/*
 * TODO Common utils should be moved into core Utils class, and JSF Utils should
 * be moved into MobiJsfUtils
 * 
 */
@Deprecated
public class Utils {
	static String TEMP_DIR = "javax.servlet.context.tmpdir";
	static String COOKIE_FORMAT = "org.icemobile.cookieformat";


	private static Logger logger = Logger.getLogger(Utils.class.getName());


	
	   /**
     * used by DateSpinner and timeSpinner to detect which type of events to use
     * mobile devices get touch events Note that Blackberry and android pad are
     * still using generic events
     * 
     * @param context
     * @return true if mobile device
     */
	@Deprecated
    public static boolean isTouchEventEnabled(FacesContext context) {
        ClientDescriptor client = MobiJSFUtils.getClientDescriptor();
        // commenting out Blackberry at this time as support of touch events is
        // problematic
        // if (uai.sniffIphone() || uai.sniffAndroid() || uai.sniffBlackberry()
        if (client.isAndroidOS() && client.isTabletBrowser())
            return false;
        if (client.isIOS() || client.isAndroidOS() ) { //assuming android phone
            return true;
        }
        return false;
    }

    /**
     * Utility to see if the date spinner will use the native input method for a
     * data input.  Current can be set by the attribute useNative and iOS
     * or blackberry
     *
     * @return ture if the native dialog should be used
     */
    @Deprecated
    public static boolean shouldUseNative() {
        ClientDescriptor client = MobiJSFUtils.getClientDescriptor();
        return client.isIOS5() || client.isIOS6() || client.isBlackBerryOS();
    }
    

}
