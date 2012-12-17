package org.icefaces.impl.event;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.visit.VisitCallback;
import javax.faces.component.visit.VisitContext;
import javax.faces.component.visit.VisitHint;
import javax.faces.component.visit.VisitResult;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.*;

public class RestoreResourceDependencies implements SystemEventListener {
    private static final Set HINTS = new HashSet(Arrays.asList(new VisitHint[] { VisitHint.SKIP_ITERATION }));

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        final FacesContext facesContext = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot = facesContext.getViewRoot();
        VisitContext visitContext = VisitContext.createVisitContext(facesContext, null, HINTS);
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
        UIViewRoot viewRoot = context.getViewRoot();
        ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
        String name = resourceDependency.name();
        String library = resourceDependency.library();
        String target = resourceDependency.target();
        target = target == null || "".equals(target) ? "head" : target;

        List<UIComponent> componentResources = viewRoot.getComponentResources(context, target);
        int position = -1;
        for (int i = 0; i < componentResources.size(); i++) {
            UIComponent c = componentResources.get(i);
            Map<String, Object> attributes = c.getAttributes();
            String resourceName = (String) attributes.get("name");
            String resourceLibrary = fixResourceParameter((String) attributes.get("library"));
            String normalizedLibrary = fixResourceParameter(library);
            if (name.equals(resourceName) && (normalizedLibrary == resourceLibrary/*both null*/ || normalizedLibrary.equals(resourceLibrary))) {
                position = i;
                break;
            }
        }

        //add only if missing
        if (position == -1) {
            String rendererType = resourceHandler.getRendererTypeForResourceName(name);
            viewRoot.addComponentResource(context, new ResourceOutput(rendererType, name, library), target);
        }
    }

    public boolean isListenerForSource(Object source) {
        return source instanceof UIViewRoot;
    }

    private static String fixResourceParameter(String value) {
        return value == null || "".equals(value) ? null : value;
    }
}
