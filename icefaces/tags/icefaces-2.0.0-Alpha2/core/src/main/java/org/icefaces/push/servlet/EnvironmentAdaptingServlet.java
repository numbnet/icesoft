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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.icefaces.push.Configuration;
import org.icefaces.push.http.Server;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class EnvironmentAdaptingServlet implements PseudoServlet {
    private static Logger log = Logger.getLogger("org.icefaces.pushservlet");
    private static final Object LOCK = new Object();

    private static EnvironmentAdaptingServletFactory factory;
    private static EnvironmentAdaptingServletFactory fallbackFactory;

    private PseudoServlet servlet;
    private PseudoServlet fallbackServlet;

    public EnvironmentAdaptingServlet(final Server server, final Configuration configuration, final ServletContext servletContext) {
        if (factory == null) {
            synchronized (LOCK) {
                if (factory == null) {
                    // checking if Servlet 3.0 ARP is available...
                    boolean isAsyncARPAvailable = isAsyncARPAvailable();
                    if (log.isLoggable(Level.FINE)) {
                        log.log(Level.FINE,"AsyncContext ARP available: " + isAsyncARPAvailable);
                    }
                    if (isAsyncARPAvailable && configuration.getAttributeAsBoolean("useARP", isAsyncARPAvailable)) {
                        log.log(Level.INFO,"Adapting to Servlet 3.0 AsyncContext ARP environment");
                        factory = new AsyncAdaptingServletFactory();
                        // instantiate a fallback factory for creating fallback servlets.
                        fallbackFactory = new ThreadBlockingAdaptingServletFactory();
                    } else {
                        log.log(Level.INFO,"Adapting to Thread Blocking environment");
                        factory = new ThreadBlockingAdaptingServletFactory();
                    }
                }
            }
        }
        servlet = factory.newServlet(server, servletContext);
        if (fallbackFactory != null) {
            fallbackServlet = fallbackFactory.newServlet(server, servletContext);
        }
    }

    public void service(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        try {
            servlet.service(request, response);
        } catch (EnvironmentAdaptingException exception) {
            if (fallbackFactory != null) {
                log.log(Level.WARNING,"Falling back to Thread Blocking environment.");
                synchronized (LOCK) {
                    factory = fallbackFactory;
                    fallbackFactory = null;
                }
            }
            if (fallbackServlet != null) {
                servlet = fallbackServlet;
                fallbackServlet = null;
                servlet.service(request, response);
            } else {
                throw exception;
            }
        }
    }

    public void shutdown() {
        servlet.shutdown();
    }

    private boolean isAsyncARPAvailable() {
        try {
            this.getClass().getClassLoader().loadClass("javax.servlet.AsyncContext");
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }

    private static interface EnvironmentAdaptingServletFactory {
        public PseudoServlet newServlet(final Server server, final ServletContext servletContext);
    }

    private static class AsyncAdaptingServletFactory implements EnvironmentAdaptingServletFactory {
        public PseudoServlet newServlet(final Server server, final ServletContext servletContext) {
            return new AsyncAdaptingServlet(server);
        }
    }

    private static class ThreadBlockingAdaptingServletFactory implements EnvironmentAdaptingServletFactory {
        public PseudoServlet newServlet(final Server server, final ServletContext servletContext) {
            return new ThreadBlockingAdaptingServlet(server);
        }
    }
}
