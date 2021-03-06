/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.impl.event;

import org.icefaces.impl.application.LazyPushManager;
import org.icefaces.impl.application.WindowScopeManager;
import org.icefaces.impl.push.SessionViewManager;
import org.icefaces.impl.push.servlet.ICEpushListenResource;
import org.icefaces.impl.renderkit.DOMRenderKit;
import org.icefaces.render.ExternalScript;
import org.icefaces.util.EnvUtils;

import javax.faces.application.Resource;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.render.RenderKit;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BridgeSetup implements SystemEventListener {
    public final static String ViewState = BridgeSetup.class.getName() + "::ViewState";
    private final static Logger log = Logger.getLogger(BridgeSetup.class.getName());
    private int seed = 0;
    private boolean standardFormSerialization;
    private boolean deltaSubmit;

    public BridgeSetup() {
        FacesContext fc = FacesContext.getCurrentInstance();
        deltaSubmit = EnvUtils.isDeltaSubmit(fc);
        standardFormSerialization = EnvUtils.isStandardFormSerialization(fc);
    }

    public boolean isListenerForSource(Object source) {
        return true;
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        final FacesContext context = FacesContext.getCurrentInstance();

        if (!EnvUtils.isICEfacesView(context)) {
            //If ICEfaces is not configured for this view, we don't need to process this event.
            return;
        }

        if (!EnvUtils.hasHeadAndBodyComponents(context)) {
            //If ICEfaces is configured for this view, but the h:head and/or h:body components
            //are not available, we cannot process it but we log the reason. 
            if (log.isLoggable(Level.WARNING)) {
                log.log(Level.WARNING, "ICEfaces configured for view " + context.getViewRoot().getViewId() +
                        " but h:head and h:body components are required");
            }
            return;
        }

        UIViewRoot root = context.getViewRoot();
        final ExternalContext externalContext = context.getExternalContext();

        root.addComponentResource(context, new JavascriptResourceOutput("jsf.js", "javax.faces"), "head");

        String invalidateHTTPCache = "";
        if (EnvUtils.isUniqueResourceURLs(context)) {
            invalidateHTTPCache = "?rand=" + hashCode();
        }

        if (EnvUtils.isICEpushPresent()) {
            root.addComponentResource(context, new JavascriptResourceOutput("icepush.js" + invalidateHTTPCache), "head");
        }

        root.addComponentResource(context, new JavascriptResourceOutput("bridge.js" + invalidateHTTPCache), "head");

        RenderKit rk = context.getRenderKit();
        if (rk instanceof DOMRenderKit) {

            // If the context param is not null then make sure it's true
            DOMRenderKit drk = (DOMRenderKit) rk;
            List<ExternalScript> scriptRenderers = drk.getCustomRenderScripts();
            String contextParamName;
            String value = "";
            int i = 0;
            for (ExternalScript es : scriptRenderers) {
                i++;
                contextParamName = es.contextParam();
                boolean insertHere = true;
                // If present, the context param must be true for rendering
                // but if not present, always insert the script. Annotation default is "Null"
                if (!contextParamName.equals("Null")) {
                    value = externalContext.getInitParameter(contextParamName);
                    insertHere = (value != null && !value.equalsIgnoreCase(""));
                }
                if (insertHere) {
                    UIOutput externalScript = new GenericScriptWriter(
                            es.scriptURL() + value);
                    externalScript.setTransient(true);
                    String externalScriptId = "external-script-" + i;
                    externalScript.setId(externalScriptId);
                    externalScript.getAttributes().put("name", externalScriptId);
                    root.addComponentResource(context, externalScript, "head");
                }
            }

            Set<ResourceDependency> addedResDeps =
                    new HashSet<ResourceDependency>();
            List<String> mandatoryResourceComponents = drk.getMandatoryResourceComponents();
            for (String compClassName : mandatoryResourceComponents) {
                try {
                    Class<UIComponent> compClass = (Class<UIComponent>)
                            Class.forName(compClassName);
                    // Iterate over ResourceDependencies, ResourceDependency 
                    // annotations, creating ResourceOutput components for 
                    // each unique one, so they'll add the mandatory
                    // resources.
                    ResourceDependencies resDeps = compClass.getAnnotation(
                            ResourceDependencies.class);
                    if (resDeps != null) {
                        for (ResourceDependency resDep : resDeps.value()) {
                            addMandatoryResourceDependency(context, root,
                                    compClassName, addedResDeps, resDep);
                        }
                    }
                    ResourceDependency resDep = compClass.getAnnotation(
                            ResourceDependency.class);
                    if (resDep != null) {
                        addMandatoryResourceDependency(context, root,
                                compClassName, addedResDeps, resDep);
                    }
                }
                catch (Exception e) {
                    if (log.isLoggable(Level.WARNING)) {
                        log.log(Level.WARNING, "When processing mandatory " +
                                "resource components, could not create instance " +
                                "of '" + compClassName + "'");
                    }
                }
            }

            /*
            // Usefull for debugging the added resources
            List<String> resStrings = new java.util.ArrayList<String>();
            List<UIComponent> resources = root.getComponentResources(context, "head");
            for (UIComponent resComp : resources) {
                resStrings.add(resComp.toString());
            }
            java.util.Collections.sort(resStrings);
            for(String resStr : resStrings) {
                System.out.println("resStr: " + resStr);
            }
            */
        }

        try {
            String tempWindowID = "unknownWindow";
            WindowScopeManager.ScopeMap windowScope =
                    WindowScopeManager.lookupWindowScope(context);
            if (null != windowScope) {
                tempWindowID = windowScope.getId();
            } else {
                log.log(Level.WARNING, "Unable to find WindowScope for view " +
                        context.getViewRoot().getViewId());
            }
            final String windowID = tempWindowID;
            final String viewID = assignViewID(externalContext);


            UIOutput icefacesSetup = new UIOutputWriter() {
                public void encode(ResponseWriter writer, FacesContext context) throws IOException {
                    String clientID = getClientId(context);
                    writer.startElement("span", this);
                    writer.writeAttribute("id", clientID, null);
                    writer.startElement("script", this);
                    //define bridge configuration
                    writer.write("ice.setupBridge('");
                    writer.write(clientID);
                    writer.write("', '");
                    writer.write(viewID);
                    writer.write("', '");
                    writer.write(windowID);
                    writer.write("', {");
                    writer.write("deltaSubmit: ");
                    writer.write(Boolean.toString(deltaSubmit));
                    writer.write(",");
                    writer.write("standardFormSerialization: ");
                    writer.write(Boolean.toString(standardFormSerialization));
                    writer.write(",");
                    writer.write("blockUIOnSubmit: ");
                    writer.write(Boolean.toString(EnvUtils.isBlockUIOnSubmit(context)));
                    writer.write("});");
                    writer.endElement("script");
                    writer.endElement("span");
                }
            };
            icefacesSetup.setTransient(true);
            icefacesSetup.setId(viewID + "_icefaces_config");
            root.addComponentResource(context, icefacesSetup, "body");

            //make sure there's always a form so that ice.singleSubmit and ice.retrieveUpdate can do their job
            UIForm retrieveUpdateSetup = new UIForm() {
                //ID is assigned uniquely by ICEpush so no need to prepend
                public String getClientId(FacesContext context) {
                    return getId();
                }
            };
            retrieveUpdateSetup.setTransient(true);
            //use viewID as element ID so that ice.singleSubmit and ice.receiveUpdate can easily lookup
            //the corresponding view state key (javax.faces.ViewState) 
            retrieveUpdateSetup.setId(viewID);
            root.addComponentResource(context, retrieveUpdateSetup, "body");

            if (EnvUtils.isICEpushPresent()) {
                SessionViewManager.addView(context, viewID);
                UIOutputWriter icepushSetup = new UIOutputWriter() {
                    public void encode(ResponseWriter writer, FacesContext context) throws IOException {
                        //need a span to make sure JSF bridge evaluates included script properly
                        writer.startElement("span", this);
                        writer.writeAttribute("id", this.getClientId(context), null);
                        writer.startElement("script", this);
                        writer.writeAttribute("type", "text/javascript", null);
                        writer.write(LazyPushManager.enablePush(context, viewID) ? "ice.setupPush('" + viewID + "');" : "");
                        Resource icepushListenResource = context.getApplication()
                                .getResourceHandler().createResource(ICEpushListenResource.RESOURCE_NAME);
                        String path = stripHostInfo(icepushListenResource.getRequestPath());
                        boolean isPortalEnvironment = EnvUtils.instanceofPortletRequest(externalContext.getRequest());
                        String contextPath = isPortalEnvironment ? "/" : externalContext.getRequestContextPath();
                        writer.write("ice.push.configuration.contextPath=\"" + contextPath + "\";");
                        String uriPattern = path.replace(ICEpushListenResource.RESOURCE_NAME, "{{command}}");
                        writer.write("ice.push.configuration.uriPattern=\"" + uriPattern + "\";");
                        writer.endElement("script");
                        writer.endElement("span");
                    }
                };
                icepushSetup.setTransient(true);
                icepushSetup.setId(viewID + "_icepush");
                root.addComponentResource(context, icepushSetup, "body");
            }
        } catch (Exception e) {
            //could re-throw as a FacesException, but WindowScope failure should
            //not be fatal to the application
            log.log(Level.WARNING, "Failed to generate JS bridge setup.", e);
        }
    }

    private static String stripHostInfo(String uriString) {
        try {
            URI uri = URI.create(uriString);
            return (new URI(null, null, uri.getPath(), uri.getQuery(), uri.getFragment())).toString();
        } catch (URISyntaxException e) {
            return uriString;
        }
    }

    private String assignViewID(ExternalContext externalContext) {
        final String viewIDParameter = externalContext.getRequestParameterMap().get("ice.view");
        //keep viewID sticky until page is unloaded
        final String viewID = viewIDParameter == null ? generateViewID() : viewIDParameter;
        //save the calculated view state key so that other parts of the framework will use the same key
        externalContext.getRequestMap().put(ViewState, viewID);
        return viewID;
    }

    private String generateViewID() {
        return "v" + Integer.toString(hashCode(), 36) + Integer.toString(++seed, 36);
    }

    /**
     * This is only valid after a postback, or during or after rendering in
     * the initial page get.
     *
     * @return The view id
     */
    public static String getViewID(ExternalContext externalContext) {
        Map requestMap = externalContext.getRequestMap();
        return (String) requestMap.get(BridgeSetup.ViewState);
    }

    private static void addMandatoryResourceDependency(
            FacesContext facesContext, UIViewRoot root, String compClassName,
            Set<ResourceDependency> addedResDeps, ResourceDependency resDep) {
        if (addedResDeps.contains(resDep)) {
            return;
        }
        addedResDeps.add(resDep);
        addMandatoryResource(facesContext, root, compClassName, resDep.name(),
                resDep.library(), resDep.target());
    }

    private static void addMandatoryResource(FacesContext facesContext,
                                             UIViewRoot root, String compClassName, String name,
                                             String library, String target) {
        if (target == null || target.length() == 0) {
            target = "head";
        }
        String rendererType = FacesContext.getCurrentInstance().
                getApplication().getResourceHandler().
                getRendererTypeForResourceName(name);
        if (rendererType == null || rendererType.length() == 0) {
            if (log.isLoggable(Level.WARNING)) {
                log.log(Level.WARNING, "Could not determine renderer type " +
                        "for mandatory resource, for component: " + compClassName +
                        ". Resource name: " + name + ", library: " + library);
            }
        } else {
            root.addComponentResource(facesContext, new ResourceOutput(
                    rendererType, name, library), target);
        }
    }

    public static class ResourceOutput extends UIOutput {
        public ResourceOutput(String rendererType, String name, String library) {
            setRendererType(rendererType);
            if (name != null && name.length() > 0) {
                getAttributes().put("name", name);
            }
            if (library != null && library.length() > 0) {
                getAttributes().put("library", library);
            }
            setTransient(true);
        }

        public String toString() {
            return String.valueOf(getAttributes().get("library")) + "/" +
                    String.valueOf(getAttributes().get("name"));
        }
    }

    public static class JavascriptResourceOutput extends UIOutput {

        public JavascriptResourceOutput(String path, String library) {
            this(path);
            getAttributes().put("library", library);
        }

        public JavascriptResourceOutput(String path) {
            setRendererType("javax.faces.resource.Script");
            getAttributes().put("name", path);
            setTransient(true);
        }
    }

    class GenericScriptWriter extends UIOutputWriter {
        private String script;

        public GenericScriptWriter(String script) {
            super();
            this.script = script;
            this.setTransient(true);
        }

        public void encode(ResponseWriter writer, FacesContext context) throws IOException {
            String clientID = getClientId(context);
            writer.startElement("script", this);
            writer.writeAttribute("id", clientID, null);
            //define potential script entries
            writer.writeAttribute("src", script, null);
            writer.writeAttribute("type", "text/javascript", null);
            writer.endElement("script");
        }

        //Convince PortletFaces Bridge that this is a valid script for
        //inserting into the Portal head

        public String getRendererType() {
            return "javax.faces.resource.Script";
        }
        //Provide a script value for PortletFaces Bridge
        //requires fix to bridge
//        public Object getValue()  {
//            return script;
//        }
    }
}
