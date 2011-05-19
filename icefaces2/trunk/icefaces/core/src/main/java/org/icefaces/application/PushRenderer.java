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

package org.icefaces.application;

import org.icefaces.impl.application.LazyPushManager;
import org.icefaces.impl.event.BridgeSetup;
import org.icefaces.impl.push.SessionViewManager;
import org.icefaces.util.EnvUtils;
import org.icepush.PushContext;
import org.icepush.PushMessage;

import javax.faces.context.FacesContext;
import java.util.Map;
import java.util.logging.Logger;

/**
 * <p>
 * The <code>PushRenderer</code>  allows an application to initiate
 * rendering asynchronously and independently of user interaction for a
 * group of sessions or views.  When a session is rendered, all windows or
 * views that a particular user has open in their session will be updated via
 * Ajax Push with the current application state.
 * </p>
 */
public class PushRenderer {
    private static Logger log = Logger.getLogger(PushRenderer.class.getName());
    public static final String ALL_SESSIONS = "PushRenderer.ALL_SESSIONS";
    private static final String MissingICEpushMessage = "ICEpush library missing. Push notification disabled.";

    /**
     * Add the current view to the specified group. Groups
     * are automatically garbage collected when all members become
     * unable to receive push updates.
     *
     * @param groupName the name of the group to add the current view to
     */
    public static synchronized void addCurrentView(String groupName) {
        if (EnvUtils.isICEpushPresent()) {
            FacesContext context = FacesContext.getCurrentInstance();
            missingFacesContext(context);
            String viewID = lookupViewState(context);
            LazyPushManager.enablePushForView(context, viewID);
            PushContext pushContext = (PushContext) context.getExternalContext().getApplicationMap().get(PushContext.class.getName());
            pushContext.addGroupMember(groupName, viewID);
        } else {
            log.warning(MissingICEpushMessage);
        }
    }

    /**
     * Remove the current view from the specified group.
     *
     * @param groupName the name of the group to remove the current view from
     */
    public static synchronized void removeCurrentView(String groupName) {
        if (EnvUtils.isICEpushPresent()) {
            FacesContext context = FacesContext.getCurrentInstance();
            missingFacesContext(context);
            String viewID = lookupViewState(context);
            LazyPushManager.disablePushForView(context, viewID);
            PushContext pushContext = (PushContext) context.getExternalContext().getApplicationMap().get(PushContext.class.getName());
            pushContext.removeGroupMember(groupName, viewID);
        } else {
            log.warning(MissingICEpushMessage);
        }
    }

    /**
     * All subsequently created views in the current session will be added to the specified group.
     * Groups of sessions are automatically garbage collected when all member sessions have
     * become invalid.
     *
     * @param groupName the name of the group to add the current session to
     */
    public static synchronized void addCurrentSession(final String groupName) {
        if (EnvUtils.isICEpushPresent()) {
            FacesContext context = FacesContext.getCurrentInstance();
            missingFacesContext(context);
            LazyPushManager.enablePushForSessionViews(context);
            SessionViewManager.startAddingNewViewsToGroup(context, groupName);
        } else {
            log.warning(MissingICEpushMessage);
        }
    }

    /**
     * Remove the current views from the specified group.  Use of this method is
     * optional as group membership is maintained automatically as clients leave.
     *
     * @param groupName the name of the group to remove the current view from
     */
    public static synchronized void removeCurrentSession(final String groupName) {
        if (EnvUtils.isICEpushPresent()) {
            FacesContext context = FacesContext.getCurrentInstance();
            missingFacesContext(context);
            LazyPushManager.disablePushForSessionViews(context);
            SessionViewManager.stopAddingNewViewsToGroup(context, groupName);
        } else {
            log.warning(MissingICEpushMessage);
        }
    }

    /**
     * Render the specified group of sessions by performing the JavaServer Faces
     * execute and render lifecycle phases.
     *
     * @param groupName the name of the group of sessions to render.
     */
    public static void render(String groupName) {
        if (EnvUtils.isICEpushPresent()) {
            FacesContext context = FacesContext.getCurrentInstance();
            missingFacesContext(context);
            PushContext pushContext = (PushContext) context.getExternalContext().getApplicationMap().get(PushContext.class.getName());
            pushContext.push(groupName);
        } else {
            log.warning(MissingICEpushMessage);
        }
    }

    /**
     * Render message to the specified group of sessions but only to the clients
     * that have their blocking connection paused.
     *
     * @param groupName the name of the group of sessions to render.
     * @param message   the message to be sent
     */
    public static void render(String groupName, PushMessage message) {
        if (EnvUtils.isICEpushPresent()) {
            FacesContext context = FacesContext.getCurrentInstance();
            missingFacesContext(context);
            PushContext pushContext = (PushContext) context.getExternalContext().getApplicationMap().get(PushContext.class.getName());
            pushContext.push(groupName, message);
        } else {
            log.warning(MissingICEpushMessage);
        }
    }

    /**
     * Create a PortableRenderer instance. PortableRenderer can trigger renderings in the context of the application.
     * Once acquired it does not need a current FacesContext in order to function.
     *
     * @return application wide PortableRenderer instance
     */
    public static PortableRenderer getPortableRenderer() {
        return getPortableRenderer(FacesContext.getCurrentInstance());
    }

    /**
     * Create a PortableRenderer instance. PortableRenderer can trigger renderings
     * in the context of the application.
     * Once acquired it does not need a current FacesContext in order to function.
     *
     * @param context the FacesContext instance
     * @return application wide PortableRenderer instance
     */
    public static PortableRenderer getPortableRenderer(FacesContext context) {
        if (EnvUtils.isICEpushPresent()) {
            final Map<String, Object> applicationMap = context.getExternalContext().getApplicationMap();
            return new PortableRenderer() {
                public void render(String group) {
                    //delay PushContext lookup until is needed
                    PushContext pushContext = (PushContext) applicationMap.get(PushContext.class.getName());
                    if (pushContext == null) {
                        log.fine("PushContext not initialized yet.");
                    } else {
                        pushContext.push(group);
                    }
                }

                public void render(String group, PushRendererMessage message) {
                    //delay PushContext lookup until is needed
                    PushContext pushContext = (PushContext) applicationMap.get(PushContext.class.getName());
                    if (pushContext == null) {
                        log.fine("PushContext not initialized yet.");
                    } else {

                        pushContext.push(group, message.toPushMessage());
                    }
                }
            };
        } else {
            log.warning(MissingICEpushMessage);

            return new PortableRenderer() {
                public void render(String group) {
                    log.warning(MissingICEpushMessage);
                }

                public void render(String group, PushRendererMessage message) {
                    log.warning(MissingICEpushMessage);
                }
            };
        }
    }

    private static String lookupViewState(FacesContext context) {
        return BridgeSetup.getViewID(context.getExternalContext());
    }

    private static void missingFacesContext(FacesContext context) {
        if (context == null) {
            throw new RuntimeException("FacesContext is not present for thread " + Thread.currentThread());
        }
    }
}