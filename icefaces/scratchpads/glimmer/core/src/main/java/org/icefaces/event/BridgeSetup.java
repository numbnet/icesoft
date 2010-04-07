package org.icefaces.event;

import org.icefaces.application.ExternalContextConfiguration;
import org.icefaces.application.LazyPushManager;
import org.icefaces.application.WindowScopeManager;
import org.icefaces.push.Configuration;
import org.icefaces.push.SessionBoundServer;
import org.icefaces.push.SessionViewManager;
import org.icefaces.util.EnvUtils;

import javax.faces.application.Application;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.logging.Logger;

public class BridgeSetup implements SystemEventListener {
    public static String ViewState = BridgeSetup.class.getName() + "::ViewState";
    private static Logger log = Logger.getLogger(BridgeSetup.class.getName());
    private Configuration configuration;

    public BridgeSetup() {
        configuration = new ExternalContextConfiguration("org.icefaces", FacesContext.getCurrentInstance().getExternalContext());
    }

    public boolean isListenerForSource(Object source) {
        return true;
    }

    public void processEvent(SystemEvent event) throws AbortProcessingException {
        final FacesContext context = FacesContext.getCurrentInstance();

        if (EnvUtils.isICEfacesView(context)) {
            UIViewRoot root = context.getViewRoot();
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
                final String windowID = WindowScopeManager.lookup(context).lookupWindowScope().getId();
                final String viewID = application.getStateManager().getViewState(context);
                //save the calculated view state key so that other framework parts will use the same key
                context.getExternalContext().getRequestMap().put(ViewState, viewID);

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
                    final String sessionExpiryPushID = SessionBoundServer.inferSessionExpiryIdentifier(windowID);
                    UIOutput icepushSetup = new UIOutput() {
                        public Object getValue() {
                            //determine lazily if ICEpush should be wired up
                            return LazyPushManager.lookup(context).enablePush(viewID) ?
                                    "<script type=\"text/javascript\">" +
                                            "ice.push.register(['" + viewID + "'], ice.retrieveUpdate('" + viewID + "'));" +
                                            "ice.push.register(['" + sessionExpiryPushID + "'], ice.sessionExpired);" +
                                            "</script>" : "";
                        }
                    };
                    icepushSetup.setTransient(true);
                    icepushSetup.getAttributes().put("escape", "false");
                    root.addComponentResource(context, icepushSetup, "body");
                }
            } catch (Exception e) {
                //could re-throw as a FacesException, but WindowScope failure should
                //not be fatal to the application
                e.printStackTrace();
            }
        }
    }
}