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
import java.util.Iterator;

public class SendUpdatedViews implements Server, Runnable {
    private static final Runnable NOOP = new Runnable() {
        public void run() {
        }
    };
    private static final ResponseHandler CloseResponse = new ResponseHandler() {
        public void respond(Response response) throws Exception {
            //let the bridge know that this blocking connection should not be re-initialized
            response.setHeader("X-Connection", "close");
            response.setHeader("Content-Length", 0);
        }
    };
    //Define here to avoid classloading problems after application exit
    private static final ResponseHandler NoopHandler = NOOPResponse.Handler;
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
    private final Collection synchronouslyUpdatedViews;
    private final String sessionID;
    private final long timeoutInterval;
    private long responseTimeoutTime;
    private Server server;

    public SendUpdatedViews(String sessionID, final Collection synchronouslyUpdatedViews, final ViewQueue allUpdatedViews, final MonitorRunner monitorRunner, Configuration configuration) {
        this.timeoutInterval = configuration.getAttributeAsLong("blockingConnectionTimeout", 90000);
        this.sessionID = sessionID;
        this.allUpdatedViews = allUpdatedViews;
        this.synchronouslyUpdatedViews = synchronouslyUpdatedViews;
        this.allUpdatedViews.onPut(new Runnable() {
            public void run() {
                respondIfViewsAvailable();
            }
        });

        //add monitor
        monitorRunner.registerMonitor(SendUpdatedViews.this);
        //initialize blocking server
        this.server = new Server() {
            public void service(final Request request) throws Exception {
                responseTimeoutTime = System.currentTimeMillis() + timeoutInterval;
                respondIfPendingRequest(CloseResponse);
                pendingRequest.put(request);
                respondIfViewsAvailable();
            }

            public void shutdown() {
                //remove monitor
                monitorRunner.unregisterMonitor(SendUpdatedViews.this);
                allUpdatedViews.onPut(NOOP);
                //avoid creating new blocking connections after shutdown
                server = AfterShutdown;
                respondIfPendingRequest(CloseResponse);
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
            respondIfPendingRequest(NoopHandler);
        }
    }

    private void respondIfViewsAvailable() {
        try {
            allUpdatedViews.removeAll(synchronouslyUpdatedViews);
            synchronouslyUpdatedViews.clear();
            if (!allUpdatedViews.isEmpty()) {
                Request request = (Request) pendingRequest.poll();
                if (request != null) {
                    request.respondWith(new FixedXMLContentHandler() {
                        public void writeTo(Writer writer) throws IOException {
                            writer.write("<updated-views>");
                            Iterator i = new HashSet(allUpdatedViews).iterator();
                            while (i.hasNext()) {
                                writer.write(sessionID);
                                writer.write(":");
                                writer.write((String) i.next());
                                writer.write(' ');
                            }
                            writer.write("</updated-views>");
                            allUpdatedViews.clear();
                        }
                    });
                }
            }
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }

    private void respondIfPendingRequest(ResponseHandler handler) {
        Request previousRequest = (Request) pendingRequest.poll();
        if (previousRequest != null) {
            try {
                previousRequest.respondWith(handler);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
