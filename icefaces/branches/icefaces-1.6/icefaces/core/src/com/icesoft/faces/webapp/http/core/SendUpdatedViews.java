package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Response;
import com.icesoft.faces.webapp.http.common.ResponseHandler;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.FixedXMLContentHandler;
import com.icesoft.util.MonitorRunner;
import edu.emory.mathcs.backport.java.util.concurrent.BlockingQueue;
import edu.emory.mathcs.backport.java.util.concurrent.LinkedBlockingQueue;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class SendUpdatedViews implements Server, Runnable {
    private static final Runnable Noop = new Runnable() {
        public void run() {
        }
    };
    private static final ResponseHandler EmptyResponseHandler = new ResponseHandler() {
        public void respond(Response response) throws Exception {
            response.setHeader("Content-Length", 0);
        }
    };
    private final BlockingQueue pendingRequest = new LinkedBlockingQueue(1);
    private final ViewQueue allUpdatedViews;
    private final long timeoutInterval;
    private long responseTimeoutTime;
    private MonitorRunner monitorRunner;

    public SendUpdatedViews(final Collection synchronouslyUpdatedViews, final ViewQueue allUpdatedViews, final MonitorRunner monitorRunner, Configuration configuration) {
        this.allUpdatedViews = allUpdatedViews;
        this.timeoutInterval = configuration.getAttributeAsLong("blockingConnectionTimeout", 90000);
        this.allUpdatedViews.onPut(new Runnable() {
            public void run() {
                try {
                    allUpdatedViews.removeAll(synchronouslyUpdatedViews);
                    synchronouslyUpdatedViews.clear();
                    Set viewIdentifiers = new HashSet(allUpdatedViews);
                    if (!viewIdentifiers.isEmpty()) {
                        Request request = (Request) pendingRequest.poll();
                        if (request != null) {
                            request.respondWith(new UpdatedViewsHandler((String[]) viewIdentifiers.toArray(new String[viewIdentifiers.size()])));
                        }
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        //add monitor
        this.monitorRunner = monitorRunner;
        this.monitorRunner.registerMonitor(this);
    }

    public void service(final Request request) throws Exception {
        responseTimeoutTime = System.currentTimeMillis() + timeoutInterval;
        respondToPreviousRequest();
        pendingRequest.put(request);
    }

    public void run() {
        if ((System.currentTimeMillis() > responseTimeoutTime) && (!pendingRequest.isEmpty())) {
            respondToPreviousRequest();
        }
    }

    private void respondToPreviousRequest() {
        Request previousRequest = (Request) pendingRequest.poll();
        if (previousRequest != null) {
            try {
                previousRequest.respondWith(EmptyResponseHandler);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown() {
        //remove monitor
        monitorRunner.unregisterMonitor(this);
        allUpdatedViews.onPut(Noop);
        respondToPreviousRequest();
    }

    private class UpdatedViewsHandler extends FixedXMLContentHandler {
        private String[] viewIdentifiers;

        public UpdatedViewsHandler(String[] viewIdentifiers) {
            this.viewIdentifiers = viewIdentifiers;
        }

        public void writeTo(Writer writer) throws IOException {
            writer.write("<updated-views>");
            for (int i = 0; i < viewIdentifiers.length; i++) {
                writer.write(viewIdentifiers[i]);
                writer.write(' ');
            }
            writer.write("</updated-views>");
        }
    }
}
