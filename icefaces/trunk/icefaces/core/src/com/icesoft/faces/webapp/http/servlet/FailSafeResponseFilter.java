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

package com.icesoft.faces.webapp.http.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import javax.servlet.http.HttpServletRequestWrapper;

public class FailSafeResponseFilter implements Filter  {
    FilterConfig filterConfig;
    ServletContext servletContext;

    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        this.servletContext = filterConfig.getServletContext();
    }

    public void destroy() {
    }

    public void doFilter ( ServletRequest request, ServletResponse response,
                        FilterChain chain ) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        chain.doFilter( new FailSafeRequestWrapper(httpRequest), 
                new FailSafeResponseWrapper(httpResponse) );
    }

}

class FailSafeRequestWrapper extends HttpServletRequestWrapper  {
    HttpServletRequest request;
    String contextPath;
    String serverName;

    public FailSafeRequestWrapper(HttpServletRequest request) {
        super(request);
        this.request = request;
        this.contextPath = request.getContextPath();
        this.serverName = request.getServerName();
    }
    
    public String getContextPath() {
        String result = contextPath;
        try {
            result = request.getContextPath();
        } catch (Exception e)  {
        }
        return result;
    }

    public String getServerName() {
        String result = serverName;
        try {
            result = request.getServerName();
        } catch (Exception e)  {
        }
        return result;
    }
}

class FailSafeResponseWrapper extends HttpServletResponseWrapper  {
    HttpServletResponse response;

    public FailSafeResponseWrapper(HttpServletResponse response) {
        super(response);
        this.response = response;
    }

    public String encodeRedirectUrl(String url) {
        String result = url;
        try {
            result = response.encodeRedirectURL(url);
        } catch (Exception e)  {
        }
        return result;
    }

    public String encodeUrl(String url) {
        String result = url;
        try {
            result = response.encodeURL(url);
        } catch (Exception e)  {
        }
        return result;
    }

    public String encodeRedirectURL(String url) {
        String result = url;
        try {
            result = response.encodeRedirectURL(url);
        } catch (Exception e)  {
        }
        return result;
    }

    public String encodeURL(String url) {
        String result = url;
        try {
            result = response.encodeURL(url);
        } catch (Exception e)  {
        }
        return result;
    }

}
