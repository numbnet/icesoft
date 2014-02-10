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

import org.icemobile.component.IDevice;
import org.icemobile.util.Utils;

public class AugTag extends DeviceTag implements IDevice{
    static final String PARAMS = "org.icemobile.jsp.tags.AugTag.params";

    public AugTag()  {
        init();
    }

    public void doInitBody()  {
        pageContext.setAttribute(PARAMS, "");
    }

    public int doAfterBody()  {
        String innerParams = (String) pageContext.getAttribute(PARAMS);
        String baseURL = Utils.getBaseURL(pageContext.getRequest());
        this.params = "ub=" + baseURL + "&" + innerParams;
        pageContext.setAttribute(PARAMS, null);
        return SKIP_BODY;
    }

    public void setParams(String params) {
        String baseURL = Utils.getBaseURL(pageContext.getRequest());
        this.params = "ub=" + baseURL + "&" + params;
    }
    
    public void release(){
        super.release();
        init();
    }
    
    public void init(){
        this.command = "aug";
        this.buttonLabel = "Augmented Reality";
        this.fallbackType = "text";
    }

}