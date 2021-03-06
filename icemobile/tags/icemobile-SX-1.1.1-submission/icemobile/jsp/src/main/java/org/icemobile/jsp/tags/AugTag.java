/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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

import javax.servlet.jsp.PageContext;

public class AugTag extends DeviceTag {
    static final String PARAMS = "org.icemobile.jsp.tags.AugTag.params";

    public AugTag()  {
        this.command = "aug";
        this.label = "Reality";
        this.fallbackType = "text";
    }

    public void doInitBody()  {
        pageContext.setAttribute(PARAMS, "");
    }

    public int doAfterBody()  {
        String innerParams = (String) pageContext.getAttribute(PARAMS);
        String baseURL = TagUtil.getBaseURL(pageContext.getRequest());
        this.params = "ub=" + baseURL + "&" + innerParams;
        pageContext.setAttribute(PARAMS, null);
        return SKIP_BODY;
    }

    public void setParams(String params) {
        String baseURL = TagUtil.getBaseURL(pageContext.getRequest());
        this.params = "ub=" + baseURL + "&" + params;
    }

}