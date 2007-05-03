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
 * Slide displayed in presentation. Basically a wrapper class for a String and
 * page location
 */
public class Slide {
    private static final String INFO_LOCATION = "resources/images/infopage.png";
    private static final Slide DEFAULT_SLIDE_VIEWER =
            new Slide("resources/images/titlepage-view.png");
    private static final Slide DEFAULT_SLIDE_MODERATOR =
            new Slide("resources/images/titlepage-mod.png");
    private static final Slide INFO_SLIDE = new Slide(INFO_LOCATION);

    private String location;

    public Slide(String location) {
        this.location = location;
    }

    /**
     * Method to get the default viewer slide
     *
     * @return the default viewer slide
     */
    public static Slide getDefaultSlide() {
        return DEFAULT_SLIDE_VIEWER;
    }

    /**
     * Method to get the default slide for either a moderator or viewer
     *
     * @param isModerator to get a moderator slide
     * @return the default slide
     */
    public static Slide getDefaultSlide(boolean isModerator) {
        if (isModerator) {
            return DEFAULT_SLIDE_MODERATOR;
        }

        return DEFAULT_SLIDE_VIEWER;
    }

    /**
     * Method to get the standard information slide
     *
     * @return the information slide
     */
    public static Slide getInfoSlide() {
        return INFO_SLIDE;
    }

    /**
     * Method to get the location of this slide
     *
     * @return location path
     */
    public String getLocation() {
        return location;
    }

    /**
     * Method to set the location of this slide
     *
     * @param location path to use
     */
    public void setLocation(String location) {
        this.location = location;
    }
}
