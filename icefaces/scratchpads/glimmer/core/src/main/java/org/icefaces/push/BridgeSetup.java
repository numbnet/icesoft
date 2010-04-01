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

            UIOutput jsfCode = new UIOutput();
            jsfCode.setTransient(true);
            jsfCode.setRendererType("javax.faces.resource.Script");
            jsfCode.getAttributes().put("name", "jsf.js");
            jsfCode.getAttributes().put("library", "javax.faces");
            root.addComponentResource(context, jsfCode, "head");

            String invalidateHTTPCache = "?a" + hashCode();

            //replace with icepush.js resource in icepush.jar
            if (EnvUtils.isICEpushPresent()) {
                UIOutput icepushCode = new UIOutput();
                icepushCode.setTransient(true);
                icepushCode.getAttributes().put("escape", "false");
                icepushCode.setValue("<script src='code.icepush.jsf" + invalidateHTTPCache + "'  type='text/javascript'></script>");
                root.addComponentResource(context, icepushCode, "head");
            }

            Application application = context.getApplication();

            String path = application.getResourceHandler().createResource("bridge.js").getRequestPath();
            UIOutput icefacesCode = new UIOutput();
            icefacesCode.setTransient(true);
            icefacesCode.getAttributes().put("escape", "false");
            icefacesCode.setValue("<script src='" + path + invalidateHTTPCache + "'  type='text/javascript'></script>");
            root.addComponentResource(context, icefacesCode, "head");

            try {
                String windowID = WindowScopeManager.lookup(context).lookupWindowScope().getId();
                String viewID = application.getStateManager().getViewState(context);

                UIOutput icefacesSetup = new UIOutput();
                icefacesSetup.setId("icefaces_bridge_config");
                icefacesSetup.setTransient(true);
                String clientID = icefacesSetup.getClientId();
                icefacesSetup.getAttributes().put("escape", "false");
                icefacesSetup.setValue("<script id='" + clientID + "' type=\"text/javascript\">" +
                        //define bridge configuration
                        "ice.configuration = {" +
                        "deltaSubmit: " + configuration.getAttributeAsBoolean("deltaSubmit", false) +
                        "};" +
                        //bridge needs the window ID
                        "window.ice.window = '" + windowID + "';" +
                        //save view state for single submit when form is missing
                        "document.getElementById('" + clientID + "').parentNode.javax_faces_ViewState='" + viewID + "';" +
                        "</script>");
                root.addComponentResource(context, icefacesSetup, "body");

                if (EnvUtils.isICEpushPresent()) {
                    SessionViewManager.lookup(context).addView(viewID);
                    UIOutput icepushSetup = new UIOutput();
                    icepushSetup.setTransient(true);
                    icepushSetup.getAttributes().put("escape", "false");
                    String sessionExpiryPushID = SessionBoundServer.inferSessionExpiryIdentifier(windowID);
                    icepushSetup.setValue("<script type=\"text/javascript\">" +
                            "ice.push.register(['" + viewID + "'], ice.retrieveUpdate('" + viewID + "'));" +
                            "ice.push.register(['" + sessionExpiryPushID + "'], ice.sessionExpired);" +
                            "</script>");
                    root.addComponentResource(context, icepushSetup, "body");
                }
            } catch (Exception e) {
                //could re-throw as a FacesException, but WindowScope failure should
                //not be fatal to the application
                e.printStackTrace();
            }
        }

        handler.renderView(context, root);
    }
}
