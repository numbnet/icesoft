package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.context.View;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Response;
import com.icesoft.faces.webapp.http.common.ResponseHandler;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.OKHandler;

import java.util.Map;

public class DisposeViews implements Server {
    private static final ResponseHandler CloseConnection = new ResponseHandler() {
        public void respond(Response response) throws Exception {
            response.setHeader("Connection", "close");
            response.writeBody().write("\\n\\n".getBytes());
        }
    };
    private Map views;

    public DisposeViews(Map views) {
        this.views = views;
    }

    public void service(Request request) throws Exception {
        String[] viewIdentifiers = request.getParameterAsStrings("ice.view.all");
        for (int i = 0; i < viewIdentifiers.length; i++) {
            String viewIdentifier = viewIdentifiers[i];
            View view = (View) views.remove(viewIdentifier);
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