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

package com.icesoft.faces.application;

import org.icefaces.application.ExternalContextConfiguration;
import org.icefaces.event.UIOutputWriter;
import org.icefaces.push.Configuration;
import org.icefaces.push.ConfigurationException;
import org.icefaces.util.EnvUtils;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.IOException;
import java.util.ListResourceBundle;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ExtrasSetup implements SystemEventListener {
    private static final ResourceBundle defaultBridgeMessages = new ListResourceBundle() {
        protected Object[][] getContents() {
            return new Object[][]{
                    {"session-expired", "User Session Expired"},
                    {"connection-lost", "Network Connection Interrupted"},
                    {"server-error", "Server Internal Error"},
                    {"description", "To reconnect click the Reload button on the browser or click the button below"},
                    {"button-text", "Reload"}
            };
        }
    };
    private final Configuration configuration;

    public ExtrasSetup() {
        configuration = new ExternalContextConfiguration("org.icefaces", FacesContext.getCurrentInstance().getExternalContext());
    }

    public boolean isListenerForSource(Object source) {
        return true;
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        final FacesContext context = FacesContext.getCurrentInstance();

        if (EnvUtils.isICEfacesView(context)) {
            UIViewRoot root = context.getViewRoot();

            root.addComponentResource(context, new JavascriptResourceOutput("compat.js"), "head");
            root.addComponentResource(context, new JavascriptResourceOutput("icefaces-compat.js"), "head");

            UIOutput output = new UIOutputWriter() {
                public void encode(ResponseWriter writer, FacesContext context) throws IOException {
                    ResourceBundle localizedBundle = defaultBridgeMessages;
                    try {
                        localizedBundle = ResourceBundle.getBundle("bridge-messages", context.getViewRoot().getLocale());
                    } catch (MissingResourceException e) {
                        localizedBundle = defaultBridgeMessages;
                    }

                    ViewHandler handler = context.getApplication().getViewHandler();

                    String connectionLostRedirectURI;
                    try {
                        String uri = configuration.getAttribute("connectionLostRedirectURI");
                        connectionLostRedirectURI = "'" + handler.getResourceURL(context, uri.replaceAll("'", "")) + "'";
                    } catch (ConfigurationException e) {
                        connectionLostRedirectURI = "null";
                    }

                    String sessionExpiredRedirectURI;
                    try {
                        String uri = configuration.getAttribute("sessionExpiredRedirectURI");
                        sessionExpiredRedirectURI = "'" + handler.getResourceURL(context, uri.replaceAll("'", "")) + "'";
                    } catch (ConfigurationException e) {
                        sessionExpiredRedirectURI = "null";
                    }

                    final String contextPath = handler.getResourceURL(context, "/");
                    final String blockUI = configuration.getAttribute("blockUIOnSubmit", "false");

                    writer.startElement("script", this);
                    writer.writeAttribute("type", "text/javascript", null);
                    writer.write("ice.DefaultIndicators({");
                    writer.write("blockUI: ");
                    writer.write(blockUI);
                    writer.write(",");
                    writer.write("connectionLostRedirectURI: ");
                    writer.write(connectionLostRedirectURI);
                    writer.write(",");
                    writer.write("sessionExpiredRedirectURI: ");
                    writer.write(sessionExpiredRedirectURI);
                    writer.write(",");
                    writer.write("connection: { context: '");
                    writer.write(contextPath);
                    writer.write("'},");
                    writer.write("messages: {");
                    writer.write("sessionExpired: '");
                    writer.write(localizedBundle.getString("session-expired"));
                    writer.write("',");
                    writer.write("connectionLost: '");
                    writer.write(localizedBundle.getString("connection-lost"));
                    writer.write("',");
                    writer.write("serverError: '");
                    writer.write(localizedBundle.getString("server-error"));
                    writer.write("',");
                    writer.write("description: '");
                    writer.write(localizedBundle.getString("description"));
                    writer.write("',");
                    writer.write("buttonText: '");
                    writer.write(localizedBundle.getString("button-text"));
                    writer.write("'");
                    writer.write("}}, document.body);");
                    writer.endElement("script");
                }
            };
            output.setTransient(true);
            root.addComponentResource(context, output, "body");
        }
    }

    public static class JavascriptResourceOutput extends UIOutput {
        public JavascriptResourceOutput(String path) {
            setRendererType("javax.faces.resource.Script");
            getAttributes().put("name", path);
            setTransient(true);
        }
    }
}