package com.icesoft.faces.webapp.http.servlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public abstract class SessionDispatcher implements PseudoServlet {
    //having a static field here is ok because web applications are started in separate classloaders
    private final static Log Log = LogFactory.getLog(SessionDispatcher.class);
    private final static List SessionDispatchers = new ArrayList();
    private final static List SessionMonitors = new ArrayList();
    private final static List SessionIDs = new ArrayList();
    private Map sessionBoundServers = new HashMap();

    protected SessionDispatcher() {
        SessionDispatchers.add(this);
    }

    protected abstract PseudoServlet newServlet(HttpSession session, Monitor sessionMonitor) throws Exception;

    public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        notifyIfNew(session);
        lookupServlet(session).service(request, response);
    }

    //synchronize access in case there are multiple SessionDispatcher instances created
    private synchronized static void notifyIfNew(HttpSession session) {
        if (!SessionIDs.contains(session.getId())) {
            notifySessionCreated(session);
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
        lookupServlet(session).shutdown();
    }

    private void sessionDestroyed(HttpSession session) {
        sessionBoundServers.remove(session.getId());
    }

    private static void notifySessionCreated(HttpSession session) {
        SessionIDs.add(session.getId());
        Monitor monitor = new Monitor(session);
        SessionMonitors.add(monitor);

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

    public static void notifySessionShutdown(HttpSession session) {
        Iterator i = SessionDispatchers.iterator();
        while (i.hasNext()) {
            try {
                SessionDispatcher sessionDispatcher = (SessionDispatcher) i.next();
                sessionDispatcher.sessionShutdown(session);
            } catch (Exception e) {
                Log.error(e);
            }
        }
        session.invalidate();
    }

    public static void notifySessionDestroyed(HttpSession session) {
        Iterator i = SessionDispatchers.iterator();
        while (i.hasNext()) {
            try {
                SessionDispatcher sessionDispatcher = (SessionDispatcher) i.next();
                sessionDispatcher.sessionDestroyed(session);
            } catch (Exception e) {
                Log.error(e);
            }
        }
    }

    //Exposing MainSessionBoundServlet for Tomcat 6 Ajax Push
    public static PseudoServlet getSingletonSessionServlet(HttpSession session) {
        return ((SessionDispatcher) SessionDispatchers.get(0)).lookupServlet(session);
    }

    public static class Listener implements HttpSessionListener, ServletContextListener {
        private boolean run = true;

        public void sessionCreated(HttpSessionEvent event) {
        }

        public void sessionDestroyed(HttpSessionEvent event) {
            notifySessionDestroyed(event.getSession());
        }

        public void contextInitialized(ServletContextEvent servletContextEvent) {
            Thread monitor = new Thread("Session Monitor") {
                public void run() {
                    while (run) {
                        try {
                            //iterate over the sessions using a copying iterator
                            Iterator iterator = new ArrayList(SessionMonitors).iterator();
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

        public boolean isExpired() {
            long elapsedInterval = System.currentTimeMillis() - lastAccess;
            long maxInterval = session.getMaxInactiveInterval() * 1000;
            //shutdown the session a bit (15s) before session actually expires
            return elapsedInterval + 15000 > maxInterval;
        }

        public void shutdown() {
            try {
                SessionMonitors.remove(session);
                notifySessionShutdown(session);
            } catch (IllegalStateException e) {
                //session was already invalidated by the container
            } catch (Throwable t) {
                //just inform that something went wrong
                Log.warn("Failed to monitor session expiry", t);
            }
        }

        public void shutdownIfExpired() {
            if (isExpired()) {
                shutdown();
            }
        }
    }
}
