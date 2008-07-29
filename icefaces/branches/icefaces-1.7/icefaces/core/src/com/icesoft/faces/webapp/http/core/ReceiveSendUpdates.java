package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.application.D2DViewHandler;
import com.icesoft.faces.el.PartialSubmitValueBinding;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;

import javax.faces.FactoryFinder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ReceiveSendUpdates implements Server {
    private static final LifecycleFactory LifecycleFactory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
    private Lifecycle lifecycle = LifecycleFactory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
    private Map commandQueues;
    private Collection synchronouslyUpdatedViews;

    public ReceiveSendUpdates(Map commandQueues, Collection synchronouslyUpdatedViews) {
        this.commandQueues = commandQueues;
        this.synchronouslyUpdatedViews = synchronouslyUpdatedViews;
    }

    public void service(final Request request) throws Exception {
        synchronouslyUpdatedViews.add(request.getParameter("ice.view"));

        FacesContext context = FacesContext.getCurrentInstance();
        renderCycle(context);

        request.respondWith(new SendUpdates.Handler(commandQueues, request));
    }

    public void shutdown() {
    }

    private void renderCycle(FacesContext context) {
        synchronized (context) {
            com.icesoft.util.SeamUtilities.removeSeamDebugPhaseListener(lifecycle);
            LifecycleExecutor.getLifecycleExecutor(context).apply(context);
        }
    }

}
