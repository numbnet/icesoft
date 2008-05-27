package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.Server;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EnvironmentAdaptingServlet implements PseudoServlet {
    private static final Log LOG = LogFactory.getLog(EnvironmentAdaptingServlet.class);
    private static final Object LOCK = new Object();

    private static EnvironmentAdaptingServletFactory factory;

    private PseudoServlet servlet;

    public EnvironmentAdaptingServlet(final Server server, final Configuration configuration, final ServletContext servletContext) {
        if (factory == null) {
            synchronized (LOCK) {
                if (factory == null) {
                    // checking if GlassFish ARP is available...
                    boolean isGlassFishARPAvailable;
                    try {
                        this.getClass().getClassLoader().loadClass("com.sun.enterprise.web.connector.grizzly.comet.CometEngine");
                        isGlassFishARPAvailable = true;
                    } catch (ClassNotFoundException exception) {
                        isGlassFishARPAvailable = false;
                    }
                    if (LOG.isInfoEnabled()) {
                        LOG.info("GlassFish ARP available: " + isGlassFishARPAvailable);
                    }
                    // checking if Jetty ARP is available...
                    boolean isJettyARPAvailable;
                    try {
                        this.getClass().getClassLoader().loadClass("org.mortbay.util.ajax.Continuation");
                        isJettyARPAvailable = true;
                    } catch (ClassNotFoundException exception) {
                        isJettyARPAvailable = false;
                    }
                    if (LOG.isInfoEnabled()) {
                        LOG.info("Jetty ARP available: " + isJettyARPAvailable);
                    }
                    if (isGlassFishARPAvailable && configuration.getAttributeAsBoolean("useARP", isGlassFishARPAvailable)) {
                        LOG.info("Adapting to GlassFish ARP environment");
                        factory = new GlassFishAdaptingServletFactory();
                    } else if (isJettyARPAvailable && configuration.getAttributeAsBoolean("useARP", configuration.getAttributeAsBoolean("useJettyContinuations", isJettyARPAvailable))) {
                        LOG.info("Adapting to Jetty ARP environment");
                        factory = new JettyAdaptingServletFactory();
                    } else {
                        LOG.info("Adapting to Thread Blocking environment");
                        factory = new ThreadBlockingAdaptingServletFactory();
                    }
                }
            }
        }
        servlet = factory.newServlet(server, servletContext);
    }

    public void service(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        servlet.service(request, response);
    }

    public void shutdown() {
        servlet.shutdown();
    }

    private static interface EnvironmentAdaptingServletFactory {
        public PseudoServlet newServlet(final Server server, final ServletContext servletContext);
    }

    private static class GlassFishAdaptingServletFactory implements EnvironmentAdaptingServletFactory {
        public PseudoServlet newServlet(final Server server, final ServletContext servletContext) {
            try {
                return new GlassFishAdaptingServlet(server, servletContext);
            } catch (ServletException exception) {
                LOG.warn("Failed to adapt to GlassFish ARP environment. Falling back to Thread Blocking environment.", exception);
                factory = new ThreadBlockingAdaptingServletFactory();
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
