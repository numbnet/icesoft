package org.icepush.servlet;

import org.icepush.http.ResponseHandler;
import org.icepush.http.Server;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Logger;

public class AsyncAdaptingServlet implements PseudoServlet {
    private final static Logger log = Logger.getLogger(AsyncAdaptingServlet.class.getName());
    private Server server;

    public AsyncAdaptingServlet(Server server) {
        this.server = server;
        log.info("Using Servlet 3.0 AsyncContext");
    }

    public void service(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
        if (!request.isAsyncSupported()) {
            throw new EnvironmentAdaptingException("Servlet 3.0 asynchronous feature not supported.");
        }
        AsyncRequestResponse requestResponse = new AsyncRequestResponse(request, response);
        server.service(requestResponse);
    }

    public void shutdown() {
        server.shutdown();
    }

    private class AsyncRequestResponse extends ServletRequestResponse {
        private AsyncContext asyncContext;

        public AsyncRequestResponse(final HttpServletRequest request, final HttpServletResponse response) throws Exception {
            super(request, response);
            asyncContext = request.isAsyncStarted() ? request.getAsyncContext() : request.startAsync();
        }

        public void respondWith(final ResponseHandler handler) throws Exception {
            super.respondWith(handler);
            asyncContext.complete();
        }
    }
}
