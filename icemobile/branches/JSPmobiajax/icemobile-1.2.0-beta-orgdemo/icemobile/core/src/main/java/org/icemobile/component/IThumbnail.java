package org.icemobile.component;

public interface IThumbnail extends IMobiComponent{
    
    public static final String CSS_CLASS = "mobi-thumb";
    public static final String CSS_DONE_CLASS = "mobi-thumb-done";
    public static final String FOR_MSG = "for component was not detected. ";

    /**
    * <p>the component id that this thumbnail represents, either camera or camcorder.</p>
    */
    public String getMFor();

    /**
     * <p> the thumb base class depends on if an upload has been detected (jsf only)</p>
     */
    public String getBaseClass();
}
