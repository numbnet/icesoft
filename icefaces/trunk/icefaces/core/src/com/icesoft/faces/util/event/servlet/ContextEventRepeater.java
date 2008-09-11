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
 *
 */

package com.icesoft.faces.util.event.servlet;

import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.servlet.ServletContextConfiguration;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/*
 * The ContextEventRepeater was designed to forward servlet events to different
 * parts of the ICEfaces framework. These events are typically of interest for
 * gracefully and/or proactively keeping track of valid sessions and allowing
 * for orderly shut down.
 *
 * This was deemed necessary since the Servlet specification does not allow a
 * programmatic way of adding and removing listeners for these events. So rather
 * than have the various ICEfaces pieces register listeners individually, we can
 * register this class and then add and remove listeners as required.
 *
 * The implementation is currently simple and broad. The class maintains a
 * static collection of listeners in a WeakHashMap and forwards all events to
 * all registered listeners.
 *
 * Future improvements might include:
 * - adapter implementations
 * - event filtering
 *
 * For now, anything that is interested in receiving events from the repeater
 * should simply implement the ContextEventListener interface and then add
 * itself to the ContextEventRepeater using the static addListener method.
 *
 * The limitation of adding a listener programmatically is that certain creation
 * events can occur before the class has a chance to add itself as a listener.
 * To mitigate this, the ContextEventRepeater buffers the creation events
 * temporarily.  When a ContextEventListener is added, the receiveBufferedEvents
 * method is called and, if it returns true, any buffered creation events are
 * sent to the listener after it has been added to the listener collection. The
 * timing of the events is off but the session information can still be useful.
 * Events are removed from the buffer when the corresponding destroy events are
 * received.  This means that that sessions that have already been created AND
 * destroyed are NOT in the buffer.
 */

/**
 *
 */
public class ContextEventRepeater
implements HttpSessionListener, ServletContextListener {
    private static final String MESSAGING_CONTEXT_EVENT_PUBLISHER_CLASS_NAME =
        "com.icesoft.faces.util.event.servlet.MessagingContextEventPublisher";

    private static final Log LOG =
            LogFactory.getLog(ContextEventRepeater.class);

    //todo: fix it... this is just a temporary solution
    private static SessionDispatcher.Listener SessionDispatcherListener;
    private static ContextDestroyedEvent classloadCDEvent;
    private static SessionDestroyedEvent classloadSDEvent;

    static {
        SessionDispatcherListener = new SessionDispatcher.Listener();
    }

    private static Map bufferedContextEvents = new HashMap();
    private static ContextEventPublisher contextEventPublisher;
    private static Map listeners = new WeakHashMap();

    /**
     * Adds the specified <code>listener</code> to this
     * <code>ContextEventRepeater</code>. </p>
     *
     * @param contextEventListener the listener to be added.
     */
    public synchronized static void addListener(
        final ContextEventListener contextEventListener) {

        if (contextEventListener == null ||
            listeners.containsKey(contextEventListener)) {

            return;
        }
        listeners.put(contextEventListener, null);
        if (contextEventListener.receiveBufferedEvents()) {
            sendBufferedEvents(contextEventListener);
        }
    }

    /**
     * Fires a new <code>ContextDestroyedEvent</code>, based on the received
     * <code>event</code>, to all registered listeners, and cleans itself
     * up. </p>
     *
     * @param event the servlet context event.
     */
    public synchronized void contextDestroyed(final ServletContextEvent event) {
        SessionDispatcherListener.contextDestroyed(event);

        ContextDestroyedEvent contextDestroyedEvent =
            new ContextDestroyedEvent(event);
        Iterator it = listeners.keySet().iterator();
        while (it.hasNext()) {
            ((ContextEventListener) it.next()).
                contextDestroyed(contextDestroyedEvent);
        }
        listeners.clear();
        bufferedContextEvents.clear();
        if (contextEventPublisher != null) {
            try {
                contextEventPublisher.publish(contextDestroyedEvent);
            } catch (Exception exception) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Couldn't publish event!", exception);
                }
            }
        }
        if (LOG.isInfoEnabled()) {
            ServletContext servletContext =
                contextDestroyedEvent.getServletContext();
            LOG.info(
                "Servlet Context Name: " +
                    servletContext.getServletContextName() + ", " +
                "Server Info: " + servletContext.getServerInfo());
        }
    }

    public synchronized void contextInitialized(
        final ServletContextEvent event) {

        SessionDispatcherListener.contextInitialized(event);

        Configuration _configuration =
            new ServletContextConfiguration(
                "com.icesoft.faces", event.getServletContext());
        // checking if Asynchronous HTTP Service is available...
        boolean isAsyncHttpServiceAvailable = isAsyncHttpServiceAvailable();
        if (LOG.isInfoEnabled()) {
            LOG.info(
                "Asynchronous HTTP Service available: " +
                    isAsyncHttpServiceAvailable);
        }
        boolean isJMSAvailable = isJMSAvailable();
        if (LOG.isInfoEnabled()) {
            LOG.info("JMS API available: " + isJMSAvailable);
        }
        if (isAsyncHttpServiceAvailable &&
            _configuration.getAttribute(
                "blockingRequestHandler",
                _configuration.getAttributeAsBoolean(
                    "async.server", true) ?
                        "icefaces-ahs" :
                        "icefaces").
                    equalsIgnoreCase("icefaces-ahs") &&
            isJMSAvailable) {

            try {
                contextEventPublisher =
                    (ContextEventPublisher)
                        Class.forName(
                            MESSAGING_CONTEXT_EVENT_PUBLISHER_CLASS_NAME).
                               newInstance();
                contextEventPublisher.setContextEventRepeater(this);
                try {
                    contextEventPublisher.publish(
                        new ContextInitializedEvent(event));
                } catch (Exception exception) {
                    contextEventPublisher = null;
                    if (LOG.isDebugEnabled()) {
                        LOG.debug("Couldn't publish event!", exception);
                    }
                }
            } catch (ClassNotFoundException exception) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("MessagingContextEventPublisher is not found!");
                }
            } catch (IllegalAccessException exception) {
                if (LOG.isFatalEnabled()) {
                    LOG.fatal(
                        "Failed to access constructor of " +
                            "MessagingContextEventPublisher!",
                        exception);
                }
            } catch (InstantiationException exception) {
                if (LOG.isFatalEnabled()) {
                    LOG.fatal(
                        "Failed to " +
                            "instantiate MessagingContextEventPublisher!",
                        exception);
                }
            }
        }
    }

    public synchronized static void iceFacesIdDisposed(
        final HttpSession source, final String iceFacesId) {

        ICEfacesIDDisposedEvent iceFacesIdDisposedEvent =
            new ICEfacesIDDisposedEvent(source, iceFacesId);
        bufferedContextEvents.put(iceFacesIdDisposedEvent, source);
        Iterator _listeners = listeners.keySet().iterator();
        while (_listeners.hasNext()) {
            ((ContextEventListener) _listeners.next()).
                iceFacesIdDisposed(iceFacesIdDisposedEvent);
        }
        if (contextEventPublisher != null) {
            try {
                contextEventPublisher.publish(iceFacesIdDisposedEvent);
            } catch (Exception exception) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Couldn't publish event!", exception);
                }
            }
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace(
                "ICEfaces ID disposed: " +
                    iceFacesIdDisposedEvent.getICEfacesID());
        }
    }

    /**
     * Fires a new <code>ICEfacesIDRetrievedEvent</code>, with the specified
     * <code>source</code> and </code>iceFacesId</code>, to all registered
     * listeners. </p>
     *
     * @param source     the source of the event.
     * @param iceFacesId the ICEfaces ID.
     */
    public synchronized static void iceFacesIdRetrieved(
        final HttpSession source, final String iceFacesId) {

        ICEfacesIDRetrievedEvent iceFacesIdRetrievedEvent =
            new ICEfacesIDRetrievedEvent(source, iceFacesId);
        bufferedContextEvents.put(iceFacesIdRetrievedEvent, source);
        Iterator _listeners = listeners.keySet().iterator();
        while (_listeners.hasNext()) {
            ((ContextEventListener) _listeners.next()).
                iceFacesIdRetrieved(iceFacesIdRetrievedEvent);
        }
        if (contextEventPublisher != null) {
            try {
                contextEventPublisher.publish(iceFacesIdRetrievedEvent);
            } catch (Exception exception) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Couldn't publish event!", exception);
                }
            }
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace(
                "ICEfaces ID retrieved: " +
                    iceFacesIdRetrievedEvent.getICEfacesID());
        }
    }

    /**
     * Removes the specified <code>listener</code> from this
     * <code>ContextEventRepeater</code>. </p>
     *
     * @param contextEventListener the listener to be removed.
     */
    public synchronized static void removeListener(
            final ContextEventListener contextEventListener) {

        if (contextEventListener == null) {
            return;
        }
        listeners.remove(contextEventListener);
    }

    public synchronized void sessionCreated(final HttpSessionEvent event) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("session Created event: " + event.getSession().getId());
        }
    }

    /**
     * Fires a new <code>SessionDestroyedEvent</code>, based on the received
     * <code>event</code>, to all registered listeners. </p>
     *
     * @param event the HTTP session event.
     */
    public synchronized void sessionDestroyed(final HttpSessionEvent event) {
        if (LOG.isDebugEnabled() ) {
            LOG.debug("SessionDestroyed event, session: " + event.getSession().getId());
        }
        // #3073 directly invoke method on SessionDispatcher for all sessions
        // (Not just wrapped ones)
        SessionDispatcherListener.sessionDestroyed(event);
        SessionDestroyedEvent sessionDestroyedEvent =
            new SessionDestroyedEvent(event);
        Iterator _listeners = listeners.keySet().iterator();
        while (_listeners.hasNext()) {
            ((ContextEventListener) _listeners.next()).
                sessionDestroyed(sessionDestroyedEvent);
        }
        removeBufferedEvents(event.getSession());
        if (contextEventPublisher != null) {
            try {
                contextEventPublisher.publish(sessionDestroyedEvent);
            } catch (Exception exception) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Couldn't publish event!", exception);
                }
            }
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace("ICEfaces ID: " + sessionDestroyedEvent.getICEfacesID());
        }
    }

    /**
     * Fires a new <code>ViewNumberRetrievedEvent</code>, with the specified
     * <code>source</code> and </code>viewNumber</code>, to all registered
     * listeners. </p>
     *
     * @param source     the source of the event.
     * @param viewNumber the view number.
     */
    public synchronized static void viewNumberRetrieved(
        final HttpSession source, final String icefacesID,
        final int viewNumber) {

        ViewNumberRetrievedEvent viewNumberRetrievedEvent =
            new ViewNumberRetrievedEvent(source, icefacesID, viewNumber);
        bufferedContextEvents.put(viewNumberRetrievedEvent, source);
        Iterator _listeners = listeners.keySet().iterator();
        while (_listeners.hasNext()) {
            ((ContextEventListener) _listeners.next()).
                viewNumberRetrieved(viewNumberRetrievedEvent);
        }
        if (contextEventPublisher != null) {
            try {
                contextEventPublisher.publish(viewNumberRetrievedEvent);
            } catch (Exception exception) {
                if (LOG.isDebugEnabled()) {
                    LOG.debug("Couldn't publish event!", exception);
                }
            }
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace(
                "New View number created: " + viewNumberRetrievedEvent.getViewNumber());
        }
    }

    ContextEvent[] getBufferedContextEvents() {
        Set _contextEventSet = bufferedContextEvents.keySet();
        return
            (ContextEvent[])
                _contextEventSet.toArray(
                    new ContextEvent[_contextEventSet.size()]);
    }

    private boolean isAsyncHttpServiceAvailable() {
        try {
            this.getClass().getClassLoader().loadClass(
                "com.icesoft.faces.async.server." +
                    "AsyncHttpServerAdaptingServlet");
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }

    private boolean isJMSAvailable() {
        try {
            this.getClass().getClassLoader().loadClass(
                "javax.jms.TopicConnectionFactory");
            return true;
        } catch (ClassNotFoundException exception) {
            return false;
        }
    }

    private synchronized static void removeBufferedEvents(
        final HttpSession session) {

        Iterator it = bufferedContextEvents.keySet().iterator();
        Object event = null;
        HttpSession bufferedSession = null;
        while (it.hasNext()) {
            event = it.next();
            bufferedSession = (HttpSession) bufferedContextEvents.get(event);
            if (bufferedSession.equals(session)) {
                //bufferedContextEvents.remove(event);
                it.remove();
            }
        }
    }

    private synchronized static void sendBufferedEvents(
        final ContextEventListener contextEventListener) {

        Iterator it = bufferedContextEvents.keySet().iterator();
        while (it.hasNext()) {
            Object event = it.next();
            if (event instanceof ICEfacesIDRetrievedEvent) {
                contextEventListener.iceFacesIdRetrieved(
                    (ICEfacesIDRetrievedEvent) event);
            } else if (event instanceof ViewNumberRetrievedEvent) {
                contextEventListener.viewNumberRetrieved(
                    (ViewNumberRetrievedEvent) event);
            }
        }
    }
}
