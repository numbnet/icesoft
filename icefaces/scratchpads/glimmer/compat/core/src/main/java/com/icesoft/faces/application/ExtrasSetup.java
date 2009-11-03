package com.icesoft.faces.application;

import javax.faces.application.Resource;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class ExtrasSetup extends ViewHandlerWrapper {
    private ViewHandler handler;

    public ExtrasSetup() {
        super();
    }

    public ExtrasSetup(ViewHandler handler) {
        this.handler = handler;
    }

    public ViewHandler getWrapped() {
        return handler;
    }

    public UIViewRoot createView(FacesContext context, String viewId) {
        UIViewRoot root = handler.createView(context, viewId);

        root.addComponentResource(context, new JavascriptResourceOutput("prototype.js"), "head");
        root.addComponentResource(context, new JavascriptResourceOutput("icesubmit.js"), "head");
        root.addComponentResource(context, new JavascriptResourceOutput("ice-extras.js"), "head");

        return root;
    }

    private static class JavascriptResourceOutput extends UIOutput {
        private String path;

        private JavascriptResourceOutput(String path) {
            this.path = path;
        }

        public void encodeBegin(FacesContext context) throws IOException {
            ResponseWriter writer = context.getResponseWriter();
            Resource bridgeCode = context.getApplication().getResourceHandler().createResource(path);
            writer.startElement("script", this);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeAttribute("src", bridgeCode.getRequestPath(), null);
            writer.endElement("script");
        }
    }
}