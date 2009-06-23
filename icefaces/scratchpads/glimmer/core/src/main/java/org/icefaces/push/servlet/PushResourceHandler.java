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
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icefaces.push.servlet;

import org.icefaces.push.Configuration;
import org.icefaces.push.CurrentContext;
import org.icefaces.push.MonitorRunner;
import org.icefaces.push.SessionBoundServer;
import org.icefaces.push.http.Server;

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

public class PushResourceHandler extends ResourceHandler implements CurrentContext {
    private static Logger log = Logger.getLogger("org.icefaces.pushservlet");
    private static final Pattern ICEfacesResourcePattern = Pattern.compile(".*/icefaces/.*");
    private static final CurrentContextPath currentContextPath = new CurrentContextPath();
    private SessionDispatcher dispatcher;
    private MonitorRunner monitor;

    private ResourceHandler handler;

    public PushResourceHandler(ResourceHandler handler) {
        this.handler = handler;
        ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        context.setAttribute(PushResourceHandler.class.getName(), this);

        final Configuration configuration = new ServletContextConfiguration("org.icefaces", context);
        monitor = new MonitorRunner(configuration.getAttributeAsLong("poolingInterval", 15000));
        dispatcher = new SessionDispatcher(configuration, context) {
            protected Server newServer(HttpSession session, Monitor sessionMonitor) {
                return new SessionBoundServer(session, monitor, sessionMonitor, configuration);
            }
        };
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
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();
        if (ICEfacesResourcePattern.matcher(request.getRequestURI()).find()) {
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
        HttpServletRequest servletRequest = (HttpServletRequest) externalContext.getRequest();
        boolean resourceRequest = handler.isResourceRequest(facesContext) || ICEfacesResourcePattern.matcher(servletRequest.getRequestURI()).find();
        if (!resourceRequest && servletRequest.getParameter("ice.session.donottouch") == null) {
            dispatcher.touchSession((HttpSession) externalContext.getSession(false));
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
                throw new RuntimeException("no message available", e);
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
        ((PushResourceHandler) context.getAttribute(PushResourceHandler.class.getName())).dispose();
    }

    private void dispose() {
        dispatcher.shutdown();
        monitor.stop();
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
