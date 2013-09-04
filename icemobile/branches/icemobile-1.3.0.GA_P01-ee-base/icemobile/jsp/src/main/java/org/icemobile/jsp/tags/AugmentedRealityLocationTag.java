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
 
package org.icemobile.jsp.tags;

import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpSession;

import javax.servlet.jsp.PageContext;

public class AugmentedRealityLocationTag extends SimpleTagSupport {
    private String locationLabel;
    //location latitude
    private String locationLat;
    //location longitude
    private String locationLon;
    //location altitude
    private String locationAlt;
    //location direction 0-359 from North
    private String locationDir;
    //location Icon URL
    private String locationIcon;

    public AugmentedRealityLocationTag()  {
    }

    public void doTag() throws JspException  {
        PageContext pageContext = (PageContext) getJspContext();
        String params = (String) pageContext.getAttribute(AugTag.PARAMS);
        if (null == params)  {
            throw new JspException(
                "AugmentedRealityLocationTag must be within AugTag" );
        }
        params += "&" + locationLabel + "=" + 
            locationLat + "," +
            locationLon + "," +
            locationAlt + "," +
            locationDir + "," +
            locationIcon;
        pageContext.setAttribute(AugTag.PARAMS, params);
    }

    public void setLocationLabel(String locationLabel) {
        this.locationLabel = locationLabel;
    }

    public void setLocationLat(String locationLat) {
        this.locationLat = locationLat;
    }

    public void setLocationLon(String locationLon) {
        this.locationLon = locationLon;
    }

    public void setLocationAlt(String locationAlt) {
        this.locationAlt = locationAlt;
    }

    public void setLocationDir(String locationDir) {
        this.locationDir = locationDir;
    }

    public void setLocationIcon(String locationIcon) {
        this.locationIcon = locationIcon;
    }

}