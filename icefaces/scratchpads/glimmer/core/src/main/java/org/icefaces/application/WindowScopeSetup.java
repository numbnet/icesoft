package org.icefaces.application;

import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

import java.util.logging.Level;
import java.util.logging.Logger;

public class WindowScopeSetup extends ViewHandlerWrapper {
    private static Logger log = Logger.getLogger("org.icefaces.windowscope");
    private ViewHandler handler;

    public WindowScopeSetup() {
        super();
    }

    public WindowScopeSetup(ViewHandler handler) {
        this.handler = handler;
    }

    public ViewHandler getWrapped() {
        return handler;
    }

    public UIViewRoot createView(FacesContext context, String viewId) {
        UIViewRoot root = handler.createView(context, viewId);
        try {
            String id = WindowScopeManager.lookup(context).determineWindowID(context);
            
            UIOutput output = new UIOutput();
            output.getAttributes().put("escape", "false");
            output.setValue("<script type=\"text/javascript\">window.ice.window = " + id + ";</script>");
            root.addComponentResource(context, output, "body");

        } catch (Exception e)  {
            if (log.isLoggable(Level.WARNING)) {
                log.log(Level.WARNING,"WindowScope createView: " + e);
            }
        }
        return root;
    }
}
