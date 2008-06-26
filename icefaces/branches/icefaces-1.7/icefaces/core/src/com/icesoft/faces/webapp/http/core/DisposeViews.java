package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.context.View;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.OKHandler;

import java.util.Map;

public class DisposeViews implements Server {
    private String sessionID;
    private Map views;

    public DisposeViews(String sessionID, Map views) {
        this.sessionID = sessionID;
        this.views = views;
    }

    public void service(Request request) throws Exception {
        String[] viewIdentifiers = request.getParameterAsStrings(sessionID);
        for (int i = 0; i < viewIdentifiers.length; i++) {
            View view = (View) views.remove(viewIdentifiers[i]);
            // Jira 1616 Logout throws NPE.
            if (view != null) {
                view.dispose();
            }
        }

        request.respondWith(OKHandler.HANDLER);
    }

    public void shutdown() {
    }
}