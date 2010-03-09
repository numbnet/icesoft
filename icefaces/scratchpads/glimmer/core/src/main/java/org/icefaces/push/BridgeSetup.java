package org.icefaces.push;

import org.icefaces.application.ExternalContextConfiguration;
import org.icefaces.application.WindowScopeManager;
import org.icefaces.push.servlet.SessionExpiredException;
import org.icefaces.util.EnvUtils;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BridgeSetup extends ViewHandlerWrapper {
    private static Logger log = Logger.getLogger("org.icefaces.util.DOMUtil");
    private static final String Marker = BridgeSetup.class.getName();
    private ViewHandler handler;
    private Configuration configuration;

    public BridgeSetup() {
        super();
        this.configuration = new ExternalContextConfiguration("org.icefaces", FacesContext.getCurrentInstance().getExternalContext());
    }

    public BridgeSetup(ViewHandler handler) {
        this();
        this.handler = handler;
    }

    public ViewHandler getWrapped() {
        return handler;
    }

    public UIViewRoot createView(FacesContext context, String viewId) {
        //todo: move the session expiry functionlity into its own view handler
        if (context.getExternalContext().getRequestMap().containsKey(SessionExpiredException.class.getName())) {
            throw new SessionExpiredException("User session has expired");
        }

        try {
            WindowScopeManager.lookup(context).determineWindowID(context);
        } catch (Exception e) {
            log.log(Level.WARNING, "Unable to set up WindowScope ", e);
        }
        return handler.createView(context, viewId);
    }

    public void renderView(FacesContext context, UIViewRoot root) throws IOException, FacesException {
        if (EnvUtils.isICEfacesView(context)) {
            Map rootAttributes = root.getAttributes();
            //modify UIViewRoot only once
            if (!rootAttributes.containsKey(Marker)) {
                rootAttributes.put(Marker, BridgeSetup.class);

                UIOutput output;

                output = new UIOutput();
                output.setRendererType("javax.faces.resource.Script");
                output.getAttributes().put("name", "jsf.js");
                output.getAttributes().put("library", "javax.faces");
                root.addComponentResource(context, output, "head");

                Application application = context.getApplication();
                String invalidateHTTPCache = "?a" + hashCode();

                //replace with icepush.js resource in icepush.jar
                if (EnvUtils.isICEpushPresent()) {
                    output = new UIOutput();
                    output.getAttributes().put("escape", "false");
                    output.setValue("<script src='code.icepush.jsf" + invalidateHTTPCache + "'  type='text/javascript'></script>");
                    root.addComponentResource(context, output, "head");
                }

                String path = application.getResourceHandler().createResource("bridge.js").getRequestPath();
                output = new UIOutput();
                output.getAttributes().put("escape", "false");
                output.setValue("<script src='" + path + invalidateHTTPCache + "'  type='text/javascript'></script>");
                root.addComponentResource(context, output, "head");

                try {
                    String id = WindowScopeManager.lookup(context).lookupWindowScope().getId();

                    output = new UIOutput();
                    output.getAttributes().put("escape", "false");
                    output.setValue("<script type=\"text/javascript\">" +
                            "ice.configuration = {" +
                            "deltaSubmit: " + configuration.getAttributeAsBoolean("deltaSubmit", false) +
                            "};" +
                            "window.ice.window = '" + id + "';" +
                            "</script>");
                    root.addComponentResource(context, output, "body");

                    if (EnvUtils.isICEpushPresent()) {
                        String viewID = context.getApplication().getStateManager().getViewState(context);
                        SessionViewManager.lookup(context).addView(viewID);
                        output = new UIOutput();
                        output.getAttributes().put("escape", "false");
                        String sessionExpiryPushID = SessionBoundServer.inferSessionExpiryIdentifier(id);
                        output.setValue("<script type=\"text/javascript\">" +
                                "ice.push.register(['" + viewID + "'], ice.retrieveUpdate('" + viewID + "'));" +
                                "ice.push.register(['" + sessionExpiryPushID + "'], ice.sessionExpired);" +
                                "</script>");
                        root.addComponentResource(context, output, "body");
                    }
                } catch (Exception e) {
                    //could re-throw as a FacesException, but WindowScope failure should
                    //not be fatal to the application
                    e.printStackTrace();
                }
            }
        }

        handler.renderView(context, root);
    }
}
