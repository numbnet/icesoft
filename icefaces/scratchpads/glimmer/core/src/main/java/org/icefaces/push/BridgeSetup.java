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

        root.addComponentResource(context, new UIOutput() {
            public void encodeBegin(FacesContext context) throws IOException {
                ResponseWriter writer = context.getResponseWriter();
                writer.startElement("script", this);
                writer.writeAttribute("type", "text/javascript", null);
                writer.writeAttribute("src", "code.icepush", null);
                writer.endElement("script");
            }
        }, "head");

        root.addComponentResource(context, new UIOutput() {
            public void encodeBegin(FacesContext context) throws IOException {
                ResponseWriter writer = context.getResponseWriter();
                Resource bridgeCode = context.getApplication().getResourceHandler().createResource("bridge.js");
                writer.startElement("script", this);
                writer.writeAttribute("type", "text/javascript", null);
                writer.writeAttribute("src", bridgeCode.getRequestPath(), null);
                writer.endElement("script");
            }
        }, "head");

        final String windowID = WindowScopeManager.lookup(context).lookupWindowScope().getId();
        root.addComponentResource(context, new UIOutput() {
            public void encodeBegin(FacesContext context) throws IOException {
                ResponseWriter writer = context.getResponseWriter();

                writer.startElement("script", this);
                writer.writeAttribute("type", "text/javascript", null);
                writer.writeText("ice.push.register(['" + windowID + "'], ice.retrieveUpdate);", null);
                writer.endElement("script");
            }
        }, "body");

        return root;
    }
}
