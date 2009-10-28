package org.icefaces.application;

import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class WindowScopeSetup extends ViewHandlerWrapper {
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
            final String id = WindowScopeManager.lookup(context).determineWindowID(context);
            root.addComponentResource(context, new UIOutput() {
                public void encodeBegin(FacesContext context) throws IOException {
                    ResponseWriter writer = context.getResponseWriter();
                    writer.startElement("script", this);
                    writer.writeAttribute("id", "ice-window-init", null);
                    writer.writeAttribute("type", "text/javascript", null);
                    writer.writeText("window.ice.window = " + id + ";", null);
                    writer.endElement("script");
                }
            }, "body");
        } catch (Exception e)  {
            e.printStackTrace();
        }
        return root;
    }
}
