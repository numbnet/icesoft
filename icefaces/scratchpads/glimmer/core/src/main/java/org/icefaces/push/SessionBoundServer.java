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
import org.icefaces.push.http.MimeTypeMatcher;
import org.icefaces.push.servlet.BasicAdaptingServlet;
import org.icefaces.push.servlet.PathDispatcher;
import org.icefaces.push.servlet.SessionDispatcher;
import org.icepush.PushContext;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.Serializable;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SessionBoundServer extends PathDispatcher implements Serializable {
    private static Logger log = Logger.getLogger(SessionBoundServer.class.getName());
    private static final String ICEFacesBridgeRequestPattern = "\\.icefaces\\.jsf$";
    public static final String SessionExpiryExtension = ":se";
    private transient PushContext pushContext;
    private String groupName;

    public SessionBoundServer(final HttpSession session, final SessionDispatcher.Monitor sessionMonitor, Configuration configuration) {
        this.groupName = session.getId();

        final MimeTypeMatcher mimeTypeMatcher = new MimeTypeMatcher() {
            public String mimeTypeFor(String path) {
                return session.getServletContext().getMimeType(path);
            }
        };

        try {
            this.pushContext = PushContext.getInstance(session.getServletContext());
            WindowScopeManager.onActivatedWindow(session, configuration, new Observer() {
                public void update(Observable observable, Object o) {
                    ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
                    HttpServletRequest request = (HttpServletRequest) context.getRequest();
                    HttpServletResponse response = (HttpServletResponse) context.getResponse();
                    //this call will set the browser ID cookie
                    pushContext.createPushId(request, response);

                    String windowID = (String) o;
                    pushContext.addGroupMember(inferSessionExpiryIdentifier(groupName), inferSessionExpiryIdentifier(windowID));
                }
            });
            WindowScopeManager.onDisactivatedWindow(session, configuration, new Observer() {
                public void update(Observable observable, Object o) {
                    String windowID = (String) o;
                    if (null != pushContext) {
                        pushContext.removeGroupMember(inferSessionExpiryIdentifier(groupName), inferSessionExpiryIdentifier(windowID));
                    }
                }
            });
        } catch (Throwable t) {
            log.log(Level.INFO, "Ajax Push Dispatching not available: " + t);
        }

        dispatchOn(".*dispose\\-window" + ICEFacesBridgeRequestPattern, new BasicAdaptingServlet(new DisposeWindowScope()));
        dispatchOn(".*icefaces\\/resource\\/.*", new BasicAdaptingServlet(new DynamicResourceDispatcher("icefaces/resource/", mimeTypeMatcher, sessionMonitor, session, configuration)));
    }

    public void shutdown() {
        if (pushContext != null) {
            pushContext.push(inferSessionExpiryIdentifier(groupName));
        }
    }

    public static String inferSessionExpiryIdentifier(String windowID) {
        return windowID + SessionExpiryExtension;
    }
}
