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

package org.icemobile.jsp.tags.layout;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import org.icemobile.component.IAccordion;
import org.icemobile.jsp.tags.BaseBodyTag;
import org.icemobile.jsp.tags.TagWriter;
import org.icemobile.jsp.util.MobiJspConstants;
import org.icemobile.jsp.util.Util;
import org.icemobile.renderkit.AccordionCoreRenderer;

public class AccordionTag extends BaseBodyTag implements IAccordion {

    private static Logger LOG = Logger.getLogger(AccordionTag.class.getName());
    
    private String name;
    private boolean autoHeight = true;//default
    private String height;
    private String selectedId;
    private AccordionCoreRenderer renderer;
    private TagWriter writer;

    public int doStartTag() throws JspException {
        renderer = new AccordionCoreRenderer();
       try {
            writer = new TagWriter(pageContext);
            renderer.encodeBegin(this, writer,true);
            writer.closeOffTag();
        } catch (IOException e) {
            throw new JspTagException("problem in doStart of AccordionTag exception="+e) ;
        }
        return EVAL_BODY_INCLUDE;
    }
   /**
     *
     * @return
     * @throws javax.servlet.jsp.JspTagException
     */
    public int doEndTag() throws JspTagException {
        try {
            renderer.encodeEnd(this, writer, true);
        } catch (IOException e) {
            throw new JspTagException("problem in doEnd of AccordionTag exception="+e) ;
        }
        return EVAL_PAGE;
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
    public void setHeight(String fixedHeight) {
        this.height = fixedHeight;
    }

    public String getHeight() {
        return this.height;
    }

    public void setAutoHeight(boolean autoHeight) {
        this.autoHeight = autoHeight;       
    }

    public boolean isAutoHeight() {
        return this.autoHeight;
    }
    /**
     *  responsible for providing the src attribute to load the javascript
     *  not used yet.
     * @return
     */
    public String getJavascriptFileRequestPath() {
        String separator = "/";
        StringBuilder sb = new StringBuilder();
        sb.append(Util.getContextRoot(pageContext.getRequest()));
        sb.append(MobiJspConstants.RESOURCE_BASE_URL).append(separator);
        sb.append(IAccordion.LIB_JSP).append(separator).append(IAccordion.JS_NAME);
        System.out.println("javscript src = "+sb.toString());
        return sb.toString();
    }

    public String getOpenedPaneClientId() {
        return selectedId;
    }

    /** this is not required for jsp since no domDiff to wipe out update script */
    public String getHashVal(){
        return null;
    }

    public void release(){
        this.renderer = null;
        this.writer = null;
        this.name = null;
    }
}