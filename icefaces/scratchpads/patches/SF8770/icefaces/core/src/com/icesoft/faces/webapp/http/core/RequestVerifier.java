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
log.info(this + " instantiated for " + sessionID);
    }

    public void service(Request request) throws Exception {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
System.out.println("'POST' request expected. Dropping connection...");
            request.respondWith(EmptyResponse.Handler);
        } else {
            if (request.containsParameter("ice.session")) {
                if (sessionID.equals(request.getParameter("ice.session"))) {
                    server.service(request);
                } else {
System.out.println(request.getParameter("ice.session") +" ice.session request parameter received by " + this + " but expiring connection because expecting " + sessionID);
                    log.debug("Missmatched 'ice.session' value. Session has expired.");
                    request.respondWith(SessionExpiredResponse.Handler);
                }
            } else {
System.out.println("Request missing 'ice.session' required parameter. Dropping connection...");
                request.respondWith(EmptyResponse.Handler);
            }
        }
    }

    public void shutdown() {
        server.shutdown();
    }
}
