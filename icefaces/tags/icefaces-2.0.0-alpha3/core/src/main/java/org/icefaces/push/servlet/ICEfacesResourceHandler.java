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

import org.icefaces.push.Configuration;
import org.icefaces.push.CurrentContext;
import org.icefaces.push.SessionBoundServer;

import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

public class ICEfacesResourceHandler extends ResourceHandler implements CurrentContext {
    private static Logger log = Logger.getLogger(ICEfacesResourceHandler.class.getName());
    private static final Pattern ICEfacesBridgeRequestPattern = Pattern.compile(".*\\.icefaces\\.jsf$");
    private static final Pattern ICEfacesResourceRequestPattern = Pattern.compile(".*/icefaces/.*");
    private static final CurrentContextPath currentContextPath = new CurrentContextPath();
    private SessionDispatcher dispatcher;

    private ResourceHandler handler;

    public ICEfacesResourceHandler(ResourceHandler handler) {
        this.handler = handler;
        FacesContext facesContext = FacesContext.getCurrentInstance();
        facesContext.getExternalContext().getApplicationMap().put(
            ICEfacesResourceHandler.class.getName(), this);
        try {
            final ServletContext context = (ServletContext) 
                    facesContext.getExternalContext().getContext();
            final Configuration configuration = 
                    new ServletContextConfiguration("org.icefaces", context);
            dispatcher = new SessionDispatcher(context) {
                protected PseudoServlet newServer(HttpSession session, Monitor sessionMonitor) {
                    return new SessionBoundServer(session, sessionMonitor, configuration);
                }
            };
        } catch (Throwable t)  {
            log.log(Level.INFO, "HttpSession Handling not available: " + t);
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
        ExternalContext externalContext = facesContext.getExternalContext();
        if (!(externalContext.getRequest() instanceof HttpServletRequest))  {
            handler.handleResourceRequest(facesContext);
            return;
        }
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        String requestURI = request.getRequestURI();
        boolean resourceRequest = ICEfacesBridgeRequestPattern.matcher(requestURI).find() || ICEfacesResourceRequestPattern.matcher(requestURI).find();

        if (resourceRequest) {
            try {
                service(request, response);
            } catch (ServletException e) {
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
        boolean resourceRequest =
                handler.isResourceRequest(facesContext) ||
                        ICEfacesBridgeRequestPattern.matcher(requestURI).find() ||
                        ICEfacesResourceRequestPattern.matcher(requestURI).find();
        if (!resourceRequest && !requestURI.endsWith("ice.session.donottouch")) {
            if (null != dispatcher) {
                dispatcher.touchSession((HttpSession) externalContext.getSession(false));
            }
        }
        if (!servletRequest.isRequestedSessionIdValid() && facesContext.isPostback() && !resourceRequest && !handler.isResourceRequest(facesContext)) {
            servletRequest.setAttribute(SessionExpiredException.class.getName(), SessionExpiredException.class);
            return false;
        }
        return resourceRequest;
    }

    public String getRendererTypeForResourceName(String s) {
        return handler.getRendererTypeForResourceName(s);
    }

    public void service(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {
        try {
            currentContextPath.attach(request.getContextPath());
            dispatcher.service(request, response);
        } catch (SocketException e) {
            if ("Broken pipe".equals(e.getMessage())) {
                // client left the page
                if (log.isLoggable(Level.FINEST)) {
                    log.log(Level.FINEST, "Connection broken by client.", e);
                } else if (log.isLoggable(Level.FINE)) {
                    log.log(Level.FINE, "Connection broken by client: " + e.getMessage());
                }
            } else {
                throw new ServletException(e);
            }
        } catch (RuntimeException e) {
            //ICE-4261: We cannot wrap RuntimeExceptions as ServletExceptions because of support for Jetty
            //Continuations.  However, if the message of a RuntimeException is null, Tomcat won't
            //properly redirect to the configured error-page.  So we need a new RuntimeException
            //that actually includes a message.
            if ("org.mortbay.jetty.RetryRequest".equals(e.getClass().getName())) {
                throw e;
            } else if (e.getMessage() != null) {
                throw e;
            } else {
                throw new RuntimeException("wrapped Exception: " +
                        e.getClass().getName(), e);
            }
        } catch (Exception e) {
            throw new ServletException(e);
        } finally {
            currentContextPath.detach();
        }
    }

    public String getPath() {
        return currentContextPath.lookup();
    }

    public static void notifyContextShutdown(ServletContext context) {
        SessionDispatcher shutdownDispatcher =
                ((ICEfacesResourceHandler) context.getAttribute(ICEfacesResourceHandler.class.getName())).dispatcher;
        if (null != shutdownDispatcher) {
            shutdownDispatcher.shutdown();
        }
    }

    //todo: factor out into a ServletContextDispatcher
    private static class CurrentContextPath extends ThreadLocal {
        public String lookup() {
            return (String) get();
        }

        public void attach(String path) {
            set(path);
        }

        public void detach() {
            set(null);
        }
    }
}
