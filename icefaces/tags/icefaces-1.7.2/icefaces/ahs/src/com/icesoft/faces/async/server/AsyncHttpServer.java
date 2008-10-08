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
import com.icesoft.faces.async.common.messaging.MessageService;
import com.icesoft.faces.async.server.nio.NioHttpConnectionAcceptor;
import com.icesoft.faces.async.common.SessionManager;
import com.icesoft.faces.async.common.ExecuteQueue;
//import com.icesoft.faces.util.event.servlet.*;
import com.icesoft.net.messaging.MessageServiceClient;

import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
/*implements ContextEventListener*/ {
    public static final String NAME = "Asynchronous HTTP Server 1.7";

    private static final String ANNOUNCEMENT_MESSAGE_TYPE = "Announcement";
    private static final Log LOG = LogFactory.getLog(AsyncHttpServer.class);

    private final EventListenerList asyncHttpServerListenerList =
        new EventListenerList();
    private final Map httpConnectionAcceptorMap = new HashMap();

    // Asynchronous HTTP Server properties with their default values:
    private boolean blocking;
    private boolean compression;
    private boolean persistent;
    private int port;

    private SessionManager sessionManager;
    private MessageService messageService;

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
        final SessionManager sessionManager,
        final MessageService messageService)
    throws IllegalArgumentException {
        if (asyncHttpServerSettings == null) {
            throw
                new IllegalArgumentException("asyncHttpServerSettings is null");
        }
        setBlocking(asyncHttpServerSettings.isBlocking());
        setCompression(asyncHttpServerSettings.useCompression());
        setPersistent(asyncHttpServerSettings.isPersistent());
        setPort(asyncHttpServerSettings.getPort());
//        ContextEventRepeater.addListener(this);
        this.sessionManager = sessionManager;
        this.messageService = messageService;
        if (!blocking) {
            httpConnectionAcceptorMap.put(
                new Integer(port),
                new NioHttpConnectionAcceptor(
                    port,
                    new ExecuteQueue(),
                    this));
        } else {
            httpConnectionAcceptorMap.put(
                new Integer(port),
                new IoHttpConnectionAcceptor(
                    port,
                    new ExecuteQueue(),
                    this));
        }
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

    public void cancelRequest(final Set iceFacesIdSet) {
        // todo: refactor this!
        com.icesoft.faces.async.common.Handler _handler =
            sessionManager.getRequestManager().pull(iceFacesIdSet);
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

    public SessionManager getSessionManager() {
        return sessionManager;
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
            LOG.info("[1] Sending announcements...");
        }
        messageService.publish(
            AsyncHttpServer.class.getName(),
            null,
            ANNOUNCEMENT_MESSAGE_TYPE,
            MessageServiceClient.CONTEXT_EVENT_TOPIC_NAME);
        messageService.publish(
            AsyncHttpServer.class.getName(),
            null,
            ANNOUNCEMENT_MESSAGE_TYPE,
            MessageServiceClient.RESPONSE_TOPIC_NAME);
        if (LOG.isInfoEnabled()) {
            LOG.info("    done.");
            LOG.info("[2] Waiting...");
        }
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException exception) {
            // do nothing.
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("    done.");
            LOG.info("[3] Start listening...");
        }
        HttpConnectionAcceptor[] _httpConnectionAcceptors =
            (HttpConnectionAcceptor[])
                httpConnectionAcceptorMap.values().
                    toArray(
                        new HttpConnectionAcceptor[
                            httpConnectionAcceptorMap.size()]);
        for (int i = 0; i < _httpConnectionAcceptors.length; i++) {
            _httpConnectionAcceptors[i].start();
        }
        if (LOG.isInfoEnabled()) {
            LOG.info("    done.");
            LOG.info(
                NAME + " started!\r\n" +
                "        blocking-mode          : " + blocking + "\r\n" +
                "        persistent connections : " + persistent + "\r\n" +
                "        HTTP compression       : " + compression);
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
}