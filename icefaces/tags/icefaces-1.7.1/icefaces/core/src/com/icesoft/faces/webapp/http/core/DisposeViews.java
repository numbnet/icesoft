package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.context.View;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.OKHandler;

import java.util.Map;

public class DisposeViews implements Server {
    private Map views;

    public DisposeViews(Map views) {
        this.views = views;
    }

    public void service(Request request) throws Exception {
        String viewIdentifier = request.getParameter("ice.view");
        View view = (View) views.remove(viewIdentifier);
        // Jira 1616 Logout throws NPE.
        if (view != null) {
            view.dispose();
        }

        request.respondWith(OKHandler.HANDLER);
    }

    public void shutdown() {
    }
}