package org.icefaces.push;

import org.icefaces.application.WindowScopeManager;

import javax.faces.application.Resource;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class BridgeSetup extends ViewHandlerWrapper {
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
        UIViewRoot root = handler.createView(context, viewId);
        UIOutput output;

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
            String windowID = WindowScopeManager.lookup(context).lookupWindowScope().getId();
            output = new UIOutput();
            output.getAttributes().put("escape", "false");
            output.setValue("<script type=\"text/javascript\">ice.push.register(['" + windowID + "'], ice.retrieveUpdate);</script>");
            root.addComponentResource(context, output, "body");
        } catch (Exception e)  {
            //could re-throw as a FacesException, but WindowScope failure should
            //not be fatal to the application
            e.printStackTrace();
        }

        return root;
    }
}
