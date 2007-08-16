/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */
package com.icesoft.faces.async.server;

/*
 * Todo: I only commented out the ContextEventListener-related code, we have to
 *       determine to go either all JMS or keep the listener option.
 */
import com.icesoft.faces.async.server.io.IoHttpConnectionAcceptor;
import com.icesoft.faces.async.server.messaging.AbstractContextEventMessageHandler;
import com.icesoft.faces.async.server.messaging.AnnouncementMessageHandler;
import com.icesoft.faces.async.server.messaging.BufferedContextEventsMessageHandler;
import com.icesoft.faces.async.server.messaging.ContextEventMessageHandler;
import com.icesoft.faces.async.server.messaging.PurgeMessageHandler;
import com.icesoft.faces.async.server.messaging.UpdatedViewsMessageHandler;
import com.icesoft.faces.async.server.nio.NioHttpConnectionAcceptor;
//import com.icesoft.faces.util.event.servlet.*;
import com.icesoft.util.net.messaging.Message;
import com.icesoft.util.net.messaging.MessageSelector;
import com.icesoft.util.net.messaging.MessageServiceClient;
import com.icesoft.util.net.messaging.MessageServiceException;
import com.icesoft.util.net.messaging.expression.Or;
import com.icesoft.util.net.messaging.jms.JMSAdapter;
import com.icesoft.util.net.messaging.jms.JMSProviderConfiguration;
import com.icesoft.util.net.messaging.jms.JMSProviderConfigurationProperties;

import java.util.EventListener;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.swing.event.EventListenerList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>
 *   The <code>AsyncHttpServer</code> class represents the "Asynchronous HTTP
 *   Server", which is an HTTP Server specifically tailored to handle the
 *   blocking asynchronous <code>XMLHttpRequest</code>s send using the
 *   ICEfaces JavaScript library.
 * </p>
 */
public class AsyncHttpServer
/*implements ContextEventListener,*/ {
    public static final String NAME = "Asynchronous HTTP Server 1.6";

    private static final String ANNOUNCEMENT_MESSAGE_TYPE =
        "Announcement";
//    private static final String RESPONSE_QUEUE_EXCEEDED_MESSAGE_TYPE =
//        "ResponseQueueExceeded";
    private static final String UPDATED_VIEWS_QUEUE_EXCEEDED_MESSAGE_TYPE =
        "UpdatedViewsQueueExceeded";

    private static final Log LOG = LogFactory.getLog(AsyncHttpServer.class);

    private EventListenerList asyncHttpServerListenerList =
        new EventListenerList();
    private ExecuteQueue executeQueue = new ExecuteQueue();
    private Map httpConnectionAcceptorMap = new HashMap();
    private final Map pendingRequestMap = new HashMap();
//    private final ResponseQueueManager responseQueueManager =
//        new ResponseQueueManager(this);
    private final Map sessionMap = new HashMap();
    private final UpdatedViewsQueueManager updatedViewsQueueManager =
        new UpdatedViewsQueueManager(this);

    // Asynchronous HTTP Server properties with their default values:
    private boolean blocking;
    private boolean compression;
    private boolean persistent;
    private int port;

    private ServletContext servletContext;

    private MessageServiceClient messageServiceClient;

    private AnnouncementMessageHandler announcementMessageHandler =
        new AnnouncementMessageHandler() {
            public void publishUpdatedViewsQueues(
                final String destinationNodeAddress) {

                AsyncHttpServer.this.publishUpdatedViewsQueues(
                    destinationNodeAddress);
            }
        };
    private AbstractContextEventMessageHandler
        contextEventMessageHandlerAdapter =
            new AbstractContextEventMessageHandler() {
                public String getMessageType() {
                    return null;
                }

                public void handle(final Message message) {
                    // do nothing.
                }

                public void contextDestroyed() {
                    // do nothing.
                }

                public void iceFacesIdDisposed(final String iceFacesId) {
                    sessionDestroyed(iceFacesId);
                }

                public void iceFacesIdRetrieved(final String iceFacesId) {
                    synchronized (sessionMap) {
                        if (!sessionMap.containsKey(iceFacesId)) {
                            sessionMap.put(iceFacesId, new HashSet());
                        }
                    }
                }

                public void sessionDestroyed(final String iceFacesId) {
                    synchronized (pendingRequestMap) {
                        synchronized (sessionMap) {
                            if (sessionMap.containsKey(iceFacesId)) {
                                if (LOG.isDebugEnabled()) {
                                    LOG.debug(
                                        "Session Destroyed: " + iceFacesId);
                                }
                                sessionMap.remove(iceFacesId);
                            }
                            if (pendingRequestMap.containsKey(iceFacesId)) {
                                ((ProcessHandler)
                                    pendingRequestMap.remove(iceFacesId)).
                                        handle();
                            }
                            updatedViewsQueueManager.remove(iceFacesId);
                        }
                    }
                }

                public void viewNumberRetrieved(
                    final String iceFacesId, final String viewNumber) {

                    synchronized (sessionMap) {
                        if (sessionMap.containsKey(iceFacesId)) {
                            Set _viewNumberSet =
                                (Set)sessionMap.get(iceFacesId);
                            if (!_viewNumberSet.contains(viewNumber)) {
                                _viewNumberSet.add(viewNumber);
                            }
                        }
                    }
                }
            };
    private BufferedContextEventsMessageHandler
        bufferedContextEventsMessageHandler =
            new BufferedContextEventsMessageHandler() {
                public void contextDestroyed() {
                    contextEventMessageHandlerAdapter.contextDestroyed();
                }

                public void iceFacesIdDisposed(final String iceFacesId) {
                    contextEventMessageHandlerAdapter.iceFacesIdDisposed(
                        iceFacesId);
                }

                public void iceFacesIdRetrieved(final String iceFacesId) {
                    contextEventMessageHandlerAdapter.iceFacesIdRetrieved(
                        iceFacesId);
                }

                public void sessionDestroyed(final String iceFacesId) {
                    contextEventMessageHandlerAdapter.sessionDestroyed(
                        iceFacesId);
                }

                public void viewNumberRetrieved(
                    final String iceFacesId, final String viewNumber) {

                    contextEventMessageHandlerAdapter.viewNumberRetrieved(
                        iceFacesId, viewNumber);
                }
            };
    private ContextEventMessageHandler contextEventMessageHandler =
        new ContextEventMessageHandler() {
            public void contextDestroyed() {
                contextEventMessageHandlerAdapter.contextDestroyed();
            }

            public void iceFacesIdDisposed(final String iceFacesId) {
                contextEventMessageHandlerAdapter.iceFacesIdDisposed(
                    iceFacesId);
            }

            public void iceFacesIdRetrieved(final String iceFacesId) {
                contextEventMessageHandlerAdapter.iceFacesIdRetrieved(
                    iceFacesId);
            }

            public void sessionDestroyed(final String iceFacesId) {
                contextEventMessageHandlerAdapter.sessionDestroyed(iceFacesId);
            }

            public void viewNumberRetrieved(
                final String iceFacesId, final String viewNumber) {

                contextEventMessageHandlerAdapter.viewNumberRetrieved(
                    iceFacesId, viewNumber);
            }
        };
    private PurgeMessageHandler purgeMessageHandler =
        new PurgeMessageHandler() {
            public void purgeUpdatedViews(final Map purgeMap) {
                AsyncHttpServer.this.purgeUpdatedViews(purgeMap);
            }
        };
//    private ResponseMessageHandler responseMessageHandler =
//        new ResponseMessageHandler() {
//            public void sendResponse(Response response) {
//                AsyncHttpServer.this.sendResponse(response);
    private UpdatedViewsMessageHandler updatedViewsMessageHandler =
        new UpdatedViewsMessageHandler() {
            public void sendUpdatedViews(final UpdatedViews updatedViews) {
                AsyncHttpServer.this.sendUpdatedViews(updatedViews);
            }
        };

    /**
     * <p>
     *   Constructs an <code>AsyncHttpServer</code> with the specified
     *   <code>asyncHttpServerSettings</code>.
     * </p>
     *
     * @param      asyncHttpServerSettings
     *                 the settings for the <code>AsyncHttpServer</code> to be
     *                 created.
     * @throws     IllegalArgumentException
     *                 if the specified <code>asyncHttpServerSettings</code> is
     *                 <code>null</code>
     */
    public AsyncHttpServer(
        final AsyncHttpServerSettings asyncHttpServerSettings,
        final ServletContext servletContext)
    throws IllegalArgumentException {
        if (asyncHttpServerSettings == null) {
            throw
                new IllegalArgumentException("asyncHttpServerSettings is null");
        }
        if (servletContext == null) {
            throw new IllegalArgumentException("servletContext is null");
        }
        setBlocking(asyncHttpServerSettings.isBlocking());
        setCompression(asyncHttpServerSettings.useCompression());
        setPersistent(asyncHttpServerSettings.isPersistent());
        setPort(asyncHttpServerSettings.getPort());
        executeQueue =
            new ExecuteQueue(asyncHttpServerSettings.getExecuteQueueSize());
//        responseQueueManager.setResponseQueueSize(
//            asyncHttpServerSettings.getResponseQueueSize());
//        responseQueueManager.setResponseQueueThreshold(
//            asyncHttpServerSettings.getResponseQueueThreshold());
//        responseQueueManager.setPurgeMessageContents(
//            asyncHttpServerSettings.getPurgeMessageContents());
        updatedViewsQueueManager.setUpdatedViewsQueueSize(
            asyncHttpServerSettings.getUpdatedViewQueueSize());
        updatedViewsQueueManager.setUpdatedViewsQueueThreshold(
            asyncHttpServerSettings.getUpdatedViewQueueThreshold());
        updatedViewsQueueManager.setPurgeMessageContents(
            asyncHttpServerSettings.getPurgeMessageContents());
//        ContextEventRepeater.addListener(this);
        this.servletContext = servletContext;
    }

    /**
     * <p>
     *   Adds the specified <code>asyncHttpServerListener</code> to this
     *   <code>AsyncHttpServer</code>.
     * </p>
     *
     * @param      asyncHttpServerListener
     *                 the Asynchronous HTTP Server listener to be added.
     * @see        #removeAsyncHttpServerListener(AsyncHttpServerListener)
     */
    public void addAsyncHttpServerListener(
        final AsyncHttpServerListener asyncHttpServerListener) {

        asyncHttpServerListenerList.add(
            AsyncHttpServerListener.class, asyncHttpServerListener);
    }

//    public void contextDestroyed(ContextDestroyedEvent event) {
//        /*
//         * SessionDestroyedEvents are fired for all sessions within the context
//         * before the ContextDestroyedEvent is fired. Therefore, all data
//         * related to the sessions should already have been cleaned up.
//         */
//    }

    /**
     * <p>
     *   Gets the execute queue of this <code>AsyncHttpServer</code>.
     * </p>
     *
     * @return     the execute queue.
     */
    public ExecuteQueue getExecuteQueue() {
        return executeQueue;
    }

    /**
     * <p>
     *   Gets the HTTP connection acceptor of this <code>AsyncHttpServer</code>
     *   that accepts HTTP connections from the specified <code>port</code>.
     * </p>
     *
     * @param      port
     *                 the port for which the HTTP connection acceptor is
     *                 requested.
     * @return     the HTTP connection acceptor or <code>null</code> if there is
     *             no HTTP connection acceptor for the specified
     *             <code>port</code>.
     */
    public HttpConnectionAcceptor getHttpConnectionAcceptor(final int port) {
        return
            (HttpConnectionAcceptor)
                httpConnectionAcceptorMap.get(new Integer(port));
    }

    public MessageServiceClient getMessageServiceClient() {
        return messageServiceClient;
    }

    /**
     * <p>
     *   Gets the port of this <code>AsyncHttpServer</code>.
     * </p>
     *
     * @return     the port.
     * @see        #setPort(int)
     */
    public int getPort() {
        return port;
    }

//    public void iceFacesIdRetrieved(ICEfacesIDRetrievedEvent event) {
//        String _iceFacesId = event.getICEfacesID();
//        synchronized (sessionMap) {
//            if (!sessionMap.containsKey(_iceFacesId)) {
//                sessionMap.put(_iceFacesId, new HashSet());
//            }
//        }
//    }

    /**
     * <p>
     *   Checks to see if this <code>AsyncHttpServer</code> is configured in
     *   blocking-mode.
     * </p>
     *
     * @return     <code>true</code> if configured in blocking-mode,
     *             <code>false</code> if not.
     * @see        #setBlocking(boolean)
     */
    public boolean isBlocking() {
        return blocking;
    }

    /**
     * <p>
     *   Checks to see if this <code>AsyncHttpServer</code> is configured in
     *   persistent-mode.
     * </p>
     *
     * @return     <code>true</code> if configured in persistent-mode,
     *             <code>false</code> if not.
     * @see        #setPersistent(boolean)
     */
    public boolean isPersistent() {
        return persistent;
    }

    public boolean isValid(final String iceFacesId) {
        synchronized (sessionMap) {
            if (sessionMap.containsKey(iceFacesId)) {
                return true;
            } else {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("ICEfaces ID '" + iceFacesId + "' is not valid!");
                }
                return false;
            }
        }
    }

    public boolean isValid(
        final String iceFacesId, final String[] viewNumbers) {

        synchronized (sessionMap) {
            if (isValid(iceFacesId)) {
                if (viewNumbers != null && viewNumbers.length != 0) {
                    Set _viewNumberSet = (Set)sessionMap.get(iceFacesId);
                    for (int i = 0; i < viewNumbers.length; i++) {
                        if (!_viewNumberSet.contains(viewNumbers[i])) {
                            if (LOG.isDebugEnabled()) {
                                LOG.debug(
                                    "View Number '" + viewNumbers[i] + "' " +
                                        "is not valid!");
                            }
                            return false;
                        }
                    }
                }
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * <p>
     *   Pulls the pending request, represented as a process handler, for the
     *   specified <code>iceFacesId</code> off the queue.
     * </p>
     * <p>
     *   If <code>iceFacesId</code> is <code>null</code> or empty, or if there
     *   is no request in the queue for the <code>iceFacesId</code>,
     *   <code>null</code> is returned.
     * </p>
     *
     * @param      iceFacesId
     *                 the ICEfaces ID that identifies the session of the
     *                 requester.
     * @return     the request or <code>null</code>.
     * @see        #pushPendingRequest(String, ProcessHandler)
     * @see        #pullPendingUpdatedViews(String, long)
     * @see        #pushPendingUpdatedViews(UpdatedViews)
     */
    public ProcessHandler pullPendingRequest(final String iceFacesId) {
        if (iceFacesId == null || iceFacesId.trim().length() == 0) {
            return null;
        }
        synchronized (pendingRequestMap) {
            if (pendingRequestMap.containsKey(iceFacesId)) {
                final ProcessHandler _processHandler =
                    (ProcessHandler)pendingRequestMap.remove(iceFacesId);
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Pulled pending request: " + iceFacesId);
                }
                return _processHandler;
            }
            return null;
        }
    }

//    /**
//     * <p>
//     *   Pulls the pending request, represented as a process handler, for the
//     *   specified <code>iceFacesId</code> and <code>viewNumber</code> off the
//     *   queue.
//     * </p>
//     * <p>
//     *   If either <code>iceFacesId</code> or <code>viewNumber</code> is
//     *   <code>null</code> or empty, or if there is no request in the queue for
//     *   the <code>iceFacesId</code>-<code>viewNumber</code> combination,
//     *   <code>null</code> is returned.
//     * </p>
//     *
//     * @param      iceFacesId
//     *                 the ICEfaces ID that identifies the session of the
//     *                 requester.
//     * @param      viewNumber
//     *                 the view number that identifies the view of the
//     *                 requester.
//     * @return     the request or <code>null</code>.
//     * @see        #pushPendingRequest(String, String[], ProcessHandler)
//     * @see        #pullPendingResponses(String, String[], String[][])
//     * @see        #pushPendingResponse(Response)
//     */
//    public ProcessHandler pullPendingRequest(
//        final String iceFacesId, final String viewNumber) {
//
//        if (iceFacesId == null || iceFacesId.trim().length() == 0 ||
//            viewNumber == null || viewNumber.trim().length() == 0) {
//
//            return null;
//        }
//        synchronized (pendingRequestMap) {
//            if (pendingRequestMap.containsKey(iceFacesId)) {
//                final Map _map = (Map)pendingRequestMap.get(iceFacesId);
//                final Iterator _keys = _map.keySet().iterator();
//                while (_keys.hasNext()) {
//                    final String[] _viewNumbers = (String[])_keys.next();
//                    for (int i = 0; i < _viewNumbers.length; i++) {
//                        if (viewNumber.equals(_viewNumbers[i])) {
//                            final ProcessHandler _processHandler =
//                                (ProcessHandler)_map.remove(_viewNumbers);
//                            /*
//                             * Removing the map every time it is empty causes
//                             * quite the object churn. Ideally, the map often
//                             * has a max size of 1. If the following code is
//                             * activated, the map is constantly garbage
//                             * collected and created again.
//                             */
////                            if (_map.isEmpty()) {
////                                pendingRequestMap.remove(iceFacesId);
////                            }
//                            if (LOG.isDebugEnabled()) {
//                                LOG.debug(
//                                    "Pulled pending request: " +
//                                        iceFacesId + "/" + viewNumber);
//                            }
//                            return _processHandler;
//                        }
//                    }
//                 }
//             }
//             return null;
//         }
//    }

//    /**
//     * <p>
//     *   Pulls the pending response(s) for the specified
//     *   <code>iceFacesId</code>-<code>viewNumbers[i]</code> combinations off the
//     *   queue.
//     * </p>
//     * <p>
//     *   If either <code>iceFacesId</code> or <code>viewNumbers</code> is
//     *   <code>null</code> or empty, or if there is no response in the queue for
//     *   any <code>iceFacesId</code>-<code>viewNumbers[i]</code> combination,
//     *   <code>null</code> is returned.
//     * </p>
//     *
//     * @param      iceFacesId
//     *                 the ICEfaces ID that identifies the session of the
//     *                 requester.
//     * @param      viewNumbers
//     *                 the view numbers that identify the views of the
//     *                 requester.
//     * @param      sequenceNumbers
//     * @return     the request or <code>null</code>.
//     * @see        Response
//     * @see        #pushPendingResponse(Response)
//     * @see        #pullPendingRequest(String, String)
//     * @see        #pushPendingRequest(String, String[], ProcessHandler)
//     */
//    public ResponseCollection pullPendingResponses(
//        final String iceFacesId, final String[] viewNumbers,
//        final String[][] sequenceNumbers) {
//
//        if (iceFacesId == null || iceFacesId.trim().length() == 0) {
//            return null;
//        } else {
//            return
//                responseQueueManager.pull(
//                    iceFacesId, viewNumbers, sequenceNumbers);
//        }
//    }

    /**
     * <p>
     *   Pulls the pending updated views for the specified
     *   <code>iceFacesId</code> off the queue.
     * </p>
     * <p>
     *   If <code>iceFacesId</code> is <code>null</code> or empty, or if there
     *   are no updated views in the queue for the <code>iceFacesId</code>,
     *   <code>null</code> is returned.
     * </p>
     *
     * @param      iceFacesId
     *                 the ICEfaces ID that identifies the session of the
     *                 requester.
     * @param      sequenceNumber
     * @return     the updated views or <code>null</code>.
     * @see        #pushPendingUpdatedViews(UpdatedViews)
     * @see        #pullPendingRequest(String)
     * @see        #pushPendingRequest(String, ProcessHandler)
     */
    public UpdatedViews pullPendingUpdatedViews(
        final String iceFacesId, final long sequenceNumber) {

        if (iceFacesId == null || iceFacesId.trim().length() == 0) {
            return null;
        } else {
            return updatedViewsQueueManager.pull(iceFacesId, sequenceNumber);
        }
    }

    /**
     * <p>
     *   Pushes the specified <code>processHandler</code>, representing the
     *   request, for the specified <code>iceFacesId</code> on the queue.
     * </p>
     * <p>
     *   If <code>iceFacesId</code> is <code>null</code> or empty, or
     *   <code>processHandler</code> is <code>null</code>, nothing is pushed on
     *   the queue.
     * </p>
     *
     * @param      iceFacesId
     *                 the ICEfaces ID that identifies the session of the
     *                 requester.
     * @param      processHandler
     *                 the process handler that represents the pending request.
     * @see        #pullPendingRequest(String)
     * @see        #pushPendingUpdatedViews(UpdatedViews)
     * @see        #pullPendingUpdatedViews(String, long)
     */
    public void pushPendingRequest(
        final String iceFacesId, final ProcessHandler processHandler) {

        if (iceFacesId == null || iceFacesId.trim().length() == 0 ||
            processHandler == null) {

            return;
        }
        synchronized (pendingRequestMap) {
            pendingRequestMap.put(iceFacesId, processHandler);
            if (LOG.isDebugEnabled()) {
                LOG.debug("Pushed pending request: " + iceFacesId);
            }
        }
    }

//    /**
//     * <p>
//     *   Pushes the specified <code>processHandler</code>, representing the
//     *   request, for the specified <code>iceFacesId</code> and
//     *   <code>viewNumbers</code> on the queue.
//     * </p>
//     * <p>
//     *   If either <code>iceFacesId</code> or <code>viewNumbers</code> is
//     *   <code>null</code> or empty, or <code>processHandler</code> is
//     *   <code>null</code>, nothing is pushed on the queue.
//     * </p>
//     *
//     * @param      iceFacesId
//     *                 the ICEfaces ID that identifies the session of the
//     *                 requester.
//     * @param      viewNumbers
//     *                 the view numbers that identify the views of the
//     *                 requester.
//     * @param      processHandler
//     *                 the process handler that represents the pending request.
//     * @see        #pullPendingRequest(String, String)
//     * @see        #pushPendingResponse(Response)
//     * @see        #pullPendingResponses(String, String[], String[][])
//     */
//    public void pushPendingRequest(
//        final String iceFacesId, final String[] viewNumbers,
//        final ProcessHandler processHandler) {
//
//        if (iceFacesId == null || iceFacesId.trim().length() == 0 ||
//            viewNumbers == null || viewNumbers.length == 0 ||
//            processHandler == null) {
//
//            return;
//        }
//        synchronized (pendingRequestMap) {
//            final Map _map;
//            if (pendingRequestMap.containsKey(iceFacesId)) {
//                _map = (Map)pendingRequestMap.get(iceFacesId);
//                if (_map.containsKey(viewNumbers)) {
//                    /*
//                     * There already is a pending request from the requester
//                     * (ICEfacesID-viewNumbers), meaning that the requester
//                     * probably issued a reload. We should remove the request
//                     * from the queue.
//                     */
//                    _map.remove(viewNumbers);
//                }
//            } else {
//                _map = new HashMap();
//                pendingRequestMap.put(iceFacesId, _map);
//            }
//            _map.put(viewNumbers, processHandler);
//            if (LOG.isDebugEnabled()) {
//                LOG.debug(
//                    "Pushed pending request: " +
//                        iceFacesId + "/" + viewNumbers);
//            }
//        }
//    }

//    /**
//     * <p>
//     *   Pushes the specified <code>response</code> on the queue.
//     * </p>
//     * <p>
//     *   If <code>response</code> is <code>null</code>, nothing is pushed onto
//     *   the queue.
//     * </p>
//     *
//     * @param      response
//     *                 the response.
//     * @see        #pullPendingResponses(String, String[], String[][])
//     * @see        #pushPendingRequest(String, String[], ProcessHandler)
//     * @see        #pullPendingRequest(String, String)
//     */
//    public void pushPendingResponse(final Response response) {
//        try {
//            responseQueueManager.push(response);
//        } catch (ResponseQueueExceededException exception) {
//            if (LOG.isWarnEnabled()) {
//                LOG.warn(
//                    "Response queue exceeded: " +
//                        response.getICEfacesID() + "/" +
//                            response.getViewNumber());
//            }
//            Properties _messageProperties = new Properties();
//            publish(
//                response.getICEfacesID() + ";" + response.getViewNumber(),
//                _messageProperties,
//                RESPONSE_QUEUE_EXCEEDED_MESSAGE_TYPE,
//                MessageServiceClient.RESPONSE_TOPIC_NAME);
//        }
//    }

    /**
     * <p>
     *   Pushes the specified <code>updatedViews</code> on the queue. If the
     *   <code>updatedViews</code> is <code>null</code> nothing is pushed onto
     *   the queue.
     * </p>
     *
     * @param      updatedViews
     *                 the pending updated views to be queued.
     * @see        #pullPendingUpdatedViews(String, long)
     * @see        #pushPendingRequest(String, ProcessHandler)
     * @see        #pullPendingRequest(String)
     */
    public void pushPendingUpdatedViews(final UpdatedViews updatedViews) {
        if (updatedViews != null) {
            try {
                updatedViewsQueueManager.push(updatedViews);
            } catch (UpdatedViewsQueueExceededException exception) {
                if (LOG.isWarnEnabled()) {
                    LOG.warn(
                        "Updated views queue exceeded: " +
                            updatedViews.getICEfacesID());
                }
                Properties _messageProperties = new Properties();
                publish(
                    updatedViews.getICEfacesID(),
                    _messageProperties,
                    UPDATED_VIEWS_QUEUE_EXCEEDED_MESSAGE_TYPE,
                    MessageServiceClient.RESPONSE_TOPIC_NAME);
            }
        }
    }

    public boolean receiveBufferedEvents() {
        return true;
    }

    /**
     * <p>
     *   Removes the specified <code>asyncHttpServerListener</code> from this
     *   <code>AsyncHttpServer</code>.
     * </p>
     *
     * @param      asyncHttpServerListener
     *                 the Asynchronous HTTP Server listener to be removed.
     * @see        #addAsyncHttpServerListener(AsyncHttpServerListener)
     */
    public void removeAsyncHttpServerListener(
        final AsyncHttpServerListener asyncHttpServerListener) {

        asyncHttpServerListenerList.remove(
            AsyncHttpServerListener.class, asyncHttpServerListener);
    }

//    /**
//     * <p>
//     *   Send the specified <code>response</code> to the requester. If no
//     *   pending request could be found for the requester, the
//     *   <code>response</code> will be put into the queue, so it can be send to
//     *   the requester as soon as a new request becomes available.
//     * </p>
//     *
//     * @param      response
//     *                 the response to be send to the requester.
//     * @throws     IllegalArgumentException
//     *                 if the specified <code>response</code> is
//     *                 <code>null</code>.
//     */
//    public void sendResponse(final Response response)
//    throws IllegalArgumentException {
//        if (response == null) {
//            throw new IllegalArgumentException("response is null");
//        }
//        pushPendingResponse(response);
//        final ProcessHandler _processHandler =
//            pullPendingRequest(
//                response.getICEfacesID(), response.getViewNumber());
//        if (_processHandler != null) {
//            _processHandler.handle();
//        }
//    }

    /**
     * <p>
     *   Send the specified <code>updatedViews</code> to the requester. If no
     *   pending request could be found for the requester, the
     *   <code>updatedViews</code> will be put into the queue, so it can be send
     *   to the requester as soon as a new request becomes available.
     * </p>
     *
     * @param      updatedViews
     *                 the updatedViews to be send to the requester.
     * @throws     IllegalArgumentException
     *                 if the specified <code>updatedViews</code> is
     *                 <code>null</code>.
     */
    public void sendUpdatedViews(final UpdatedViews updatedViews)
    throws IllegalArgumentException {
        if (updatedViews == null) {
            throw new IllegalArgumentException("updatedViews is null");
        }
        pushPendingUpdatedViews(updatedViews);
        final ProcessHandler _processHandler =
            pullPendingRequest(updatedViews.getICEfacesID());
        if (_processHandler != null) {
            _processHandler.handle();
        }
    }

//    public void sessionDestroyed(SessionDestroyedEvent event) {
//        String _iceFacesId = event.getICEfacesID();
//        synchronized (sessionMap) {
//            if (sessionMap.containsKey(_iceFacesId)) {
//                sessionMap.remove(_iceFacesId);
//                synchronized (pendingRequestMap) {
//                    if (pendingRequestMap.containsKey(_iceFacesId)) {
//                        Iterator _processHandlers =
//                            ((Map)pendingRequestMap.remove(_iceFacesId)).
//                                values().iterator();
//                        while (_processHandlers.hasNext()) {
//                            ((ProcessHandler)_processHandlers.next()).handle();
//                        }
//                    }
//                }
//                synchronized (pendingResponseMap) {
//                    if (pendingResponseMap.containsKey(_iceFacesId)) {
//                        pendingResponseMap.remove(_iceFacesId);
//                    }
//                }
//            }
//        }
//    }

    /**
     * <p>
     *   Sets the blocking-mode of this <code>AsyncHttpServer</code> to the
     *   specified <code>blocking</code>.
     * </p>
     * <p>
     *   Please note that a restart of this <code>AsyncHttpServer</code> is
     *   required in order for it to be in blocking-mode.
     * </p>
     *
     * @param      blocking
     *                 the new blocking-mode value.
     * @see        #isBlocking()
     * @see        #stop()
     * @see        #start()
     */
    public void setBlocking(final boolean blocking) {
        this.blocking = blocking;
    }

    /**
     * <p>
     *   Sets the compression-mode of this <code>AsyncHttpServer</code> to
     *   the specified <code>compression</code>.
     * </p>
     *
     * @param      compression
     *                 the new compression-mode value.
     * @see        #useCompression()
     */
    public void setCompression(final boolean compression) {
        this.compression = compression;
    }

    /**
     * <p>
     *   Sets the persistent-mode of this <code>AsyncHttpServer</code> to the
     *   specified <code>persistent</code>.
     * </p>
     *
     * @param      persistent
     *                 the new persistent-mode value.
     * @see        #isPersistent()
     */
    public void setPersistent(final boolean persistent) {
        this.persistent = persistent;
    }

    /**
     * <p>
     *   Sets the port of this <code>AsyncHttpServer</code> to the specified
     *   <code>port</code>.
     * </p>
     * <p>
     *   Please note that a restart of this <code>AsyncHttpServer</code> is
     *   required in order for it to start accepting HTTP connections on the new
     *   port.
     * </p>
     *
     * @param      port
     *                 the new port.
     * @throws     IllegalArgumentException
     *                 if the specified <code>port</code> is lesser than
     *                 <code>0</code> or greater than <code>65535</code>.
     * @see        #getPort()
     * @see        #stop()
     * @see        #start()
     */
    public void setPort(final int port)
    throws IllegalArgumentException {
        if (port < 0 || port > 65535) {
            throw new IllegalArgumentException("Illegal port: " + port);
        }
        this.port = port;
    }

    /**
     * <p>
     *   Starts this <code>AsyncHttpServer</code>.
     * </p>
     * <p>
     *   When this <code>AsyncHttpServer</code> is started an
     *   <code>AsyncHttpServerEvent</code> is fired to all registered
     *   <code>AsyncHttpServerListener</code>s indicating it has started.
     * </p>
     *
     * @see        #stop()
     */
    public void start() {
        if (LOG.isInfoEnabled()) {
            LOG.info("Starting " + NAME + "...");
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("[1] Starting Message Service Client...");
        }
        setUpMessageServiceClient(servletContext);
        if (LOG.isInfoEnabled()) {
            LOG.info("    done.");
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("[2] Sending announcements...");
        }
        publish(
            AsyncHttpServer.class.getName(),
            null,
            ANNOUNCEMENT_MESSAGE_TYPE,
            MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME);
        publish(
            AsyncHttpServer.class.getName(),
            null,
            ANNOUNCEMENT_MESSAGE_TYPE,
            MessageServiceClient.RESPONSE_TOPIC_NAME);
        if (LOG.isInfoEnabled()) {
            LOG.info("    done.");
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("[3] Waiting...");
        }
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException exception) {
            // do nothing.
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("    done.");
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("[4] Start listening...");
        }
        if (blocking) {
            startBlocking();
        } else {
            startNonBlocking();
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("    done.");
        }
        if (LOG.isInfoEnabled()) {
            LOG.info(
                NAME + " started!\r\n" +
                "        blocking-mode          : " + blocking + "\r\n" +
                "        persistent connections : " + persistent + "\r\n" +
                "        HTTP compression       : " + compression + "\r\n" +
                "        execute queue size     : " +
                    executeQueue.getMaximumThreadPoolSize());
        }
        fireStartedEvent();
    }

    /**
     * <p>
     *   Stops this <code>AsyncHttpServer</code>.
     * </p>
     * <p>
     *   When this <code>AsyncHttpServer</code> is stopped an
     *   <code>AsyncHttpServerEvent</code> is fired to all registered
     *   <code>AsyncHttpServerListener</code>s indicating it has stopped.
     * </p>
     *
     * @see        #stop()
     */
    public void stop() {
        try {
        if (LOG.isInfoEnabled()) {
            LOG.info("Stopping " + NAME + "...");
        }
        tearDownMessageServiceClient();
        HttpConnectionAcceptor[] _httpConnectionAcceptors =
            (HttpConnectionAcceptor[])
                httpConnectionAcceptorMap.values().
                    toArray(
                        new HttpConnectionAcceptor[
                            httpConnectionAcceptorMap.size()]);
        for (int i = 0; i < _httpConnectionAcceptors.length; i++) {
            _httpConnectionAcceptors[i].requestStop();
        }
        httpConnectionAcceptorMap.clear();
        if (LOG.isInfoEnabled()) {
            LOG.info(NAME + " stopped!");
        }
        fireStoppedEvent();
        } catch (Throwable throwable) {
            LOG.error("ERROR!", throwable);
        }
    }

    /**
     * <p>
     *   Checks to see if this <code>AsyncHttpServer</code> is configured in
     *   compression-mode.
     * </p>
     *
     * @return     <code>true</code> if configured in compression-mode,
     *             <code>false</code> if not.
     * @see        #setCompression(boolean)
     */
    public boolean useCompression() {
        return compression;
    }

//    public void viewNumberRetrieved(ViewNumberRetrievedEvent event) {
//        String _iceFacesId = event.getICEfacesID();
//        synchronized (sessionMap) {
//            if (sessionMap.containsKey(_iceFacesId)) {
//                Set _viewNumberSet = (Set)sessionMap.get(_iceFacesId);
//                String _viewNumber = Integer.toString(event.getViewNumber());
//                if (!_viewNumberSet.contains(_viewNumber)) {
//                    _viewNumberSet.add(_viewNumber);
//                }
//            }
//        }
//    }

    private void fireStartedEvent() {
        EventListener[] _asyncHttpServerListeners =
            asyncHttpServerListenerList.getListeners(
                AsyncHttpServerListener.class);
        if (_asyncHttpServerListeners.length > 0) {
            AsyncHttpServerEvent _asyncHttpServerEvent =
                new AsyncHttpServerEvent(this);
            for (int i = 0; i < _asyncHttpServerListeners.length; i++) {
                ((AsyncHttpServerListener)_asyncHttpServerListeners[i]).
                    started(_asyncHttpServerEvent);
            }
        }
    }

    private void fireStoppedEvent() {
        EventListener[] _asyncHttpServerListeners =
            asyncHttpServerListenerList.getListeners(
                AsyncHttpServerListener.class);
        if (_asyncHttpServerListeners.length > 0) {
            AsyncHttpServerEvent _asyncHttpServerEvent =
                new AsyncHttpServerEvent(this);
            for (int i = 0; i < _asyncHttpServerListeners.length; i++) {
                ((AsyncHttpServerListener)_asyncHttpServerListeners[i]).
                    stopped(_asyncHttpServerEvent);
            }
        }
    }

    private void publish(
        final String message, final Properties messageProperties,
        final String messageType, final String topicName) {

        messageServiceClient.publish(
            message, messageProperties, messageType, topicName);
    }

//    private void publishResponseQueues(final String destinationNodeAddress) {
//        responseQueueManager.publishResponseQueues(destinationNodeAddress);
//    }

    private void publishUpdatedViewsQueues(
        final String destinationNodeAddress) {

        updatedViewsQueueManager.publishUpdatedViewsQueues(
            destinationNodeAddress);
    }

//    private void purgeResponses(final Map purgeMap) {
//        responseQueueManager.purgeAll(purgeMap);
//    }

    private void purgeUpdatedViews(final Map purgeMap) {
        updatedViewsQueueManager.purgeAll(purgeMap);
    }

    private void setUpMessageServiceClient(
        final ServletContext servletContext) {

        JMSProviderConfiguration _jmsProviderConfiguration =
            new JMSProviderConfigurationProperties(servletContext);
        messageServiceClient =
            new MessageServiceClient(
                _jmsProviderConfiguration,
                new JMSAdapter(_jmsProviderConfiguration),
                servletContext);
        try {
            messageServiceClient.subscribe(
                MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME,
                new MessageSelector(
                    new Or(
                        bufferedContextEventsMessageHandler.
                            getMessageSelector().getExpression(),
                        contextEventMessageHandler.
                            getMessageSelector().getExpression())));
        } catch (MessageServiceException exception) {
            if (LOG.isFatalEnabled()) {
                LOG.fatal(
                    "Failed to subscribe to topic: " +
                        MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME,
                    exception);
            }
        }
        try {
            messageServiceClient.subscribe(
                MessageServiceClient.RESPONSE_TOPIC_NAME,
                new MessageSelector(
                    new Or(
                        announcementMessageHandler.
                            getMessageSelector().getExpression(),
                        new Or (
                            updatedViewsMessageHandler.
                                getMessageSelector().getExpression(),
                            purgeMessageHandler.
                                getMessageSelector().getExpression()))));
        } catch (MessageServiceException exception) {
            if (LOG.isFatalEnabled()) {
                LOG.fatal(
                    "Failed to subscribe to topic: " +
                        MessageServiceClient.RESPONSE_TOPIC_NAME,
                    exception);
            }
        }
        messageServiceClient.addMessageHandler(
            bufferedContextEventsMessageHandler,
            MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME);
        messageServiceClient.addMessageHandler(
            contextEventMessageHandler,
            MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME);
        messageServiceClient.addMessageHandler(
            announcementMessageHandler,
            MessageServiceClient.RESPONSE_TOPIC_NAME);
        messageServiceClient.addMessageHandler(
                updatedViewsMessageHandler,
            MessageServiceClient.RESPONSE_TOPIC_NAME);
        messageServiceClient.addMessageHandler(
            purgeMessageHandler,
            MessageServiceClient.RESPONSE_TOPIC_NAME);
        try {
            messageServiceClient.start();
        } catch (MessageServiceException exception) {
            if (LOG.isFatalEnabled()) {
                LOG.fatal("Failed to start message delivery!", exception);
            }
        }
    }

    private void startBlocking() {
        HttpConnectionAcceptor _httpConnectionAcceptor =
            new IoHttpConnectionAcceptor(port, this);
        httpConnectionAcceptorMap.put(
            new Integer(port), _httpConnectionAcceptor);
        _httpConnectionAcceptor.start();
    }

    private void startNonBlocking() {
        HttpConnectionAcceptor _httpConnectionAcceptor =
            new NioHttpConnectionAcceptor(port, this);
        httpConnectionAcceptorMap.put(
            new Integer(port), _httpConnectionAcceptor);
        _httpConnectionAcceptor.start();
    }

    private void tearDownMessageServiceClient() {
        try {
            messageServiceClient.stop();
        } catch (MessageServiceException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error("Failed to stop message delivery!", exception);
            }
        }
        messageServiceClient.removeMessageHandler(
            bufferedContextEventsMessageHandler,
            MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME);
        messageServiceClient.removeMessageHandler(
            contextEventMessageHandler,
            MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME);
        messageServiceClient.removeMessageHandler(
            announcementMessageHandler,
            MessageServiceClient.RESPONSE_TOPIC_NAME);
        messageServiceClient.removeMessageHandler(
                updatedViewsMessageHandler,
            MessageServiceClient.RESPONSE_TOPIC_NAME);
        messageServiceClient.removeMessageHandler(
            purgeMessageHandler,
            MessageServiceClient.RESPONSE_TOPIC_NAME);
        try {
            messageServiceClient.closeConnection();
        } catch (MessageServiceException exception) {
            if (LOG.isErrorEnabled()) {
                LOG.error(
                    "Failed to close connection due to some internal error!",
                    exception);
            }
        }
    }
}

