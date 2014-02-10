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

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.SimpleTagSupport;

import org.icemobile.util.ClientDescriptor;

public abstract class BaseSimpleTag  extends SimpleTagSupport {
	
	protected String id;
    protected boolean disabled;
	protected String style;
	protected String styleClass;
	
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
    	return (HttpServletRequest)getContext().getRequest();
    }
    
    protected HttpServletResponse getResponse(){
    	return (HttpServletResponse)getContext().getResponse();
    }
    
    protected PageContext getContext(){
    	return (PageContext)getJspContext();
    }
    
    protected String getContextRoot(){
    	return getRequest().getContextPath();
    }
    
    ServletContext getServletContext()  {
        return getContext().getServletContext();
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
        return id;
    }
    public void release(){
        this.disabled = false;
        this.styleClass= null;
        this.style = null;
        this.id= null;
    }
}
