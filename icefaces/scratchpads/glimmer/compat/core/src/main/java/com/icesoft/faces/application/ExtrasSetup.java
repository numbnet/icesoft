package com.icesoft.faces.application;

import org.icefaces.application.ExternalContextConfiguration;
import org.icefaces.push.Configuration;
import org.icefaces.push.ConfigurationException;
import org.icefaces.util.EnvUtils;

import javax.faces.application.ViewHandler;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.util.ListResourceBundle;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
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
    private Configuration configuration;

    public ExtrasSetup() {
        this.configuration = new ExternalContextConfiguration("org.icefaces", FacesContext.getCurrentInstance().getExternalContext());
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

            String contextPath = handler.getResourceURL(context, "/");
            String blockUI = configuration.getAttribute("blockUIOnSubmit", "false");
            UIOutput output = new UIOutput();
            output.setTransient(true);
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