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

package org.icefaces.impl.application;

import org.icefaces.util.EnvUtils;

import javax.faces.context.FacesContext;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class LazyPushManager {

    public static boolean enablePush(FacesContext context, String viewID) {
        //If push is configured to be non-lazy either through the context param or the
        //ice:config attribute, then we enable it.
        if(!EnvUtils.isLazyPush(context)){
            return true;
        }

        State state = getState(context);
        if (state.noOfRegistrations > 0) {
            return true;
        }

        Integer no = (Integer) state.individualyRegisteredViews.get(viewID);
        return no != null && no > 0;
    }

    public static void enablePushForView(FacesContext context, String viewID) {
        State state = getState(context);
        Integer no = (Integer) state.individualyRegisteredViews.get(viewID);
        if (no == null) {
            state.individualyRegisteredViews.put(viewID, 1);
        } else {
            state.individualyRegisteredViews.put(viewID, ++no);
        }
    }

    public static void disablePushForView(FacesContext context, String viewID) {
        State state = getState(context);
        Integer no = (Integer) state.individualyRegisteredViews.get(viewID);
        if (no != null) {
            --no;

            if (no > 0) {
                state.individualyRegisteredViews.put(viewID, no);
            } else {
                state.individualyRegisteredViews.remove(viewID);
            }
        }
    }

    public static void enablePushForSessionViews(FacesContext context) {
        State state = getState(context);
        state.noOfRegistrations++;
    }

    public static void disablePushForSessionViews(FacesContext context) {
        State state = getState(context);
        state.noOfRegistrations--;
    }

    private static State getState(FacesContext context) {
        Map sessionMap = context.getExternalContext().getSessionMap();
        State state = (State) sessionMap.get(LazyPushManager.class.getName());
        if (state == null) {
            state = new State();
            sessionMap.put(LazyPushManager.class.getName(), state);
        }

        return state;
    }

    //it is okay to serialize *static* inner classes: http://java.sun.com/javase/6/docs/platform/serialization/spec/serial-arch.html#7182
    private static class State implements Serializable {
        private int noOfRegistrations = 0;
        private HashMap individualyRegisteredViews = new HashMap();
    }
}
