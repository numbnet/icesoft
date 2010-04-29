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
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
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

        String invalidateHTTPCache = "?a" + hashCode();

        //replace with icepush.js resource in icepush.jar
        if (EnvUtils.isICEpushPresent()) {
            UIOutput icepushCode = new UIOutput();
            icepushCode.setTransient(true);
            icepushCode.getAttributes().put("escape", "false");
            icepushCode.setValue("<script src='code.icepush.jsf" + invalidateHTTPCache + "'  type='text/javascript'></script>");
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

            UIOutput icefacesSetup = new UIOutput();
            icefacesSetup.setId(viewID + "_icefaces_config");
            icefacesSetup.setTransient(true);
            String clientID = icefacesSetup.getClientId();
            icefacesSetup.getAttributes().put("escape", "false");
            icefacesSetup.setValue("<script id='" + clientID + "' type=\"text/javascript\">" +
                    //define bridge configuration
                    "document.getElementById('" + clientID + "').parentNode.configuration = {" +
                    "deltaSubmit: " + deltaSubmit + "," +
                    //associate viewID with its corresponding DOM fragment
                    "viewID: '" + viewID + "'," +
                    "standardFormSerialization: " + standardFormSerialization +
                    "};" +
                    //bridge needs the window ID
                    "window.ice.window = '" + windowID + "';" +
                    "</script>");
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
                UIOutput icepushSetup = new UIOutput() {
                    public Object getValue() {
                        //determine lazily if ICEpush should be wired up
                        return LazyPushManager.lookup(context).enablePush(viewID) ?
                                "<script type=\"text/javascript\">ice.setupPush('" +
                                        viewID + "', '" + sessionExpiryPushID + "');</script>" : "";
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