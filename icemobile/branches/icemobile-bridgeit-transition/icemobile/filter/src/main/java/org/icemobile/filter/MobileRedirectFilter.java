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
package org.icemobile.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icemobile.util.ClientDescriptor;

public class MobileRedirectFilter implements Filter{
    
    private FilterConfig filterConfig = null;
    private String phoneRedirectURL;
    private String tabletRedirectURL;
    
    public void init(FilterConfig config) throws ServletException {
        this.filterConfig = config;  
        this.phoneRedirectURL = config.getInitParameter("phoneRedirect");
        this.tabletRedirectURL = config.getInitParameter("tabletRedirect");
    }

    public void destroy() {
        this.filterConfig = null;
    }

    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) 
            throws IOException, ServletException {
        if (filterConfig == null)
           return;
        
        HttpServletResponse httpResp = (HttpServletResponse)resp;
        
        ClientDescriptor client = ClientDescriptor.getInstance((HttpServletRequest)req);
        if( this.phoneRedirectURL != null && client.isHandheldBrowser()){
            httpResp.sendRedirect(phoneRedirectURL);
        }
        else if( this.tabletRedirectURL != null && client.isTabletBrowser() ){
            httpResp.sendRedirect(tabletRedirectURL);
        }
        chain.doFilter(req, resp);
    }

    
}
