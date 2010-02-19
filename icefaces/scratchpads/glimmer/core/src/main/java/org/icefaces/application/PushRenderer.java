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

import org.icefaces.push.ViewNotificationManager;
import org.icepush.PushContext;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.util.logging.Logger;

public class PushRenderer {
    private static Logger log = Logger.getLogger(PushRenderer.class.getName());

    public static final String ALL_SESSIONS = "PushRenderer.ALL_SESSIONS";
    public static PushContext pushContext;

    /**
     * Add the current view to the specified group. Groups
     * are automatically garbage collected when all members become
     * unable to receive push updates.
     *
     * @param groupName the name of the group to add the current view to
     */
    public static synchronized void addCurrentView(String groupName) {
        FacesContext context = FacesContext.getCurrentInstance();
        String viewID = (String) context.getViewRoot().getAttributes().get(ViewNotificationManager.class.getName());
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
        String viewID = (String) context.getViewRoot().getAttributes().get(ViewNotificationManager.class.getName());
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
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        ViewNotificationManager viewNotificationManager = (ViewNotificationManager) externalContext.getSessionMap().get(ViewNotificationManager.class.getName());
        viewNotificationManager.addCurrentViewsToGroup(groupName);
    }

    /**
     * Remove the current view from the specified group.  Use of this method is
     * optional as group membership is maintained automatically as clients leave.
     *
     * @param groupName the name of the group to remove the current view from
     */
    public static synchronized void removeCurrentSession(final String groupName) {
        ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
        ViewNotificationManager viewNotificationManager = (ViewNotificationManager) externalContext.getSessionMap().get(ViewNotificationManager.class.getName());
        viewNotificationManager.removeCurrentViewsFromGroup(groupName);
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
        FacesContext context = FacesContext.getCurrentInstance();
        if (context != null) {
            PushContext pushContext = (PushContext) context.getExternalContext().getApplicationMap().get(PushContext.class.getName());
            pushContext.push(groupName);
        }
    }

    public static PortableRenderer getPortableRenderer(FacesContext context) {
        final PushContext pushContext = (PushContext) context.getExternalContext().getApplicationMap().get(PushContext.class.getName());
        return new PortableRenderer() {
            public void render(String group) {
                pushContext.push(group);
            }
        };
    }
}