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

import java.util.logging.Logger;

import javax.servlet.jsp.JspException;

import org.icemobile.component.IAccordion;
import org.icemobile.renderkit.AccordionCoreRenderer;

public class AccordionTag extends BaseBodyTag implements IAccordion {

    private static Logger LOG = Logger.getLogger(AccordionTag.class.getName());
    
    private String name;
    private boolean autoHeight = true;//default
    private String fixedHeight = "200px";
    private String selectedId;

    public int doStartTag() throws JspException {
        renderer = new AccordionCoreRenderer();
        return _doStartTag(); 
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }
    public void setFixedHeight(String fixedHeight) {
        this.fixedHeight = fixedHeight;        
    }

    public String getFixedHeight() {
        return this.fixedHeight;
    }

    public void setAutoHeight(boolean autoHeight) {
        this.autoHeight = autoHeight;       
    }

    public boolean isAutoHeight() {
        return this.autoHeight;
    }

    public String getJavascriptFileRequestPath() {
        //not used in JSP
        return null;
    }

    public boolean isScriptLoaded() {
       //not used in JSP
        return false;
    }

    public void setScriptLoaded() {
      //not used in JSP
    }

    public String getOpenedPaneClientId() {
        // TODO Auto-generated method stub
        return null;
    }
}