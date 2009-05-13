/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icefaces.push;

import org.icefaces.push.http.Request;
import org.icefaces.push.http.Response;
import org.icefaces.push.http.ResponseHandler;
import org.icefaces.push.http.Server;
import org.icefaces.push.http.standard.FixedXMLContentHandler;
import org.icefaces.push.http.standard.ResponseHandlerServer;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SendUpdatedViews implements Server, Runnable {
    private static final ResponseHandler CloseResponse = new ResponseHandler() {
        public void respond(Response response) throws Exception {
            //let the bridge know that this blocking connection should not be re-initialized
            response.setHeader("X-Connection", "close");
            response.setHeader("Content-Length", 0);
        }
    };
    //Define here to avoid classloading problems after application exit
    private static final ResponseHandler NoopHandler = new FixedXMLContentHandler() {
        public void writeTo(Writer writer) throws IOException {
            writer.write("<noop/>");
        }
    };
    private static final ResponseHandler PongHandler = new FixedXMLContentHandler() {
        public void writeTo(Writer writer) throws IOException {
            writer.write("<pong/>");
        }
    };
    private static final Server AfterShutdown = new ResponseHandlerServer(CloseResponse);

    private final BlockingQueue pendingRequest = new LinkedBlockingQueue(1);
    private final long timeoutInterval;
    private final MonitorRunner monitorRunner;
    private long responseTimeoutTime;
    private Server activeServer;
    private String[] updatedViews;
    private String[] participatingViews;

    public SendUpdatedViews(HttpSession session, Observable pingPongNotifier, UpdateNotifier notifier, final MonitorRunner monitorRunner, Configuration configuration) {
        this.timeoutInterval = configuration.getAttributeAsLong("blockingConnectionTimeout", 90000);
        this.monitorRunner = monitorRunner;
        pingPongNotifier.addObserver(new Observer() {
            public void update(Observable o, Object arg) {
                respondWhenRequestAvailable();
            }
        });

        //inject SessionRenderer into session bound beans
        final SessionRenderer sessionRenderer = new SessionRenderer() {
            public void renderViews() {
                updatedViews = participatingViews;
                respondIfViewsAvailable();
                updatedViews = null;
            }
        };
        Enumeration e = session.getAttributeNames();
        while (e.hasMoreElements()) {
            Object value = session.getAttribute((String) e.nextElement());
            if (value instanceof SessionRenderable) {
                ((SessionRenderable) value).setSessionRenderer(sessionRenderer);
            }
        }

        notifier.onUpdate(new UpdateNotifier.Observer() {
            public void updated(String[] latestViews) {
                updatedViews = latestViews;
                respondIfViewsAvailable();
            }
        });
        //add monitor
        monitorRunner.registerMonitor(this);

        //define blocking server
        activeServer = new Server() {
            public void service(final Request request) throws Exception {
                responseTimeoutTime = System.currentTimeMillis() + timeoutInterval;
                respondIfPendingRequest(CloseResponse);
                participatingViews = request.getParameterAsStrings("ice.view");
                System.out.println("view >> " + Arrays.asList(participatingViews));
                pendingRequest.put(request);
                respondIfViewsAvailable();
            }

            public void shutdown() {
                //avoid creating new blocking connections after shutdown
                activeServer = AfterShutdown;
                respondIfPendingRequest(CloseResponse);
            }
        };
    }

    public void service(final Request request) throws Exception {
        activeServer.service(request);
    }

    public void shutdown() {
        //remove monitor
        monitorRunner.unregisterMonitor(this);
        activeServer.shutdown();
    }

    public void run() {
        if ((System.currentTimeMillis() > responseTimeoutTime) && (!pendingRequest.isEmpty())) {
            respondIfPendingRequest(NoopHandler);
        }
    }

    private void respondIfViewsAvailable() {
        if (updatedViews != null && updatedViews.length > 0) {
            respondIfPendingRequest(new UpdatedViewsHandler(updatedViews));
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

    private void respondWhenRequestAvailable() {
        try {
            Request request = (Request) pendingRequest.take();
            request.respondWith(PongHandler);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private class UpdatedViewsHandler extends FixedXMLContentHandler {
        private String[] views;

        private UpdatedViewsHandler(String[] views) {
            this.views = views;
        }

        public void writeTo(Writer writer) throws IOException {
            writer.write("<updated-views>");
            for (String updatedView : views) {
                writer.write(updatedView);
                writer.write(' ');
            }
            writer.write("</updated-views>");
        }
    }
}
