package org.icefaces.component;

import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponentBase;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@ResourceDependency(name = "bridge.js", target = "head")
public class PushEnabler extends UIComponentBase {

    public String getFamily() {
        return "org.icefaces.component.PushEnabler";
    }

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
}
