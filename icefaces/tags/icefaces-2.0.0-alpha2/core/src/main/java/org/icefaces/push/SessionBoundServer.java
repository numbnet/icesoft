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

package org.icefaces.push;

import org.icefaces.application.WindowScopeManager;
import org.icefaces.push.http.MimeTypeMatcher;
import org.icefaces.push.servlet.BasicAdaptingServlet;
import org.icefaces.push.servlet.PathDispatcher;
import org.icefaces.push.servlet.SessionDispatcher;
import org.icepush.PushContext;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;
import java.util.Observable;
import java.util.Observer;

public class SessionBoundServer extends PathDispatcher {
    private static final String ICEFacesBridgeRequestPattern = "\\.icefaces\\.jsf$";

    public SessionBoundServer(final PushContext pushContext, final HttpSession session, final SessionDispatcher.Monitor sessionMonitor, Configuration configuration) {
        final MimeTypeMatcher mimeTypeMatcher = new MimeTypeMatcher() {
            public String mimeTypeFor(String path) {
                return session.getServletContext().getMimeType(path);
            }
        };
        final WindowScopeManager windowScopeManager = WindowScopeManager.lookup(session, configuration);
        final String groupName = session.getId();
        windowScopeManager.onActivatedWindow(new Observer() {
            public void update(Observable observable, Object o) {
                ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                HttpServletRequest request = (HttpServletRequest) context.getRequest();
                HttpServletResponse response = (HttpServletResponse) context.getResponse();
                //this call will set the browser ID cookie 
                pushContext.createPushId(request, response);

                pushContext.addGroupMember(groupName, (String) o);
            }
        });
        windowScopeManager.onDisactivatedWindow(new Observer() {
            public void update(Observable observable, Object o) {
                pushContext.removeGroupMember(groupName, (String) o);
            }
        });
        final SessionRenderer sessionRenderer = new SessionRenderer() {
            public void renderViews() {
                pushContext.push(groupName);
            }
        };
        session.setAttribute(SessionRenderer.class.getName(), sessionRenderer);

        dispatchOn(".*dispose\\-window" + ICEFacesBridgeRequestPattern, new BasicAdaptingServlet(new DisposeWindowScope(windowScopeManager)));
        dispatchOn(".*icefaces\\/resource\\/.*", new BasicAdaptingServlet(new DynamicResourceDispatcher("icefaces/resource/", mimeTypeMatcher, sessionMonitor, session, configuration)));
    }
}
