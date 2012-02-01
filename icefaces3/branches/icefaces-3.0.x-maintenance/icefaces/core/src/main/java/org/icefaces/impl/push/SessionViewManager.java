/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.impl.push;

import org.icepush.PushContext;

import javax.faces.context.FacesContext;
import javax.portlet.PortletSession;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;

public class SessionViewManager {

    public static void addView(FacesContext context, String id) {
        PushContext pushContext = getPushContext(context);
        State state = getState(context);
        pushContext.addGroupMember(state.groupName, id);
        Iterator i = state.groups.iterator();
        while (i.hasNext()) {
            pushContext.addGroupMember((String) i.next(), id);
        }
    }

    public static void removeView(FacesContext context, String id) {
        PushContext pushContext = getPushContext(context);
        State state = getState(context);
        pushContext.removeGroupMember(state.groupName, id);
        Iterator i = state.groups.iterator();
        while (i.hasNext()) {
            pushContext.removeGroupMember((String) i.next(), id);
        }
    }

    public static void startAddingNewViewsToGroup(FacesContext context, String groupName) {
        State state = getState(context);
        state.groups.add(groupName);
    }

    public static void stopAddingNewViewsToGroup(FacesContext context, String groupName) {
        State state = getState(context);
        state.groups.remove(groupName);
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
        private HashSet groups = new HashSet();

        private State(String groupName) {
            this.groupName = groupName;
        }
    }
}
