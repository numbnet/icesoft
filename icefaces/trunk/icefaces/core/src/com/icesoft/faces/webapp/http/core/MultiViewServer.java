package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.context.View;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;

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

    public MultiViewServer(HttpSession session, String sessionID, SessionDispatcher.Monitor sessionMonitor, Map views, ViewQueue asynchronouslyUpdatedViews, Configuration configuration, ResourceDispatcher resourceDispatcher) {
        this.session = session;
        this.sessionID = sessionID;
        this.sessionMonitor = sessionMonitor;
        this.views = views;
        this.asynchronouslyUpdatedViews = asynchronouslyUpdatedViews;
        this.configuration = configuration;
        this.resourceDispatcher = resourceDispatcher;
    }

    public void service(Request request) throws Exception {
        //extract viewNumber if this request is from a redirect
        String viewNumber = String.valueOf(++viewCount);
        View view = new View(viewNumber, sessionID, session, request, asynchronouslyUpdatedViews, configuration, sessionMonitor, resourceDispatcher, lifecycle);
        views.put(viewNumber, view);
        sessionMonitor.touchSession();
        view.servePage(request);
        view.release();
    }

    public void shutdown() {
    }
}
