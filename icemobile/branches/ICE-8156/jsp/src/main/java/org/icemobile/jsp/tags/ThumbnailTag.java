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

public class ThumbnailTag extends SimpleTagSupport {

    private String id;
    private String style;
    private String styleClass;
    private String mFor;

    public void doTag() throws IOException {

        PageContext pageContext = (PageContext) getJspContext();

        Writer out = pageContext.getOut();

        out.write("<span ");

        if (id != null && !"".equals(id)) {
            out.write(" id='" + getId() + "'");
        }
        if (null != styleClass)  {
            out.write(" class='" + styleClass + "'");
        }
        if (null != style)  {
            out.write(" style='" + style + "'");
        }
        out.write("><img height='64' width='64' id='" + mFor + "-thumb'></span>");
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
    
    public String getFor() {
        return mFor;
    }

    public void setFor(String mFor) {
        this.mFor = mFor;
    }

}