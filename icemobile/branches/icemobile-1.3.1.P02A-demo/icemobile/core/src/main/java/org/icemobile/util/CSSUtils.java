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

import javax.servlet.http.HttpServletRequest;

public class CSSUtils {
    
    /* Common CSS Class Names */
    public static final String HIDDEN = "ui-screen-hidden";
    public static final String NONE = "none"; //for MOBI-1094 when navigation to page without deviceResource
    /* Mobi Style Classes */
    public static final String STYLECLASS_BUTTON = "mobi-button ui-btn-up-c";
    public static final String STYLECLASS_BUTTON_DISABLED = " mobi-button-dis";
    public static final String STYLECLASS_BUTTON_ATTENTION = "mobi-button-attention";
    public static final String STYLECLASS_BUTTON_IMPORTANT = "mobi-button-important";
    public static final String STYLECLASS_BUTTON_BACK = "mobi-button-back";
    
    /* jQuery Mobile Classes */
    public static final String STYLECLASS_DISABLED = "ui-disabled";
    /* Collapsible */
    //div
    public static final String STYLECLASS_COLLAPSIBLESET = "ui-collapsible-set";
       //div
    public static final String STYLECLASS_COLLAPSIBLE = "ui-collapsible";
    public static final String STYLECLASS_COLLAPSED = "ui-collapsible-collapsed";
    public static final String STYLECLASS_COLLAPSIBLEINSET = "ui-collapsible-inset"; //if inset
          //h2
    public static final String STYLECLASS_COLLAPSIBLEHEADING = "ui-collapsible-heading";
    public static final String STYLECLASS_COLLAPSIBLEHEADINGTOGGLE = "ui-collapsible-heading-toggle";
       //div
    public static final String STYLECLASS_COLLAPSIBLECONTENT = "ui-collapsible-content";
    /* Bar */
    public static final String STYLECLASS_BAR_A = "ui-bar-a"; //default for headers, footers
    public static final String STYLECLASS_BAR_B = "ui-bar-b"; //default for list group headers
    
    /* globals */
    public static final String STYLECLASS_ACTIVE = "ui-btn-active";
    
        
    public enum Theme{ 
        BASE, IPAD, IPHONE, BBERRY, ANDROID, HONEYCOMB, 
        ARCHAIC, ANDROID_LIGHT, ANDROID_DARK, BB10, JQM, IOS7;
        public String fileName(){
            return this.name().toLowerCase();
        }
        public static Theme getEnum(String val){
            if( val == null ){
                return null;
            }
            Theme result = null;
            try{
                result = Theme.valueOf(val.toUpperCase());
            }
            catch(IllegalArgumentException e){}
            return result;
        }
    }
    public enum View{ LARGE, SMALL;
        public static View getEnum(String val){
            if( val == null ){
                return null;
            }
            View result = null;
            try{
                result = View.valueOf(val.toUpperCase());
            }
            catch(IllegalArgumentException e){}
            return result;
        }
    }
    
    /**
     * Derive the appropriate theme for the request. 
     * 
     * If the targetView is supplied, the theme is derived based on the specified
     * view along with the the detected platform. If the targetView 
     * is not supplied, the theme is determined from the browser form factor 
     * and platform.
     * @param targetView The target view, 'small' or 'large'
     * @param request The servlet request
     * @return The theme name
     */
    public static Theme deriveTheme(String targetView, HttpServletRequest request) {
        ClientDescriptor client = ClientDescriptor.getInstance(request);
        Theme theme = null;
        View view = View.getEnum(targetView);
        if( view == null ){
            view = client.isHandheldBrowser() ? View.SMALL : View.LARGE;
        }
        
        if (client.isBlackBerry10OS()) {
            theme = Theme.BB10;
        }
        else if (client.isBlackBerryOS()) {
            theme = Theme.BBERRY;
        } 
        else if (client.isAndroidOS() || client.isIE10orHigherBrowser()) {
            theme = Theme.ANDROID_LIGHT;
        } 
        else if (client.isIOS()) {
            if( client.isIOS7() ){
                theme = Theme.IOS7;
            }
            else if (view == View.SMALL) {
                theme = Theme.IPHONE;
            }
            else if( view == View.LARGE ){
                theme = Theme.IPAD;
            }
        } 
        else if( client.isIE9orLessBrowser()){ 
            theme = Theme.ARCHAIC;
        }
        else{
            theme = Theme.ANDROID_LIGHT; //default for all others
        }
        return theme;
    }
    
    /**
     * Derive the appropriate theme for the request. 
     * 
     * The theme is automatically determined from the browser form factor 
     * and platform.
     * @param request
     * @return
     */
    public static Theme deriveTheme(HttpServletRequest request){
        return deriveTheme(null, request);
    }

    /**
     * Note that MOBI-1094 showed an NPE when navigating from a page with a
     * deviceResource component to a page without one (like an non-icemobile
     * error page).  So....let the default be ANDROID_LIGHT in the case of this
     * if theme or theme.fileName() are null, return ANDROID_LIGHT
     * @param theme
     * @param production
     * @return
     */
    public static String getThemeCSSFileName(Theme theme, boolean production){
        if (theme !=null && theme.fileName() !=null){
            return theme.fileName() + ".css";
        }else {
            return NONE + ".css";  //mobi-1094 better to have 404 than NPE
        }
    }


}
