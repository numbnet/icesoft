/**
 * Copyright (C) 2005, ICEsoft Technologies Inc.
 */
package com.icesoft.applications.faces.webmail;

import javax.faces.event.ValueChangeEvent;

/**
 * <p>The StyleBean class is the backing bean which manages the demonstractions
 * active theme.  There are currently two themes support by the bean; Xp and
 * Royale. </p>
 * <p/>
 * <p>The webpages style attributes are modified by changing link in the header
 * of the html document.  The calendar and tree components styles are changed
 * by changing the location of their image src directories.</p>
 *
 * @since 0.3.0
 */
public class StyleBean {

    /**
     * Possible themes to chose from
     */
    private final String XP = "xp";
//    private final String ROYALE = "royale";

    // default theme
    private String currentStyle = XP;
    private String tempStyle = XP;
    // available style list

    // default theme image directory for calendar and theme.
    private String imageDirectory = "./xmlhttp/css/xp/css-images/";

    /**
     * Creates a new instance of the styleBean.
     */
    public StyleBean() {
    }

    /**
     * Gets the current style.
     *
     * @return current style.
     */
    public String getCurrentStyle() {
        return currentStyle;
    }

    /**
     * Sets the current style of the applications to one of the pretermined themse.
     *
     * @param currentStyle
     */
    public void setCurrentStyle(String currentStyle) {
        this.tempStyle = currentStyle;
    }

    /**
     * Gets the html needed to insert a valid css link tag.
     *
     * @return the tag information needed for a valid css link tag.
     */
    public String getStyle() {
        return "<link rel='stylesheet' type='text/css' href='./xmlhttp/css/" +
                currentStyle + "/" + currentStyle + ".css" + "'/>";
    }

    /**
     * Gets the image directory to use for the calendar and tree theming.
     *
     * @return image directory used for theming.
     */
    public String getImageDirectory() {
        return imageDirectory;
    }

    /**
     * Updates the current style and image directory used by the applications.
     *
     * @param event contains the name of the theme to use.
     */
    public void styleChanged(ValueChangeEvent event) {
        currentStyle = (String) event.getNewValue();
        imageDirectory = "./xmlhttp/css/" + currentStyle + "/css-images/";
    }

    /**
     * Applies tmp style to to the currnet style and image directory
     *
     * @return the reload navigation attribute.
     */
    public String changeStyle() {
        currentStyle = tempStyle;
        imageDirectory = "./xmlhttp/css/" + currentStyle + "/css-images/";
        return "reload";
    }

}