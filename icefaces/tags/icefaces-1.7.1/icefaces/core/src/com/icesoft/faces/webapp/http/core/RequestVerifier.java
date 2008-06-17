package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.webapp.command.SessionExpired;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Response;
import com.icesoft.faces.webapp.http.common.ResponseHandler;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.FixedXMLContentHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.Writer;

public class RequestVerifier implements Server {
    private final static Log log = LogFactory.getLog(RequestVerifier.class);
    private final static SessionExpired SessionExpired = new SessionExpired();
    private final static ResponseHandler SessionExpiredResponse = new FixedXMLContentHandler() {
        public void writeTo(Writer writer) throws IOException {
            SessionExpired.serializeTo(writer);
        }
    };
    private static final ResponseHandler EmptyResponse = new ResponseHandler() {
        public void respond(Response response) throws Exception {
            response.setHeader("Content-Length", 0);
        }
    };

    private String sessionID;
    private Server server;

    public RequestVerifier(String sessionID, Server server) {
        this.sessionID = sessionID;
        this.server = server;
    }

    public void service(Request request) throws Exception {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            log.info("'POST' request expected. Dropping connection...");
            request.respondWith(EmptyResponse);
        } else {
            if (request.containsParameter("ice.session")) {
                if (sessionID.equals(request.getParameter("ice.session"))) {
                    server.service(request);
                } else {
                    log.debug("Missmatched 'ice.session' value. Session has expired.");
                    request.respondWith(SessionExpiredResponse);
                }
            } else {
                log.info("Request missing 'ice.session' required parameter. Dropping connection...");
                request.respondWith(EmptyResponse);
            }
        }
    }

    public void shutdown() {
        server.shutdown();
    }
}
