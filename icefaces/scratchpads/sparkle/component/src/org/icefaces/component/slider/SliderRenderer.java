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
        StringBuilder call= new StringBuilder();
        call.append("ice.yui.slider.updateProperties('");
        call.append(clientId);
        call.append("', '");
        call.append(slider.getVarName());
        call.append("', {");
        call.append("min:");
        call.append(slider.getMin());
        call.append(", ");
        call.append("max:");
        call.append(slider.getMax());
        call.append(", ");
        call.append("value:");
        call.append(slider.getValue());
        call.append(", ");
        call.append("axis:'");
        call.append(slider.getAxis());
        call.append("', ");
        call.append("railSize:'");
        call.append(slider.getRailSize());
        call.append("'},{});");
        System.out.println(call);        
        writer.write(call.toString());
        writer.endElement(HTML.SCRIPT_ELEM);  
                
    }

}
