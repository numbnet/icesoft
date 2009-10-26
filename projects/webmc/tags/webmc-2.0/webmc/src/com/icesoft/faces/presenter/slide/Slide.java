/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */
package com.icesoft.faces.presenter.slide;

/**
 * Slide displayed in presentation.
 */
public class Slide {
    public static final int MAX_WIDTH = 720;
    public static final int MAX_HEIGHT = 540;
    public static final int MOBILE_MAX_WIDTH = 336;
    public static final int MOBILE_MAX_HEIGHT = 252;

    private static final String INFO_LOCATION = "resources/images/infopage.png";
    private static final String INFO_LOCATION_MOBILE = "resources/images/infopage.png";
    private static final Slide INFO_SLIDE = new Slide(INFO_LOCATION, false);
    private static final Slide INFO_SLIDE_MOBILE = new Slide(INFO_LOCATION_MOBILE, true);

    private static final Slide DEFAULT_SLIDE_VIEWER =
            new Slide("resources/images/titlepage-view.png", false);
    private static final Slide DEFAULT_SLIDE_VIEWER_MOBILE =
        new Slide("resources/images/titlepage-view-mobile.png", true);
    private static final Slide DEFAULT_SLIDE_MODERATOR =
            new Slide("resources/images/titlepage-mod.png", false);
    private static final Slide DEFAULT_SLIDE_MODERATOR_MOBILE =
        new Slide("resources/images/titlepage-mod-mobile.png", true);

    private String location;
    private boolean mobile;
    private boolean isMovieSlide;
    
    public Slide(String location, boolean mobile) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean getMovieSlide() {
        return isMovieSlide;
    }

    public void setMovieSlide(boolean isMovieSlide) {
        this.isMovieSlide = isMovieSlide;
    }

    public String getPlayer()  {
        if (location.endsWith(".mov"))  {
            return "quicktime";
        }  else if (location.endsWith(".mp4")) {
            return "quicktime";
        }  else if (location.endsWith(".m4v")) {
            return "quicktime";
        }  else if (location.endsWith(".swf")) {
            return "flash";
        }  else if (location.endsWith(".flv")) {
            return "flash";
        }  else if (location.endsWith(".mpg")) {
            return "quicktime";
        }  else if (location.endsWith(".mpeg")) {
            return "quicktime";
        }  else if (location.endsWith(".avi")) {
            return "windows";
        }  else if (location.endsWith(".wmv")) {
            return "windows";
        }
        return "";
    }

	public boolean isMobile() {
		return mobile;
	}

    /**
     * Method to get the default slide for either a moderator or viewer
     *
     * @param isModerator to get a moderator slide
     * @param mobile to get a moderator slide sized for a mobile device
     * @return the default slide
     */
    public static Slide getDefaultSlide(boolean isModerator, boolean mobile) {
        if (isModerator) {
        	if(mobile){
        		return DEFAULT_SLIDE_MODERATOR_MOBILE;
        	}
            return DEFAULT_SLIDE_MODERATOR;
        }
        if(mobile){
        	return DEFAULT_SLIDE_VIEWER_MOBILE;
        }
        return DEFAULT_SLIDE_VIEWER;
    }

    /**
     * Method to get the standard information slide
     * 
     * @param mobile to get an information slide sized for a mobile device
     * @return the information slide
     */
    public static Slide getInfoSlide(boolean mobile) {
        if(mobile){
        	return INFO_SLIDE_MOBILE;
        }
        return INFO_SLIDE;
    }

}
