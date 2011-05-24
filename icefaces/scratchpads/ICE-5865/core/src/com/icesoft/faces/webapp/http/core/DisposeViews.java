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
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.common.standard.OKResponse;
import com.icesoft.faces.webapp.http.portlet.page.AssociatedPageViews;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

public class DisposeViews implements Server {
    private static final Log Log = LogFactory.getLog(DisposeViews.class);
    private String sessionID;
    private Map views;
    private AssociatedPageViews associatedPageViews;

    public DisposeViews(String sessionID, Map views, AssociatedPageViews associatedPageViews) {
        this.sessionID = sessionID;
        this.views = views;
        this.associatedPageViews = associatedPageViews;
    }

    public void service(Request request) throws Exception {
        if (request.containsParameter(sessionID)) {
            String[] viewIdentifiers = request.getParameterAsStrings(sessionID);
            for (int i = 0; i < viewIdentifiers.length; i++) {
                View view = (View) views.remove(viewIdentifiers[i]);
                // Jira 1616 Logout throws NPE.
                if (view != null) {
                    if (Log.isDebugEnabled())  {
                        Log.debug("Disposing View: " + view);
                    }
                    associatedPageViews.disposeAssociatedViews(views,view);
                    view.dispose();
                }
            }
            if (Log.isDebugEnabled())  {
                Log.debug("Views disposed for " + sessionID + ". Remaining views: " + views);
            }
        } else {
            //this usually happens with Seam filters in synchronous mode
            Log.warn("Request belonging to a different session. Most probably servlet filters mangled the request.");
        }

        request.respondWith(OKResponse.Handler);
    }

    public void shutdown() {
    }
}