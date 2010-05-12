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

package org.icefaces.push.servlet;

import org.icepush.servlet.MainServlet;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.regex.Pattern;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ICEpushResourceHandler extends ResourceHandler {
    private static Logger log = Logger.getLogger(ICEpushResourceHandler.class.getName());
    private static final Pattern ICEpushRequestPattern = Pattern.compile(".*\\.icepush\\.jsf$");
    private ResourceHandler handler;
    private MainServlet mainServlet;

    public ICEpushResourceHandler(ResourceHandler handler) {
        try {
            this.handler = handler;
            ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            mainServlet = new MainServlet(context);
            context.setAttribute(ICEpushResourceHandler.class.getName(), this);
        } catch (Throwable t)  {
            log.log(Level.INFO, "Ajax Push Resource Handling not available: " + t);
        }
    }

    public Resource createResource(String s) {
        return handler.createResource(s);
    }

    public Resource createResource(String s, String s1) {
        return handler.createResource(s, s1);
    }

    public Resource createResource(String s, String s1, String s2) {
        return handler.createResource(s, s1, s2);
    }

    public boolean libraryExists(String s) {
        return handler.libraryExists(s);
    }

    public void handleResourceRequest(FacesContext facesContext) throws IOException {
        if (null == mainServlet)  {
            handler.handleResourceRequest(facesContext);
            return;
        }
        ExternalContext externalContext = facesContext.getExternalContext();
        if (!(externalContext.getRequest() instanceof HttpServletRequest))  {
            handler.handleResourceRequest(facesContext);
            return;
        }
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        String requestURI = request.getRequestURI();
        if (ICEpushRequestPattern.matcher(requestURI).find()) {
            try {
                mainServlet.service(request, response);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            handler.handleResourceRequest(facesContext);
        }
    }

    public boolean isResourceRequest(FacesContext facesContext) {
        ExternalContext externalContext = facesContext.getExternalContext();
        if (!(externalContext.getRequest() instanceof HttpServletRequest))  {
            return handler.isResourceRequest(facesContext);
        }
        HttpServletRequest servletRequest = (HttpServletRequest) externalContext.getRequest();
        String requestURI = servletRequest.getRequestURI();
        return handler.isResourceRequest(facesContext) || ICEpushRequestPattern.matcher(requestURI).find();
    }

    public String getRendererTypeForResourceName(String s) {
        return handler.getRendererTypeForResourceName(s);
    }

    public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            mainServlet.service(request, response);
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public static void notifyContextShutdown(ServletContext context) {
        try {
            ((ICEpushResourceHandler) context.getAttribute(ICEpushResourceHandler.class.getName())).mainServlet.shutdown();
        } catch (Throwable t) {
            //no need to log this exception for optional Ajax Push, but may be
            //useful for diagnosing other failures
            log.log(Level.INFO, "MainServlet not found in application scope: " + t);
        }
    }
}
