package org.icefaces.impl.renderkit.html_basic;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import javax.faces.render.Renderer;
import java.io.IOException;

public class PartialSubmitRenderer extends Renderer {

    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
        String parentId = component.getParent().getClientId();
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", component);
        writer.writeAttribute("id", component.getClientId(), "id");
        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", "type");
        writer.writeText("ice.enablePartialSubmit('" + parentId + "');", component, null);
        writer.endElement("script");
        writer.endElement("div");
    }

}
