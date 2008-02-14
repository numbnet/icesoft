package com.icesoft.faces.webapp.http.servlet;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
            notifySessionInitialized(session);
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
        PseudoServlet servlet = (PseudoServlet) sessionBoundServers.remove(session.getId());
        servlet.shutdown();
    }

    private static void notifySessionInitialized(HttpSession session) {
        SessionIDs.add(session.getId());
        Monitor monitor = new Monitor(session);

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

    public synchronized static void notifySessionShutdown(final HttpSession session) {
        SessionIDs.remove(session.getId());
        Iterator i = SessionDispatchers.iterator();
        while (i.hasNext()) {
            try {
                SessionDispatcher sessionDispatcher = (SessionDispatcher) i.next();
                sessionDispatcher.sessionShutdown(session);
            } catch (Exception e) {
                Log.error(e);
            }
        }

        //invalidate session right away!
        //see ICE-2731 -- delaying session invalidation doesn't work since JBoss+Catalina resuses session objects and
        //IDs which causes a lot of confusion in applications that have logout processes (invalidate session and
        //immediately initiate new session)
        session.invalidate();
    }

    //Exposing MainSessionBoundServlet for Tomcat 6 Ajax Push
    public static PseudoServlet getSingletonSessionServlet(HttpSession session) {
        return ((SessionDispatcher) SessionDispatchers.get(0)).lookupServlet(session);
    }

    public static class Listener implements ServletContextListener {
        private boolean run = true;

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
            SessionMonitors.add(this);
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
            try {
                SessionMonitors.remove(this);
                notifySessionShutdown(session);
            } catch (IllegalStateException e) {
                //session was already invalidated by the container
                Log.debug("Session already invalidated.");
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
