package org.icemobile.component;

public interface IAccordion extends IMobiComponent{
    
    public static final String ACCORDION_CLASS = "mobi-accordion";
    public static final String ACCORDION_RIGHT_POINTING_TRIANGLE = "&#9654;";
    public static final String ACCORDION_RIGHT_POINTING_POINTER= "&#9658;";
    public static final String ACCORDION_LEFT_POINTING_TRIANGLE = "&#9664;";
    public static final String ACCORDION_LEFT_POINTING_POINTER= "&#9668;";
    public static final String ACCORDION_DOWN_POINTING_ARROW="&#9660;";
    public static final String JS_NAME = "accordion.js";
    public static final String JS_MIN_NAME = "accordion-min.js";
    public static final String LIB_JSF = "org.icefaces.component.accordion";
    public static final String LIB_JSP = "javascript";
        
    public void setHeight(String fixedHeight);
    public String getHeight();
    public void setSelectedId(String currentId);
    public String getSelectedId();
    public void setAutoHeight(boolean autoHeight);
    public boolean isAutoHeight();
    public String getJavascriptFileRequestPath();
    public boolean isScriptLoaded();
    public void setScriptLoaded();
    public String getOpenedPaneClientId();
    public String getHashVal();
}
