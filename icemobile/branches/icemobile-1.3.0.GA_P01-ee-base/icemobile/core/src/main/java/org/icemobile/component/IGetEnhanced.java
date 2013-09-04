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

package org.icemobile.component;

public interface IGetEnhanced extends IMobiComponent{
    
    public static final String CSS_CLASS = "mobi-getenhanced";
    public static final String INFO_MSG = "ICEmobile container was not detected. ";
    public static final String DOWNLOAD = "Download ICEmobile";
    public static final String ENABLE = "Enable ICEmobile";
    public static final String IOS_LINK = "http://itunes.apple.com/us/app/icemobile-sx/id485908934?mt=8";
    public static final String ANDROID_LINK = "https://play.google.com/store/apps/developer?id=ICEsoft+Technologies";
    public static final String BLACKBERRY_LINK = "http://appworld.blackberry.com/webstore/content/59555/";
    public static final String BLACKBERRY10_LINK = "http://appworld.blackberry.com/webstore/content/28712888/";
    public static final String ICEMOBILE_LINK = "http://www.icesoft.org/projects/ICEmobile/containers.jsf";

    
    /**
    * <p>Set the value of the <code>includeLink</code> property.</p>
    */
    public void setIncludeLink(boolean includeLink) ;

    /**
    * <p>Return the value of the <code>includeLink</code> property.</p>
    */
    public boolean isIncludeLink() ;
    /**
    * <p>Set the value of the <code>androidMsg</code> property.</p>
    */
    public void setAndroidMsg(String androidMsg) ;

    /**
    * <p>Return the value of the <code>androidMsg</code> property.</p>
    */
    public String getAndroidMsg();

    /**
    * <p>Set the value of the <code>iosMsg</code> property.</p>
    */
    public void setIosMsg(String iosMsg) ;

    /**
    * <p>Return the value of the <code>iosMsg</code> property.</p>
    */
    public String getIosMsg();

    /**
    * <p>Set the value of the <code>blackberryMsg</code> property.</p>
    */
    public void setBlackberryMsg(String blackberryMsg);

    /**
    * <p>Return the value of the <code>blackberryMsg</code> property.</p>
    */
    public String getBlackberryMsg();
    
    public String getICEmobileRegisterSXScript();
    
    public boolean isIOSSmartBannerRendered();


}
