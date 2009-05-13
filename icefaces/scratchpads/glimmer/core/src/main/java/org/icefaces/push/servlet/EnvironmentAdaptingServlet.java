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
                    // checking if GlassFish ARP is available...
                    boolean isGlassFishARPAvailable = isGlassFishARPAvailable();
                    if (log.isLoggable(Level.FINE)) {
                        log.log(Level.FINE,"GlassFish ARP available: " + isGlassFishARPAvailable);
                    }
                    // checking if Jetty ARP is available...
                    boolean isJettyARPAvailable = isJettyARPAvailable();
                    if (log.isLoggable(Level.FINE)) {
                        log.log(Level.FINE,"Jetty ARP available: " + isJettyARPAvailable);
                    }
                    if (isGlassFishARPAvailable && configuration.getAttributeAsBoolean("useARP", isGlassFishARPAvailable)) {
                        log.log(Level.INFO,"Adapting to GlassFish ARP environment");
                        factory = new GlassFishAdaptingServletFactory();
                        // instantiate a fallback factory for creating fallback servlets.
                        fallbackFactory = new ThreadBlockingAdaptingServletFactory();
                    } else if (isJettyARPAvailable && configuration.getAttributeAsBoolean("useARP", configuration.getAttributeAsBoolean("useJettyContinuations", isJettyARPAvailable))) {
                        log.log(Level.INFO,"Adapting to Jetty ARP environment");
                        factory = new JettyAdaptingServletFactory();
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

    private boolean isGlassFishARPAvailable() {
        try {
            this.getClass().getClassLoader().loadClass("com.sun.enterprise.web.connector.grizzly.comet.CometEngine");
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }

    private boolean isJettyARPAvailable() {
        try {
            this.getClass().getClassLoader().loadClass("org.mortbay.util.ajax.Continuation");
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }

    private static interface EnvironmentAdaptingServletFactory {
        public PseudoServlet newServlet(final Server server, final ServletContext servletContext);
    }

    private static class GlassFishAdaptingServletFactory implements EnvironmentAdaptingServletFactory {
        public PseudoServlet newServlet(final Server server, final ServletContext servletContext) {
            try {
                return new GlassFishAdaptingServlet(server, servletContext);
            } catch (ServletException exception) {
                log.log(Level.WARNING,"Failed to adapt to GlassFish ARP environment. Falling back to Thread Blocking environment.", exception);
                synchronized (LOCK) {
                    factory = fallbackFactory;
                    fallbackFactory = null;
                }
                return factory.newServlet(server, servletContext);
            }
        }
    }

    private static class JettyAdaptingServletFactory implements EnvironmentAdaptingServletFactory {
        public PseudoServlet newServlet(final Server server, final ServletContext servletContext) {
            return new JettyAdaptingServlet(server);
        }
    }

    private static class ThreadBlockingAdaptingServletFactory implements EnvironmentAdaptingServletFactory {
        public PseudoServlet newServlet(final Server server, final ServletContext servletContext) {
            return new ThreadBlockingAdaptingServlet(server);
        }
    }
}
