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

import org.icemobile.util.ClientDescriptor;
import org.icemobile.util.UserAgentInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 *
 */
public class FlipSwitchTag extends SimpleTagSupport {

    public static final String FLIPSWITCH_ON_CLASS = "mobi-flipswitch mobi-flipswitch-on ui-btn-down-c";
    public static final String FLIPSWITCH_OFF_CLASS = "mobi-flipswitch mobi-flipswitch-off ui-btn-down-c";

    private static Logger LOG = Logger.getLogger(FlipSwitchTag.class.getName());


    public void doTag() throws IOException {


        PageContext pageContext = (PageContext) getJspContext();
        Writer out = pageContext.getOut();

        out.write("<a id=\"" + getId() + "\"");

        String autoClass = FLIPSWITCH_OFF_CLASS;
        boolean isChecked = isChecked(value);
        if (isChecked) {
            autoClass = FLIPSWITCH_ON_CLASS;
        }
        out.write(" class=\"" + autoClass);
        if (styleClass != null && !"".equals(styleClass)) {
            out.write(" " + this.styleClass);
        }
        out.write("\"");
        if (style != null && !"".equals(style)) {
            out.write(" style=\"" + this.style + "\"");
        }
        if (isDisabled()) {
            out.write(" disabled=\"true\"");
        }

        // write javascript
        StringBuilder builder = new StringBuilder(255);
        builder.append("ice.mobi.flipvalue('").append(getId()).append("' ,{ event: event, elVal: this");
        if ( isTransformerHack(pageContext )) {
            builder.append(", transHack: 'true'");
        }
        builder.append("} );");

        if (!isDisabled() | !isReadOnly()) {
            out.write(" onclick=\"" + builder.toString() + "\"");
        }
        out.write(">");

        // write the nested child objects containing both on/off labels.
        out.write("<span class='mobi-flipswitch-txt-on" + (isChecked ? "" : " ui-btn-up-c" ) + "'>");
        out.write(getLabelOn());
        out.write("</span>");

        // Concat _hidden to the id for uniqueness
        if (name != null && !"".equals(name)) {
            out.write("<input id=\"" + getId() + "_hidden\"");
            out.write(" name=\"" + getName() + "\"");
            out.write(" value=\"" + getValue() + "\" type=\"hidden\"/>");

        } else {
            LOG.warning("Flipswitch tag (id: " + id + ") has no name for value submission");
        }

        out.write("<span class='mobi-flipswitch-txt-off" + (isChecked ? " ui-btn-up-c" : "") + "'>");
        out.write(getLabelOff());
        out.write("</span>");
        out.write("</input>");
        out.write("</a>");
    }

    private boolean isTransformerHack(PageContext pageContext) {

        Object request = pageContext.getRequest();
        if (request instanceof HttpServletRequest) {
            HttpServletRequest hsr = (HttpServletRequest) request;
            ClientDescriptor client = ClientDescriptor.getInstance(hsr);

            String ua = client.getUserAgent();
            if (ua != null) {
                ua = ua.toLowerCase();
                return ua.contains( UserAgentInfo.TABLET_TRANSORMER_PRIME );
            }
        }
        return false;
    }

    private boolean isChecked(String hiddenValue) {
        if (hiddenValue == null) {
            return false;
        }
        return Boolean.valueOf(hiddenValue);
    }

    private String id;
    private String name;
    private String style;
    private String styleClass;

    private boolean disabled;
    // value will be set to true/false
    private String value;
    private boolean readOnly;
    private String labelOn;
    private String labelOff;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getLabelOn() {
        return labelOn;
    }

    public void setLabelOn(String labelOn) {
        this.labelOn = labelOn;
    }

    public String getLabelOff() {
        return labelOff;
    }

    public void setLabelOff(String labelOff) {
        this.labelOff = labelOff;
    }
}
