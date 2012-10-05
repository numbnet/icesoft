package org.icemobile.jsp.tags;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.tagext.BodyTagSupport;

import org.icemobile.util.ClientDescriptor;

public abstract class BaseBodyTag extends BodyTagSupport{
    
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
        return (HttpServletRequest)pageContext.getRequest();
    }
    
    protected HttpServletResponse getResponse(){
        return (HttpServletResponse)pageContext.getResponse();
    }
    
    protected String getContextRoot(){
        return getRequest().getContextPath();
    }
    
    protected ClientDescriptor getClient(){
        return ClientDescriptor.getInstance(getRequest());
    }
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

}
