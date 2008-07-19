package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.util.ThreadLocalUtility;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public abstract class SessionDispatcher implements PseudoServlet {
    private final static Log Log = LogFactory.getLog(SessionDispatcher.class);
    //ICE-3073 - manage sessions with this structure
    private final static Map SessionMonitors = new HashMap();
    private Map sessionBoundServers = new HashMap();
    private ServletContext context;

    public SessionDispatcher(ServletContext context) {
        //avoid instance collision -- Glassfish shares EAR module classloaders
        associateSessionDispatcher(context);
        this.context = context;
    }

    protected abstract PseudoServlet newServlet(HttpSession session, Monitor sessionMonitor) throws Exception;

    public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        checkSession(session);
        lookupServlet(session).service(request, response);
    }

    protected void checkSession(HttpSession session) {
        try {
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
                    sessionBoundServers.put(id, this.newServlet(session, monitor));
                }
            }
        } catch (Exception e) {
            Log.error(e);
        }
    }

    protected PseudoServlet lookupServlet(final HttpSession session) {
        return lookupServlet(session.getId());
    }

    protected PseudoServlet lookupServlet(final String sessionId) {
        return (PseudoServlet) sessionBoundServers.get(sessionId);
    }

    public void shutdown() {
        Iterator i = sessionBoundServers.values().iterator();
        while (i.hasNext()) {
            PseudoServlet server = (PseudoServlet) i.next();
            server.shutdown();
        }
    }

    private void sessionShutdown(HttpSession session) {
        PseudoServlet servlet = (PseudoServlet) sessionBoundServers.get(session.getId());
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
        Log.debug("Shutting down session: " + session.getId());
        String sessionID = session.getId();
        // avoid executing this method twice
        if (!SessionMonitors.containsKey(sessionID)) {
            Log.debug("Session: " + sessionID + " already shutdown, skipping");
            return;
        }

        SessionDispatcher sessionDispatcher = lookupSessionDispatcher(context);
        //shutdown session bound server
        try {
            sessionDispatcher.sessionShutdown(session);
        } catch (Exception e) {
            Log.error(e);
        }

        synchronized (SessionMonitors) {
            try {
                sessionDispatcher.sessionDestroy(session);
            } catch (Exception e) {
                Log.error(e);
            }
            //ICE-3189 - do this before invalidating the session
            SessionMonitors.remove(sessionID);
        }
    }

    //Exposing MainSessionBoundServlet for Tomcat 6 Ajax Push
    public static PseudoServlet getSingletonSessionServlet(final HttpSession session, ServletContext context) {
        return lookupSessionDispatcher(context).lookupServlet(session);
    }

    public static PseudoServlet getSingletonSessionServlet(final String sessionId, Map applicationMap) {
        return lookupSessionDispatcher(applicationMap).lookupServlet(sessionId);
    }

    public static class Listener implements ServletContextListener, HttpSessionListener {
        private boolean run = true;

        public void contextInitialized(ServletContextEvent servletContextEvent) {
            Thread monitor = new Thread("Session Monitor") {
                public void run() {
                    while (run) {
                        try {
                            // Iterate over the session monitors using a copying iterator
                            Iterator iterator = new ArrayList(SessionMonitors.values()).iterator();
                            while (iterator.hasNext()) {
                                final Monitor sessionMonitor = (Monitor) iterator.next();
                                sessionMonitor.shutdownIfExpired();

                                if (Log.isTraceEnabled()) {
                                    ThreadLocalUtility.checkThreadLocals(ThreadLocalUtility.EXITING_SESSION_MONITOR);
                                }
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

        public void contextDestroyed(ServletContextEvent servletContextEvent) {
            run = false;
        }

        public void sessionCreated(HttpSessionEvent event) {
        }

        public void sessionDestroyed(HttpSessionEvent event) {
            HttpSession session = event.getSession();
            notifySessionShutdown(session, session.getServletContext());
        }
    }

    private void associateSessionDispatcher(ServletContext context) {
        context.setAttribute(SessionDispatcher.class.getName(), this);
    }

    private static SessionDispatcher lookupSessionDispatcher(ServletContext context) {
        return (SessionDispatcher) context.getAttribute(SessionDispatcher.class.getName());
    }

    private static SessionDispatcher lookupSessionDispatcher(Map applicationMap) {
        return (SessionDispatcher) applicationMap.get(SessionDispatcher.class.getName());
    }

    public static class Monitor {
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
            long maxInterval = session.getMaxInactiveInterval() * 1000;
            //shutdown the session a bit (15s) before session actually expires
            return elapsedInterval + 15000 > maxInterval;
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
                Log.info("Session already invalidated.");
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
}
