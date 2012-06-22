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
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

public class AccordianTag extends TagSupport {


    private static final String ACCORDION_CLASS = "mobi-accordion";
    private String id;
    private String styleClass;
    private String style;
    private boolean autoHeight;
    private int maxHeight;
    private String selectedId;

     private static Logger LOG = Logger.getLogger(AccordianTag.class.getName());

    public int doStartTag() throws JspTagException {

        Writer out = pageContext.getOut();

        StringBuilder sb = new StringBuilder(TagUtil.DIV_TAG);
        sb.append(" id=\"").append(getId()).append("\"");

        sb.append(" class=\"").append(ACCORDION_CLASS);
        if (styleClass != null && !"".equals(styleClass)) {
            sb.append(" ").append( getStyleClass() );
        }
        sb.append("\"");

        if (style != null && !"".equals(style)) {
            sb.append(" style=\"").append(getStyle()).append("\"");
        }
        sb.append(">");
        try {
            out.write(sb.toString());

        } catch (IOException ioe) {


        }

        return  EVAL_BODY_INCLUDE;

    }


    public void encodeScript(Writer writer ) throws IOException {
        //need to initialize the component on the page and can also
        String clientId = getId();
        StringBuilder sb = new StringBuilder(TagUtil.SPAN_TAG);
        sb.append(" id=\"").append( getId() ).append("_script\">");

        sb.append(TagUtil.SCRIPT_TAG);
        sb.append(" type=\"text/javascript\">");

        StringBuilder cfg = new StringBuilder("{ ");
        boolean autoheight = isAutoHeight();
        cfg.append(", autoheight: ").append( autoheight );
        cfg.append(", maxheight: '").append( getMaxHeight()).append("'");
        cfg.append("}");

        writer.write( sb.toString () );
        writer.write("mobi.accordionController.initClient('" + clientId + "'," + cfg.toString()+");");
        writer.write(TagUtil.SCRIPT_TAG_END);
        writer.write(TagUtil.SPAN_TAG_END);
    }

    public String getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(String selectedId) {
        this.selectedId = selectedId;
    }

    public boolean isAutoHeight() {
        return autoHeight;
    }

    public void setAutoHeight(boolean autoHeight) {
        this.autoHeight = autoHeight;
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

     public int getMaxHeight() {
        return maxHeight;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }
}