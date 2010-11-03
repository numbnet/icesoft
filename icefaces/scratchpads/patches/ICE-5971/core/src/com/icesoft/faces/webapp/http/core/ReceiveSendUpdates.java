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
import com.icesoft.faces.webapp.http.common.Response;
import com.icesoft.faces.webapp.http.common.ResponseHandler;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;
import com.icesoft.util.pooling.CSSNamePool;
import com.icesoft.util.pooling.ClientIdPool;
import com.icesoft.util.pooling.ELPool;
import com.icesoft.util.pooling.XhtmlPool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import java.util.Collection;
import java.util.Map;

public class ReceiveSendUpdates implements Server {

    private static final Log LOG = LogFactory.getLog(ReceiveSendUpdates.class);
    private static final ResponseHandler MissingParameterHandler = new ResponseHandler() {
        public void respond(Response response) throws Exception {
            response.setStatus(500);
            response.writeBody().write("Cannot match view instance. 'ice.view' parameter is missing.".getBytes());
        }
    };
    private static final ResponseHandler MalformedParameterHandler = new ResponseHandler() {
        public void respond(Response response) throws Exception {
            response.setStatus(500);
            response.writeBody().write("Cannot lookup view instance. 'ice.view' parameter is malformed.".getBytes());
        }
    };
    private static Lifecycle lifecycle;

    static {
        LifecycleFactory LifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        lifecycle = LifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    }

    private final SessionDispatcher.Monitor sessionMonitor;
    private final Map views;
    private final Collection synchronouslyUpdatedViews;
    private final PageTest pageTest;

    public ReceiveSendUpdates(final Map views, final Collection synchronouslyUpdatedViews, final SessionDispatcher.Monitor sessionMonitor, final PageTest pageTest) {
        this.views = views;
        this.synchronouslyUpdatedViews = synchronouslyUpdatedViews;
        this.sessionMonitor = sessionMonitor;
        this.pageTest = pageTest;
    }

    public void service(final Request request) throws Exception {
        String viewNumber = request.getParameter("ice.view");
        if (viewNumber == null) {
            request.respondWith(MissingParameterHandler);
        } else if (!ViewIdVerifier.isValid(viewNumber)) {
            request.respondWith(MalformedParameterHandler);
        } else {
            if (!pageTest.isLoaded()) {
                request.respondWith(new ReloadResponse(""));
            } else {
                View view = (View) views.get(viewNumber);
                if (view == null) {
                    request.respondWith(new ReloadResponse(viewNumber));
                } else {
                    try {
                        view.processPostback(request);
                        sessionMonitor.touchSession();

                        //mark that updates for this view are sent on the UI connection
                        //this avoids unblocking the blocking connection for updates generated during the execution of JSF lifecycle
                        synchronouslyUpdatedViews.add(viewNumber);
                        try {
                            renderCycle(view.getFacesContext());
                        } catch (Exception e) {
                            String viewID = "Unknown View";
                            try {
                                viewID = view.getFacesContext().getViewRoot().getViewId();
                            } catch (NullPointerException npe) {
                            }
                            LOG.error("Exception occured during rendering on " +
                                    request.getURI() + " [" + viewID + "]", e);
                            throw e;
                        }
                        synchronouslyUpdatedViews.remove(viewNumber);

                        request.respondWith(new SendUpdates.Handler(views, request));
                        //String pools usage logging
                        LOG.debug("String intern pools sizes:"
                                + "\nClientIdPool: " + ClientIdPool.getSize()
                                + "\nCSSNamePool: " + CSSNamePool.getSize()
                                + "\nELPool: " + ELPool.getSize()
                                + "\nXhtmlPool: " + XhtmlPool.getSize());
                    } catch (FacesException e) {
                        //"workaround" for exceptions zealously captured & wrapped by the JSF implementations
                        Throwable nestedException = e.getCause();
                        if (nestedException == null) {
                            throw e;
                        } else {
                            throw findInitialCause(nestedException, e);
                        }
                    } catch (SessionExpiredException e) {
                        //exception thrown in the middle of JSF lifecycle
                        //respond immediately with session-expired message to avoid any new connections
                        //being initiated by the bridge.
                        request.respondWith(SessionExpiredResponse.Handler);
                    } finally {
                        view.release();
                    }
                }
            }
        }
    }

    public void shutdown() {
    }

    private static Exception findInitialCause(Throwable nestedException, FacesException defaultException) {
        //find the deepest cause
        while (nestedException.getCause() != null) {
            nestedException = nestedException.getCause();
        }

        if (nestedException instanceof Exception) {
            return (Exception) nestedException;
        } else {
            return defaultException;
        }
    }

    private void renderCycle(FacesContext context) {
        com.icesoft.util.SeamUtilities.removeSeamDebugPhaseListener(lifecycle);
        LifecycleExecutor.getLifecycleExecutor(context).apply(context);
    }
}
