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
import org.icefaces.push.SessionBoundServer;
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
            if (log.isLoggable(Level.INFO)) {
                log.log(Level.INFO, "ICEfaces disabled for view " + context.getViewRoot().getViewId() +
                        " (h:head and h:body tags must be used)");
            }
            return;
        }

        UIViewRoot root = context.getViewRoot();
        ExternalContext externalContext = context.getExternalContext();

        root.addComponentResource(context, new JavascriptResourceOutput("jsf.js", "javax.faces"), "head");

        final String invalidateHTTPCache = "?a" + hashCode();

        //replace with icepush.js resource in icepush.jar
        if (EnvUtils.isICEpushPresent()) {
            UIOutput icepushCode = new UIOutputWriter() {
                public void encode(ResponseWriter writer) throws IOException {
                    writer.startElement("script", this);
                    writer.writeAttribute("type", "text/javascript", null);
                    writer.writeAttribute("src", "code.icepush.jsf" + invalidateHTTPCache, null);
                    writer.endElement("script");
                }
            };
            icepushCode.setTransient(true);
            root.addComponentResource(context, icepushCode, "head");
        }

        root.addComponentResource(context, new JavascriptResourceOutput("bridge.js" + invalidateHTTPCache), "head");

        try {
            final String windowID = WindowScopeManager.lookup(context).lookupWindowScope().getId();
            final String viewIDParameter = externalContext.getRequestParameterMap().get("ice.view");
            //keep viewID sticky until page is unloaded
            final String viewID = viewIDParameter == null ? generateViewID() : viewIDParameter;
            //save the calculated view state key so that other parts of the framework will use the same key
            externalContext.getRequestMap().put(ViewState, viewID);

            UIOutput icefacesSetup = new UIOutputWriter() {
                public void encode(ResponseWriter writer) throws IOException {
                    String clientID = getClientId();
                    writer.startElement("script", this);
                    writer.writeAttribute("id", clientID, null);
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
                }
            };
            icefacesSetup.setTransient(true);
            icefacesSetup.setId(viewID + "_icefaces_config");
            root.addComponentResource(context, icefacesSetup, "body");

            //make sure there's always a form so that ice.singleSubmit and ice.retrieveUpdate can do their job
            UIForm retrieveUpdateSetup = new UIForm();
            retrieveUpdateSetup.setTransient(true);
            //use viewID as element ID so that ice.singleSubmit and ice.receiveUpdate can easily lookup
            //the corresponding view state key (javax.faces.ViewState) 
            retrieveUpdateSetup.setId(viewID);
            root.addComponentResource(context, retrieveUpdateSetup, "body");

            if (EnvUtils.isICEpushPresent()) {
                SessionViewManager.lookup(context).addView(viewID);
                final String sessionExpiryPushID = SessionBoundServer.inferSessionExpiryIdentifier(windowID);
                UIOutputWriter icepushSetup = new UIOutputWriter() {
                    public void encode(ResponseWriter writer) throws IOException {
                        //need a span to make sure JSF bridge evaluates included script properly
                        writer.startElement("span", this);
                        writer.writeAttribute("id", this.getClientId(context), null);
                        writer.startElement("script", this);
                        writer.writeAttribute("type", "text/javascript", null);
                        writer.write(LazyPushManager.lookup(context).enablePush(viewID) ?
                                "ice.setupPush('" + viewID + "', '" + sessionExpiryPushID + "');" : "");
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

    private String generateViewID() {
        return "v" + Integer.toString(hashCode(), 36) + Integer.toString(++seed, 36);
    }

    public static class JavascriptResourceOutput extends UIOutput {
        public JavascriptResourceOutput(String path, String library) {
            setRendererType("javax.faces.resource.Script");
            getAttributes().put("name", path);
            if (null != library) {
                getAttributes().put("library", library);
            }
            setTransient(true);
        }

        public JavascriptResourceOutput(String path) {
            this(path, null);
        }
    }
}