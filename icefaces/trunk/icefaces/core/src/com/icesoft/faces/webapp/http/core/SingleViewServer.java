package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.context.View;
import com.icesoft.faces.util.event.servlet.ContextEventRepeater;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class SingleViewServer implements Server {
    private static final String viewNumber = "1";
    private Map views;
    private String sessionID;
    private ViewQueue allUpdatedViews;
    private Configuration configuration;
    private SessionDispatcher.Monitor sessionMonitor;
    private HttpSession session;
    private Server server;
    private ResourceDispatcher resourceDispatcher;

    public SingleViewServer(HttpSession session, String sessionID, SessionDispatcher.Monitor sessionMonitor, Map views, ViewQueue allUpdatedViews, Configuration configuration, ResourceDispatcher resourceDispatcher) {
        this.session = session;
        this.sessionID = sessionID;
        this.sessionMonitor = sessionMonitor;
        this.views = views;
        this.allUpdatedViews = allUpdatedViews;
        this.configuration = configuration;
        this.resourceDispatcher = resourceDispatcher;
        this.server = new PageServer();
    }

    public void service(Request request) throws Exception {
        //create single view or re-create view if the request is the result of a redirect 
        View view = (View) views.get(viewNumber);
        if (view == null) {
            view = new View(viewNumber, sessionID, request, allUpdatedViews, configuration, sessionMonitor, resourceDispatcher);
            views.put(viewNumber, view);
            ContextEventRepeater.viewNumberRetrieved(session, sessionID, Integer.parseInt(viewNumber));
        } else {
            view.updateOnPageRequest(request);
        }

        view.switchToNormalMode();
        sessionMonitor.touchSession();
        server.service(request);
        view.switchToPushMode();
        view.release();
    }

    public void shutdown() {
        server.shutdown();
    }
}
