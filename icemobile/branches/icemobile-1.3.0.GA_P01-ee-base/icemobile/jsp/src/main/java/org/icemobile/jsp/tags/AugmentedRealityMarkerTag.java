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

public class AugmentedRealityMarkerTag extends SimpleTagSupport {
    static String INDEX =
            AugmentedRealityMarkerTag.class.getName() + ".index";
    //Marker String label
    private String markerLabel;
    //Marker 3D model URL
    private String markerModel;

    public AugmentedRealityMarkerTag()  {
    }

    public void doTag() throws JspException  {
        PageContext pageContext = (PageContext) getJspContext();
        String params = (String) pageContext.getAttribute(AugTag.PARAMS);
        if (null == params)  {
            throw new JspException(
                "AugmentedRealityMarkerTag must be within AugTag" );
        }
        Integer index = (Integer) pageContext.getAttribute(INDEX);
        if (null == index)  {
            index = new Integer(0);
        }
        if (!params.contains("v=vuforia"))  {
            //AR Markers force vuforia viewer
            params += "&" + "v=vuforia";
        }
        String indexedLabel = markerLabel + "_" + index.toString();
        params += "&" + indexedLabel + "=" + markerModel;
        pageContext.setAttribute(AugTag.PARAMS, params);
        pageContext.setAttribute(INDEX, new Integer(index + 1));
    }

    public void setMarkerLabel(String markerLabel) {
        this.markerLabel = markerLabel;
    }

    public void setMarkerModel(String markerModel) {
        this.markerModel = markerModel;
    }

}