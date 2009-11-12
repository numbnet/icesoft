package com.icesoft.faces.application;

import javax.faces.FacesException;
import javax.faces.application.Resource;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;

import org.icefaces.util.EnvUtils;

public class ExtrasSetup extends ViewHandlerWrapper {
    private ViewHandler handler;
    private static String EXTRAS_SETUP_MARKER = ExtrasSetup.class.getName();

    public ExtrasSetup() {
        super();
    }

    public ExtrasSetup(ViewHandler handler) {
        this.handler = handler;
    }

    public ViewHandler getWrapped() {
        return handler;
    }

    public void renderView(FacesContext context, UIViewRoot root) 
    throws IOException, FacesException  {
        if (!EnvUtils.isICEfacesView(context)) {
            handler.renderView(context, root);
            return;
        }
        Map rootAttributes = root.getAttributes();
        if (rootAttributes.containsKey(EXTRAS_SETUP_MARKER))  {
            handler.renderView(context, root);
            return;
        }
        rootAttributes.put(EXTRAS_SETUP_MARKER, EXTRAS_SETUP_MARKER);

        root.addComponentResource(context, new JavascriptResourceOutput("prototype.js"), "head");
        root.addComponentResource(context, new JavascriptResourceOutput("icesubmit.js"), "head");
        root.addComponentResource(context, new JavascriptResourceOutput("ice-extras.js"), "head");

        handler.renderView(context, root);
        return;
    }

    public static class JavascriptResourceOutput extends UIOutput {

        public JavascriptResourceOutput() {
        }

        public JavascriptResourceOutput(String path) {
            setRendererType("javax.faces.resource.Script");
            getAttributes().put("name", path);
        }
    }
}