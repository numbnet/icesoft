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

package com.icesoft.faces.context.effects;

import com.icesoft.util.CoreComponentUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.StringTokenizer;

/**
 * Make HTML Elements draggable or droppable
 * Makes the element drop (Move Down)  and fade out at the same time.
 */
public class DragDrop {


    /**
     * Make an HTML element draggable
     *
     * @param id
     * @param handleId
     * @param options
     * @param mask
     * @param facesContext
     * @return
     */
    public static String addDragable(String id, String handleId, String options,
                                     String mask,
                                     FacesContext facesContext) {
        boolean revert = false;
        boolean ghosting = false;
        boolean solid = false;
        boolean dragGhost = false;
        boolean pointerDraw = false;
        if (options != null) {
            StringTokenizer st = new StringTokenizer(options, ",");
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                token = token.trim();
                if ("revert".equalsIgnoreCase(token)) {
                    revert = true;
                } else if ("ghosting".equalsIgnoreCase(token)) {
                    ghosting = true;
                }
                if ("solid".equalsIgnoreCase(token)) {
                    solid = true;
                }
                if ("dragGhost".equalsIgnoreCase(token)) {
                    dragGhost = true;
                }
                if ("pointerDraw".equalsIgnoreCase(token)) {
                    pointerDraw = true;
                }
            }
        }
        return addDragable(id, handleId, revert, ghosting, solid, dragGhost,
                pointerDraw, mask, facesContext);
    }

    /**
     * make an HTML element draggable
     *
     * @param id
     * @param handleId
     * @param revert
     * @param ghosting
     * @param solid
     * @param dragGhost
     * @param pointerDraw
     * @param mask
     * @param facesContext
     * @return
     */
    public static String addDragable(String id, String handleId, boolean revert,
                                     boolean ghosting, boolean solid,
                                     boolean dragGhost, boolean pointerDraw,
                                     String mask, FacesContext facesContext) {

        EffectsArguments ea = new EffectsArguments();
        ea.add("handle", handleId);
        ea.add("revert", revert);
        ea.add("ghosting", ghosting);
        ea.add("mask", mask);
        ea.add("dragGhost", dragGhost);
        ea.add("dragCursor", pointerDraw);
        if (solid) {
            //Setting start and end effect functions to blank to remove transparency while dragging.
            ea.addFunction("starteffect", "function(){}");
            ea.addFunction("endeffect", "function(){}");
        }
        String call = "new Ice.Scriptaculous.Draggable('" + id + "'" + ea.toString();
        return call;
    }

    /**
     * Make an HTML element droppable
     *
     * @param uiComponent
     * @param acceptClass
     * @param facesContext
     * @param mask
     * @param hoverClass
     * @return
     */
    public static String addDroptarget(UIComponent uiComponent, String acceptClass,
                                       FacesContext facesContext, String mask,
                                       String hoverClass) {
        String id = uiComponent.getClientId(facesContext);
        String scrollid = (String) uiComponent.getAttributes().get("dropTargetScrollerId");
        if (scrollid != null && scrollid.trim().length() > 0) {
            UIComponent scroller = CoreComponentUtils.findComponentInView(facesContext.getViewRoot(), scrollid);
            if (scroller != null) {
                scrollid = scroller.getClientId(facesContext);
            }
        }
        EffectsArguments ea = new EffectsArguments();
        ea.add("accept", acceptClass);
        ea.add("mask", mask);
        ea.add("hoverclass", hoverClass);
        if (scrollid != null && scrollid.trim().length() > 0) {
            ea.add("scrollid", scrollid);
        }
        String call = "Ice.Scriptaculous.Droppables.add('" + id + "'" + ea.toString();
        JavascriptContext.addJavascriptCall(facesContext, call);
        return call;
    }
}
