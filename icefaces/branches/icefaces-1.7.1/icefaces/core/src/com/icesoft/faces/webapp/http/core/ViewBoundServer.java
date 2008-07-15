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
    private static final Command SessionExpired = new SessionExpired();
    private static final FixedXMLContentHandler SessionExpiredHandler = new FixedXMLContentHandler() {
        public void writeTo(Writer writer) throws IOException {
            SessionExpired.serializeTo(writer);
        }
    };
    private static final ResponseHandler MissingParameterHandler = new ResponseHandler() {
        public void respond(Response response) throws Exception {
            response.setStatus(500);
            response.writeBody().write("Cannot match view instance. 'ice.view' parameter is missing.".getBytes());
        }
    };
    private Map views;
    private SessionDispatcher.Monitor sessionMonitor;
    private Server server;

    public ViewBoundServer(Server server, SessionDispatcher.Monitor sessionMonitor, Map views) {
        this.server = server;
        this.sessionMonitor = sessionMonitor;
        this.views = views;
    }

    public void service(Request request) throws Exception {
        String viewNumber = request.getParameter("ice.view");
        if (viewNumber == null) {
            request.respondWith(MissingParameterHandler);
        } else {
            View view = (View) views.get(viewNumber);
            if (view == null) {
                //todo: revisit this -- maybe the session was not created yet
                request.respondWith(SessionExpiredHandler);
            } else {

                // #2615. Without the following synchronization, the following
                // problems can occur. Assume HTTP-1 has a request and is in some
                // phase of the JSF lifecycle. Another partial submit request
                // arrives on HTTP-2 immediately after. It wont hit synchronization
                // until the renderCycle.. methods of ReceiveSendUpdates object.
                // This means that HTTP-2 is free to modify the requestParameter maps
                // via the updateOnXMLHttpRequest method on the view. Further, once HTTP-1
                // releases the monitor and HTTP-2 gets it, the outbound thread can
                // clear the requestMap member variable via view.release() below
                synchronized (view.getFacesContext()) {
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
                    } catch (SessionExpiredException e) {
                        //exception thrown in the middle of JSF lifecycle
                        //respond immediately with session-expired message to avoid any new connections
                        //being initiated by the bridge.
                        request.respondWith(SessionExpiredHandler);
                    } finally {
                        view.release();
                    }
                }
            }
        }
    }

    public void shutdown() {
        server.shutdown();
    }
}
