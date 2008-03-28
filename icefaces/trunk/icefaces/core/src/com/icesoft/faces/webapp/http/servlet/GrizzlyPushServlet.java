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

package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.util.event.servlet.ContextDestroyedEvent;
import com.icesoft.faces.util.event.servlet.ContextEventListener;
import com.icesoft.faces.util.event.servlet.ContextEventRepeater;
import com.icesoft.faces.util.event.servlet.ICEfacesIDDisposedEvent;
import com.icesoft.faces.util.event.servlet.ICEfacesIDRetrievedEvent;
import com.icesoft.faces.util.event.servlet.SessionDestroyedEvent;
import com.icesoft.faces.util.event.servlet.ViewNumberRetrievedEvent;
import com.icesoft.faces.webapp.http.core.ViewQueue;
import com.sun.enterprise.web.connector.grizzly.comet.CometContext;
import com.sun.enterprise.web.connector.grizzly.comet.CometEngine;
import com.sun.enterprise.web.connector.grizzly.comet.CometEvent;
import com.sun.enterprise.web.connector.grizzly.comet.CometHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GrizzlyPushServlet extends HttpServlet
implements ContextEventListener {
    private static final Log LOG = LogFactory.getLog(GrizzlyPushServlet.class);

    private final Map<HttpSession, EventResponder> eventResponderMap =
        new HashMap<HttpSession, EventResponder>();

    private CometContext cometContext;

    public void contextDestroyed(final ContextDestroyedEvent event) {
        // do nothing.
    }

    public void destroy() {
    }

    public void iceFacesIdDisposed(final ICEfacesIDDisposedEvent event) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException exception) {
            // ignore interrupts.
        }
        HttpSession session = event.getHttpSession();
        synchronized (session) {
            if (eventResponderMap.containsKey(session)) {
                eventResponderMap.get(session).dispose();
            }
        }
    }

    public void iceFacesIdRetrieved(final ICEfacesIDRetrievedEvent event) {
        // do nothing.
    }

    public void init(final ServletConfig servletConfig)
    throws ServletException {
        ContextEventRepeater.addListener(this);
        super.init(servletConfig);
        try {
            cometContext =
                CometEngine.getEngine().register(
                    ServletContext.class.getMethod("getContextPath").
                        invoke(
                            servletConfig.getServletContext(),
                            (Object[])null) +
                        "/block/receive-updated-views");
            cometContext.setExpirationDelay(-1);
        } catch (Exception exception) {
            throw
                new ServletException(
                    "ServletContext.getContextPath not defined", exception);
        }
    }

    public boolean receiveBufferedEvents() {
        return true;
    }

    public void sessionDestroyed(final SessionDestroyedEvent event) {
        HttpSession session = event.getHttpSession();
        synchronized (session) {
            if (eventResponderMap.containsKey(session)) {
                eventResponderMap.remove(session);
            }
        }
    }

    public void viewNumberRetrieved(final ViewNumberRetrievedEvent event) {
        // do nothing.
    }

    protected void service(
        final HttpServletRequest request, final HttpServletResponse response)
    throws IOException, ServletException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.setContentType("text/xml; charset=UTF-8");
            PrintWriter writer = response.getWriter();
            writer.write("<session-expired/>");
            writer.flush();
            return;
        }
        EventResponder eventResponder;
        synchronized (session) {
            if (!eventResponderMap.containsKey(session)) {
                MainSessionBoundServlet servlet =
                    (MainSessionBoundServlet)
                        SessionDispatcher.getSingletonSessionServlet(session);
                eventResponder =
                    new EventResponder(
                        servlet.getSessionID(),
                        servlet.getAllUpdatedViews(),
                        servlet.getSynchronouslyUpdatedViews(),
                        cometContext);
                eventResponderMap.put(session, eventResponder);
            } else {
                eventResponder = eventResponderMap.get(session);
            }
        }
        synchronized (eventResponder.allUpdatedViews) {
            Set<String> updatedViewSet = eventResponder.getUpdatedViews();
            if (!updatedViewSet.isEmpty()) {
                eventResponder.sendResponse(response, updatedViewSet);
            } else {
                eventResponder.attach(response);
                cometContext.addCometHandler(eventResponder);
            }
        }
    }

    private static class EventResponder
    implements CometHandler<HttpServletResponse>, Runnable {
        private static final Log LOG = LogFactory.getLog(EventResponder.class);

        private final String sessionID;
        private final ViewQueue allUpdatedViews;
        private final Collection synchronouslyUpdatedViews;
        private final CometContext cometContext;

        private HttpServletResponse response;

        private EventResponder(
            final String sessionID, final ViewQueue allUpdatedViews,
            final Collection synchronouslyUpdatedViews,
            final CometContext cometContext) {

            this.sessionID = sessionID;
            this.allUpdatedViews = allUpdatedViews;
            this.allUpdatedViews.onPut(this);
            this.synchronouslyUpdatedViews = synchronouslyUpdatedViews;
            this.cometContext = cometContext;
        }

        public void attach(final HttpServletResponse response) {
            this.response = response;
        }

        public void dispose() {
            synchronized (allUpdatedViews) {
                if (response != null) {
                    Set<String> updatedViewSet = getUpdatedViews();
//                    LOG.info("dispose():: updatedViewSet: " + updatedViewSet);
                    if (!updatedViewSet.isEmpty()) {
                        try {
                            while (!cometContext.isActive(this)) {
                                Thread.sleep(50);
                            }
                        } catch (InterruptedException exception) {
                            // ignoring interrupts.
                        }
                        try {
                            sendResponse(response, updatedViewSet);
                            response = null;
                            cometContext.resumeCometHandler(this);
                        } catch (IOException exception) {
                            if (LOG.isErrorEnabled()) {
                                LOG.error(
                                    "An I/O error occurred " +
                                        "while trying to send a response.",
                                    exception);
                            }
                        }
                    }
                }
                responseSender = CONNECTION_CLOSE_SENDER;
            }
        }

        public Set<String> getUpdatedViews() {
            Set<String> viewIdentifierSet;
            synchronized (allUpdatedViews) {
                allUpdatedViews.removeAll(synchronouslyUpdatedViews);
                synchronouslyUpdatedViews.clear();
                viewIdentifierSet = new HashSet<String>(allUpdatedViews);
                allUpdatedViews.clear();
            }
            return viewIdentifierSet;
        }

        public synchronized void onEvent(final CometEvent event)
        throws IOException {
            // do nothing.
        }

        public void onInitialize(final CometEvent event)
        throws IOException {
            // do nothing.
        }


        public void onInterrupt(CometEvent event)
        throws IOException {
            // do nothing.
        }

        public void onTerminate(final CometEvent event)
        throws IOException {
            // do nothing.
        }

        public void run() {
            synchronized (allUpdatedViews) {
                if (response != null) {
                    Set<String> updatedViewSet = getUpdatedViews();
                    if (!updatedViewSet.isEmpty()) {
                        try {
                            while (!cometContext.isActive(this)) {
                                Thread.sleep(50);
                            }
                        } catch (InterruptedException exception) {
                            // ignoring interrupts.
                        }
                        try {
                            sendResponse(response, updatedViewSet);
                            response = null;
                            cometContext.resumeCometHandler(this);
                        } catch (IOException exception) {
                            if (LOG.isErrorEnabled()) {
                                LOG.error(
                                    "An I/O error occurred " +
                                        "while trying to send a response.",
                                    exception);
                            }
                        }
                    }
                }
            }
        }

        private void sendResponse(
            final HttpServletResponse response,
            final Set<String> updatedViewSet)
        throws IOException {
            responseSender.send(sessionID, response, updatedViewSet);
        }

        private static final ResponseSender UPDATED_VIEWS_SENDER =
            new ResponseSender() {
                public void send(
                    final String sessionID, final HttpServletResponse response,
                    final Set<String> updatedViewSet)
                throws IOException {
                    response.setContentType("text/xml; charset=UTF-8");
                    String[] viewIdentifiers =
                        updatedViewSet.toArray(new String[updatedViewSet.size()]);
                    PrintWriter writer = response.getWriter();
                    writer.write("<updated-views>");
                    for (int i = 0; i < viewIdentifiers.length; i++) {
                        if (i != 0) {
                            writer.write(' ');
                        }
                        writer.write(sessionID + ":" + viewIdentifiers[i]);
                    }
                    writer.write("</updated-views>");
                    writer.flush();
                }
            };
        private static final ResponseSender CONNECTION_CLOSE_SENDER =
            new ResponseSender() {
                public void send(
                    final String sessionID, final HttpServletResponse response,
                    final Set<String> updatedViewSet)
                throws IOException {
                    /*
                     * let the bridge know that this blocking connection should
                     * not be re-initialized...
                     */
                    // entity header fields
                    response.setHeader("Content-Length", "0");
                    // extension header fields
                    response.setHeader("X-Connection", "close");
                }
            };

        private ResponseSender responseSender = UPDATED_VIEWS_SENDER;

        private static interface ResponseSender {
            public void send(
                final String sessionID,
                final HttpServletResponse response,
                final Set<String> updatedViewSet)
            throws IOException;
        }
    }
}
