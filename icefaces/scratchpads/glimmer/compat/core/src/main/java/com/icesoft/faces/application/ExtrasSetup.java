package com.icesoft.faces.application;

import org.icefaces.application.ExternalContextConfiguration;
import org.icefaces.push.Configuration;
import org.icefaces.push.ConfigurationException;
import org.icefaces.util.EnvUtils;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.ListResourceBundle;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class ExtrasSetup extends ViewHandlerWrapper {
    private ViewHandler handler;
    private static final String EXTRAS_SETUP_MARKER = ExtrasSetup.class.getName();
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

    public ExtrasSetup() {
        super();
    }

    public ExtrasSetup(ViewHandler handler) {
        this.handler = handler;
    }

    public ViewHandler getWrapped() {
        return handler;
    }

    public void renderView(FacesContext context, UIViewRoot root)
            throws IOException, FacesException {
        if (!EnvUtils.isICEfacesView(context)) {
            handler.renderView(context, root);
            return;
        }
        Map rootAttributes = root.getAttributes();
        if (rootAttributes.containsKey(EXTRAS_SETUP_MARKER)) {
            handler.renderView(context, root);
            return;
        }
        rootAttributes.put(EXTRAS_SETUP_MARKER, EXTRAS_SETUP_MARKER);

        root.addComponentResource(context, new JavascriptResourceOutput("prototype.js"), "head");
        root.addComponentResource(context, new JavascriptResourceOutput("icesubmit.js"), "head");
        root.addComponentResource(context, new JavascriptResourceOutput("ice-extras.js"), "head");
        root.addComponentResource(context, new JavascriptResourceOutput("status.js"), "head");

        ResourceBundle localizedBundle = defaultBridgeMessages;
        try {
            localizedBundle = ResourceBundle.getBundle("bridge-messages", context.getViewRoot().getLocale());
        } catch (MissingResourceException e) {
            localizedBundle = defaultBridgeMessages;
        }
        //todo: see if the configuration can be created once in the constructor
        Configuration configuration = new ExternalContextConfiguration("org.icefaces", context.getExternalContext());
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
        String contextPath = handler.getResourceURL(context, "/");
        String blockUI = configuration.getAttribute("blockUIOnSubmit", "false");
        UIOutput output = new UIOutput();
        output.getAttributes().put("escape", "false");
        output.setValue("<script type=\"text/javascript\">" +
                "ice.DefaultIndicators({" +
                "blockUI: " + blockUI + "," +
                "connectionLostRedirectURI: " + connectionLostRedirectURI + "," +
                "sessionExpiredRedirectURI: " + sessionExpiredRedirectURI + "," +
                "connection: { context: '" + contextPath + "'}," +
                "messages: {" +
                "sessionExpired: '" + localizedBundle.getString("session-expired") + "'," +
                "connectionLost: '" + localizedBundle.getString("connection-lost") + "'," +
                "serverError: '" + localizedBundle.getString("server-error") + "'," +
                "description: '" + localizedBundle.getString("description") + "'," +
                "buttonText: '" + localizedBundle.getString("button-text") + "'" +
                "}}, document.body);" +
                "</script>");
        root.addComponentResource(context, output, "body");

        handler.renderView(context, root);
        return;
    }

    public static class JavascriptResourceOutput extends UIOutput {

        public JavascriptResourceOutput() {
        }

        public JavascriptResourceOutput(String path) {
            setRendererType("javax.faces.resource.Script");
            getAttributes().put("name", path);
        }
    }
}