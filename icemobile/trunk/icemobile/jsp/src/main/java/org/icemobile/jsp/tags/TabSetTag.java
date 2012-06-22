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
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.io.Writer;

public class TabSetTag extends TagSupport {

    private final String TABSET_CONTAINER_CLASS = "mobi-tabset ";

    public int doStartTag() throws JspTagException {

        Writer out = pageContext.getOut();


        try {

//        writeJavascriptFile(facesContext, uiComponent, JS_NAME, JS_MIN_NAME, JS_LIBRARY);

            StringBuilder tabset = new StringBuilder(TagUtil.DIV_TAG);
            tabset.append(" id=\"").append(getId()).append("\"");
            tabset.append(" class=\"").append(TABSET_CONTAINER_CLASS);
            if (styleClass != null && !"".equals(styleClass)) {
                tabset.append(styleClass);
            }
            tabset.append("\"");

            if (style != null && !"".equals(style)) {
                tabset.append(" style=\"").append(style).append("\"");
            }
            tabset.append(">");
            out.write(tabset.toString());


        } catch (IOException ioe) {
        }

        mIndex = 0;
        return EVAL_BODY_INCLUDE;
    }


    // Actually, this doesn't require iteration, since it's not that type of problem.
    // The outer tag isn't interpreting the collection and writing child input tags.
    // It's simple execution of the inner tags, whatever they may be.


    public int doEndTag() {

        Writer out = pageContext.getOut();
        try {

            // close the tabs
            out.write(TagUtil.DIV_TAG_END);

            encodeScript(out);

        } catch (IOException ieo) {
        }

        return EVAL_PAGE;
    }


    public void encodeScript(Writer writer) throws IOException {

        String clientId = getId();

        StringBuilder sb = new StringBuilder(TagUtil.SPAN_TAG);
        sb.append(" id=\"").append(clientId).append("_script\">");
        sb.append(TagUtil.SCRIPT_TAG);

        sb.append(" type=\"text/javascript\">");

        StringBuilder cfg = new StringBuilder("{ ");


        //     boolean autoheight = tabset.isAutoHeight();
        cfg.append(" tIndex: ").append(selectedTab);
//        if (tabset.isUpdatePropScriptTag()){
//            cfg.append(", stmp: ").append(System.currentTimeMillis());
//        }
        cfg.append("}");
        //just have to add behaviors if we are going to use them.
        sb.append("ice.mobi.tabsetController.initClient('").append(clientId).
                                                                                    append("',").append(cfg.toString()).append(");");

        sb.append(TagUtil.SCRIPT_TAG_END);
        sb.append(TagUtil.SPAN_TAG_END);
        writer.write(sb.toString());
    }

    private String id;
    private String style;
    private String styleClass;
    private String selectedTab;
    private int mIndex;
//    private String

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

    public String getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(String selectedIndex) {
        this.selectedTab = selectedIndex;
    }

    public String getIndex() {
        return Integer.toString(mIndex++);
    }

    public void resetIndex() {
        mIndex = 0;
    }
}