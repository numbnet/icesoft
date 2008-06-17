package com.icesoft.faces.async.servlet;

import com.icesoft.faces.async.common.ExecuteQueue;
import com.icesoft.faces.async.common.SessionManager;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.standard.PathDispatcherServer;
import com.icesoft.faces.webapp.http.servlet.EnvironmentAdaptingServlet;
import com.icesoft.faces.webapp.http.servlet.PseudoServlet;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SessionBoundServlet
implements PseudoServlet {
    private static final Log LOG = LogFactory.getLog(SessionBoundServlet.class);

    private PseudoServlet servlet;

    public SessionBoundServlet(
        final Configuration configuration, final SessionManager sessionManager,
        final ExecuteQueue executeQueue,
        final SessionDispatcher.Monitor monitor,
        final ServletContext servletContext)
    throws IllegalArgumentException {
        if (configuration == null) {
            throw new IllegalArgumentException("configuration is null");
        }
        if (sessionManager == null) {
            throw new IllegalArgumentException("sessionManager is null");
        }
        if (executeQueue == null) {
            throw new IllegalArgumentException("executeQueue is null");
        }
        PathDispatcherServer _pathDispatcherServer = new PathDispatcherServer();
        _pathDispatcherServer.dispatchOn(
            ".*block\\/receive\\-updated\\-views$",
            new SendUpdatedViewsServer(sessionManager, executeQueue, monitor));
        servlet =
            new EnvironmentAdaptingServlet(
                _pathDispatcherServer, configuration, servletContext);
    }

    public void service(
        final HttpServletRequest httpServletRequest,
        final HttpServletResponse httpServletResponse)
    throws Exception {
        servlet.service(httpServletRequest, httpServletResponse);
    }

    public void shutdown() {
        servlet.shutdown();
    }
}
