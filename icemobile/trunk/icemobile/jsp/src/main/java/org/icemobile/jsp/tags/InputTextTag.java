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

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.Writer;

public class InputTextTag extends SimpleTagSupport {

    private String id;
    private String style;
    private String styleClass;
    private String type;
    private String value;

    public void doTag() throws IOException {

        PageContext pageContext = (PageContext) getJspContext();
        boolean isEnhanced = TagUtil.isEnhancedBrowser(pageContext);
        Writer out = pageContext.getOut();

        String element = "input";
        String type = getType();
        if (type != null && type.equalsIgnoreCase("textarea")) {
            element = "textarea";
        }
        out.write("<" + element);
        out.write(" id=\"" + getId() + "\"");
        out.write(" name=\"" + getId() + "\"");

        StringBuilder baseClass = new StringBuilder("mobi-input-text");
        String styleClass = getStyleClass();
        if (styleClass != null && !"".equals(styleClass)) {
            baseClass.append(" ").append(styleClass);
        }

        out.write(" class=\"" + baseClass.toString() + "\"");

        out.write(" value=\"" + getValue() + "\"/>");
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}