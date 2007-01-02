package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.webapp.http.Request;
import com.icesoft.faces.webapp.http.Response;
import com.icesoft.faces.webapp.http.ResponseHandler;
import com.icesoft.faces.webapp.http.Server;

import java.io.OutputStreamWriter;

public class SendUpdates implements Server {
    private UpdateManager updateManager;

    public SendUpdates(UpdateManager updateManager) {
        this.updateManager = updateManager;
    }

    public void service(final Request request) throws Exception {
        request.respondWith(new ResponseHandler() {
            public void respond(Response response) throws Exception {
                response.setHeader("Content-Type", "text/xml");
                String[] views = request.getParameterAsStrings("viewNumber");
                OutputStreamWriter writer = new OutputStreamWriter(response.writeBody());
                updateManager.serialize(views, writer);
                writer.flush();
            }
        });
    }
}
