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

import org.icefaces.event.BridgeSetup;
import org.icefaces.push.SessionViewManager;
import org.icepush.PushContext;

import javax.faces.context.FacesContext;
import java.util.Map;
import java.util.logging.Logger;

public class PushRenderer {
    private static Logger log = Logger.getLogger(PushRenderer.class.getName());
    public static final String ALL_SESSIONS = "PushRenderer.ALL_SESSIONS";

    /**
     * Add the current view to the specified group. Groups
     * are automatically garbage collected when all members become
     * unable to receive push updates.
     *
     * @param groupName the name of the group to add the current view to
     */
    public static synchronized void addCurrentView(String groupName) {
        FacesContext context = FacesContext.getCurrentInstance();
        missingFacesContext(context);
        String viewID = lookupViewState(context);
        LazyPushManager.lookup(context).enablePushForView(viewID);
        PushContext pushContext = (PushContext) context.getExternalContext().getApplicationMap().get(PushContext.class.getName());
        pushContext.addGroupMember(groupName, viewID);
    }

    /**
     * Remove the current view from the specified group.
     *
     * @param groupName the name of the group to remove the current view from
     */
    public static synchronized void removeCurrentView(String groupName) {
        FacesContext context = FacesContext.getCurrentInstance();
        missingFacesContext(context);
        String viewID = lookupViewState(context);
        LazyPushManager.lookup(context).disablePushForView(viewID);
        PushContext pushContext = (PushContext) context.getExternalContext().getApplicationMap().get(PushContext.class.getName());
        pushContext.removeGroupMember(groupName, viewID);
    }

    /**
     * Add the current session to the specified group. Groups of sessions
     * are automatically garbage collected when all member sessions have
     * become invalid.
     *
     * @param groupName the name of the group to add the current session to
     */
    public static synchronized void addCurrentSession(final String groupName) {
        FacesContext context = FacesContext.getCurrentInstance();
        missingFacesContext(context);
        Map sessionMap = context.getExternalContext().getSessionMap();
        LazyPushManager.lookup(context).enablePushForSessionViews();
        SessionViewManager sessionViewManager = (SessionViewManager) sessionMap.get(SessionViewManager.class.getName());
        sessionViewManager.addCurrentViewsToGroup(groupName);
    }

    /**
     * Remove the current view from the specified group.  Use of this method is
     * optional as group membership is maintained automatically as clients leave.
     *
     * @param groupName the name of the group to remove the current view from
     */
    public static synchronized void removeCurrentSession(final String groupName) {
        FacesContext context = FacesContext.getCurrentInstance();
        missingFacesContext(context);
        LazyPushManager.lookup(context).disablePushForSessionViews();
        Map sessionMap = context.getExternalContext().getSessionMap();
        SessionViewManager sessionViewManager = (SessionViewManager) sessionMap.get(SessionViewManager.class.getName());
        sessionViewManager.removeCurrentViewsFromGroup(groupName);
    }

    /**
     * Render the specified group of sessions by performing the JavaServer Faces
     * execute and render lifecycle phases.
     * For more fine-grained control use the RenderManager API.
     *
     * @param groupName the name of the group of sessions to render.
     */
    public static void render(String groupName) {
        FacesContext context = FacesContext.getCurrentInstance();
        missingFacesContext(context);
        PushContext pushContext = (PushContext) context.getExternalContext().getApplicationMap().get(PushContext.class.getName());
        pushContext.push(groupName);
    }

    /**
     * Create a PortableRenderer instance. PortableRenderer can trigger renderings in the context of the application.
     * Once acquired it does not need a current FacesContext in order to function.
     *
     * @param context the FacesContext instance
     * @return application wide PortableRenderer instance
     */
    public static PortableRenderer getPortableRenderer(FacesContext context) {
        final PushContext pushContext = (PushContext) context.getExternalContext().getApplicationMap().get(PushContext.class.getName());
        return new PortableRenderer() {
            public void render(String group) {
                if (FacesContext.getCurrentInstance() == null) {
                    pushContext.push(group);
                }
            }
        };
    }

    private static String lookupViewState(FacesContext context) {
        Map requestMap = context.getExternalContext().getRequestMap();
        return (String) requestMap.get(BridgeSetup.ViewState);
    }

    private static void missingFacesContext(FacesContext context) {
        if (context == null) {
            throw new RuntimeException("FacesContext is not present for thread " + Thread.currentThread());
        }
    }
}