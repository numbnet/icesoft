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

package org.icepush.servlet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class SessionDispatcher implements PseudoServlet {
    private static Logger log = Logger.getLogger(SessionDispatcher.class.getName());
    private final Map sessionBoundServers = new HashMap();

    public SessionDispatcher(ServletContext context) {
        //avoid instance collision -- Glassfish shares EAR module classloaders
        associateSessionDispatcher(context);
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession(true);
        checkSession(session);
        lookupServer(session).service(request, response);
    }

    public void shutdown() {
        Iterator i = sessionBoundServers.values().iterator();
        while (i.hasNext()) {
            PseudoServlet servlet = (PseudoServlet) i.next();
            servlet.shutdown();
        }
    }

    protected abstract PseudoServlet newServer(HttpSession session) throws Exception;

    protected void checkSession(HttpSession session) throws Exception {
        final String id = session.getId();
        synchronized (sessionBoundServers) {
            if (!sessionBoundServers.containsKey(id)) {
                sessionBoundServers.put(id, this.newServer(session));
            }
        }
    }

    protected PseudoServlet lookupServer(final HttpSession session) {
        return (PseudoServlet) sessionBoundServers.get(session.getId());
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
        if (log.isLoggable(Level.FINE)) {
            log.fine("Shutting down session: " + session.getId());
        }
        SessionDispatcher sessionDispatcher = lookupSessionDispatcher(context);
        //shutdown session bound server
        try {
            sessionDispatcher.sessionShutdown(session);
        } catch (Exception e) {
            log.log(Level.SEVERE, "Unable to shutdown session", e);
        }

        synchronized (session) {
            try {
                sessionDispatcher.sessionDestroy(session);
            } catch (Exception e) {
                log.log(Level.SEVERE, "Unable to destory sessionDispatcher ", e);
            }
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
}
