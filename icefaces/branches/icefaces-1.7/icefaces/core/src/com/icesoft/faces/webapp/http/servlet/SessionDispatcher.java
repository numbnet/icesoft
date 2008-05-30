package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.webapp.http.core.SessionExpiredException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionListener;
import javax.servlet.http.HttpSessionEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class SessionDispatcher implements PseudoServlet {
    //having a static field here is ok because web applications are started in separate classloaders
    private final static Log Log = LogFactory.getLog(SessionDispatcher.class);
    private final static List SessionDispatchers = new ArrayList();
    // # 3073 Manage sessions with this structure
    private final static Map SessionMonitors = new HashMap();
    private Map sessionBoundServers = new HashMap();
    private PseudoServlet invalidRequestServlet;

    protected SessionDispatcher() {
        this(new PseudoServlet() {
            public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
                throw new SessionExpiredException();
            }

            public void shutdown() {
            }
        });
    }

    protected SessionDispatcher(PseudoServlet invalidRequestServlet) {
        this.invalidRequestServlet = invalidRequestServlet;
        SessionDispatchers.add(this);
    }

    protected abstract PseudoServlet newServlet(HttpSession session, Monitor sessionMonitor) throws Exception;

    public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
        //only HTTP GET requests can create new sessions 
        if (request.isRequestedSessionIdValid() || "GET".equalsIgnoreCase(request.getMethod())) {
            HttpSession session = request.getSession(true);
            notifyIfNew(session);
            lookupServlet(session).service(request, response);
        } else {
            invalidRequestServlet.service(request, response);
        }
    }

    //synchronize access in case there are multiple SessionDispatcher instances created
    private static void notifyIfNew(HttpSession session) {
        synchronized (SessionMonitors) {
            if (!SessionMonitors.containsKey(session.getId())) {
                notifySessionInitialized(session);
            }
        }
    }

    private PseudoServlet lookupServlet(HttpSession session) {
        return (PseudoServlet) sessionBoundServers.get(session.getId());
    }

    public void shutdown() {
        Iterator i = sessionBoundServers.values().iterator();
        while (i.hasNext()) {
            PseudoServlet server = (PseudoServlet) i.next();
            server.shutdown();
        }
    }

    private void sessionCreated(HttpSession session, Monitor monitor) {
        try {
            sessionBoundServers.put(session.getId(), this.newServlet(session, monitor));
        } catch (Exception e) {
            Log.warn(e);
            throw new RuntimeException(e);
        } catch (Throwable t) {
            Log.warn(t);
            throw new RuntimeException(t);
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
     * Create new session bound servers.
     */
    private static void notifySessionInitialized(HttpSession session) {
//        SessionIDs.add(session.getId());
        Monitor monitor = new Monitor(session);
        SessionMonitors.put(session.getId(), monitor);

        Iterator i = SessionDispatchers.iterator();
        while (i.hasNext()) {
            try {
                SessionDispatcher sessionDispatcher = (SessionDispatcher) i.next();
                sessionDispatcher.sessionCreated(session, monitor);
            } catch (Exception e) {
                Log.error(e);
            }
        }
    }

    private static void notifySessionShutdown(final HttpSession session) {

        if (Log.isDebugEnabled()) {
            Log.debug("Shutting down session: " + session.getId());
        }
        synchronized( SessionMonitors ) {
            String sessionID = session.getId();
            // avoid executing this method twice
            if (!SessionMonitors.containsKey(sessionID)) {
                if (Log.isDebugEnabled()) {
                    Log.debug("Session: " + sessionID + " already shutdown, skipping");
                    return;
                }
            }

            //shutdown session bound servers
            Iterator i = SessionDispatchers.iterator();
            while (i.hasNext()) {
                try {
                    SessionDispatcher sessionDispatcher = (SessionDispatcher) i.next();
                    sessionDispatcher.sessionShutdown(session);
                } catch (Exception e) {
                    Log.error(e);
                }
            }

        //invalidate session and discard session ID
                i = SessionDispatchers.iterator();
            while (i.hasNext()) {
                try {
                    SessionDispatcher sessionDispatcher = (SessionDispatcher) i.next();
                    sessionDispatcher.sessionDestroy(session);
                } catch (Exception e) {
                    Log.error(e);
                }
            }

//            String sessionID = session.getId();
            try {
                session.invalidate();
            } catch (IllegalStateException e) {
                Log.info("Session already invalidated.");
            } finally {
//                SessionIDs.remove(sessionID);
                SessionMonitors.remove(sessionID);
            }
        }
    }

    //Exposing MainSessionBoundServlet for Tomcat 6 Ajax Push
    public static PseudoServlet getSingletonSessionServlet(HttpSession session) {
        return ((SessionDispatcher) SessionDispatchers.get(0)).lookupServlet(session);
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
            notifySessionShutdown(event.getSession());
        } 
    }


    public static class Monitor {
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
            notifySessionShutdown(session);
        }

        public void shutdownIfExpired() {
            if (isExpired()) {
                shutdown();
            }
        }
    }
}
