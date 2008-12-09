package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.context.View;
import com.icesoft.faces.util.event.servlet.ContextEventRepeater;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;

import javax.faces.FactoryFinder;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class SingleViewServer implements Server {
    private final static LifecycleFactory LIFECYCLE_FACTORY = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    private Lifecycle lifecycle = LIFECYCLE_FACTORY.getLifecycle(LIFECYCLE_FACTORY.DEFAULT_LIFECYCLE);

    private static final String viewNumber = "1";
    private Map views;
    private String sessionID;
    private ViewQueue allUpdatedViews;
    private Configuration configuration;
    private SessionDispatcher.Monitor sessionMonitor;
    private HttpSession session;
    private ResourceDispatcher resourceDispatcher;

    public SingleViewServer(HttpSession session, String sessionID, SessionDispatcher.Monitor sessionMonitor, Map views, ViewQueue allUpdatedViews, Configuration configuration, ResourceDispatcher resourceDispatcher) {
        this.session = session;
        this.sessionID = sessionID;
        this.sessionMonitor = sessionMonitor;
        this.views = views;
        this.allUpdatedViews = allUpdatedViews;
        this.configuration = configuration;
        this.resourceDispatcher = resourceDispatcher;
    }

    //synchronize to avoid concurrent state modifications of the single View
    public synchronized void service(Request request) throws Exception {
        //create single view or re-create view if the request is the result of a redirect
        View view = (View) views.get(viewNumber);
        if (view == null) {
            view = new View(viewNumber, sessionID, allUpdatedViews, configuration, sessionMonitor, resourceDispatcher, lifecycle);
            views.put(viewNumber, view);
            ContextEventRepeater.viewNumberRetrieved(session, sessionID, Integer.parseInt(viewNumber));
        }

        try {
            sessionMonitor.touchSession();
            view.servePage(request);
        } finally {
            view.release();
        }
    }

    public void shutdown() {
    }
}
