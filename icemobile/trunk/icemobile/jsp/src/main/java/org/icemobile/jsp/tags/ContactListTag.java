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

import java.io.IOException;

import org.icemobile.component.IContactList;
import org.icemobile.renderkit.ContactListCoreRenderer;

public class ContactListTag extends BaseSimpleTag implements IContactList {

    private String label;
    private String pattern;
    private boolean multipleSelect;
    private String fields;

    public void doTag() throws IOException {
        
        ContactListCoreRenderer renderer = new ContactListCoreRenderer();
        renderer.encode(this,  new TagWriter(getContext()));
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }
    public void release(){
        super.release();
        this.label = null;
        this.pattern = null;
        this.fields= null;
        this.multipleSelect = false;
    }

}
