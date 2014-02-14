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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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

package com.icesoft.faces.webapp.http.servlet;

import com.icesoft.faces.context.ViewStatus;
import com.icesoft.faces.env.Authorization;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.standard.ResponseHandlerServer;
import com.icesoft.faces.webapp.http.core.SessionExpiredException;
import com.icesoft.faces.webapp.http.core.SessionExpiredResponse;
import com.icesoft.util.ThreadLocalUtility;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public abstract class SessionDispatcher implements PseudoServlet {
    private final static Log Log = LogFactory.getLog(SessionDispatcher.class);
    //ICE-3073 - manage sessions with this structure
    private final static Map SessionMonitors = new HashMap();
    private static String sessionIdDelimiter;

    private final Map sessionBoundServers = new WeakHashMap();
    private final Map activeRequests = new HashMap();
    private final PseudoServlet notFoundServer;
    private final PseudoServlet sessionExpiredServlet;

    private ServletContext context;

    public SessionDispatcher(final ServletContext context, final Configuration configuration) {
        associateSessionDispatcher(context);
        this.context = context;
        this.sessionIdDelimiter = configuration.getAttribute("sessionIdDelimiter", null);
        this.notFoundServer = new BasicAdaptingServlet(new NotFoundServer(), configuration);
        this.sessionExpiredServlet = new BasicAdaptingServlet(new ResponseHandlerServer(SessionExpiredResponse.Handler), configuration);
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        checkSession(session);

        //attach session bound server to the current thread -- this is a lock-free strategy
        String id = getId(session);
        try {
            //put the request in the pool of active request in case HttpServletRequest.isUserInRole need to be called
            addRequest(id, request);
            //lookup session bound server -- this is a lock-free strategy
            PseudoServlet sessionBoundServer = lookupServer(session);
            if (sessionBoundServer != null) {
                sessionBoundServer.service(request, response);
            } else {
                notFoundServer.service(request, response);
            }
        } catch (SessionExpiredException e) {
            sessionExpiredServlet.service(request, response);
        } finally {
            //remove the request from the active requests pool
            removeRequest(id, request);
        }
    }

    public void shutdown() {
        Iterator i = sessionBoundServers.values().iterator();
        while (i.hasNext()) {
            PseudoServlet pseudoServlet = (PseudoServlet) ((WeakReference) i.next()).get();
            if (null != pseudoServlet) {
                pseudoServlet.shutdown();
            }
        }
    }

    protected abstract PseudoServlet newServer(HttpSession session, Monitor sessionMonitor, Authorization authorization) throws Exception;

    protected void checkSession(HttpSession session) throws SessionExpiredException {
        try {
            final String id = getId(session);
            final Monitor monitor;
            synchronized (SessionMonitors) {
                if (!SessionMonitors.containsKey(id)) {
                    monitor = new Monitor(id, session);
                    SessionMonitors.put(id, monitor);
                } else {
                    monitor = (Monitor) SessionMonitors.get(id);
                }
                //it is possible to have multiple web-app contexts associated with the same session ID
                monitor.addInSessionContext(context);
            }

            synchronized (sessionBoundServers) {
                if (!sessionBoundServers.containsKey(id)) {
                    PseudoServlet pservlet = this.newServer(session, monitor, new Authorization() {
                        public boolean isUserInRole(String role) {
                            return inRole(id, role);
                        }
                    });
                    //add to the session so that our WeakHashMap does not ignore the
                    //reference
                    session.setAttribute(id + ":sessionboundserver", new KeepAliveHolder(pservlet));
                    sessionBoundServers.put(id, new WeakReference(pservlet));
                }
            }
        } catch (Exception e) {
            throw new SessionExpiredException(e);
        }
    }

    protected PseudoServlet lookupServer(final HttpSession session) {
        return lookupServer(getId(session));
    }

    protected PseudoServlet lookupServer(final String sessionId) {
        return (PseudoServlet) ((WeakReference) sessionBoundServers.get(getId(sessionId))).get();
    }

    private void sessionShutdown(final String sessionId) {
        PseudoServlet servlet = (PseudoServlet) ((WeakReference) sessionBoundServers.get(sessionId)).get();
        if (null != servlet) {
            servlet.shutdown();
        }
    }

    private void sessionDestroy(final String sessionId) {
        sessionBoundServers.remove(sessionId);
    }

    private void addRequest(String key, HttpServletRequest request) {
        synchronized (activeRequests) {
            if (activeRequests.containsKey(key)) {
                List requests = (List) activeRequests.get(key);
                requests.add(request);
            } else {
                List requests = new ArrayList();
                requests.add(request);
                activeRequests.put(key, requests);
            }
        }
    }

    private void removeRequest(String key, HttpServletRequest request) {
        synchronized (activeRequests) {
            List requests = (List) activeRequests.get(key);
            if (requests != null) {
                requests.remove(request);
                if (requests.isEmpty()) {
                    activeRequests.remove(key);
                }
            }
        }
    }

    private static String getId(final HttpSession session) {
        return getId(session.getId());
    }

    private static String getId(final String sessionID) {
        if (sessionIdDelimiter == null) {
            return sessionID;
        } else {
            int index = sessionID.indexOf(sessionIdDelimiter);
            if (index == -1) {
                return sessionID;
            } else {
                return sessionID.substring(0, index - 1);
            }
        }
    }

    private boolean inRole(String sessionID, String role) {
        Collection sessionRequests = (Collection) activeRequests.get(getId(sessionID));
        if (sessionRequests != null && !sessionRequests.isEmpty()) {
            Iterator i = new ArrayList(sessionRequests).iterator();
            while (i.hasNext()) {
                try {
                    HttpServletRequest request = (HttpServletRequest) i.next();
                    if (request.isUserInRole(role)) {
                        return true;
                    }
                } catch (Throwable t) {
                    //ignore
                }
            }
        }
        return false;
    }

    private void notifySessionShutdown(final HttpSession session) {
        String sessionId = getId(session);
        Log.debug("Shutting down session: " + sessionId);
        // avoid executing this method twice
        if (!SessionMonitors.containsKey(sessionId)) {
            Log.debug("Session: " + sessionId + " already shutdown, skipping");
            return;
        }
        //shutdown session bound server
        try {
            sessionShutdown(sessionId);
        } catch (Exception e) {
            Log.debug(e);
        }
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
        SessionDispatcher sessionDispatcher = lookupSessionDispatcher(context);
        if (sessionDispatcher != null) {
            sessionDispatcher.notifySessionShutdown(session);

            synchronized (SessionMonitors) {
                String sessionId = getId(session);
                try {
                    sessionDispatcher.sessionDestroy(sessionId);
                } catch (Exception e) {
                    Log.debug(e);
                }
                Monitor monitor = (Monitor) SessionMonitors.get(sessionId);
                if (monitor != null) {
                      monitor.removeSessionContext(context);
                      if (monitor.getSessionContextSize() == 0){
                            //ICE-3189 - do this before invalidating the session
                            SessionMonitors.remove(sessionId);
                      }
                }
            }
        }
    }

    //Exposing MainSessionBoundServlet for Tomcat 6 Ajax Push
    public static PseudoServlet getSingletonSessionServer(final HttpSession session, ServletContext context) {
        return lookupSessionDispatcher(context).lookupServer(session);
    }

    public static PseudoServlet getSingletonSessionServer(final String sessionId, Map applicationMap) {
        // the lookupServer(...) method does the getId(sessionId) invocation.
        return lookupSessionDispatcher(applicationMap).lookupServer(sessionId);
    }

    public static PseudoServlet getSingletonSessionServer(final String sessionId, final ServletContext servletContext) {
        // the lookupServer(...) method does the getId(sessionId) invocation.
        return lookupSessionDispatcher(servletContext).lookupServer(sessionId);
    }

    public static class Listener implements ServletContextListener, HttpSessionListener {
        private Thread monitor;
        private boolean run = true;

        public void contextInitialized(ServletContextEvent servletContextEvent) {
            try {
                monitor = new Thread("Session Monitor") {
                    public void run() {
                        int count = 0;
                        while (run) {
                            if (count == 10) { // do this every 10 sec
                                count = 0;
                                Iterator iterator;
                                synchronized (SessionMonitors) {
                                    // Iterate over the session monitors using a copying iterator
                                    iterator = new ArrayList(SessionMonitors.values()).iterator();
                                }

                                while (iterator.hasNext()) {
                                    final Monitor sessionMonitor = (Monitor) iterator.next();
                                    sessionMonitor.shutdownIfExpired();
                                    ThreadLocalUtility.checkThreadLocals(ThreadLocalUtility.EXITING_SESSION_MONITOR);
                                }

                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                //ignore interrupts
                            }
                            count++;
                        }
                    }
                };
                monitor.setDaemon(true);
                monitor.start();
            } catch (Exception e) {
                Log.error("Unable to initialize Session Monitor ", e);
            }
        }

        public void contextDestroyed(ServletContextEvent servletContextEvent) {
            run = false;
            try {
                monitor.join(2000);
            } catch (InterruptedException exception) {
                // Ignore
            }
        }

        public void sessionCreated(HttpSessionEvent event) {
        }

        public void sessionDestroyed(HttpSessionEvent event) {
            HttpSession session = event.getSession();
            ViewStatus.log(session);
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

    public static class Monitor implements Externalizable {
        private final String POSITIVE_SESSION_TIMEOUT = "positive_session_timeout";
        private Set contexts = new HashSet();
        private HttpSession session;
        private long lastAccess;
        private String id;

        public Monitor(String id, HttpSession session) {
            this.id = id;
            this.session = session;
            this.lastAccess = session.getLastAccessedTime();
            session.setAttribute(Monitor.class.getName(), this);
        }

        public static Monitor lookupSessionMonitor(HttpSession session) {
            return (Monitor) session.getAttribute(Monitor.class.getName());
        }

        public void touchSession() {
            lastAccess = System.currentTimeMillis();
        }

        public Date expiresBy() {
            return new Date(lastAccess + (session.getMaxInactiveInterval() * 1000));
        }

        public boolean isExpired() {

            long elapsedInterval = System.currentTimeMillis() - lastAccess;
            try {
                int maxInterval = session.getMaxInactiveInterval();
                // 4496 return true if session is already expired
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

                //a negative time indicates the session should never timeout
                if (maxInterval < 0) {
                    return false;
                } else {
                    return elapsedInterval + 15000 > maxInterval * 1000;
                }
            } catch (Exception e) {
                return true;
            }
        }

        public void shutdown() {
            //notify all the contexts associated to this monitored session
            synchronized (SessionMonitors) {
                Iterator i = new ArrayList(contexts).iterator();
                while (i.hasNext()) {
                    ServletContext context = (ServletContext) i.next();
                    notifySessionShutdown(session, context);
                }
            }
            try {
                session.invalidate();
            } catch (IllegalStateException e) {
                Log.debug("Session already invalidated.");
                synchronized (SessionMonitors) {
                    SessionMonitors.remove(id);
                }
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

        public void writeExternal(ObjectOutput out) throws IOException {
            //ignore
        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            //ignore
        }

        public void removeSessionContext(ServletContext context) {
            contexts.remove(context);
        }

        public int getSessionContextSize() {
            return contexts.size();
        }

        public Monitor() {
            //ignore
        }
    }
}

class KeepAliveHolder implements Serializable {
    transient Object held = null;

    public KeepAliveHolder(Object object) {
        held = object;
    }
}