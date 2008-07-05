package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.EmptyResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RequestVerifier implements Server {
    private final static Log log = LogFactory.getLog(RequestVerifier.class);
    private String sessionID;
    private Server server;

    public RequestVerifier(String sessionID, Server server) {
        this.sessionID = sessionID;
        this.server = server;
    }

    public void service(Request request) throws Exception {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            log.info("'POST' request expected. Dropping connection...");
            request.respondWith(EmptyResponse.Handler);
        } else {
            if (request.containsParameter("ice.session")) {
                if (sessionID.equals(request.getParameter("ice.session"))) {
                    server.service(request);
                } else {
                    log.debug("Missmatched 'ice.session' value. Session has expired.");
                    request.respondWith(SessionExpiredResponse.Handler);
                }
            } else {
                log.info("Request missing 'ice.session' required parameter. Dropping connection...");
                request.respondWith(EmptyResponse.Handler);
            }
        }
    }

    public void shutdown() {
        server.shutdown();
    }
}
