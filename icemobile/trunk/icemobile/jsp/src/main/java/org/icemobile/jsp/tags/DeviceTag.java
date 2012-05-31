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

import javax.servlet.jsp.tagext.SimpleTagSupport;
import javax.servlet.jsp.PageContext;

import java.io.Writer;
import java.io.IOException;

public class DeviceTag extends SimpleTagSupport {
    String command = "undefined";
    String label = "unlabeled";
    String id;
    String style;
    String styleClass;
    boolean disabled;

    public void doTag() throws IOException {
        PageContext pageContext = (PageContext) getJspContext();
        boolean isEnhanced = TagUtil.isEnhancedBrowser(pageContext);
        Writer out = pageContext.getOut();
        if (isEnhanced)  {
            out.write("<input type='button' id='" + id + "'"); 
            writeStandardAttributes(out);
            out.write(" onclick='ice." + command + "(\"" + id + "\");'");
            out.write(" value='" + label + "'>");
        } else {
            out.write("<input id='" + id + "' type='file' name='" + id + "' />");
            //or for iOS until we can store the ICEmobile-SX registration
            //without a session (likely a cookie)
            out.write("<input type='button' data-id='" + id + "'");
            writeStandardAttributes(out);
            out.write(" data-command='" + command + "' onclick='ice.mobilesx(this)' ");
            out.write(" value='" + label + " (ICEmobile-SX)'>");
        }
    }

    public void writeStandardAttributes(Writer out) throws IOException  {
        if (null != styleClass)  {
            out.write(" class='" + styleClass + "'");
        }
        if (null != style)  {
            out.write(" style='" + style + "'");
        }
        if (disabled)  {
            out.write(" disabled='disabled'");
        }
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

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

}