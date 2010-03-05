package org.icefaces.component.slider;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.icefaces.component.utils.HTML;


public class SliderRenderer extends Renderer{

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        String clientId = uiComponent.getClientId(facesContext);
        ResponseWriter writer = facesContext.getResponseWriter();        
        Slider slider = (Slider) uiComponent;
        
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);      
        writer.writeAttribute(HTML.CLASS_ATTR, "class"+ slider.getVarName(), HTML.CLASS_ATTR);             
        writer.endElement(HTML.DIV_ELEM);  
        
        writer.startElement(HTML.SCRIPT_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "script", HTML.ID_ATTR);             
        writer.write("ice.yui.slider.updateProperties('"+ clientId +"', '"+ slider.getVarName() +"', {}, {});");
        writer.endElement(HTML.SCRIPT_ELEM);  
                
    }

}
