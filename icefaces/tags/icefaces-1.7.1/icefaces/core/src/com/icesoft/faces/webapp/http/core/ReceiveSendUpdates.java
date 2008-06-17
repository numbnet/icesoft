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
    private static final String REQUIRED = "required";
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
        if (request.getParameterAsBoolean("ice.submit.partial", false)) {
            String componentID = request.getParameter("ice.event.captured");
            UIComponent component = D2DViewHandler.findComponent(componentID, context.getViewRoot());
            renderCyclePartial(context, component, componentID);
        } else {
            renderCycle(context);
        }

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

    private void renderCyclePartial(FacesContext context,
                                    UIComponent component, String clientId) {
        synchronized (context) {
            Map alteredRequiredComponents =
                    setRequiredFalseInFormContaining(component, clientId);
            com.icesoft.util.SeamUtilities.removeSeamDebugPhaseListener(lifecycle);
            LifecycleExecutor.getLifecycleExecutor(context).apply(context);
            setRequiredTrue(alteredRequiredComponents);
        }
    }

    private void setRequiredTrue(Map requiredComponents) {
        Iterator i = requiredComponents.keySet().iterator();
        UIInput next = null;
        while (i.hasNext()) {
            next = (UIInput) i.next();
            ValueBinding valueBinding = (ValueBinding)
                    requiredComponents.get(next);
            if (null != valueBinding) {
                next.setValueBinding(REQUIRED, valueBinding);
            } else {
                next.setRequired(true);
            }
        }
    }

    private Map setRequiredFalseInFormContaining(
            UIComponent component, String clientId) {
        Map alteredComponents = new HashMap();
        UIComponent form = getContainingForm(component);
        setRequiredFalseOnAllChildrenExceptOne(form, component, clientId,
                alteredComponents);
        return alteredComponents;
    }


    private void setRequiredFalseOnAllChildrenExceptOne(
            UIComponent parent,
            UIComponent componentToAvoid, String clientIdToAvoid,
            Map alteredComponents) {

        FacesContext facesContext = FacesContext.getCurrentInstance();
        //turn off required simply with false for all but iterative case
        ValueBinding FALSE_BINDING = facesContext
                .getApplication().createValueBinding("#{false}");
        
        Iterator kidsAndFacets = parent.getFacetsAndChildren();
        while (kidsAndFacets.hasNext()) {
            UIComponent next = (UIComponent) kidsAndFacets.next();
            if (next instanceof UIInput) {
                UIInput input = (UIInput) next;
                ValueBinding valueBinding =
                        input.getValueBinding(REQUIRED);
                if (null != valueBinding) {
                    ValueBinding replacementBinding = null;
                    if (input == componentToAvoid) {
                        //The component that caused the partialSubmit may
                        //be used iteratively (in a dataTable).  We use
                        //PartialSubmitValueBinding to detect which single
                        //client instance of the component to avoid
                        replacementBinding = new PartialSubmitValueBinding(
                                valueBinding, input, clientIdToAvoid);
                    } else {
                        replacementBinding = FALSE_BINDING;
                    }
                    input.setValueBinding(REQUIRED, replacementBinding);
                    alteredComponents.put(input, valueBinding);
                } else {
                    if (input.isRequired() && input != componentToAvoid &&
                            input.isValid()) {
                        input.setRequired(false);
                        alteredComponents.put(input, null);
                    }
                }
            }
            setRequiredFalseOnAllChildrenExceptOne(next,
                    componentToAvoid, clientIdToAvoid, alteredComponents);
        }
    }


    private UIComponent getContainingForm(UIComponent component) {
        if (null == component) {
            return FacesContext.getCurrentInstance().getViewRoot();
        }
        UIComponent parent = component.getParent();
        while (parent != null) {
            if (parent instanceof UIForm) {
                break;
            }
            parent = parent.getParent();
        }
        return (UIForm) parent;
    }


}
