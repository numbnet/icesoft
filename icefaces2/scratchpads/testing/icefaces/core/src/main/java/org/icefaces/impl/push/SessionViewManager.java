/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.impl.push;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpSession;

import org.icepush.PushContext;

public class SessionViewManager {
    public static String addView(FacesContext context, String id) {
        PushContext pushContext = getPushContext(context);
        State state = getState(context);
        if (!state.viewIDs.contains(id)) {
            state.viewIDs.add(id);
            pushContext.addGroupMember(state.groupName, id);
            Iterator i = state.groups.iterator();
            while (i.hasNext()) {
                pushContext.addGroupMember((String) i.next(), id);
            }
        }
        return id;
    }

    public static void removeView(FacesContext context, String id) {
        PushContext pushContext = getPushContext(context);
        State state = getState(context);
        if (state.viewIDs.contains(id)) {
            state.viewIDs.remove(id);
            pushContext.removeGroupMember(state.groupName, id);
            Iterator i = state.groups.iterator();
            while (i.hasNext()) {
                pushContext.removeGroupMember((String) i.next(), id);
            }
        }
    }

    public static void addCurrentViewsToGroup(FacesContext context, String groupName) {
        PushContext pushContext = getPushContext(context);
        State state = getState(context);
        state.groups.add(groupName);
        Iterator i = state.viewIDs.iterator();
        while (i.hasNext()) {
            pushContext.addGroupMember(groupName, (String) i.next());
        }
    }

    public static void removeCurrentViewsFromGroup(FacesContext context, String groupName) {
        PushContext pushContext = getPushContext(context);
        State state = getState(context);
        state.groups.remove(groupName);
        Iterator i = state.viewIDs.iterator();
        while (i.hasNext()) {
            pushContext.removeGroupMember(groupName, (String) i.next());
        }
    }

    private static PushContext getPushContext(FacesContext facesContext) {
        return (PushContext) facesContext.getExternalContext().getApplicationMap().get(PushContext.class.getName());
    }

    private static SessionViewManager.State getState(FacesContext context) {
        Map sessionMap = context.getExternalContext().getSessionMap();
        State state = (State) sessionMap.get(SessionViewManager.class.getName());
        if (state == null) {
            Object session = context.getExternalContext().getSession(true);
            if (session instanceof HttpSession) {
                state = new State(((HttpSession) session).getId());
            } else if (session instanceof PortletSession) {
                state = new State(((PortletSession) session).getId());
            } else {
                throw new RuntimeException("Unknown session object: " + session);
            }
            sessionMap.put(SessionViewManager.class.getName(), state);
        }

        return state;
    }

    //it is okay to serialize *static* inner classes: http://java.sun.com/javase/6/docs/platform/serialization/spec/serial-arch.html#7182 
    private static class State implements Serializable {
        private String groupName;
        private HashSet viewIDs = new HashSet();
        private HashSet groups = new HashSet();

        private State(String groupName) {
            this.groupName = groupName;
        }
    }
}
