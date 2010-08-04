/*
 * Version: MPL 1.1
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
 */

package org.icefaces.event;

import org.icefaces.application.ExternalContextConfiguration;
import org.icefaces.application.LazyPushManager;
import org.icefaces.application.WindowScopeManager;
import org.icefaces.push.Configuration;
import org.icefaces.push.SessionViewManager;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BridgeSetup implements SystemEventListener {
    public final static String ViewState = BridgeSetup.class.getName() + "::ViewState";
    private final static Logger log = Logger.getLogger(BridgeSetup.class.getName());
    private int seed = 0;
    private boolean standardFormSerialization;
    private boolean deltaSubmit;

    public BridgeSetup() {
        Configuration configuration = new ExternalContextConfiguration("org.icefaces", FacesContext.getCurrentInstance().getExternalContext());
        deltaSubmit = configuration.getAttributeAsBoolean("deltaSubmit", false);
        standardFormSerialization = configuration.getAttributeAsBoolean("standardFormSerialization", false);
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

        if(!EnvUtils.hasHeadAndBodyComponents(context)){
            //If ICEfaces is configured for this view, but the h:head and/or h:body components
            //are not available, we cannot process it but we log the reason. 
            if (log.isLoggable(Level.WARNING)) {
                log.log(Level.WARNING, "ICEfaces configured for view " + context.getViewRoot().getViewId() +
                        " but h:head and h:body components are required");
            }
            return;
        }

        UIViewRoot root = context.getViewRoot();
        ExternalContext externalContext = context.getExternalContext();

        root.addComponentResource(context, new JavascriptResourceOutput("jsf.js", "javax.faces"), "head");

        final String invalidateHTTPCache = "?a" + hashCode();

        if (EnvUtils.isICEpushPresent()) {
            root.addComponentResource(context, new JavascriptResourceOutput("icepush.js" + invalidateHTTPCache), "head");
        }

        root.addComponentResource(context, new JavascriptResourceOutput("bridge.js" + invalidateHTTPCache), "head");

        try {
            String tempWindowID = "unknownWindow";
            WindowScopeManager.ScopeMap windowScope = 
                    WindowScopeManager.lookupWindowScope(context);
            if (null != windowScope)  {
                tempWindowID =  windowScope.getId();
            } else {
                log.log(Level.WARNING, "Unable to find WindowScope for view " + 
                        context.getViewRoot().getViewId() );
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
                    writer.write("document.getElementById('");
                    writer.write(clientID);
                    writer.write("').parentNode.configuration = {");
                    writer.write("deltaSubmit: ");
                    writer.write(Boolean.toString(deltaSubmit));
                    writer.write(",");
                    //associate viewID with its corresponding DOM fragment
                    writer.write("viewID: '");
                    writer.write(viewID);
                    writer.write("',");
                    writer.write("standardFormSerialization: ");
                    writer.write(Boolean.toString(standardFormSerialization));
                    writer.write("};");
                    //bridge needs the window ID
                    writer.write("window.ice.window = '");
                    writer.write(windowID);
                    writer.write("';");
                    writer.endElement("script");
                    writer.endElement("span");
                }
            };
            icefacesSetup.setTransient(true);
            icefacesSetup.setId(viewID + "_icefaces_config");
            root.addComponentResource(context, icefacesSetup, "body");

            //make sure there's always a form so that ice.singleSubmit and ice.retrieveUpdate can do their job
            UIForm retrieveUpdateSetup = new UIForm() {
                public void encodeEnd(FacesContext context) throws IOException {
                    ResponseWriter writer = context.getResponseWriter();
                    //apply similar fix as for http://jira.icefaces.org/browse/ICE-5728
                    if (EnvUtils.needViewStateHack() && context.isPostback()) {
                        writer.startElement("input", this);
                        writer.writeAttribute("id", "javax.faces.ViewState", null);
                        writer.writeAttribute("type", "hidden", null);
                        writer.writeAttribute("autocomplete", "off", null);
                        writer.writeAttribute("value", context.getApplication().getStateManager().getViewState(context), null);
                        writer.writeAttribute("name", "javax.faces.ViewState", null);
                        writer.endElement("input");
                    }

                    super.encodeEnd(context);
                }

                //ID is assigned uniquely by ICEpush so no need to prepend
                public String getClientId(FacesContext context)  {
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
                final String sessionExpiryPushID = windowID + ":se";
                UIOutputWriter icepushSetup = new UIOutputWriter() {
                    public void encode(ResponseWriter writer, FacesContext context) throws IOException {
                        //need a span to make sure JSF bridge evaluates included script properly
                        writer.startElement("span", this);
                        writer.writeAttribute("id", this.getClientId(context), null);
                        writer.startElement("script", this);
                        writer.writeAttribute("type", "text/javascript", null);
                        writer.write(LazyPushManager.enablePush(context, viewID) ?
                                "ice.setupPush('" + viewID + "', '" + sessionExpiryPushID + "');" : "");
                        String[] pathTemplate = EnvUtils.getPathTemplate();
                        String rawURL = pathTemplate[0] + "listen.icepush" 
                                + pathTemplate[1];
                        String encodedURL = context.getExternalContext()
                            .encodeResourceURL(rawURL);
                        if (!rawURL.equals(encodedURL))  {
                            writer.write("ice.push.configuration.uri=\"" +
                               encodedURL + "\";");
                        }
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
            e.printStackTrace();
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
}