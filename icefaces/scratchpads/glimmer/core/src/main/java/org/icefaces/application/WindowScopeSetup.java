package org.icefaces.application;

import javax.faces.FacesException;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.icefaces.util.EnvUtils;

//The functionality in this class has been moved to BridgeSetup
public class WindowScopeSetup extends ViewHandlerWrapper {
    private static Logger log = Logger.getLogger("org.icefaces.windowscope");
    private static String WSCOPE_SETUP_MARKER = WindowScopeSetup.class.getName();
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

    public void renderView(FacesContext context, UIViewRoot root) 
    throws IOException, FacesException  {
        //Consider moving this code to BridgeSetup to avoid duplicate
        //ICEfaces view detection
        if (!EnvUtils.isICEfacesView(context)) {
            handler.renderView(context, root);
            return;
        }
        Map rootAttributes = root.getAttributes();
        if (rootAttributes.containsKey(WSCOPE_SETUP_MARKER))  {
            handler.renderView(context, root);
            return;
        }
        rootAttributes.put(WSCOPE_SETUP_MARKER, WSCOPE_SETUP_MARKER);

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

        handler.renderView(context, root);
        return;
    }
}
