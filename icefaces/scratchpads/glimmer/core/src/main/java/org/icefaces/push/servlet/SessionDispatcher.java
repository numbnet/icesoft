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

package org.icefaces.push.servlet;

import org.icefaces.push.Configuration;
import org.icefaces.push.http.AbstractServer;
import org.icefaces.push.http.Request;
import org.icefaces.push.http.Server;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class SessionDispatcher extends EnvironmentAdaptingServlet {
    private static Logger log = Logger.getLogger("org.icefaces.pushservlet");
    //ICE-3073 - manage sessions with this structure
    private final static Map SessionMonitors = new HashMap();
    private final static CurrentServer CurrentSessionBoundServer = new CurrentServer();
    private final Map sessionBoundServers = new HashMap();
    private ServletContext context;
    private boolean run = true;

    public SessionDispatcher(Configuration configuration, ServletContext context) {
        super(new AbstractServer() {
            public void service(Request request) throws Exception {
                //lookup session bound server -- this is a lock-free strategy
                CurrentSessionBoundServer.lookup().service(request);
            }
        }, configuration, context);
        //avoid instance collision -- Glassfish shares EAR module classloaders
        associateSessionDispatcher(context);
        this.context = context;

        Thread monitor = new Thread("Session Monitor") {
            public void run() {
                while (run) {
                    try {
                        // Iterate over the session monitors using a copying iterator
                        Iterator iterator = new ArrayList(SessionMonitors.values()).iterator();
                        while (iterator.hasNext()) {
                            final Monitor sessionMonitor = (Monitor) iterator.next();
                            sessionMonitor.shutdownIfExpired();
                        }

                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        //ignore interrupts
                    }
                }
            }
        };
        monitor.setDaemon(true);
        monitor.start();
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        checkSession(session);
        //attach session bound server to the current thread -- this is a lock-free strategy
        try {
            CurrentSessionBoundServer.attach(lookupServer(session));
            super.service(request, response);
        } finally {
            CurrentSessionBoundServer.detach();
        }
    }

    public void shutdown() {
        Iterator i = sessionBoundServers.values().iterator();
        while (i.hasNext()) {
            Server server = (Server) i.next();
            server.shutdown();
        }
        run = false;
    }

    protected abstract Server newServer(HttpSession session, Monitor sessionMonitor) throws Exception;

    protected void checkSession(HttpSession session) throws Exception {
        final String id = session.getId();
        final Monitor monitor;
        synchronized (SessionMonitors) {
            if (!SessionMonitors.containsKey(id)) {
                monitor = new Monitor(session);
                SessionMonitors.put(id, monitor);
            } else {
                monitor = (Monitor) SessionMonitors.get(id);
            }
            //it is possible to have multiple web-app contexts associated with the same session ID
            monitor.addInSessionContext(context);
        }

        synchronized (sessionBoundServers) {
            if (!sessionBoundServers.containsKey(id)) {
                sessionBoundServers.put(id, this.newServer(session, monitor));
            }
        }
    }

    protected Server lookupServer(final HttpSession session) {
        return lookupServer(session.getId());
    }

    protected Server lookupServer(final String sessionId) {
        return (Server) sessionBoundServers.get(sessionId);
    }

    private void sessionShutdown(HttpSession session) {
        Server servlet = (Server) sessionBoundServers.get(session.getId());
        servlet.shutdown();
    }

    private void sessionDestroy(HttpSession session) {
        sessionBoundServers.remove(session.getId());
    }

    /**
     * Perform the session shutdown tasks for a session that has either been invalidated via
     * the ICEfaces Session wrapper (internal) or via a sessionDestroyed event from a container
     * (external). #3164 If the Session has been externally invalidated this method doesn't need
     * to invalidate it again as that can cause infinite loops in some containers.
     *
     * @param session Session to invalidate
     */
    private static void notifySessionShutdown(final HttpSession session, final ServletContext context) {
        if (log.isLoggable(Level.FINE)) {
            log.fine("Shutting down session: " + session.getId());
        }
        String sessionID = session.getId();
        // avoid executing this method twice
        if (!SessionMonitors.containsKey(sessionID)) {
            log.fine("Session: " + sessionID + " already shutdown, skipping");
            return;
        }

        SessionDispatcher sessionDispatcher = lookupSessionDispatcher(context);
        //shutdown session bound server
        try {
            sessionDispatcher.sessionShutdown(session);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Unable to shutdown session", e);
        }

        synchronized (SessionMonitors) {
            try {
                sessionDispatcher.sessionDestroy(session);
            } catch (Exception e) {
                log.log(Level.SEVERE, "Unable to destory sessionDispatcher ", e);
            }
            //ICE-3189 - do this before invalidating the session
            SessionMonitors.remove(sessionID);
        }
    }

    public static void notifySessionShutdown(final HttpSession session) {
        notifySessionShutdown(session, session.getServletContext());
    }

    private void associateSessionDispatcher(ServletContext context) {
        context.setAttribute(SessionDispatcher.class.getName(), this);
    }

    private static SessionDispatcher lookupSessionDispatcher(ServletContext context) {
        return (SessionDispatcher) context.getAttribute(SessionDispatcher.class.getName());
    }

    public static class Monitor {
        private final String POSITIVE_SESSION_TIMEOUT = "positive_session_timeout";
        private Set contexts = new HashSet();
        private HttpSession session;
        private long lastAccess;

        private Monitor(HttpSession session) {
            this.session = session;
            this.lastAccess = session.getLastAccessedTime();
        }

        public void touchSession() {
            lastAccess = System.currentTimeMillis();
        }

        public Date expiresBy() {
            return new Date(lastAccess + (session.getMaxInactiveInterval() * 1000));
        }

        public boolean isExpired() {
            long elapsedInterval = System.currentTimeMillis() - lastAccess;
            int maxInterval = session.getMaxInactiveInterval();

            Object o = session.getAttribute(POSITIVE_SESSION_TIMEOUT);

            // Try to reset the max session timeout if it is -1 from a Failover on Tomcat...
            // But if it was originally negative, it should stay that way.
            if (maxInterval > 0) {

                if (o == null) {
                    session.setAttribute(POSITIVE_SESSION_TIMEOUT, new Integer(maxInterval));
                }
            } else {
                if (o != null) {
                    maxInterval = ((Integer) o).intValue();
                    session.setMaxInactiveInterval(maxInterval);
                }
            }

            return elapsedInterval + 15000 > maxInterval * 1000;
        }

        public void shutdown() {
            //notify all the contexts associated to this monitored session
            Iterator i = contexts.iterator();
            while (i.hasNext()) {
                ServletContext context = (ServletContext) i.next();
                notifySessionShutdown(session, context);
            }
            try {
                session.invalidate();
            } catch (IllegalStateException e) {
                log.info("Session already invalidated.");
            }
        }

        public void shutdownIfExpired() {
            if (isExpired()) {
                shutdown();
            }
        }

        public void addInSessionContext(ServletContext context) {
            contexts.add(context);
        }
    }

    private static class CurrentServer extends ThreadLocal {
        public Server lookup() {
            return (Server) get();
        }

        public void attach(Server server) {
            set(server);
        }

        public void detach() {
            set(null);
        }
    }
}
