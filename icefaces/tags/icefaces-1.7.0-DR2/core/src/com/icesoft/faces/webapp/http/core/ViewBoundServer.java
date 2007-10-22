package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.context.View;
import com.icesoft.faces.webapp.command.Command;
import com.icesoft.faces.webapp.command.SessionExpired;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Response;
import com.icesoft.faces.webapp.http.common.ResponseHandler;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.FixedXMLContentHandler;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;

import javax.faces.FacesException;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;

public class ViewBoundServer implements Server {
    private final static Command SessionExpired = new SessionExpired();
    private Map views;
    private SessionDispatcher.Listener.Monitor sessionMonitor;
    private Server server;

    public ViewBoundServer(Server server, SessionDispatcher.Listener.Monitor sessionMonitor, Map views) {
        this.server = server;
        this.sessionMonitor = sessionMonitor;
        this.views = views;
    }

    public void service(Request request) throws Exception {
        String viewNumber = request.getParameter("ice.view.active");
        if (viewNumber == null) {
            request.respondWith(new ResponseHandler() {
                public void respond(Response response) throws Exception {
                    response.setStatus(500);
                    response.writeBody().write("Cannot match view instance. 'ice.view.active' parameter is missing.".getBytes());
                }
            });
        } else {
            View view = (View) views.get(viewNumber);
            if (view == null) {
                //todo: revisit this -- maybe the session was not created yet
                request.respondWith(new FixedXMLContentHandler() {
                    public void writeTo(Writer writer) throws IOException {
                        SessionExpired.serializeTo(writer);
                    }
                });
            } else {
                try {
                    view.updateOnXMLHttpRequest(request);
                    sessionMonitor.touchSession();
                    server.service(request);
                } catch (FacesException e) {
                    //"workaround" for exceptions zealously captured & wrapped by the JSF implementations
                    Throwable nestedException = e.getCause();
                    if (nestedException == null || nestedException instanceof Error) {
                        throw e;
                    } else {
                        throw (Exception) nestedException;
                    }
                } finally {
                    view.release();
                }
            }
        }
    }

    public void shutdown() {
        server.shutdown();
    }
}
