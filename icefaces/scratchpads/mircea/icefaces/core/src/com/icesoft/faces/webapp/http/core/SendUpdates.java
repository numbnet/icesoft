package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.FixedXMLContentHandler;
import com.icesoft.faces.webapp.http.servlet.ServletView;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Map;

public class SendUpdates implements Server {
    private Map commandQueues;
    private Collection allUpdatedViews;

    public SendUpdates(Map commandQueues, Collection allUpdatedViews) {
        this.commandQueues = commandQueues;
        this.allUpdatedViews = allUpdatedViews;
    }

    public void service(final Request request) throws Exception {
        request.respondWith(new FixedXMLContentHandler() {

            public void writeTo(Writer writer) throws IOException {
                String[] viewIdentifiers = request.getParameterAsStrings("viewNumber");
                for (int i = 0; i < viewIdentifiers.length; i++) {
                    String viewIdentifier = viewIdentifiers[i];
                    //cancel asynchronous update for these view since it is served synchronously
                    allUpdatedViews.remove(viewIdentifier);
                    ServletView view = (ServletView) commandQueues.get(viewIdentifier);
                    view.take().serializeTo(writer);
                }
            }
        });
    }

    public void shutdown() {
    }
}
