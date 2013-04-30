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

import org.icemobile.component.IAccordion;
import org.icemobile.jsp.tags.BaseBodyTag;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

import org.icemobile.component.IContentPane;
import org.icemobile.jsp.tags.TagWriter;
import org.icemobile.renderkit.AccordionPaneCoreRenderer;

public class AccordionPaneTag extends BaseBodyTag implements IContentPane {

    private static Logger LOG = Logger.getLogger(AccordionPaneTag.class.getName());

    private AccordionTag mParent;
    private AccordionPaneCoreRenderer renderer;
    private boolean accordionPane;
    private TagWriter writer;

    public void setParent(Tag parent) {

        if (!(parent instanceof AccordionTag)) {
            throw new IllegalArgumentException("AccordionPane must be child of AccordionTag");
        }
        this.accordionPane=true;
        mParent = (AccordionTag) parent;
    }

    public int doStartTag() throws JspTagException {
        renderer = new AccordionPaneCoreRenderer();
        writer = new TagWriter(pageContext);
         try {
            renderer.encodeBegin(this, writer, true, false);
            writer.closeOffTag();
        } catch (IOException e) {
            throw new JspTagException("problem in doStart of AccordionPaneTag exception="+e) ;
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

    // Tag attributes
    private String title;

    public boolean isAccordionPane() {
        return this.accordionPane;
    }

    public IAccordion getAccordionParent() {
        return mParent;
    }

    public boolean isStackPane() {
        return false;
    }

    public boolean isTabPane() {
        return false;
    }

    public boolean isClient() {
        return false;
    }

    public String getId() {
        return super.getClientId();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public void release(){
        this.renderer = null;
        this.mParent = null;
        this.title = null;
    }
}
