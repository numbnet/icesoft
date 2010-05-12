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

package org.icefaces.push;

import org.icefaces.application.WindowScopeManager;
import org.icefaces.push.http.AbstractServer;
import org.icefaces.push.http.Request;
import org.icefaces.push.http.standard.OKHandler;
import org.icefaces.util.EnvUtils;

public class DisposeWindowScope extends AbstractServer {
    private final WindowScopeManager windowScopeManager;
    private SessionViewManager sessionViewManager;

    public DisposeWindowScope(WindowScopeManager windowScopeManager, SessionViewManager sessionViewManager) {
        this.windowScopeManager = windowScopeManager;
        this.sessionViewManager = sessionViewManager;
    }

    public void service(Request request) throws Exception {
        String windowID = request.getParameter("ice.window");
        windowScopeManager.disposeWindow(windowID);

        if (EnvUtils.isICEpushPresent()) {
            try {
                String[] viewIDs = request.getParameterAsStrings("ice.view");
                for (int i = 0; i < viewIDs.length; i++) {
                    sessionViewManager.removeView(viewIDs[i]);
                }
            } catch (RuntimeException e) {
                //missing ice.view parameters means that none of the views within the page
                //was registered with PushRenderer before page unload
            }
        }

        request.respondWith(OKHandler.HANDLER);
    }
}
