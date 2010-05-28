package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.context.View;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.OKResponse;
import com.icesoft.faces.webapp.http.portlet.page.AssociatedPageViews;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

public class DisposeViews implements Server {
    private static final Log Log = LogFactory.getLog(DisposeViews.class);
    private String sessionID;
    private Map views;
    private AssociatedPageViews associatedPageViews;

    public DisposeViews(String sessionID, Map views, AssociatedPageViews associatedPageViews) {
        this.sessionID = sessionID;
        this.views = views;
        this.associatedPageViews = associatedPageViews;
    }

    public void service(Request request) throws Exception {
        if (request.containsParameter(sessionID)) {
            String[] viewIdentifiers = request.getParameterAsStrings(sessionID);
            //System.out.println("DisposeViews.service: looking to dispose views " + viewIdentifiers);
            for (int i = 0; i < viewIdentifiers.length; i++) {
                View view = (View) views.remove(viewIdentifiers[i]);
                // Jira 1616 Logout throws NPE.
                if (view != null) {
                    view.dispose();
                    associatedPageViews.disposeAssociatedViews(views, view);
                }
            }

        } else {
            //System.out.println("DisposeViews.service: request does not contain session id " + sessionID);
            //this usually happens with Seam filters in synchronous mode
            Log.warn("Request belonging to a different session. Most probably servlet filters mangled the request.");
        }

        request.respondWith(OKResponse.Handler);
    }

    public void shutdown() {
    }
}