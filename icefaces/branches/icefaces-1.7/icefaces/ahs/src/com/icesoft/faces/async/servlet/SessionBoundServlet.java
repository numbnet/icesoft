package com.icesoft.faces.async.servlet;

import com.icesoft.faces.async.common.ExecuteQueue;
import com.icesoft.faces.async.common.SessionManager;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.PathDispatcherServer;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;

//todo: rename to SessionBoundServer
public class SessionBoundServlet
        implements Server {
    private static final Log LOG = LogFactory.getLog(SessionBoundServlet.class);
    private PathDispatcherServer pathDispatcherServer;

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
        // todo: I should've done this differently... When revisiting this I
        //       should create a separate DisposeViewsServer and register that
        //       with the appropriate Request URI.
        Server _server =
                new SendUpdatedViewsServer(sessionManager, executeQueue, monitor);
        pathDispatcherServer = new PathDispatcherServer();
        pathDispatcherServer.dispatchOn(
                ".*block\\/receive\\-updated\\-views$", _server);
        pathDispatcherServer.dispatchOn(
                ".*block\\/dispose\\-views$", _server);
    }

    public void service(Request request) throws Exception {
        pathDispatcherServer.service(request);
    }

    public void shutdown() {
        pathDispatcherServer.shutdown();
    }
}
