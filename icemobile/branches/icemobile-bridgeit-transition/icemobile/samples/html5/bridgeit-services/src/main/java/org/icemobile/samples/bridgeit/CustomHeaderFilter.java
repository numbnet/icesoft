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
package org.icemobile.samples.bridgeit;

import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomHeaderFilter implements Filter {
    private static final Logger LOGGER = Logger.getLogger(CustomHeaderFilter.class.getName());

    private FilterConfig filterConfig = null;

    public void destroy() {
        this.filterConfig = null;
    }

    public void init(final FilterConfig filterConfig)
    throws ServletException {
        this.filterConfig = filterConfig;
    }

    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain filterChain)
    throws IOException, ServletException {
        HttpServletRequest _httpRequest = (HttpServletRequest)request;
        HttpServletResponse _httpResponse = (HttpServletResponse) response;
        Enumeration headerNames = filterConfig.getInitParameterNames();
        while (headerNames.hasMoreElements())  {
            String _headerName = (String)headerNames.nextElement();
            String _headerValue = filterConfig.getInitParameter(_headerName);
            if ("Access-Control-Allow-Origin".equalsIgnoreCase(_headerName) && "*".equals(_headerValue))  {
                _httpResponse.addHeader("Access-Control-Allow-Origin", _httpRequest.getHeader("Origin"));
            } else {
                _httpResponse.addHeader(_headerName, _headerValue);
            }
        }
        String method = _httpRequest.getMethod();
        // Hack to not pass OPTIONS requests to ICEpush
        if ("OPTIONS".equalsIgnoreCase(method))  {
            return;
        }
        filterChain.doFilter(request, response);
    }
}
