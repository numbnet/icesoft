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

import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.JspException;
import javax.servlet.http.HttpSession;

import java.io.Writer;
import java.io.IOException;

public class DeviceTag extends BodyTagSupport {
    String command = "undefined";
    String label = "unlabeled";
    String params = null;
    String id;
    String style;
    String styleClass;
    boolean disabled;
    String fallbackType = "file";

    public int doEndTag() throws JspException {
        boolean isEnhanced = TagUtil.isEnhancedBrowser(pageContext);
        try {
            Writer out = pageContext.getOut();
            if (isEnhanced)  {
                out.write("<input type='button' id='" + id + "'"); 
                writeStandardAttributes(out);
                String paramClause = "";
                if (null != params)  {
                    paramClause = ", \"" + params + "\"";
                } 
                out.write(" onclick='ice." + command + "(\"" + id + "\"" +
                        paramClause + ");'");

                out.write(" value='" + label + "'>");
            } else {
                if (!TagUtil.isIOS(pageContext))  {
                    out.write(String.format("<input id='%1$s' type='%2$s' name='%1$s'", 
                    		id, fallbackType));
                    writeStandardAttributesForNonEnhanced(out);
                    out.write("/>");
                } else {
                   //or for iOS until we can store the ICEmobile-SX registration
                    //without a session (likely a cookie)
                    out.write("<input type='button' data-id='" + id + "' ");
                    if (null != params)  {
                        out.write("data-params='" + params + "' ");
                    }

                    HttpSession session = pageContext.getSession();
                    if (null != session)  {
                        String sessionID = session.getId();
                        out.write("data-jsessionid='" + sessionID + "' ");
                    }

                    writeStandardAttributes(out);
                    out.write(" data-command='" + command + 
                            "' onclick='ice.mobilesx(this)' ");
                    out.write(" value='" + label + " ...'>");
                }
            }
        } catch (IOException e) {
            throw new JspException(e);
        }
        
        return EVAL_PAGE;
    }

    public void writeStandardAttributes(Writer out) throws IOException  {
    	writeStandardAttributes(out,true);      
    }
    
    public void writeStandardAttributes(Writer out, boolean enhancedOrIOS) throws IOException  {
    	StringBuilder inputStyle = new StringBuilder(enhancedOrIOS ? CommandButtonTag.BASE_STYLE_CLASS : "");
        if (disabled){
            inputStyle.append(CommandButtonTag.DISABLED_STYLE_CLASS);
        }
        if (null != styleClass)  {
            inputStyle.append(" ").append(styleClass);
        }
        if( inputStyle.length() > 0 ){
        	out.write("class='" + inputStyle + "'");
        }
        if (null != style)  {
            out.write(" style='" + style + "'");
        }
        if (disabled)  {
            out.write(" disabled='disabled' ");
        }
    }
    
    /* non-enhanced, non-ios clients only have a file upload rendered, so this does
     * not include the mobi-button style class, which can't be used on input[type='file']
     */
    public void writeStandardAttributesForNonEnhanced(Writer out) throws IOException  {
    	writeStandardAttributes(out,false);
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