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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BlockingConnectionServer extends TimerTask implements Server, Observer {
    private static final Logger log = Logger.getLogger(BlockingConnectionServer.class.getName());
    private static final ResponseHandler CloseResponse = new CloseConnectionResponseHandler();
    //Define here to avoid classloading problems after application exit
    private static final ResponseHandler NoopResponse = new NoopResponseHandler();
    private static final Server AfterShutdown = new ResponseHandlerServer(CloseResponse);

    private final BlockingQueue pendingRequest = new LinkedBlockingQueue(1);
    private long heartbeat;
    private long minHeartbeat;
    private long maxHeartbeat;

    private long heartbeatUpdateTime = System.currentTimeMillis();
    private long responseTimeoutTime;
    private Server activeServer;
    private ConcurrentLinkedQueue notifiedPushIDs = new ConcurrentLinkedQueue();
    private List participatingPushIDs = Collections.emptyList();
    private PushGroupManager pushGroupManager;

    private String lastWindow = "";
    private String[] lastNotifications = new String[]{};
    private int sequenceNo = 0;

    public BlockingConnectionServer(final PushGroupManager pushGroupManager, final Timer monitorRunner, final Configuration configuration, final boolean terminateBlockingConnectionOnShutdown) {
        this.heartbeat = configuration.getAttributeAsLong("heartbeatTimeout", 15000);
        this.minHeartbeat = configuration.getAttributeAsLong("minHeartbeatInterval", heartbeat / 3);
        this.maxHeartbeat = configuration.getAttributeAsLong("maxHeartbeatInterval", Math.round(3 * heartbeat));

        this.pushGroupManager = pushGroupManager;
        //add monitor
        monitorRunner.scheduleAtFixedRate(this, 0, 1000);
        this.pushGroupManager.addObserver(this);

        //define blocking server
        activeServer = new RunningServer(pushGroupManager, terminateBlockingConnectionOnShutdown);
    }

    public void update(Observable observable, Object o) {
        sendNotifications((String[]) o);
    }

    public void service(final Request request) throws Exception {
        activeServer.service(request);
    }

    public void shutdown() {
        cancel();
        pushGroupManager.deleteObserver(this);
        activeServer.shutdown();
    }

    public void run() {
        if ((System.currentTimeMillis() > responseTimeoutTime) && (!pendingRequest.isEmpty())) {
            respondIfPendingRequest(NoopResponse);
        }
    }

    private void sendNotifications(String[] ids) {
        //stop sending notifications if pushID are not used anymore by the browser
        List pushIDs = new ArrayList(Arrays.asList(ids));
        pushIDs.retainAll(participatingPushIDs);
        if (!pushIDs.isEmpty()) {
            notifiedPushIDs.addAll(pushIDs);
            resetTimeout();
            respondIfNotificationsAvailable();
        }
    }

    private void resendLastNotifications() {
        sendNotifications(lastNotifications);
    }

    private synchronized void respondIfNotificationsAvailable() {
        if (!notifiedPushIDs.isEmpty()) {
            //save notifications, maybe they will need to be resent when blocking connection switches to another window 
            lastNotifications = (String[]) notifiedPushIDs.toArray(new String[0]);
            respondIfPendingRequest(new NotificationHandler(lastNotifications) {
                public void writeTo(Writer writer) throws IOException {
                    super.writeTo(writer);

                    if (log.isLoggable(Level.FINEST)) {
                        log.finest("Sending notifications for " + notifiedPushIDs + ".");
                    }
                    notifiedPushIDs.removeAll(Arrays.asList(lastNotifications));
                }
            });
        }
    }

    private void resetTimeout() {
        responseTimeoutTime = System.currentTimeMillis() + heartbeat;
    }

    private void respondIfPendingRequest(final ResponseHandler handler) {
        final Request previousRequest = (Request) pendingRequest.poll();
        if (previousRequest != null) {
            try {
                previousRequest.respondWith(new AdjustHeartbeat(previousRequest, handler));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static class NoopResponseHandler extends FixedXMLContentHandler {
        public void writeTo(Writer writer) throws IOException {
            writer.write("<noop/>");

            if (log.isLoggable(Level.FINEST)) {
                log.finest("Sending NoOp.");
            }
        }
    }

    private static class CloseConnectionResponseHandler implements ResponseHandler {
        public void respond(Response response) throws Exception {
            //let the bridge know that this blocking connection should not be re-initialized
            response.setHeader("X-Connection", "close");
            response.setHeader("Content-Length", 0);

            if (log.isLoggable(Level.FINEST)) {
                log.finest("Close current blocking connection.");
            }
        }
    }

    private class NotificationHandler extends FixedXMLContentHandler {
        private String[] pushIDs;

        private NotificationHandler(String[] pushIDs) {
            this.pushIDs = pushIDs;
        }

        public void writeTo(Writer writer) throws IOException {
            writer.write("<notified-pushids>");
            for (int i = 0; i < pushIDs.length; i++) {
                String id = pushIDs[i];
                if (i > 0) {
                    writer.write(' ');
                }
                writer.write(id);
            }
            writer.write("</notified-pushids>");
        }
    }

    private class AdjustHeartbeat implements ResponseHandler {
        private static final double IntervalExpansionFactor = 1.1;
        private static final double IntervalReductionFactor = 0.75;
        private final Request request;
        private final ResponseHandler handler;

        public AdjustHeartbeat(Request request, ResponseHandler handler) {
            this.request = request;
            this.handler = handler;
        }

        public void respond(Response response) throws Exception {
            long previousSequenceNo;
            try {
                previousSequenceNo = request.getHeaderAsInteger("ice.push.sequence");
            } catch (RuntimeException e) {
                previousSequenceNo = 0;
            }
            //if sequence number sent by the client is different than the one
            //on the server it can be assumed that at least one heartbeat response was lost at some point
            boolean matchingSequenceNo = previousSequenceNo == sequenceNo;
            if (matchingSequenceNo) {
                //increase interval since heartbeat still runs great
                //increase interval only after the previous interval has elapsed to avoid increasing the value too rapidly
                if (updateHeartbeat()) {
                    heartbeat = Math.min(maxHeartbeat, Math.round(heartbeat * IntervalExpansionFactor));
                }
            } else {
                //decrease interval since heartbeat was lost
                heartbeat = Math.max(minHeartbeat, Math.round(heartbeat * IntervalReductionFactor));
            }
            ++sequenceNo;

            //send heartbeat only when current heartbeat interval has elapsed, this avoids
            //oscillations or rapid changes in the heartbeat interval
            if (!matchingSequenceNo || updateHeartbeat()) {
                response.setHeader("ice.push.heartbeat", heartbeat);
                heartbeatUpdateTime = System.currentTimeMillis();
            }
            response.setHeader("ice.push.sequence", sequenceNo);
            handler.respond(response);
        }

        private boolean updateHeartbeat() {
            return System.currentTimeMillis() > heartbeat + heartbeatUpdateTime;
        }
    }

    private class RunningServer implements Server {
        private final PushGroupManager pushGroupManager;
        private final boolean terminateBlockingConnectionOnShutdown;

        public RunningServer(PushGroupManager pushGroupManager, boolean terminateBlockingConnectionOnShutdown) {
            this.pushGroupManager = pushGroupManager;
            this.terminateBlockingConnectionOnShutdown = terminateBlockingConnectionOnShutdown;
        }

        public void service(final Request request) throws Exception {
            resetTimeout();
            respondIfPendingRequest(CloseResponse);

            //resend notifications if the window owning the blocking connection has changed
            String currentWindow = request.getHeader("ice.push.window");
            currentWindow = currentWindow == null ? "" : currentWindow;
            boolean resend = !lastWindow.equals(currentWindow);
            lastWindow = currentWindow;

            pendingRequest.put(request);
            try {
                participatingPushIDs = Arrays.asList(request.getParameterAsStrings("ice.pushid"));
                if (log.isLoggable(Level.FINEST)) {
                    log.finest("Participating pushIds: " + participatingPushIDs + ".");
                }

                if (resend) {
                    resendLastNotifications();
                } else {
                    respondIfNotificationsAvailable();
                }
                pushGroupManager.notifyObservers(new ArrayList(participatingPushIDs));
            } catch (RuntimeException e) {
                log.fine("Request does not contain pushIDs.");
                respondIfPendingRequest(NoopResponse);
            }
        }

        public void shutdown() {
            //avoid creating new blocking connections after shutdown
            activeServer = AfterShutdown;
            respondIfPendingRequest(terminateBlockingConnectionOnShutdown ? CloseResponse : NoopResponse);
        }
    }
}
