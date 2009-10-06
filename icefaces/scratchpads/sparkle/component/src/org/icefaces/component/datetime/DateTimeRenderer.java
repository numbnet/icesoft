package org.icefaces.component.datetime;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.icefaces.component.utils.HTML;


public class DateTimeRenderer  extends Renderer{

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        DateTimeAnnotated dateTime = (DateTimeAnnotated) uiComponent;
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        writer.write(String.valueOf(dateTime.getValue()));
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        //super.encodeEnd() helps dealing with faces messages in ajax environment, 
        //as well as take care of any effect related change if applies.
        super.encodeEnd(facesContext, uiComponent);  
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HTML.DIV_ELEM);
    }
}
