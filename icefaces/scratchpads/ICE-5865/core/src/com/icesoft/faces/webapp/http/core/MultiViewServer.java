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
import com.icesoft.faces.webapp.http.portlet.page.AssociatedPageViews;

import javax.servlet.http.HttpSession;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MultiViewServer implements Server {
    private static final Log LOG = LogFactory.getLog(MultiViewServer.class);
    private int viewCount = 0;
    private int viewCap = 0;
    private Map views;
    private ViewQueue asynchronouslyUpdatedViews;
    private String sessionID;
    private Configuration configuration;
    private SessionDispatcher.Monitor sessionMonitor;
    private HttpSession session;
    private ResourceDispatcher resourceDispatcher;
    private String blockingRequestHandlerContext;
    private Authorization authorization;
    private AssociatedPageViews associatedPageViews;

    public MultiViewServer(final HttpSession session,
                           final String sessionID,
                           final SessionDispatcher.Monitor sessionMonitor,
                           final Map views,
                           final ViewQueue asynchronouslyUpdatedViews,
                           final Configuration configuration,
                           final ResourceDispatcher resourceDispatcher,
                           final String blockingRequestHandlerContext,
                           final Authorization authorization,
                           final AssociatedPageViews associatedPageViews) {
        this.session = session;
        this.sessionID = sessionID;
        this.sessionMonitor = sessionMonitor;
        this.views = views;
        this.asynchronouslyUpdatedViews = asynchronouslyUpdatedViews;
        this.configuration = configuration;
        this.resourceDispatcher = resourceDispatcher;
        this.blockingRequestHandlerContext = blockingRequestHandlerContext;
        this.authorization = authorization;
        this.associatedPageViews = associatedPageViews;
        this.viewCap = configuration.getAttributeAsInteger("concurrentViewLimit", 50);
    }

    public void service(Request request) throws Exception {
        //extract viewNumber if this request is from a redirect
        final View view;
        synchronized (views) {
            if (request.containsParameter("rvn")) {
                final String viewIdentifier = request.getParameter("rvn");
                if (isInteger(viewIdentifier) && views.containsKey(viewIdentifier)) {
                    view = (View) views.get(viewIdentifier);
                } else {
                    view = createView();
                    if (LOG.isDebugEnabled())  {
                        LOG.debug("View created: " + view + " " + request.getURI());
                    }
                }
            } else {
                view = createView();
                if (LOG.isDebugEnabled())  {
                    LOG.debug("View created: " + view + " " + request.getURI());
                }
            }
        }

        try {
            sessionMonitor.touchSession();
            view.servePage(request);
            associatedPageViews.add(view);
        } catch (Exception ex) {
            views.remove(view.getViewIdentifier());
            view.dispose();
            throw ex;
        } finally {
            view.release();
        }
    }

    private View createView() throws Exception {
        if (views.size()  > viewCap)  {
            //check views.size rather than viewCount since disposed views
            //are not a problem
            LOG.warn("Concurrent view limit of " + viewCap + 
                " exceeded for session " + sessionID);
            throw new RuntimeException("Concurrent view limit exceeded.");
        }
        String viewNumber = String.valueOf(++viewCount);
        View view = new View(viewNumber, sessionID, session, asynchronouslyUpdatedViews, configuration, sessionMonitor, resourceDispatcher, blockingRequestHandlerContext, authorization);
        views.put(viewNumber, view);
        return view;
    }

    public void shutdown() {
    }

    private static boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
