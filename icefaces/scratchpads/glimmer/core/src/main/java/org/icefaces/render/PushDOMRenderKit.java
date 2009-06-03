package org.icefaces.render;

import javax.faces.application.Resource;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.context.ResponseWriterWrapper;
import javax.faces.render.RenderKit;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Writer;

public class PushDOMRenderKit extends DOMRenderKit {

    public PushDOMRenderKit() {
        super();
    }

    public PushDOMRenderKit(RenderKit delegate) {
        super(delegate);
    }

    public ResponseWriter createResponseWriter(Writer writer, String contentTypeList, String encoding) {
        return new PushResponseWriter(super.createResponseWriter(writer, contentTypeList, encoding));
    }

    private static class PushResponseWriter extends ResponseWriterWrapper {
        private final ResponseWriter responseWriter;

        public PushResponseWriter(ResponseWriter responseWriter) {
            this.responseWriter = responseWriter;
        }

        public void startElement(String name, UIComponent component) throws IOException {
            super.startElement(name, component);
            if ("head".equalsIgnoreCase(name)) {
                Resource bridgeCode = FacesContext.getCurrentInstance().getApplication().getResourceHandler().createResource("bridge.js");
                responseWriter.startElement("script", component);
                responseWriter.writeAttribute("type", "text/javascript", null);
                responseWriter.writeAttribute("src", bridgeCode.getRequestPath(), null);
                responseWriter.endElement("script");
            }
            if ("body".equalsIgnoreCase(name)) {
                ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
                String sessionID = ((HttpSession) externalContext.getSession(true)).getId();
                String contextPath = externalContext.getRequestContextPath();
                responseWriter.startElement("script", component);
                responseWriter.writeAttribute("type", "text/javascript", null);
                responseWriter.writeText("ice.onLoad(function() { try {" +
                        "document.body.bridge = ice.Application({" +
                        "blockUI: false," +
                        "session: '" + sessionID + "'," +
                        "connection: {" +
                        "heartbeat: {}," +
                        "context: {current: '" + contextPath + "/',async: '" + contextPath + "/'}" +
                        "}," +
                        "messages: {sessionExpired: 'User Session Expired',connectionLost: 'Network Connection Interrupted',serverError: 'Server Internal Error',description: 'To reconnect click the Reload button on the browser or click the button below',buttonText: 'Reload'}}," +
                        "document.body); } catch (e) { alert(e); }; });", null);
                responseWriter.endElement("script");
            }
        }

        public ResponseWriter cloneWithWriter(Writer writer) {
            return new PushResponseWriter(super.cloneWithWriter(writer));
        }

        public ResponseWriter getWrapped() {
            return responseWriter;
        }
    }
}
