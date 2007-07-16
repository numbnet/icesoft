/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */

package com.icesoft.faces.env;

import com.icesoft.jasper.Constants;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * A wrapper for HttpServletRequest.
 * <p/>
 * It is up to the user to ensure that casts to this specific type and use the
 * specific methods if you are running in the appropriate environment.  Also,
 * since we wrap real requests, the state of those requests can get changed by
 * the application server, so it's possible that certain calls may result in
 * exceptions being thrown.
 * <p/>
 */
public class ServletEnvironmentRequest extends CommonEnvironmentRequest
        implements HttpServletRequest {
    private static final Log log =
            LogFactory.getLog(ServletEnvironmentRequest.class);
    private static final String ACEGI_AUTH_CLASS = "org.acegisecurity.Authentication";
    private static Class acegiAuthClass;
    private HttpServletRequest request;
    private Map headers;
    private Cookie[] cookies;
    private String method;
    private String pathInfo;
    private String pathTranslated;
    private String queryString;
    private String requestURI;
    private StringBuffer requestURL;
    private String servletPath;
    private HttpSession servletSession;
    private boolean isRequestedSessionIdFromCookie;
    private boolean isRequestedSessionIdFromURL;
    private String characterEncoding;
    private int contentLength;
    private String contentType;
    private String protocol;
    private String remoteAddr;
    private int remotePort;
    private String remoteHost;
    private String localName;
    private String localAddr;
    private int localPort;
    private AcegiAuthWrapper acegiAuthWrapper;

    static {
        try {
            acegiAuthClass = Class.forName(ACEGI_AUTH_CLASS);
            if (log.isDebugEnabled()) {
                log.debug("Acegi Security engaged.");
            }
        } catch (Throwable t) {
            if (log.isDebugEnabled()) {
                log.debug("Acegi Security not detected.");
            }
        }
    }

    public ServletEnvironmentRequest(Object request) {
        this.request = (HttpServletRequest) request;
        //Copy common data
        authType = this.request.getAuthType();
        contextPath = this.request.getContextPath();
        remoteUser = this.request.getRemoteUser();
        userPrincipal = this.request.getUserPrincipal();
        if (null != acegiAuthClass) {
            if (acegiAuthClass.isInstance(userPrincipal)) {
                acegiAuthWrapper = new AcegiAuthWrapper(userPrincipal);
            }
        }
        requestedSessionId = this.request.getRequestedSessionId();
        requestedSessionIdValid = this.request.isRequestedSessionIdValid();

        attributes = new Hashtable();
        Enumeration attributeNames = this.request.getAttributeNames();
        while (attributeNames.hasMoreElements()) {
            String name = (String) attributeNames.nextElement();
            Object attribute = this.request.getAttribute(name);
            if ((null != name) && (null != attribute)) {
                attributes.put(name, attribute);
            }
        }

        // Warning:  For some reason, the various javax.include.* attributes are
        // not available via the getAttributeNames() call.  This may be limited
        // to a Liferay issue but when the MainPortlet dispatches the call to
        // the MainServlet, all of the javax.include.* attributes can be
        // retrieved using this.request.getAttribute() but they do NOT appear in
        // the Enumeration of names returned by getAttributeNames().  So here
        // we manually add them to our map to ensure we can find them later.
        String[] incAttrKeys = Constants.INC_CONSTANTS;
        for (int index = 0; index < incAttrKeys.length; index++) {
            String incAttrKey = incAttrKeys[index];
            Object incAttrVal = this.request.getAttribute(incAttrKey);
            if (incAttrVal != null) {
                attributes.put(incAttrKey, this.request.getAttribute(incAttrKey));
            }
        }

        headers = new HashMap();
        Enumeration headerNames = this.request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = (String) headerNames.nextElement();
            Enumeration values = this.request.getHeaders(name);
            headers.put(name, Collections.list(values));
        }

        parameters = new HashMap();
        Enumeration parameterNames = this.request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = (String) parameterNames.nextElement();
            parameters.put(name, this.request.getParameterValues(name));
        }

        scheme = this.request.getScheme();
        serverName = this.request.getServerName();
        serverPort = this.request.getServerPort();
        locale = this.request.getLocale();
        locales = Collections.list(this.request.getLocales());
        secure = this.request.isSecure();

        //Copy servlet specific data
        cookies = this.request.getCookies();
        method = this.request.getMethod();
        pathInfo = this.request.getPathInfo();
        pathTranslated = this.request.getPathTranslated();
        queryString = this.request.getQueryString();
        requestURI = this.request.getRequestURI();
        requestURL = this.request.getRequestURL();
        servletPath = this.request.getServletPath();
        servletSession = this.request.getSession();
        isRequestedSessionIdFromCookie = this.request.isRequestedSessionIdFromCookie();
        isRequestedSessionIdFromURL = this.request.isRequestedSessionIdFromURL();
        characterEncoding = this.request.getCharacterEncoding();
        contentLength = this.request.getContentLength();
        contentType = this.request.getContentType();
        protocol = this.request.getProtocol();
        remoteAddr = this.request.getRemoteAddr();
        remoteHost = this.request.getRemoteHost();
        remotePort = this.request.getRemotePort();
        localName = this.request.getLocalName();
        localAddr = this.request.getLocalAddr();
        localPort = this.request.getLocalPort();
    }

    public boolean isUserInRole(String role) {
        if (null != acegiAuthWrapper) {
            return acegiAuthWrapper.isUserInRole(role);
        }
        return request.isUserInRole(role);
    }

    public Cookie[] getCookies() {
        return cookies;
    }

    public void setAttribute(String name, Object value) {
        super.setAttribute(name, value);
        try {
            request.setAttribute(name, value);
        } catch (Exception e) {
            //ignore because the container disposed servletRequest by now 
        }
    }

    public void removeAttribute(String name) {
        super.removeAttribute(name);
        try {
            request.removeAttribute(name);
        } catch (Exception e) {
            //ignore because the container disposed servletRequest by now
        }
    }

    public long getDateHeader(String name) {
        String header = getHeader(name);
        if (header == null) {
            return -1;
        }
        //TODO
        //Convert header string to a date
        return -1;
    }

    public String getHeader(String name) {
        List values = (List) headers.get(name);
        return values == null || values.isEmpty() ?
                null : (String) values.get(0);
    }

    public Enumeration getHeaders(String name) {
        List values = (List) headers.get(name);
        return Collections.enumeration(values);
    }

    public Enumeration getHeaderNames() {
        return Collections.enumeration(headers.keySet());
    }

    public int getIntHeader(String name) {
        String header = getHeader(name);
        if (header == null) {
            return -1;
        }
        return Integer.parseInt(name, -1);
    }

    public String getMethod() {
        return method;
    }

    public String getPathInfo() {
        return pathInfo;
    }

    public String getPathTranslated() {
        return pathTranslated;
    }

    public String getQueryString() {
        return queryString;
    }

    public String getRequestURI() {
        return requestURI;
    }

    public StringBuffer getRequestURL() {
        return requestURL;
    }

    public String getServletPath() {
        return servletPath;
    }

    public HttpSession getSession(boolean create) {
        return request.getSession(create);
    }

    public HttpSession getSession() {
        return servletSession;
    }

    public boolean isRequestedSessionIdFromCookie() {
        return isRequestedSessionIdFromCookie;
    }

    public boolean isRequestedSessionIdFromURL() {
        return isRequestedSessionIdFromURL;
    }

    public boolean isRequestedSessionIdFromUrl() {
        return isRequestedSessionIdFromURL();
    }

    public String getCharacterEncoding() {
        return characterEncoding;
    }

    public void setCharacterEncoding(String encoding) throws UnsupportedEncodingException {
        characterEncoding = encoding;
    }

    public int getContentLength() {
        return contentLength;
    }

    public String getContentType() {
        return contentType;
    }

    public ServletInputStream getInputStream() throws IOException {
        return request.getInputStream();
    }

    public String getProtocol() {
        return protocol;
    }

    public BufferedReader getReader() throws IOException {
        return request.getReader();
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public String getRemoteHost() {
        return remoteHost;
    }

    public RequestDispatcher getRequestDispatcher(String name) {
        return request.getRequestDispatcher(name);
    }

    public String getRealPath(String path) {
        return request.getRealPath(path);
    }

    public int getRemotePort() {
        return remotePort;
    }

    public String getLocalName() {
        return localName;
    }

    public String getLocalAddr() {
        return localAddr;
    }

    public int getLocalPort() {
        return localPort;
    }
}
