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

import static org.icemobile.util.HTML.*;

import java.io.IOException;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;

import org.icemobile.util.ClientDescriptor;

public class DeviceTag extends BaseBodyTag {
    
    private static final String CONTAINER_ONCLICK = "ice.%s('%s');";
    private static final String CONTAINER_ONCLICK_PARAMS = "ice.%s('%s','%s');";
    
    String command = "undefined";
    String label = "unlabeled";
    String params = null;
    String fallbackType = "file";

    public int doEndTag() throws JspException {
        ClientDescriptor client = getClient();
        try {
            TagWriter writer = new TagWriter(pageContext);
            writer.startElement(INPUT_ELEM);
            if (client.isICEmobileContainer()){
                writer.writeAttribute(TYPE_ATTR, "button");
                writer.writeAttribute(ID_ATTR, id);
                writeStandardAttributes(writer);
                String onclick = null;
                if (null != params)  {
                    onclick = String.format(CONTAINER_ONCLICK_PARAMS, command, id, params);
                } 
                else{
                    onclick = String.format(CONTAINER_ONCLICK, command, id);
                }
                writer.writeAttribute(ONCLICK_ATTR, onclick);
                writer.writeAttribute(VALUE_ATTR, label);
            } else {
                if (!client.isIOS())  {
                    writer.writeAttribute(ID_ATTR, id);
                    writer.writeAttribute(TYPE_ATTR, fallbackType);
                    writer.writeAttribute(NAME_ATTR, id);
                    writeStandardAttributesForNonEnhanced(writer);
                } else {
                   //or for iOS until we can store the ICEmobile-SX registration
                    //without a session (likely a cookie)
                    writer.writeAttribute(TYPE_ATTR, "button");
                    writer.writeAttribute("data-id", id);
                    if (null != params)  {
                        writer.writeAttribute("data-params", params);
                    }

                    HttpSession session = getRequest().getSession();
                    if (null != session)  {
                        String sessionID = session.getId();
                        writer.writeAttribute("data-jsessionid", sessionID);
                    }
                    writeStandardAttributes(writer);
                    writer.writeAttribute("data-command", command);
                    writer.writeAttribute(ONCLICK_ATTR, "ice.mobilesx(this)");
                    writer.writeAttribute(VALUE_ATTR, label);
                }
            }
            writer.endElement();
        } catch (IOException e) {
            throw new JspException(e);
        }
        return EVAL_PAGE;
    }

    public void writeStandardAttributes(TagWriter writer) throws IOException  {
    	writeStandardAttributes(writer,true);      
    }
    
    public void writeStandardAttributes(TagWriter writer, boolean enhancedOrIOS) throws IOException  {
    	StringBuilder inputStyle = new StringBuilder(enhancedOrIOS ? CommandButtonTag.BASE_STYLE_CLASS : "");
        if (disabled){
            inputStyle.append(CommandButtonTag.DISABLED_STYLE_CLASS);
        }
        if (null != styleClass)  {
            inputStyle.append(" ").append(styleClass);
        }
        if( inputStyle.length() > 0 ){
            writer.writeAttribute(CLASS_ATTR, inputStyle);
        }
        if (null != style)  {
            writer.writeAttribute(STYLE_ATTR, style);
        }
        if (disabled)  {
            writer.writeAttribute(DISABLED_ATTR, "disabled");
        }
    }
    
    /* non-enhanced, non-ios clients only have a file upload rendered, so this does
     * not include the mobi-button style class, which can't be used on input[type='file']
     */
    public void writeStandardAttributesForNonEnhanced(TagWriter writer) throws IOException  {
    	writeStandardAttributes(writer,false);
    }
    public String getParams() {
        return styleClass;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

}