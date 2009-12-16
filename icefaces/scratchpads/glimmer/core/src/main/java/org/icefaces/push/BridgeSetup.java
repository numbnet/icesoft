package org.icefaces.push;

import org.icefaces.application.WindowScopeManager;
import org.icefaces.util.EnvUtils;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;

public class BridgeSetup extends ViewHandlerWrapper {
    private static final String Marker = BridgeSetup.class.getName();
    private ViewHandler handler;

    public BridgeSetup() {
        super();
    }

    public BridgeSetup(ViewHandler handler) {
        this.handler = handler;
    }

    public ViewHandler getWrapped() {
        return handler;
    }

    public UIViewRoot createView(FacesContext context, String viewId) {
        WindowScopeManager.lookup(context).determineWindowID(context);
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

                //replace with icepush.js resource in icepush.jar
                output = new UIOutput();
                output.getAttributes().put("escape", "false");
                output.setValue("<script src='code.icepush.jsf' type='text/javascript'></script>");
                root.addComponentResource(context, output, "head");

                output = new UIOutput();
                output.setRendererType("javax.faces.resource.Script");
                output.getAttributes().put("name", "bridge.js");
                root.addComponentResource(context, output, "head");

                try {
                    String id = WindowScopeManager.lookup(context).lookupWindowScope().getId();

                    output = new UIOutput();
                    output.getAttributes().put("escape", "false");
                    output.setValue("<script type=\"text/javascript\">window.ice.window = " + id + ";</script>");
                    root.addComponentResource(context, output, "body");

                    output = new UIOutput();
                    output.getAttributes().put("escape", "false");
                    output.setValue("<script type=\"text/javascript\">ice.push.register(['" + id + "'], ice.retrieveUpdate);</script>");
                    root.addComponentResource(context, output, "body");
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
