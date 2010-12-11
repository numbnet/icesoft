package org.icefaces.impl.renderkit.html_basic;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.component.UIComponent;
import javax.faces.render.Renderer;
import java.io.IOException;

public class SingleSubmitRenderer extends Renderer {
    public static String SINGLE_SUBMIT_MARKER = SingleSubmitRenderer.class.getName();

    @Override
    public void encodeBegin(FacesContext context, UIComponent component)
            throws IOException {
        UIComponent parent = component.getParent();
        String parentId = parent.getClientId();
        parent.getAttributes().put(SINGLE_SUBMIT_MARKER, SINGLE_SUBMIT_MARKER);
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", component);
        writer.writeAttribute("id", component.getClientId(), "id");
        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", "type");
        writer.writeText("ice.enableSingleSubmit('" + parentId + "');", component, null);
        writer.endElement("script");
        writer.endElement("div");
    }

}
