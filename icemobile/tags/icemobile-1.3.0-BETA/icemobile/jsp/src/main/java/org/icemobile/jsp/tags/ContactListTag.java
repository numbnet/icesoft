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

import java.io.IOException;
import javax.servlet.http.HttpSession;

import org.icemobile.component.IContactList;
import org.icemobile.renderkit.ContactListCoreRenderer;

public class ContactListTag extends BaseSimpleTag implements IContactList {

    private String buttonLabel;
    private String pattern;
    private String fields;
    
    public ContactListTag(){
        init();
    }

    public void doTag() throws IOException {
        
        ContactListCoreRenderer renderer = new ContactListCoreRenderer();
        renderer.encode(this,  new TagWriter(getContext()));
    }

    public String getButtonLabel() {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }
    
    private void init(){
        this.buttonLabel = "Fetch Contact";
        this.fields= "name, email, phone";
    }
    
    public void release(){
        super.release();
        init();
    }

    public String getScript(String id, boolean isSX)  {
        return "ice.mobi.sx(this)";
    }

    public String getPostURL()  {
        return null;
    }

    //this will not work on websphere
    public String getSessionId(){
        HttpSession session = getRequest().getSession(false);
        if (null == session)  {
            return null;
        }
        return session.getId();
    }

}
