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
 *
 */

package org.icemobile.jsp.tags;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

public class ContentPaneTag extends TagSupport {

    private static final String MOBI_TABPAGE = "mobi-tabpage";
    private static final String MOBI_TABPAGE_HIDDEN = "mobi-tabpage-hidden";
    private static Logger LOG = Logger.getLogger(ContentTag.class.getName());

    private String mActiveContentClass;
    private String mPassiveContentClass;

    private ContentTag mParent;
    private String mSelectedItem;

    // Values for tabset inherited content
    private String mMyIndex;

    public void setParent(Tag parent) {

        if (!(parent instanceof ContentTag)) {
            throw new IllegalArgumentException("ContentPane must be child of ContentTag");
        }

        mParent = (ContentTag) parent;
        if (mParent.isTabParent()) {
            mActiveContentClass = MOBI_TABPAGE;
            mPassiveContentClass = MOBI_TABPAGE_HIDDEN;

        } else {
//
            mActiveContentClass = "open";
            mPassiveContentClass = "closed";

        }
        mSelectedItem = mParent.getSelectedItem();
        mMyIndex = mParent.getIndex();
    }


    public int doStartTag() throws JspTagException {

        Writer out = pageContext.getOut();
        if (mParent.isTabParent()) {
            encodeTabContent(out);
        } else {
            encodeAccordionContent(out);
        }
        return EVAL_BODY_INCLUDE;
    }

    private void encodeTabContent(Writer out) {

        StringBuilder tag = new StringBuilder(TagUtil.DIV_TAG);
        tag.append(" id=\"").append(mParent.getId()).append("tab").append(mMyIndex).append("_wrapper\"");

        tag.append(" class=\"");
        if (mMyIndex.equals(mSelectedItem)) {
            tag.append(mActiveContentClass);
        } else {
            tag.append(mPassiveContentClass);
        }
        tag.append("\" >");

        try {
            out.write(tag.toString());
        } catch (IOException ioe) {
            LOG.severe("IOException starting ContentTag: " + ioe);
        }
    }

    private void encodeAccordionContent(Writer out) {

        StringBuilder tag = new StringBuilder(TagUtil.SECTION_TAG);

        if (id != null) {
            tag.append(" id=\"").append(id).append("\"");
        }

        if (mSelectedItem != null && mSelectedItem.equals(id)) {
            tag.append(" class=\"").append(mActiveContentClass).append("\"");
        } else {
            tag.append(" class=\"").append(mPassiveContentClass).append("\"");
        }

        tag.append(">");

        tag.append(TagUtil.DIV_TAG).append(" class=\"handle\"");
        tag.append(" onclick=\"").append("ice.mobi.accordionController.toggleClient('")
                .append(mParent.getId()).append("', this, false);\">");

        tag.append(TagUtil.DIV_TAG).append(" class=\"pointer\">&#9658;</div>");
        if (botitle != null && !"".equals(botitle)) {
            tag.append(botitle);
        }

        try {

            out.write(tag.toString());
        } catch (IOException ioe) {
            LOG.severe("IOException starting Accordion ContentTag: " + ioe);
        }
    }


    /**
     * Common end tag structure
     *
     * @return
     * @throws JspTagException
     */
    public int doEndTag() throws JspTagException {

        Writer out = pageContext.getOut();

        try {
            out.write(TagUtil.DIV_TAG_END);

            if (!mParent.isTabParent()) {
                out.write(TagUtil.SECTION_TAG_END);
            }
        } catch (IOException ioe) {
            LOG.severe("IOException closing ContentTag: " + ioe);
        }
        return EVAL_PAGE;
    }

    private String styleClass;
    private String style;
    private String botitle;

    private String id;

    public String getBotitle() {
        return botitle;
    }

    public void setBotitle(String title) {
        this.botitle = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }
}
