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

package com.icesoft.tutorial;

/**
 * Bean holding time zone specific information.
 * Checking a selectBooleanCheckbox in the UI will cause instances of this class to populate the checkedTimeZoneList ArrayList in <code>TimeZoneBean</code>.
 * That ArrayList is used to create a DataTable of checked time zones in the UI.
 */

import java.util.TimeZone;

public class TimeZoneWrapper {
    /* Variables */
    /**
     * {@link TimeZone} id used to identify the time zone. This id can be passed
     * to <code>TimeZone.getTimeZone(String)</code>, to get the appropriate
     * {@link TimeZone} object.
     */
    private String id;

    /**
     * The component id of the commandButton, in the map UI, corresponding to
     * this time zone.
     */
    private String mapCommandButtonId;

    /* Constructors */
    /**
     * @param id    id used to identify the time zone.
     * @param mapId map button component id in web page
     */
    public TimeZoneWrapper(String id, String mapId) {
        this.id = id;
        this.mapCommandButtonId = mapId;
    }

    /**
     * Gets the {@link TimeZone} id used to identify this time zone in the Java
     * code.
     *
     * @return String
     */
    public String getId() {
        return id;
    }

    /**
     * Ascertains whether mapCommandButtonId is a part of componentId.
     * componentId might be a fully qualified id, with a prefix corresponding to
     * container component(s).
     *
     * @param componentId Id of some component that may be related to this time
     *                    zone
     */
    public boolean isRelevantComponentId(String componentId) {
        boolean relevant = componentId.endsWith(mapCommandButtonId);
        return relevant;
    }

    /**
     * Gets the component id of the commandButton, in the map UI, corresponding
     * to this time zone.
     */
    public String getMapCommandButtonId() {
        return mapCommandButtonId;
    }
} // End of TimeZoneWrapper class
