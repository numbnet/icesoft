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

import static org.icemobile.util.HTML.CLASS_ATTR;
import static org.icemobile.util.HTML.DIV_ELEM;
import static org.icemobile.util.HTML.ID_ATTR;
import static org.icemobile.util.HTML.SPAN_ELEM;
import static org.icemobile.util.HTML.STYLE_ATTR;

import java.io.IOException;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;

import org.icemobile.component.IAccordion;
import org.icemobile.jsp.tags.BaseBodyTag;
import org.icemobile.jsp.tags.TagWriter;
import org.icemobile.util.CSSUtils;
import org.icemobile.util.ClientDescriptor;
import org.icemobile.util.HTML;
import org.icemobile.util.Utils;

public class AccordionPaneTag extends BaseBodyTag{

    private static final long serialVersionUID = 6211283240843344115L;
    private AccordionTag mParent;
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
        writer = new TagWriter(pageContext);
        try {
             String accordionId = mParent.getClientId();
             String clientId = this.getClientId();
             boolean client = this.isClient();
             String handleClass = "handle " + CSSUtils.STYLECLASS_BAR_B;
             String pointerClass = "pointer";
             writer.startElement(DIV_ELEM, this);
             writer.writeAttribute(ID_ATTR, clientId+"_sect");
             StringBuilder styleClass = new StringBuilder("closed");
             String userDefinedClass = this.getStyleClass();
             if (userDefinedClass != null && userDefinedClass.length() > 0){
                  handleClass+= " " + userDefinedClass;
                  pointerClass+=" " + userDefinedClass;
             }
             writer.writeAttribute(CLASS_ATTR, styleClass);
             writer.writeAttribute(STYLE_ATTR, this.getStyle());
             writer.startElement(DIV_ELEM, this);
             writer.writeAttribute(ID_ATTR, clientId+"_hndl");
             writer.writeAttribute(CLASS_ATTR, handleClass);
             StringBuilder args = new StringBuilder("ice.mobi.accordionController.toggleClient('");
             args.append(accordionId).append("', this, '").append(client).append("'");
             ClientDescriptor cd = mParent.getClient();
             if (Utils.isTransformerHack(cd)) {
                 args.append(", true");
             }
             args.append(");");
             writer.writeAttribute(HTML.ONCLICK_ATTR, args.toString());
             writer.startElement(SPAN_ELEM, this);
             writer.writeAttribute(HTML.CLASS_ATTR, pointerClass);
             writer.endElement(SPAN_ELEM);
             String title = this.getTitle();
             writer.writeText(title);
             writer.endElement(DIV_ELEM);
             writer.startElement(DIV_ELEM);
             writer.writeAttribute(ID_ATTR, clientId+"wrp");
             writer.startElement(DIV_ELEM);
             writer.writeAttribute(ID_ATTR, clientId);
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
            writer.endElement(DIV_ELEM);
            writer.endElement(DIV_ELEM);
            writer.endElement(DIV_ELEM);
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
