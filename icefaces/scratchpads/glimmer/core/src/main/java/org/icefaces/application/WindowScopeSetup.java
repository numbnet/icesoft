package org.icefaces.application;

import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;

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

        ExternalContext externalContext = context.getExternalContext();
        Map sessionMap = externalContext.getSessionMap();
        String id = externalContext.getRequestParameterMap().get("ice.window");
        final WindowScopeManager.ScopeMap scopeMap = WindowScopeManager.lookup(sessionMap, externalContext).determineWindowScope(id);
        sessionMap.put("window", scopeMap);
        root.addComponentResource(context, new UIOutput() {
            public void encodeBegin(FacesContext context) throws IOException {
                ResponseWriter writer = context.getResponseWriter();
                writer.startElement("script", this);
                writer.writeAttribute("type", "text/javascript", null);
                writer.writeText("window.ice.window = " + scopeMap.getId() + ";", null);
                writer.endElement("script");
            }
        }, "body");
        return root;
    }
}
