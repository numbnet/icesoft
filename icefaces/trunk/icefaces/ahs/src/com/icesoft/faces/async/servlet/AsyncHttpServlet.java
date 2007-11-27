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
import com.icesoft.faces.webapp.http.servlet.PathDispatcher;
import com.icesoft.faces.webapp.http.servlet.PseudoServlet;
import com.icesoft.faces.webapp.http.servlet.ServletConfigConfiguration;
import com.icesoft.faces.webapp.http.servlet.ServletContextConfiguration;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AsyncHttpServlet
extends HttpServlet {
    private static final Log LOG = LogFactory.getLog(AsyncHttpServlet.class);

    private AsyncHttpServer asyncHttpServer;
    private MessageService messageService;
    private PathDispatcher pathDispatcher = new PathDispatcher();

    public void destroy() {
        super.destroy();
        if (asyncHttpServer != null) {
            asyncHttpServer.start();
        }
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
            boolean _asyncHttpServer;
            try {
                _asyncHttpServer =
                    _servletContextConfiguration.getAttributeAsBoolean(
                        "async.server");
            } catch (ConfigurationException exception) {
                _asyncHttpServer = false;
            }
            if (_asyncHttpServer) {
                asyncHttpServer =
                    new AsyncHttpServer(
                        new AsyncHttpServerSettings(
                            _servletConfigConfiguration),
                        _sessionManager,
                        messageService);
                messageService.start();
                asyncHttpServer.start();
            } else {
                SessionDispatcher _sessionDispatcher = new SessionDispatcher() {
                    protected PseudoServlet newServlet(
                        final HttpSession httpSession, final Monitor monitor) {

                        return
                            new SessionBoundServlet(
                                _servletContextConfiguration,
                                _sessionManager,
                                new ExecuteQueue());
                    }
                };
                pathDispatcher.dispatchOn(".*", _sessionDispatcher);
                messageService.start();
            }
        } catch (Exception exception) {
            throw new ServletException(exception);
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
