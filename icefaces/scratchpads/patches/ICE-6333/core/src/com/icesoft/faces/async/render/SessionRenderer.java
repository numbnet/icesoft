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

package com.icesoft.faces.async.render;

/**
 * <p>
 *   The <code>SessionRenderer</code> class allows an application to initiate
 *   rendering asynchronously and independently of user interaction for a
 *   session or a group of sessions.  When a session is rendered, all windows or
 *   views that a particular user has open in their session will be updated via
 *   Ajax Push with the current application state.
 * </p>
 */
public class SessionRenderer {
    public static final String ALL_SESSIONS = "SessionRenderer.ALL_SESSIONS";

    /**
     * <p>
     *   Adds the current session to the group of sessions with the specified
     *   <code>groupName</code>.  The group is automatically garbage collected
     *   when all its member sessions have become invalid.
     * </p>
     * <p>
     *   For more fine-grained control use the {@link RenderManager} API.
     * </p>
     *
     * @param      groupName
     *                 the name of the group of sessions to add the current
     *                 session to.
     * @throws     IllegalStateException
     *                 if no current session is active.
     * @see        #removeCurrentSession(String)
     */
    public static void addCurrentSession(final String groupName)
    throws IllegalStateException {
        // Creates an OnDemandRenderer instance if not already done so.
        RenderManager.getInstance().getOnDemandRenderer(groupName).
            addCurrentSession();
    }

    /**
     * <p>
     *   Removes the current session from the group of sessions with the
     *   specified <code>groupName</code>.  The group is automatically garbage
     *   collected when all its member sessions have been removed.
     * </p>
     * <p>
     *   For more fine-grained control use the {@link RenderManager} API.
     * </p>
     *
     * @param      groupName
     *                 the name of the group of sessions to remove the current
     *                 session from.
     * @see        #addCurrentSession(String)
     */
    public static void removeCurrentSession(final String groupName) {
        // Does not create an OnDemandRenderer instance.
        OnDemandRenderer renderer = getRenderer(groupName);
        if (renderer != null) {
            renderer.removeCurrentSession();
            removeRendererIfEmpty(renderer);
        }
    }

    /**
     * <p>
     *   Renders the group of sessions with the specified <code>groupName</code>
     *   by performing the JavaServer Faces execute and render life cycle
     *   phases.  If a <code>FacesContext</code> is in the scope of the current
     *   thread scope, the current view will not be asynchronously rendered as
     *   it is already rendered as a result of the user event being processed.
     * </p>
     * <p>
     *   For more fine-grained control use the {@link RenderManager} API.
     * </p>
     *
     * @param      groupName
     *                 the name of the group of sessions to render.
     */
    public static void render(final String groupName) {
        // Does not create an OnDemandRenderer instance.
        OnDemandRenderer renderer = getRenderer(groupName);
        if (renderer != null) {
            renderer.requestRender();
            removeRendererIfEmpty(renderer);
        }
    }

    private static OnDemandRenderer getRenderer(final String groupName) {
        return
            (OnDemandRenderer)
                RenderManager.getInstance().getRenderer(groupName);
    }

    private static void removeRendererIfEmpty(final OnDemandRenderer renderer) {
        if (renderer.isEmpty()) {
            RenderManager.getInstance().removeRenderer(renderer);
        }
    }
}
