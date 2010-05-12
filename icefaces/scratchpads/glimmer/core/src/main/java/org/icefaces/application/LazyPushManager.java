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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package org.icefaces.application;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

public class LazyPushManager {
    public LazyPushManager(HttpSession session) {
        session.setAttribute(LazyPushManager.class.getName(), this);
    }

    private int noOfRegistrations = 0;
    private HashMap individualyRegisteredViews = new HashMap();

    public boolean enablePush(String viewID) {
        if (noOfRegistrations > 0) {
            return true;
        }

        Integer no = (Integer) individualyRegisteredViews.get(viewID);
        return no != null && no > 0;
    }

    public void enablePushForView(String viewID) {
        Integer no = (Integer) individualyRegisteredViews.get(viewID);
        if (no == null) {
            individualyRegisteredViews.put(viewID, 1);
        } else {
            individualyRegisteredViews.put(viewID, ++no);
        }
    }

    public void disablePushForView(String viewID) {
        Integer no = (Integer) individualyRegisteredViews.get(viewID);
        if (no != null) {
            --no;

            if (no > 0) {
                individualyRegisteredViews.put(viewID, no);
            } else {
                individualyRegisteredViews.remove(viewID);
            }
        }
    }

    public void enablePushForSessionViews() {
        noOfRegistrations++;
    }

    public void disablePushForSessionViews() {
        noOfRegistrations--;
    }

    public static LazyPushManager lookup(FacesContext context) {
        Map sessionMap = context.getExternalContext().getSessionMap();
        return (LazyPushManager) sessionMap.get(LazyPushManager.class.getName());
    }
}
