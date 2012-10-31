package org.icefaces.impl.event;

import org.icefaces.impl.renderkit.DOMRenderKit;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.util.EnvUtils;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.application.ResourceHandler;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.render.RenderKit;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MandatoryResourcesSetup implements SystemEventListener {
    private final static Logger log = Logger.getLogger(MandatoryResourcesSetup.class.getName());
    private final static String JAVAX_FACES_RESOURCE_SCRIPT = "javax.faces.resource.Script";

    public boolean isListenerForSource(Object source) {
        if (!(source instanceof UIViewRoot)) {
            return false;
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        if (!EnvUtils.isICEfacesView(facesContext)) {
            // If ICEfaces is not configured for this view, we don't need to process this event.
            return false;
        }
        if (!EnvUtils.hasHeadAndBodyComponents(facesContext)) {
            // If ICEfaces is configured for this view, but the h:head and/or h:body components
            // are not available, we cannot process it but we log the reason.
            if (log.isLoggable(Level.WARNING)) {
                log.log(Level.WARNING, "ICEfaces configured for view " + facesContext.getViewRoot().getViewId() +
                        " but h:head and h:body components are required");
            }
            return false;
        }
        return true;
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        Map collectedResourceComponents = new HashMap();
        String version = EnvUtils.isUniqueResourceURLs(context) ? String.valueOf(hashCode()) : null;
        ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
        //make resource containers transient so that the removal and addition of resource is not track by the JSF state saving
        Collection<UIComponent> facets = context.getViewRoot().getFacets().values();
        for (UIComponent c: facets) {
            c.setInView(false);
        }
        //add mandatory resources, replace any resources previously added by JSF
        addMandatoryResources(context, collectedResourceComponents, version);
        //jsf.js might be added already by a page or component
        UIOutput jsfResource = new JavascriptResourceOutput(resourceHandler, "jsf.js", "javax.faces", version);
        //add jsf.js resource or replace it if already added by JSF
        addOrCollectReplacingResource(context, "jsf.js", "javax.faces", "head", jsfResource, collectedResourceComponents);
        //restore resource containers to non-transient state
        for (UIComponent c: facets) {
            c.setInView(true);
        }
    }

    private void addMandatoryResources(FacesContext context,
                                       Map collectedResourceComponents,
                                       String version) {
        RenderKit rk = context.getRenderKit();
        if (rk instanceof DOMRenderKit) {
            DOMRenderKit drk = (DOMRenderKit) rk;
            Set<ResourceDependency> addedResourceDependencies = new HashSet<ResourceDependency>();
            List<MandatoryResourceComponent> mandatoryResourceComponents = drk.getMandatoryResourceComponents();
            String resourceConfig = EnvUtils.getMandatoryResourceConfig(context);
            //pad with spaces to allow contains checking
            String resourceConfigPad = " " + resourceConfig + " ";
            for (MandatoryResourceComponent mrc : mandatoryResourceComponents) {
                String compClassName = mrc.value();
                if (!"all".equalsIgnoreCase(resourceConfig)) {
                    String tagName = mrc.tagName();
                    if (!resourceConfigPad.contains(" " + compClassName + " ") &&
                            (tagName != null && tagName.length() > 0 &&
                                    !resourceConfigPad.contains(" " + tagName + " "))) {
                        continue;
                    }
                }
                try {
                    Class<UIComponent> compClass = (Class<UIComponent>) Class.forName(compClassName);
                    // Iterate over ResourceDependencies, ResourceDependency
                    // annotations, creating ResourceOutput components for
                    // each unique one, so they'll add the mandatory
                    // resources.
                    ResourceDependencies resourceDependencies = compClass.getAnnotation(ResourceDependencies.class);
                    if (resourceDependencies != null) {
                        for (ResourceDependency resDep : resourceDependencies.value()) {
                            addMandatoryResourceDependency(context, compClassName, addedResourceDependencies, resDep, version, collectedResourceComponents);
                        }
                    }
                    ResourceDependency resourceDependency = compClass.getAnnotation(ResourceDependency.class);
                    if (resourceDependency != null) {
                        addMandatoryResourceDependency(context, compClassName, addedResourceDependencies, resourceDependency, version, collectedResourceComponents);
                    }
                } catch (Exception e) {
                    if (log.isLoggable(Level.WARNING)) {
                        log.log(Level.WARNING, "When processing mandatory " +
                                "resource components, could not create instance " +
                                "of '" + compClassName + "'");
                    }
                }
            }


            //replace collected resource mandatory components in one shot, otherwise MyFaces will keep re-adding
            //the components registered directly by it
            replaceCollectedResourceComponents(context, "head", collectedResourceComponents);
            replaceCollectedResourceComponents(context, "body", collectedResourceComponents);
        }
    }

    private void replaceCollectedResourceComponents(FacesContext context,
                                                    String target,
                                                    Map collectedResourceComponents) {
        UIViewRoot root = context.getViewRoot();
        List<UIComponent> components = new ArrayList<UIComponent>(root.getComponentResources(context, target));
        for (UIComponent next : components) {
            root.removeComponentResource(context, next, target);
        }

        for (UIComponent next : components) {
            String name = (String) next.getAttributes().get("name");
            String library = (String) next.getAttributes().get("library");
            UIComponent c = (UIComponent) collectedResourceComponents.get(calculateKey(name, library, target));
            if (c == null) {
                root.addComponentResource(context, next, target);
            } else {
                root.addComponentResource(context, c, target);
            }
        }
    }

    private static void addMandatoryResourceDependency(
            FacesContext facesContext,
            String compClassName,
            Set<ResourceDependency> addedResDeps,
            ResourceDependency resDep,
            String version,
            Map collectedResourceComponents) {
        if (addedResDeps.contains(resDep)) {
            return;
        }
        addedResDeps.add(resDep);
        addMandatoryResource(facesContext, compClassName, resDep.name(),
                resDep.library(), version, resDep.target(), collectedResourceComponents);
    }

    private static void addMandatoryResource(FacesContext facesContext,
                                             String compClassName, String name,
                                             String library, String version,
                                             String target,
                                             Map collectedResourceComponents) {
        if (target == null || target.length() == 0) {
            target = "head";
        }

        ResourceHandler resourceHandler = facesContext.getApplication().getResourceHandler();
        String rendererType = resourceHandler.getRendererTypeForResourceName(name);
        if (rendererType == null || rendererType.length() == 0) {
            if (log.isLoggable(Level.WARNING)) {
                log.log(Level.WARNING, "Could not determine renderer type " +
                        "for mandatory resource, for component: " + compClassName +
                        ". Resource name: " + name + ", library: " + library);
            }
        } else {
            UIComponent component = newResourceOutput(resourceHandler, rendererType, name, library, version);
            addOrCollectReplacingResource(facesContext, name, library, target, component, collectedResourceComponents);
        }
    }

    public static void addOrCollectReplacingResource(FacesContext context,
                                                     String name,
                                                     String library,
                                                     String target,
                                                     UIComponent component,
                                                     Map collectedResourceComponents) {
        UIViewRoot viewRoot = context.getViewRoot();
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

        if (position > -1) {
            //collect the component resource to replace it after all mandatory resources are read
            collectedResourceComponents.put(calculateKey(name, library, target), component);
        } else {
            viewRoot.addComponentResource(context, component, target);
        }
    }

    private static String calculateKey(String name, String library, String target) {
        return name + ":" + library + ":" + target;
    }

    private static UIComponent newResourceOutput(ResourceHandler resourceHandler,
                                                 String rendererType,
                                                 String name, String library,
                                                 String version) {
        if (JAVAX_FACES_RESOURCE_SCRIPT.endsWith(rendererType)) {
            return new JavascriptResourceOutput(resourceHandler, name, library, version);
        } else {
            return new ResourceOutput(rendererType, name, library);
        }
    }


    private static String fixResourceParameter(String value) {
        return value == null || "".equals(value) ? null : value;
    }

    public static class NonTransientJavascriptResourceOutput extends JavascriptResourceOutput {
        private static String ScriptURL = "ScriptURL";

        public NonTransientJavascriptResourceOutput() {
        }

        private NonTransientJavascriptResourceOutput(ResourceHandler resourceHandler,
                                                     String name,
                                                     String library,
                                                     String version) {
            super(resourceHandler, name, library, version);
            //save the calculated URL so that it can be restored
            Map attributes = getAttributes();
            attributes.put(ScriptURL, script);

            setTransient(false);
        }

        public void restoreState(FacesContext context, Object state) {
            //call method from super-class to restore the component attributes first
            super.restoreState(context, state);
            Map attributes = getAttributes();
            script = (String) attributes.get(ScriptURL);
        }
    }
}
