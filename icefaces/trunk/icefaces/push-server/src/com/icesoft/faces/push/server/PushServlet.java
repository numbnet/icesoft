package com.icesoft.faces.push.server;

import com.icesoft.faces.env.Authorization;
import com.icesoft.faces.push.server.arp.PushServer;
import com.icesoft.faces.push.server.arp.PushServerSettings;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.ConfigurationException;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.servlet.PathDispatcher;
import com.icesoft.faces.webapp.http.servlet.ServletConfigConfiguration;
import com.icesoft.faces.webapp.http.servlet.ServletContextConfiguration;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.icefaces.push.server.ExecuteQueue;
import org.icefaces.push.server.SessionBoundServlet;
import org.icefaces.push.server.SessionManager;

public class PushServlet
extends HttpServlet {
    private static final Log LOG = LogFactory.getLog(PushServlet.class);

    private PushServer pushServer;
    private MessageService messageService;
    private PathDispatcher pathDispatcher = new PathDispatcher();

    public void destroy() {
        super.destroy();
        if (pushServer != null) {
            pushServer.stop();
        }
        messageService.stop();
        messageService.close();
        pathDispatcher.shutdown();
    }

    public void init(final ServletConfig servletConfig)
    throws ServletException {
        super.init(servletConfig);
        try {
            final ServletContext _servletContext =
                servletConfig.getServletContext();
            final Configuration _servletConfigConfiguration =
                new ServletConfigConfiguration(
                    "com.icesoft.faces.async.server", servletConfig);
            final Configuration _servletContextConfiguration =
                new ServletContextConfiguration(
                    "com.icesoft.faces", _servletContext);
            messageService = new MessageService(_servletContext);
            final SessionManager _sessionManager =
                new SessionManager(_servletConfigConfiguration, messageService);
            String _asyncService;                           // new property name
            try {
                _asyncService =
                    _servletContextConfiguration.getAttribute("async.service");
            } catch (ConfigurationException exception) {
                _asyncService = null;
            }
            boolean _asyncServer;                           // old property name
            try {
                _asyncServer =
                    _servletContextConfiguration.getAttributeAsBoolean(
                        "async.server");
            } catch (ConfigurationException exception) {
                _asyncServer = false;
            }
            if ((_asyncService != null &&
                    _asyncService.equalsIgnoreCase("server")) ||
                (_asyncService == null && _asyncServer)) {

                if (LOG.isInfoEnabled()) {
                    LOG.info(
                        "Starting the " + ProductInfo.PRODUCT + " " +
                            "in server-mode...");
                }
                pushServer =
                    new PushServer(
                        new PushServerSettings(_servletConfigConfiguration),
                        _sessionManager,
                        messageService);
                messageService.start();
                pushServer.start();
            } else {
                if (_asyncService != null &&
                    !_asyncService.equalsIgnoreCase("servlet")) {

                    if (LOG.isWarnEnabled()) {
                        LOG.warn(
                            "Unknown property value for " +
                                "com.icesoft.faces.async.service: " +
                                _asyncService);
                    }
                }
                if (LOG.isInfoEnabled()) {
                    LOG.info(
                        "Starting the " + ProductInfo.PRODUCT + " " +
                            "in servlet-mode...");
                }
                SessionDispatcher _sessionDispatcher =
                    new SessionDispatcher(
                        _servletContextConfiguration, _servletContext) {

                        private final ExecuteQueue executeQueue =
                            new ExecuteQueue();

                        protected Server newServer(
                            final HttpSession httpSession,
                            final Monitor monitor,
                            final Authorization authorization) {

                            return
                                new SessionBoundServlet(
                                    _sessionManager, executeQueue, monitor);
                        }
                    };
                pathDispatcher.dispatchOn(".*", _sessionDispatcher);
                messageService.start();
            }
        } catch (Exception exception) {
            LOG.error(
                "An error occurred while initializing the PushServlet!",
                exception);
        }
    }

    protected void service(
        final HttpServletRequest httpServletRequest,
        final HttpServletResponse httpServletResponse)
    throws IOException, ServletException {
        try {
            pathDispatcher.service(httpServletRequest, httpServletResponse);
        } catch (RuntimeException exception) {
            throw exception;
        } catch (Exception exception) {
            throw new ServletException(exception);
        }
    }
}
