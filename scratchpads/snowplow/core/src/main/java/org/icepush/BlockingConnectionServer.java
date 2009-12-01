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

package org.icepush;

import org.icepush.http.Request;
import org.icepush.http.Response;
import org.icepush.http.ResponseHandler;
import org.icepush.http.Server;
import org.icepush.http.standard.FixedXMLContentHandler;
import org.icepush.http.standard.ResponseHandlerServer;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingConnectionServer extends TimerTask implements Server, Observer {
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
    private static final Server AfterShutdown = new ResponseHandlerServer(CloseResponse);

    private final BlockingQueue pendingRequest = new LinkedBlockingQueue(1);
    private final long timeoutInterval;
    private long responseTimeoutTime;
    private Server activeServer;
    private ConcurrentLinkedQueue updatedViews = new ConcurrentLinkedQueue();
    private List participatingViews = Collections.emptyList();
    private Observable notifier;

    public BlockingConnectionServer(Observable outboundNotifier, final Observable inboundNotifier, final Timer monitorRunner, Configuration configuration) {
        this.timeoutInterval = configuration.getAttributeAsLong("blockingConnectionTimeout", 3000);
        this.notifier = outboundNotifier;
        //add monitor
        monitorRunner.scheduleAtFixedRate(this, 0, 1000);
        outboundNotifier.addObserver(this);

        //define blocking server
        activeServer = new Server() {
            public void service(final Request request) throws Exception {
                resetTimeout();
                respondIfPendingRequest(CloseResponse);
                participatingViews = Arrays.asList(request.getParameterAsStrings("ice.view"));
                pendingRequest.put(request);
                respondIfViewsAvailable();
                inboundNotifier.notifyObservers(participatingViews);
            }

            public void shutdown() {
                //avoid creating new blocking connections after shutdown
                activeServer = AfterShutdown;
                respondIfPendingRequest(CloseResponse);
            }
        };
    }

    public void update(Observable observable, Object o) {
        //stop sending notifications if pushID are not used anymore by the browser
        //todo: verify if this kind of filtering is scalable enough
        List pushIDs = new ArrayList(Arrays.asList((String[]) o));
        pushIDs.retainAll(participatingViews);
        if (!pushIDs.isEmpty()) {
            updatedViews.addAll(pushIDs);
            resetTimeout();
            respondIfViewsAvailable();
        }
    }

    public void service(final Request request) throws Exception {
        activeServer.service(request);
    }

    public void shutdown() {
        cancel();
        notifier.deleteObserver(this);
        activeServer.shutdown();
    }

    public void run() {
        if ((System.currentTimeMillis() > responseTimeoutTime) && (!pendingRequest.isEmpty())) {
            respondIfPendingRequest(NoopHandler);
        }
    }

    private synchronized void respondIfViewsAvailable() {
        if (!updatedViews.isEmpty()) {
            final String[] views = (String[]) updatedViews.toArray(new String[0]);
            respondIfPendingRequest(new UpdatedViewsHandler(views) {
                public void writeTo(Writer writer) throws IOException {
                    super.writeTo(writer);
                    updatedViews.removeAll(Arrays.asList(views));
                }
            });
        }
    }

    private void resetTimeout() {
        responseTimeoutTime = System.currentTimeMillis() + timeoutInterval;
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
