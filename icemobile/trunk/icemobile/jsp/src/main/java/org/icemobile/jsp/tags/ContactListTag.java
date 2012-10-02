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
 */

package org.icemobile.jsp.tags;

import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;
import java.io.Writer;
import java.util.logging.Logger;

/**
 *
 */
public class ContactListTag extends SimpleTagSupport {


    private static Logger LOG = Logger.getLogger(ContactListTag.class.getName());

    public void doTag() throws IOException {

        PageContext pageContext = (PageContext) getJspContext();
        Writer out = pageContext.getOut();

        TagWriter tag = new TagWriter(pageContext);

        tag.startDiv();
        tag.writeAttribute("id", getId());
        tag.startElement("input");
        tag.writeAttribute("type", "button");


        StringBuilder args = new StringBuilder();

        if (pattern != null && !"".equals(pattern)) {
            args.append("pattern=").append(pattern);
        }


        if (multipleSelect) {
            if (args.length() > 0) {
                args.append("&");
            }
            args.append("select=multiple");
        }

        if (args.length() > 0) {
            tag.writeAttribute("onclick", "ice.fetchContacts('" + getId() + "', '" + args.toString() + "' );");
        } else {
            tag.writeAttribute("onclick", "ice.fetchContacts('" + getId() + "' );");
        }
        tag.writeAttribute("value", getLabel());
        tag.endElement();
        tag.endElement();

    }

    private String label;
    private String id;
    private String pattern;
    private boolean multipleSelect;


    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public boolean isMultipleSelect() {
        return multipleSelect;
    }

    public void setMultipleSelect(boolean multipleSelect) {
        this.multipleSelect = multipleSelect;
    }
}
