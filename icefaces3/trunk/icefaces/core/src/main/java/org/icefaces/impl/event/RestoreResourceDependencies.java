package org.icefaces.impl.event;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

public class RestoreResourceDependencies implements SystemEventListener {

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot = facesContext.getViewRoot();
        VisitContext visitContext = VisitContext.createVisitContext(facesContext);
        viewRoot.visitTree(visitContext, new VisitCallback() {
            public VisitResult visit(VisitContext context, UIComponent target) {
                VisitResult result = VisitResult.ACCEPT;
                Class<UIComponent> compClass = (Class<UIComponent>) target.getClass();
                ResourceDependencies resourceDependencies = compClass.getAnnotation(ResourceDependencies.class);
                if (resourceDependencies != null) {
                    for (ResourceDependency resDep : resourceDependencies.value()) {
                        addResourceDependency(facesContext, resDep);
                    }
                }
                ResourceDependency resourceDependency = compClass.getAnnotation(ResourceDependency.class);
                if (resourceDependency != null) {
                    addResourceDependency(facesContext, resourceDependency);
                }
                return result;
            }
        });
    }

    private void addResourceDependency(FacesContext context, ResourceDependency resourceDependency) {
        ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
        String name = resourceDependency.name();
        String library = resourceDependency.library();
        String target = resourceDependency.target();
        target = target == null || "".equals(target) ? "head" : target;
        String rendererType = resourceHandler.getRendererTypeForResourceName(name);

        context.getViewRoot().addComponentResource(context, new ResourceOutput(rendererType, name, library), target);
    }

    public boolean isListenerForSource(Object source) {
        return source instanceof UIViewRoot;
    }

    public static class ResourceOutput extends UIOutput {
        public ResourceOutput() {
        }

        public ResourceOutput(String rendererType, String name, String library) {
            setRendererType(rendererType);
            if (name != null && name.length() > 0) {
                getAttributes().put("name", name);
            }
            if (library != null && library.length() > 0) {
                getAttributes().put("library", library);
            }
        }
    }
}
