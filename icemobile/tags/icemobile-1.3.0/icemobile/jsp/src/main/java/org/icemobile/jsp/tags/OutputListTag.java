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
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 *
 */
public class OutputListTag extends TagSupport {

    public static final String OUTPUTLIST_CLASS = "mobi-list ui-listview ";
    public static final String OUTPUTLISTINSET_CLASS = "mobi-list-inset ui-listview-inset ";
    private static Logger LOG = Logger.getLogger(OutputListTag.class.getName());

    public int doStartTag() throws JspTagException {

        Writer out = pageContext.getOut();

        StringBuilder tag = new StringBuilder(TagUtil.UL_TAG);
        tag.append(" id=\"").append(getId()).append("\"");

        tag.append(" class=\"").append(OUTPUTLIST_CLASS);
        if (isInset()) {
            tag.append(OUTPUTLISTINSET_CLASS);
        }
        if (styleClass != null && !"".equals(styleClass)) {
            tag.append(styleClass);
        }

        if (style != null && !"".equals(style)) {
            tag.append(" style=\"").append(style).append("\"");
        }

        tag.append("\">");

        try {
            out.write(tag.toString());
        } catch (IOException ioe) {
            LOG.severe("IOException writing OutputListTag: " + ioe);
        }
        return EVAL_BODY_INCLUDE;
    }


    public int doEndTag() throws JspTagException {

        Writer out = pageContext.getOut();
        try {
            out.write(TagUtil.UL_TAG_END);
        } catch (IOException ioe) {
            LOG.severe("IOException closing OutputListTag: " + ioe);
        }
        return EVAL_PAGE;
    }

    private boolean inset;
    private String id;
    private String style;
    private String styleClass;
    private boolean group;

    public boolean isInset() {
        return inset;
    }

    public boolean isGroup() {
        return group;
    }

    public void setGroup(boolean group) {
        this.group = group;
    }

    public void setInset(boolean inset) {
        this.inset = inset;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }
}
