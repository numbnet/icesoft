/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package org.icefaces.impl.push.servlet;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProxyHttpServletRequest implements HttpServletRequest {
    private static Logger log = Logger.getLogger(ProxyHttpServletRequest.class.getName());
    private FacesContext facesContext;

    public ProxyHttpServletRequest(FacesContext facesContext) {
        this.facesContext = facesContext;
    }

    public javax.servlet.DispatcherType getDispatcherType()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  void setAsyncTimeout(long timeout)  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return ;
    }

    public  long getAsyncTimeout()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return -1;
    }

    public  void addAsyncListener(javax.servlet.AsyncListener listener)  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return;
    }

    public  void addAsyncListener(javax.servlet.AsyncListener listener, javax.servlet.ServletRequest request, javax.servlet.ServletResponse response)  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return ;
    }

    public  boolean isAsyncStarted(){
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return false;
    }

    public  boolean isAsyncSupported(){
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return false;
    }

    public  javax.servlet.AsyncContext getAsyncContext()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public javax.servlet.AsyncContext startAsync()       throws java.lang.IllegalStateException  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }
    
    public javax.servlet.AsyncContext startAsync(javax.servlet.ServletRequest request, javax.servlet.ServletResponse response)       throws java.lang.IllegalStateException  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }
    
    public  javax.servlet.ServletContext getServletContext() {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.lang.Object getAttribute(java.lang.String name) {
        return facesContext.getExternalContext().getRequestMap().get(name);
    }

    public  java.util.Enumeration getAttributeNames()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.lang.String getCharacterEncoding()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  void setCharacterEncoding(java.lang.String encoding)       throws java.io.UnsupportedEncodingException  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return ;
    }

    public  int getContentLength()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return -1;
    }

    public  java.lang.String getContentType()  {
        return facesContext.getExternalContext().getRequestContentType();
    }

    public  javax.servlet.ServletInputStream getInputStream()       throws java.io.IOException  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.lang.String getParameter(java.lang.String name)  {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        return (String) requestParameterMap.get(name);
    }

    public  java.util.Enumeration getParameterNames()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.lang.String[] getParameterValues(java.lang.String name)  {
        return facesContext.getExternalContext().getRequestParameterValuesMap().get(name);
    }

    public  java.util.Map getParameterMap()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.lang.String getProtocol()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.lang.String getScheme()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.lang.String getServerName()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  int getServerPort()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return -1;
    }

    public  java.io.BufferedReader getReader()       throws java.io.IOException  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.lang.String getRemoteAddr()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.lang.String getRemoteHost()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  void setAttribute(java.lang.String name, java.lang.Object value)  {
        facesContext.getExternalContext().getRequestMap().put(name, value);
    }

    public  void removeAttribute(java.lang.String name)  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return ;
    }

    public  java.util.Locale getLocale()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.util.Enumeration getLocales()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  boolean isSecure()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return false;
    }

    public  javax.servlet.RequestDispatcher getRequestDispatcher(java.lang.String dispatcher)  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.lang.String getRealPath(java.lang.String path)  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  int getRemotePort()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return -1;
    }

    public  java.lang.String getLocalName()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.lang.String getLocalAddr()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  int getLocalPort()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return -1;
    }


    public  java.lang.String getAuthType()  {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  javax.servlet.http.Cookie[] getCookies() {
        Cookie[] cookies = new Cookie[0];
        Map cookieMap = facesContext.getExternalContext().getRequestCookieMap();
        if( cookieMap == null ){
            return cookies;
        }
        return (Cookie[]) cookieMap.values().toArray(cookies);
    }

    public  long getDateHeader(String name) {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return -1;
    }

    public  java.lang.String getHeader(String name) {
        return facesContext.getExternalContext().getRequestHeaderMap().get(name);
    }

    public  java.util.Enumeration<java.lang.String> getHeaders(String name) {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.util.Enumeration<java.lang.String> getHeaderNames() {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  int getIntHeader(String name) {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return -1;
    }

    public  java.lang.String getMethod() {
        // ICE-6371; The getMethod call is not available on the ExternalContext.  While s
        // supported on HttpServletRequest, it's not available on all types of Portlet 2.0
        // requests so we do it reflectively. If it's there, we use it.  If not
        // we just return null rather than fail with an exception.
        Object rawRequestObject = facesContext.getExternalContext().getRequest();
        try {
            Method meth = rawRequestObject.getClass().getMethod("getMethod", new Class[0] );
            Object result = meth.invoke(rawRequestObject);
            return (String)result;
        } catch (Exception e) {
            log.log(Level.FINE, "request object doesn't support 'getMethod' call", e);
        }
        return null;
    }

    public  java.lang.String getPathInfo() {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.lang.String getPathTranslated() {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.lang.String getContextPath() {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.lang.String getQueryString() {
        //get from requestParameterMap
        return "";
    }

    public  java.lang.String getRemoteUser() {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  boolean isUserInRole(String role) {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return false;
    }

    public  java.security.Principal getUserPrincipal() {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.lang.String getRequestedSessionId() {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  java.lang.String getRequestURI() {
        String resourceName = facesContext.getExternalContext()
            .getRequestParameterMap().get("javax.faces.resource");
        if (null != resourceName)  {
            return resourceName;
        }
        return null;
    }

    public  java.lang.StringBuffer getRequestURL() {
        String resourceName = facesContext.getExternalContext()
            .getRequestParameterMap().get("javax.faces.resource");
        if (null != resourceName)  {
            return new StringBuffer(resourceName);
        }
        return null;
    }

    public  java.lang.String getServletPath() {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  javax.servlet.http.HttpSession getSession(boolean create) {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  javax.servlet.http.HttpSession getSession() {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  boolean isRequestedSessionIdValid() {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return false;
    }

    public  boolean isRequestedSessionIdFromCookie() {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return false;
    }

    public  boolean isRequestedSessionIdFromURL() {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return false;
    }

    public  boolean isRequestedSessionIdFromUrl() {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return false;
    }

    public  boolean authenticate(javax.servlet.http.HttpServletResponse response)       throws java.io.IOException, javax.servlet.ServletException {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return false;
    }

    public  void login(String s1, String s2)       throws javax.servlet.ServletException {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return;
    }

    public  void logout()       throws javax.servlet.ServletException {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return ;
    }

    public Collection<Part> getParts() {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }

    public  javax.servlet.http.Part getPart(String name)       throws java.lang.IllegalArgumentException {
        log.severe("ProxyHttpServletRequest unsupported operation");
        if (true) throw new UnsupportedOperationException();
        return null;
    }


}
