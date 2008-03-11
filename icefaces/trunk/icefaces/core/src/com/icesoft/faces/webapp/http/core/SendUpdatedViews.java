package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.webapp.command.NOOP;
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
    private static final NOOP NOOPCommand = new NOOP();
    private static final Runnable NOOP = new Runnable() {
        public void run() {
        }
    };
    private static final ResponseHandler NOOPResponse = new FixedXMLContentHandler() {
        public void writeTo(Writer writer) throws IOException {
            NOOPCommand.serializeTo(writer);
        }
    };
    private static final ResponseHandler CloseResponse = new ResponseHandler() {
        public void respond(Response response) throws Exception {
            //let the bridge know that this blocking connection should not be re-initialized
            response.setHeader("X-Connection", "close");
            response.setHeader("Content-Length", 0);
        }
    };
    private static final Server AfterShutdown = new Server() {
        public void service(Request request) throws Exception {
            request.respondWith(CloseResponse);
        }

        public void shutdown() {
            //do nothing
        }
    };

    private final BlockingQueue pendingRequest = new LinkedBlockingQueue(1);
    private final ViewQueue allUpdatedViews;
    private final String sessionID;
    private final long timeoutInterval;
    private long responseTimeoutTime;
    private Server server;

    public SendUpdatedViews(String sessionID, final Collection synchronouslyUpdatedViews, final ViewQueue allUpdatedViews, final MonitorRunner monitorRunner, Configuration configuration) {
        this.timeoutInterval = configuration.getAttributeAsLong("blockingConnectionTimeout", 90000);
        this.sessionID = sessionID;
        this.allUpdatedViews = allUpdatedViews;
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
        monitorRunner.registerMonitor(SendUpdatedViews.this);
        //initialize blocking server
        this.server = new Server() {
            public void service(Request request) throws Exception {
                responseTimeoutTime = System.currentTimeMillis() + timeoutInterval;
                respondIfPreviousRequest(CloseResponse);
                pendingRequest.put(request);
            }

            public void shutdown() {
                //remove monitor
                monitorRunner.unregisterMonitor(SendUpdatedViews.this);
                allUpdatedViews.onPut(NOOP);
                //avoid creating new blocking connections after shutdown
                server = AfterShutdown;
                respondIfPreviousRequest(CloseResponse);
            }
        };
    }

    public void service(final Request request) throws Exception {
        server.service(request);
    }

    public void shutdown() {
        server.shutdown();
    }

    public void run() {
        if ((System.currentTimeMillis() > responseTimeoutTime) && (!pendingRequest.isEmpty())) {
            respondIfPreviousRequest(NOOPResponse);
        }
    }

    private void respondIfPreviousRequest(ResponseHandler handler) {
        Request previousRequest = (Request) pendingRequest.poll();
        if (previousRequest != null) {
            try {
                previousRequest.respondWith(handler);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private class UpdatedViewsHandler extends FixedXMLContentHandler {
        private String[] viewIdentifiers;

        public UpdatedViewsHandler(String[] viewIdentifiers) {
            this.viewIdentifiers = viewIdentifiers;
        }

        public void writeTo(Writer writer) throws IOException {
            writer.write("<updated-views>");
            for (int i = 0; i < viewIdentifiers.length; i++) {
                writer.write(sessionID + ":" + viewIdentifiers[i]);
                writer.write(' ');
            }
            writer.write("</updated-views>");
        }
    }
}
