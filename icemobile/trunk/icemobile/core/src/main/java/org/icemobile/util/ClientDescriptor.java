package org.icemobile.util;

import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class ClientDescriptor {
    private final static String HEADER_ACCEPT = "Accept";
    private final static String HEADER_ACCEPT_BLACKBERRY_EMUL = "vnd.rim";  //found when emulating IE or FF on BB
    private final static String COOKIE_NAME_ICEMOBILE_CONTAINER = "com.icesoft.user-agent";
    private final static String COOKIE_VALUE_ICEMOBILE_CONTAINER = "HyperBrowser";
    private final static String SESSION_KEY_SX = "iceAuxRequestMap";

    private static Logger log = Logger.getLogger(ClientDescriptor.class.getName());
    
    private String userAgent;
    private String httpAccepted;
    
    private DeviceType deviceType;
    private boolean handheld;
    private boolean tablet;
    private boolean icemobileContainer;
    private boolean sx;
    
    private UserAgentInfo _userAgentInfo;
    
    public ClientDescriptor(HttpServletRequest request){
        updateUserAgent(request);
        updateHttpAccepted(request);
        updateICEmobileContainer(request);
        updateSXContainer(request);
    }

    public String getUserAgent() {
        return userAgent;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public boolean isHandheld() {
        return handheld;
    }

    public boolean isTablet() {
        return tablet;
    }
    
    private void updateDeviceType(){
        if( _userAgentInfo != null ){
            if (_userAgentInfo.isIphone()) {
                deviceType = DeviceType.IPHONE;
            }
            if (_userAgentInfo.isAndroidTablet()) {
                deviceType = DeviceType.ANDROID_TABLET;
            }
            if (_userAgentInfo.isAndroidPhone()) {
                deviceType = DeviceType.ANDROID_PHONE;
            }
            if (_userAgentInfo.isBlackberry() || httpAccepted.contains(HEADER_ACCEPT_BLACKBERRY_EMUL)) {
                deviceType = DeviceType.BLACKBERRY;
            }
            if (_userAgentInfo.isIpad()) {
                deviceType = DeviceType.IPAD;
            }
        }
    }

    /**
     * Test for Android
     * 
     * @return true if client is Android
     */
    public boolean isAndroidPhone() {
        return _userAgentInfo.isAndroidPhone();
    }

    /**
     * Test for BlackBerry
     * 
     * @return true if client is BlackBerry
     */
    public boolean isBlackBerry() {
        return _userAgentInfo.isBlackberry();
    }

    /**
     * Test for Internet Explorerer
     * 
     * @return true if client is Internet Explorer
     */
    public boolean isIE() {
        return _userAgentInfo.isIE();
    }

    /**
     * Test for iOS
     * 
     * @return true if client is iOS
     */
    public boolean isIOS() {
        return _userAgentInfo.isIOS();
    }

    /**
     * Some input components may have html5 support for iOS5 such as DateSpinner
     * 
     * @return true if request header denotes os 5_0
     */
    public boolean isIOS5() {
        return _userAgentInfo.isIOS5();
    }

    /**
     * @return true if request header denotes os 6_0
     */
    public boolean isIOS6() {
        return _userAgentInfo.isIOS6();
    }
    
    public boolean isSX(){
        return sx;
    }
    
    public boolean isICEmobileContainer(){
        return icemobileContainer;
    }
    
    private void updateUserAgent(HttpServletRequest request){
        userAgent = request.getHeader("User-Agent");
        if( userAgent == null || userAgent.length() == 0 ){
            log.warning("Could not detect device settings, user agent is null");
        }
        else{
            tablet = _userAgentInfo.isTabletBrowser();
            handheld = _userAgentInfo.isMobileBrowser();
            updateDeviceType();
        }
    }

    private void updateHttpAccepted(HttpServletRequest request){
        String accept = request.getHeader(HEADER_ACCEPT);
        httpAccepted = (accept == null ? null : accept.toLowerCase());
    }
    
    private void updateICEmobileContainer(HttpServletRequest request){
        Cookie cookie = Utils.getCookie(COOKIE_NAME_ICEMOBILE_CONTAINER, request);
        if (null != cookie && cookie.getValue().startsWith(COOKIE_VALUE_ICEMOBILE_CONTAINER)) {
            icemobileContainer = true;
        }
        else{
            icemobileContainer = false;
        }
    }
    
    private void updateSXContainer(HttpServletRequest request){
        sx = request.getSession().getAttribute(SESSION_KEY_SX) != null;
    }

}
