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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
 */

package com.icesoft.faces.webapp.parser;

import javax.servlet.http.HttpServletRequest;

/**
 * This is a stubbed out version of the HttpServletRequest class.  Only the
 * mimimum number of members required by the parser are implemented.
 */
public class StubHttpServletRequest implements HttpServletRequest {


    public java.lang.String getAuthType() {
        throw new UnsupportedOperationException();
    }

    public javax.servlet.http.Cookie[] getCookies() {
        throw new UnsupportedOperationException();
    } 

    public long getDateHeader(java.lang.String string) {
        throw new UnsupportedOperationException();
    } 

    public java.lang.String getHeader(java.lang.String string) {
        throw new UnsupportedOperationException();
    } 

    public java.util.Enumeration getHeaders(java.lang.String string) {
        throw new UnsupportedOperationException();
    } 

    public java.util.Enumeration getHeaderNames() {
        throw new UnsupportedOperationException();
    } 

    public int getIntHeader(java.lang.String string) {
        throw new UnsupportedOperationException();
    } 

    public java.lang.String getMethod() {
        throw new UnsupportedOperationException();
    } 

    public java.lang.String getPathInfo() {
        throw new UnsupportedOperationException();
    } 

    public java.lang.String getPathTranslated() {
        throw new UnsupportedOperationException();
    } 

    public java.lang.String getContextPath() {
        throw new UnsupportedOperationException();
    } 

    public java.lang.String getQueryString() {
        throw new UnsupportedOperationException();
    } 

    public java.lang.String getRemoteUser() {
        throw new UnsupportedOperationException();
    } 

    public boolean isUserInRole(java.lang.String string) {
        throw new UnsupportedOperationException();
    } 

    public java.security.Principal getUserPrincipal() {
        throw new UnsupportedOperationException();
    } 

    public java.lang.String getRequestedSessionId() {
        throw new UnsupportedOperationException();
    } 

    public java.lang.String getRequestURI() {
        throw new UnsupportedOperationException();
    } 

    public java.lang.StringBuffer getRequestURL() {
        throw new UnsupportedOperationException();
    } 

    public java.lang.String getServletPath() {
        throw new UnsupportedOperationException();
    } 

    public javax.servlet.http.HttpSession getSession(boolean b) {
        throw new UnsupportedOperationException();
    } 

    public javax.servlet.http.HttpSession getSession() {
        throw new UnsupportedOperationException();
    } 

   public  boolean isRequestedSessionIdValid() {
        throw new UnsupportedOperationException();
    } 

    public boolean isRequestedSessionIdFromCookie() {
        throw new UnsupportedOperationException();
    } 

    public boolean isRequestedSessionIdFromURL() {
        throw new UnsupportedOperationException();
    } 

    /**
     * @deprecated
     */
    public boolean isRequestedSessionIdFromUrl() {
        throw new UnsupportedOperationException();
    }

    public java.lang.Object getAttribute(java.lang.String string) {
        throw new UnsupportedOperationException();
    }

    public java.util.Enumeration getAttributeNames() {
        throw new UnsupportedOperationException();
    }

    public java.lang.String getCharacterEncoding() {
        throw new UnsupportedOperationException();
    }
    public void setCharacterEncoding(java.lang.String string)
            throws java.io.UnsupportedEncodingException {
    }

    public int getContentLength() {
        throw new UnsupportedOperationException();
    }

    public java.lang.String getContentType() {
        throw new UnsupportedOperationException();
    }

    public javax.servlet.ServletInputStream getInputStream() throws java.io.IOException {
        throw new UnsupportedOperationException();
    }

    public java.lang.String getParameter(java.lang.String string) {
        throw new UnsupportedOperationException();
    }

    public java.util.Enumeration getParameterNames() {
        throw new UnsupportedOperationException();
    }

    public java.lang.String[] getParameterValues(java.lang.String string) {
        throw new UnsupportedOperationException();
    }

    public java.util.Map getParameterMap() {
        throw new UnsupportedOperationException();
    }

    public java.lang.String getProtocol() {
        throw new UnsupportedOperationException();
    }

    public java.lang.String getScheme() {
        throw new UnsupportedOperationException();
    }

    public java.lang.String getServerName() {
        throw new UnsupportedOperationException();
    }

    public int getServerPort() {
        throw new UnsupportedOperationException();
    }

    public java.io.BufferedReader getReader() throws java.io.IOException {
        throw new UnsupportedOperationException();
    }

    public java.lang.String getRemoteAddr() {
        throw new UnsupportedOperationException();
    }

    public java.lang.String getRemoteHost() {
        throw new UnsupportedOperationException();
    }

    public void setAttribute(java.lang.String string, java.lang.Object object) {
    }

    public void removeAttribute(java.lang.String string) {
        throw new UnsupportedOperationException();
    }

    public java.util.Locale getLocale() {
        throw new UnsupportedOperationException();
    }

    public java.util.Enumeration getLocales() {
        throw new UnsupportedOperationException();
    }

   public  boolean isSecure() {
        throw new UnsupportedOperationException();
    }

    public javax.servlet.RequestDispatcher getRequestDispatcher(java.lang.String string) {
        throw new UnsupportedOperationException();
    }

    /**
     * @deprecated
     */
    public java.lang.String getRealPath(java.lang.String string) {
        throw new UnsupportedOperationException();
    }

    public int getRemotePort() {
        throw new UnsupportedOperationException();
    }
   public  java.lang.String getLocalName() {
        throw new UnsupportedOperationException();
    }

    public java.lang.String getLocalAddr() {
        throw new UnsupportedOperationException();
    }

    public int getLocalPort() {
        throw new UnsupportedOperationException();
    }

}
