package org.icefaces.component.datetime;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.icefaces.component.utils.HTML;


public class DateTimeRenderer extends Renderer{

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        DateTime dateTime = (DateTime) uiComponent;
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        writer.write(String.valueOf(dateTime.getValue()));
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HTML.DIV_ELEM);
    }
}
