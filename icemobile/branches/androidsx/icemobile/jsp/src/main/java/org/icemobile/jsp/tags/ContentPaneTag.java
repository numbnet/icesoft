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
            mMyIndex = mParent.getIndex();
        }
        mSelectedItem = mParent.getSelectedItem();
    }


    public int doStartTag() throws JspTagException {

        Writer out = pageContext.getOut();
        if (mParent.isTabParent()) {
            encodeTabContent(out);
        }
        return EVAL_BODY_INCLUDE;
    }

    /**
     * The Tabset generates an id based on the childs sibling index. In order
     * to duplicate this this object needs to fetch the index from a parent
     */
    private void encodeTabContent(Writer out) {

        StringBuilder tag = new StringBuilder(TagUtil.DIV_TAG);
        tag.append(" id=\"").append(mParent.getId()).append("tab").append(mMyIndex).append("_wrapper\"");

        tag.append(" class=\"");
        if (mMyIndex.equals(mSelectedItem)) {
            tag.append(mActiveContentClass);
        } else {
            tag.append(mPassiveContentClass);
        }
        if (styleClass != null && !"".equals(styleClass)) {
            tag.append(" ").append(styleClass);
        }
        tag.append("\"");

        if (style != null && !"".equals(style)) {
            tag.append(" style=\"").append(style).append("\"");
        }
        tag.append(">");

        try {
            out.write(tag.toString());
        } catch (IOException ioe) {
            LOG.severe("IOException starting ContentTag: " + ioe);
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
            if (mParent.isTabParent()) {
                out.write(TagUtil.DIV_TAG_END);
            }
        } catch (IOException ioe) {
            LOG.severe("IOException closing ContentTag: " + ioe);
        }
        return EVAL_PAGE;
    }

    // Tag attributes
    private String styleClass;
    private String style;
    private String title;

    private String id;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
