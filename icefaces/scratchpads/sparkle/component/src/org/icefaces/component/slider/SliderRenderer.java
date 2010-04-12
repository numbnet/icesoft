package org.icefaces.component.slider;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.render.Renderer;

import org.icefaces.component.utils.ARIA;
import org.icefaces.component.utils.HTML;
import org.icefaces.util.EnvUtils;


public class SliderRenderer extends Renderer{

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.containsKey("ice.event.captured")) {
            Slider slider = (Slider)uiComponent;
            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
            String clientId = uiComponent.getClientId();
            if (clientId.equals(source)) {
                try {
                    int value = slider.getValue();
                    int submittedValue = Integer.parseInt((String.valueOf(requestParameterMap.get(clientId+"_value"))));
                    if (value != submittedValue) { 
                        uiComponent.queueEvent(new ValueChangeEvent (uiComponent, 
                                       new Integer(value), new Integer(submittedValue)));
                    }
                } catch (Exception e) {}
            }
        }
    }
    
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        String clientId = uiComponent.getClientId(facesContext);
        ResponseWriter writer = facesContext.getResponseWriter();        
        Slider slider = (Slider)uiComponent;
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);      
        String styleClass = slider.getStyleClass();
        if (styleClass == null) {
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, HTML.CLASS_ATTR);
        }
        writer.endElement(HTML.DIV_ELEM);  
        
        writer.startElement(HTML.SCRIPT_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "script", HTML.ID_ATTR); 
        StringBuilder call= new StringBuilder();
        call.append("ice.yui.slider.updateProperties('");
        call.append(clientId);
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
        call.append("'},{");
        if (slider.getThumbImage()!= null) {
            call.append("thumbImage:'");
            call.append(slider.getThumbImage());
            call.append("', ");
        }
        call.append("submitOn:'");
        call.append(slider.getSubmitOn());
        call.append("', ");
        call.append("singleSubmit:");
        call.append(slider.isSingleSubmit());
        call.append(", ");        
        call.append("slideInterval:");
        call.append(slider.getSlideInterval());       
        call.append(", ");        
        call.append("aria:");
        call.append(EnvUtils.isAriaEnabled(facesContext)); 
        call.append(", ");        
        call.append("tabindex:");
        call.append(slider.getTabindex());   
        call.append("},{});");
        System.out.println(call);        
        writer.write(call.toString());
        writer.endElement(HTML.SCRIPT_ELEM);  
                
    }

}
