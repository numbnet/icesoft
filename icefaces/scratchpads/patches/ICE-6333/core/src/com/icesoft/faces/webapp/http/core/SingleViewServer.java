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

package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.context.View;
import com.icesoft.faces.env.Authorization;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;

import javax.servlet.http.HttpSession;
import java.util.Map;

public class SingleViewServer implements Server {
    private static final String viewNumber = "1";
    private Map views;
    private String sessionID;
    private ViewQueue allUpdatedViews;
    private Configuration configuration;
    private SessionDispatcher.Monitor sessionMonitor;
    private HttpSession session;
    private ResourceDispatcher resourceDispatcher;
    private String blockingRequestHandlerContext;
    private Authorization authorization;

    public SingleViewServer(final HttpSession session, final String sessionID, final SessionDispatcher.Monitor sessionMonitor, final Map views, final ViewQueue allUpdatedViews, final Configuration configuration, final ResourceDispatcher resourceDispatcher, final String blockingRequestHandlerContext, final Authorization authorization) {
        this.session = session;
        this.sessionID = sessionID;
        this.sessionMonitor = sessionMonitor;
        this.views = views;
        this.allUpdatedViews = allUpdatedViews;
        this.configuration = configuration;
        this.resourceDispatcher = resourceDispatcher;
        this.blockingRequestHandlerContext = blockingRequestHandlerContext;
        this.authorization = authorization;
    }

    //synchronize to avoid concurrent state modifications of the single View
    public void service(Request request) throws Exception {
        //create single view or re-create view if the request is the result of a redirect
        final View view;
        synchronized (views) {
            if (views.containsKey(viewNumber)) {
                view = (View) views.get(viewNumber);
            } else {
                view = new View(viewNumber, sessionID, session, allUpdatedViews, configuration, sessionMonitor, resourceDispatcher, blockingRequestHandlerContext, authorization);
                views.put(viewNumber, view);
            }
        }

        try {
            sessionMonitor.touchSession();
            view.servePage(request);
        } finally {
            view.release();
        }
    }

    public void shutdown() {
    }
}
