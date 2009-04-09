package com.icesoft.faces.async.servlet;

import com.icesoft.faces.async.common.ExecuteQueue;
import com.icesoft.faces.async.common.RequestManager;
import com.icesoft.faces.async.common.SessionManager;
import com.icesoft.faces.async.common.UpdatedViewsManager;
import com.icesoft.faces.async.common.messaging.MessageService;
import com.icesoft.faces.async.server.AsyncHttpServer;
import com.icesoft.faces.async.server.AsyncHttpServerSettings;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.ConfigurationException;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.servlet.PathDispatcher;
import com.icesoft.faces.webapp.http.servlet.ServletConfigConfiguration;
import com.icesoft.faces.webapp.http.servlet.ServletContextConfiguration;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AsyncHttpServlet
        extends HttpServlet {
    private static final Log LOG = LogFactory.getLog(AsyncHttpServlet.class);

    private AsyncHttpServer asyncHttpServer;
    private MessageService messageService;
    private PathDispatcher pathDispatcher = new PathDispatcher();

    public void destroy() {
        super.destroy();
        if (asyncHttpServer != null) {
            asyncHttpServer.stop();
        }
        messageService.stop();
        messageService.close();
        pathDispatcher.shutdown();
    }

    public void init(final ServletConfig servletConfig)
            throws ServletException {
        super.init(servletConfig);
        try {
            final Configuration _servletConfigConfiguration =
                    new ServletConfigConfiguration(
                            "com.icesoft.faces.async.server", servletConfig);
            final Configuration _servletContextConfiguration =
                    new ServletContextConfiguration(
                            "com.icesoft.faces", servletConfig.getServletContext());
            messageService =
                    new MessageService(servletConfig.getServletContext());
            final SessionManager _sessionManager =
                    new SessionManager(
                            new RequestManager(),
                            new UpdatedViewsManager(
                                    _servletConfigConfiguration, messageService),
                            messageService);
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
                            "Starting the Asynchronous HTTP Server " +
                                    "in server-mode...");
                }
                asyncHttpServer =
                        new AsyncHttpServer(
                                new AsyncHttpServerSettings(
                                        _servletConfigConfiguration),
                                _sessionManager,
                                messageService);
                messageService.start();
                asyncHttpServer.start();
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
                            "Starting the Asynchronous HTTP Server " +
                                    "in servlet-mode...");
                }
                final ExecuteQueue _executeQueue = new ExecuteQueue();
                SessionDispatcher _sessionDispatcher = new SessionDispatcher(_servletContextConfiguration, getServletContext()) {
                    protected Server newServer(final HttpSession httpSession, final Monitor monitor) {
                        return new SessionBoundServlet(
                                _servletContextConfiguration,
                                _sessionManager,
                                _executeQueue,
                                monitor,
                                servletConfig.getServletContext());
                    }
                };
                pathDispatcher.dispatchOn(".*", _sessionDispatcher);
                messageService.start();
            }
        } catch (Exception exception) {
            LOG.error(
                    "An error occurred while initializing the AsyncHttpServlet!",
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
