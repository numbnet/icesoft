package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.context.View;
import com.icesoft.faces.util.event.servlet.ContextEventRepeater;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class MultiViewServer implements Server {
    private int viewCount = 0;
    private Map views;
    private ViewQueue asynchronouslyUpdatedViews;
    private String sessionID;
    private Configuration configuration;
    private SessionDispatcher.Monitor sessionMonitor;
    private HttpSession session;
    private Server server;
    private ResourceDispatcher resourceDispatcher;

    public MultiViewServer(HttpSession session, String sessionID, SessionDispatcher.Monitor sessionMonitor, Map views, ViewQueue asynchronouslyUpdatedViews, Configuration configuration, ResourceDispatcher resourceDispatcher) {
        this.session = session;
        this.sessionID = sessionID;
        this.sessionMonitor = sessionMonitor;
        this.views = views;
        this.asynchronouslyUpdatedViews = asynchronouslyUpdatedViews;
        this.configuration = configuration;
        this.resourceDispatcher = resourceDispatcher;
        this.server = new PageServer();
    }

    public void service(Request request) throws Exception {
        //extract viewNumber if this request is from a redirect
        View view;
        if (request.containsParameter("rvn")) {
            String redirectViewNumber = request.getParameter("rvn");
            view = (View) views.get(redirectViewNumber);
            if (view == null) {
                view = new View(redirectViewNumber, sessionID, request, asynchronouslyUpdatedViews, configuration, sessionMonitor, resourceDispatcher);
                views.put(redirectViewNumber, view);
                ContextEventRepeater.viewNumberRetrieved(session, sessionID, Integer.parseInt(redirectViewNumber));
            } else {
                view.updateOnPageRequest(request);
                view.switchToNormalMode();
            }
        } else {
            String viewNumber = String.valueOf(++viewCount);
            view = new View(viewNumber, sessionID, request, asynchronouslyUpdatedViews, configuration, sessionMonitor, resourceDispatcher);
            views.put(viewNumber, view);
            ContextEventRepeater.viewNumberRetrieved(session, sessionID, Integer.parseInt(viewNumber));
        }
        sessionMonitor.touchSession();
        server.service(request);
        view.switchToPushMode();
        view.release();
    }

    public void shutdown() {
        server.shutdown();
    }
}
