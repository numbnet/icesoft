package org.icefaces.push;

import javax.faces.application.Resource;
import javax.faces.application.ViewHandler;
import javax.faces.application.ViewHandlerWrapper;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class BridgeSetup extends ViewHandlerWrapper {
    private static final UIOutput BridgeCode = new UIOutput() {
        public void encodeBegin(FacesContext context) throws IOException {
            ResponseWriter writer = context.getResponseWriter();
            Resource bridgeCode = FacesContext.getCurrentInstance().getApplication().getResourceHandler().createResource("bridge.js");
            writer.startElement("script", this);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeAttribute("src", bridgeCode.getRequestPath(), null);
            writer.endElement("script");
        }
    };
    private static final UIOutput BridgeInitialization = new UIOutput() {
        public void encodeBegin(FacesContext context) throws IOException {
            ResponseWriter writer = context.getResponseWriter();
            ExternalContext externalContext = context.getExternalContext();
            String sessionID = ((HttpSession) externalContext.getSession(true)).getId();
            String contextPath = externalContext.getRequestContextPath();
            writer.startElement("script", this);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeText("ice.onLoad(function() { try {" +
                    "document.body.bridge = ice.Application({" +
                    "blockUI: false," +
                    "session: '" + sessionID + "'," +
                    "connection: {" +
                    "heartbeat: {}," +
                    "context: {current: '" + contextPath + "/',async: '" + contextPath + "/'}" +
                    "}," +
                    "messages: {sessionExpired: 'User Session Expired',connectionLost: 'Network Connection Interrupted',serverError: 'Server Internal Error',description: 'To reconnect click the Reload button on the browser or click the button below',buttonText: 'Reload'}}," +
                    "document.body); } catch (e) { alert(e); }; });", null);
            writer.endElement("script");
        }
    };
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
        root.addComponentResource(context, BridgeCode, "head");
        root.addComponentResource(context, BridgeInitialization, "body");
        return root;
    }
}
