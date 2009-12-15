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

package org.icefaces.application;

import org.icefaces.push.SessionRenderer;
import org.icefaces.util.EnvUtils;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpSession;
import java.lang.ref.WeakReference;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

public class PushRenderer {
    public static final String ALL_SESSIONS = "PushRenderer.ALL_SESSIONS";
    private static Hashtable renderGroups = new Hashtable();

    /**
     * Add the current view to the specified group. Groups
     * are automatically garbage collected when all members become
     * unable to receive push updates.
     *
     * @param groupName the name of the group to add the current view to
     */
    public static synchronized void addCurrentView(String groupName) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Remove the current view from the specified group.
     *
     * @param groupName the name of the group to remove the current view from
     */
    public static synchronized void removeCurrentView(String groupName) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Add the current session to the specified group. Groups of sessions
     * are automatically garbage collected when all member sessions have
     * become invalid.
     *
     * @param groupName the name of the group to add the current session to
     */
    public static synchronized void addCurrentSession(String groupName) {
        Set group = (Set) renderGroups.get(groupName);
        if (null == group) {
            group = new CopyOnWriteArraySet();
            renderGroups.put(groupName, group);
        }
        ExternalContext externalContext = FacesContext.getCurrentInstance()
                .getExternalContext();

        group.add(new SessionHolder(externalContext.getSession(false)));
    }

    /**
     * Remove the current view from the specified group.  Use of this method is
     * optional as group membership is maintained automatically as clients leave.
     *
     * @param groupName the name of the group to remove the current view from
     */
    public static synchronized void removeCurrentSession(String groupName) {
    }

    /**
     * Render the specified group of sessions by performing the JavaServer Faces
     * execute and render lifecycle phases.  If a FacesContext is in the
     * scope of the current thread scope, the current view will not be
     * asynchronously rendered
     * (it is already rendered as a result of the user event being
     * processed).  For more fine-grained control
     * use the RenderManager API.
     *
     * @param groupName the name of the group of sessions to render.
     */
    public static void render(String groupName) {
        if (null == renderGroups) {
            return;
        }
        Set group = (Set) renderGroups.get(groupName);
        if (null == group) {
            return;
        }

//        PersistentFacesState suppressedView = PersistentFacesState.getInstance();

        Iterator sessionHolders = group.iterator();
        while (sessionHolders.hasNext()) {
            SessionHolder sessionHolder = (SessionHolder) sessionHolders.next();
            HttpSession session = (HttpSession) sessionHolder.getSession();
            if ((null != session) && (isValid(session))) {
                SessionRenderer sessionRenderer = (SessionRenderer) session.getAttribute(SessionRenderer.class.getName());
                if (null != sessionRenderer) {
                    sessionRenderer.renderViews();
                }
            } else {
                //remove any null or expired sessions
                group.remove(sessionHolder);
                removeGroupIfEmpty(groupName);
            }
        }
    }

    static boolean isValid(HttpSession session) {
        try {
            Object test = session.getAttribute("isTheSessionValid?");
            return true;
        } catch (IllegalStateException e) {
        }
        return false;
    }

    //this method will not be called frequently enough to completely remove
    //all empty groups.  We can either run this during notification of session
    //shutdown or have a ReferenceQueue that we poll() in addCurrentSession()
    //(if addCurrentSession() is never called, the leak is not significant)
    static synchronized void removeGroupIfEmpty(String groupName) {
        Set group = (Set) renderGroups.get(groupName);
        if (null == group) {
            return;
        }
        if (group.isEmpty()) {
            renderGroups.remove(groupName);
        }
    }

}


class SessionHolder {
    WeakReference sessionReference = null;
    String sessionId = null;

    public SessionHolder(Object session) {
        sessionReference = new WeakReference(session);
        if (session instanceof HttpSession) {
            sessionId = ((HttpSession) session).getId();
        }
        if (EnvUtils.instanceofPortletSession(session)) {
            sessionId = ((PortletSession) session).getId();
        }
    }

    public Object getSession() {
        return sessionReference.get();
    }

    public String getId() {
        return sessionId;
    }

    public boolean equals(Object o) {
        if (o instanceof SessionHolder) {
            return (sessionId.equals(((SessionHolder) o).getId()));
        }
        return false;
    }

    public int hashCode() {
        return sessionId.hashCode();
    }
}
