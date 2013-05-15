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
import java.util.logging.Logger;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.icemobile.component.IMobiComponent;
import org.icemobile.renderkit.IRenderer;
import org.icemobile.util.ClientDescriptor;

public abstract class BaseBodyTag extends BodyTagSupport implements IMobiComponent{
    
    private static Logger LOG = Logger.getLogger(BaseBodyTag.class.getName());
    
    protected String id;
    protected boolean disabled;
    protected String style;
    protected String styleClass;
    protected IRenderer renderer;
    protected TagWriter writer;
    
    /* Base impl of doStartTag
     * Subclasses must set renderer on Tag before calling
     */
    protected int _doStartTag() throws JspException {
        LOG.warning("start");
        
        try {
            writer = new TagWriter(pageContext);
            renderer.encodeBegin(this, writer, true);
            //writer.closeOffTag();
        }catch (IOException e) {
            throw new JspException(e);
        }
        LOG.warning("end");
        return EVAL_BODY_INCLUDE;
    }
    
    public int doEndTag() throws JspException{
        LOG.warning("start");
        try {
            renderer.encodeEnd(this, writer, true);
        }catch (IOException e) {
            throw new JspException(e);
        }
        LOG.warning("end");
        return EVAL_PAGE;
    }
    
    public boolean isDisabled() {
        return disabled;
    }
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
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
    protected Cookie getCookie(String cookieName){
        Cookie cookie = null;
        Cookie[] cookies = getRequest().getCookies();
        if ( cookies != null ) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookieName.equals(cookies[i].getName()) ) {
                    cookie = cookies[i];
                }
            }
        }
        return cookie;
    }
    
    protected HttpServletRequest getRequest(){
        return (HttpServletRequest)pageContext.getRequest();
    }
    
    protected HttpServletResponse getResponse(){
        return (HttpServletResponse)pageContext.getResponse();
    }

    protected String getContextRoot(){
        return getRequest().getContextPath();
    }
    
    public ClientDescriptor getClient(){
        return ClientDescriptor.getInstance(getRequest());
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getClientId(){
        return this.id;
    }
    public void release(){
        this.disabled = false;
        this.styleClass= null;
        this.style = null;
        this.id= null;
        this.renderer = null;
        this.writer = null;
    }

}
