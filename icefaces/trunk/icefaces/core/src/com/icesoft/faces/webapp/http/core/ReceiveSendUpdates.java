package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.context.View;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Response;
import com.icesoft.faces.webapp.http.common.ResponseHandler;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;

import javax.faces.FacesException;
import javax.faces.FactoryFinder;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import java.util.Collection;
import java.util.Map;

public class ReceiveSendUpdates implements Server {
    private static final ResponseHandler MissingParameterHandler = new ResponseHandler() {
        public void respond(Response response) throws Exception {
            response.setStatus(500);
            response.writeBody().write("Cannot match view instance. 'ice.view' parameter is missing.".getBytes());
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

    public ReceiveSendUpdates(Map views, Collection synchronouslyUpdatedViews, SessionDispatcher.Monitor sessionMonitor) {
        this.views = views;
        this.synchronouslyUpdatedViews = synchronouslyUpdatedViews;
        this.sessionMonitor = sessionMonitor;
    }

    public void service(final Request request) throws Exception {
        String viewNumber = request.getParameter("ice.view");
        if (viewNumber == null) {
            request.respondWith(MissingParameterHandler);
        } else {
            View view = (View) views.get(viewNumber);
            if (view == null) {
                //todo: revisit this -- maybe the session was not created yet
                request.respondWith(SessionExpiredResponse.Handler);
            } else {
                try {
                    view.updateOnXMLHttpRequest(request);
                    synchronouslyUpdatedViews.add(request.getParameter("ice.view"));
                    sessionMonitor.touchSession();
                    renderCycle(view.getFacesContext());
                    request.respondWith(new SendUpdates.Handler(views, request));
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
