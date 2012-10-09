package org.icemobile.component;

import org.icemobile.util.ClientDescriptor;



public interface IGetEnhanced {
    
    public static final String CSS_CLASS = "mobi-getenhanced";
    public static final String INFO_MSG = "ICEmobile container was not detected. ";
    public static final String DOWNLOAD = "Download ICEmobile";
    public static final String ENABLE = "Enable ICEmobile";
    public static final String IOS_LINK = "http://itunes.apple.com/us/app/icemobile-sx/id485908934?mt=8";
    public static final String ANDROID_LINK = "https://play.google.com/store/apps/details?id=org.icemobile.client.android";
    public static final String BLACKBERRY_LINK = "http://appworld.blackberry.com/webstore/content/59555/";

    
    /**
    * <p>Set the value of the <code>includeLink</code> property.</p>
    */
    public void setIncludeLink(boolean includeLink) ;

    /**
    * <p>Return the value of the <code>includeLink</code> property.</p>
    */
    public boolean isIncludeLink() ;

    /**
    * <p>Set the value of the <code>style</code> property.</p>
    */
    public void setStyle(String style) ;

    /**
    * <p>Return the value of the <code>style</code> property.</p>
    */
    public String getStyle() ;

    /**
    * <p>Set the value of the <code>androidMsg</code> property.</p>
    */
    public void setAndroidMsg(String androidMsg) ;

    /**
    * <p>Return the value of the <code>androidMsg</code> property.</p>
    */
    public String getAndroidMsg();

    /**
    * <p>Set the value of the <code>styleClass</code> property.</p>
    */
    public void setStyleClass(String styleClass);

    /**
    * <p>Return the value of the <code>styleClass</code> property.</p>
    */
    public String getStyleClass() ;

    /**
    * <p>Set the value of the <code>iosMsg</code> property.</p>
    */
    public void setIosMsg(String iosMsg) ;

    /**
    * <p>Return the value of the <code>iosMsg</code> property.</p>
    */
    public String getIosMsg();

    /**
    * <p>Set the value of the <code>disabled</code> property.</p>
    */
    public void setDisabled(boolean disabled);
    /**
    * <p>Return the value of the <code>disabled</code> property.</p>
    */
    public boolean isDisabled();

    /**
    * <p>Set the value of the <code>blackberryMsg</code> property.</p>
    */
    public void setBlackberryMsg(String blackberryMsg);

    /**
    * <p>Return the value of the <code>blackberryMsg</code> property.</p>
    */
    public String getBlackberryMsg();
    
    public String getClientId();
    
    public String getICEmobileRegisterSXScript();
    
    public ClientDescriptor getClient();
    
    public boolean isIOSSmartBannerRendered();


}
