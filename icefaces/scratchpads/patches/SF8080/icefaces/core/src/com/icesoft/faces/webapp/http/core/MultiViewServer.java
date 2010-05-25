package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.context.View;
import com.icesoft.faces.util.event.servlet.ContextEventRepeater;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;
import com.icesoft.faces.webapp.http.portlet.page.AssociatedPageViews;

import javax.faces.FactoryFinder;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.http.HttpSession;
import java.util.Map;

public class MultiViewServer implements Server {
    private final static LifecycleFactory LIFECYCLE_FACTORY = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    private Lifecycle lifecycle = LIFECYCLE_FACTORY.getLifecycle(LIFECYCLE_FACTORY.DEFAULT_LIFECYCLE);

    private int viewCount = 0;
    private Map views;
    private ViewQueue asynchronouslyUpdatedViews;
    private String sessionID;
    private Configuration configuration;
    private SessionDispatcher.Monitor sessionMonitor;
    private HttpSession session;
    private ResourceDispatcher resourceDispatcher;
    private AssociatedPageViews associatedPageViews;

    public MultiViewServer(final HttpSession session,
                           final String sessionID,
                           final SessionDispatcher.Monitor sessionMonitor,
                           final Map views,
                           final ViewQueue asynchronouslyUpdatedViews,
                           final Configuration configuration,
                           final ResourceDispatcher resourceDispatcher,
                           final AssociatedPageViews associatedPageViews) {
        this.session = session;
        this.sessionID = sessionID;
        this.sessionMonitor = sessionMonitor;
        this.views = views;
        this.asynchronouslyUpdatedViews = asynchronouslyUpdatedViews;
        this.configuration = configuration;
        this.resourceDispatcher = resourceDispatcher;
        this.associatedPageViews = associatedPageViews;
    }

    public void service(Request request) throws Exception {
        //extract viewNumber if this request is from a redirect
        View view;
        if (request.containsParameter("rvn")) {
            String redirectViewNumber = request.getParameter("rvn");
            //synchronize to atomically test and possibly put new View into the map
            synchronized (views) {
                view = (View) views.get(redirectViewNumber);
                if (view == null) {
                    view = new View(redirectViewNumber, sessionID, asynchronouslyUpdatedViews, configuration, sessionMonitor, resourceDispatcher, lifecycle);
                    views.put(redirectViewNumber, view);
                    ContextEventRepeater.viewNumberRetrieved(session, sessionID, Integer.parseInt(redirectViewNumber));
                }
            }
        } else {
            String viewNumber = String.valueOf(++viewCount);
            view = new View(viewNumber, sessionID, asynchronouslyUpdatedViews, configuration, sessionMonitor, resourceDispatcher, lifecycle);
            views.put(viewNumber, view);
            ContextEventRepeater.viewNumberRetrieved(session, sessionID, Integer.parseInt(viewNumber));
        }

        try {
            sessionMonitor.touchSession();
            view.servePage(request);
            associatedPageViews.add(view);
        } finally {
            view.release();
        }
    }

    public void shutdown() {
    }
}
