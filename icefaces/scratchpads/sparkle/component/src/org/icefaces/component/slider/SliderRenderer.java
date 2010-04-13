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

/**
 * The sliderRender renders following elements:
 * 1. A div with a client id(e.g.)
 *  <div id="xxx" />
 *  which will be used by the YUI slider, as a slider holder
 *  
 * 2. A script node that makes a call to communicate with slider object on client
 *   <script>
 *        ice.yui.slider.updateProperties(clientId, yuiProps, jsfProps);
 *   </script>
 *   
 *   In addition to the rendering the renderer performs decode as well. This component
 *   doesn't use a hidden field for it value instead takes advantage of param support of JSF2
 */
public class SliderRenderer extends Renderer{

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();

        //"ice.event.captured" should be holding the event source id
        if (requestParameterMap.containsKey("ice.event.captured")) {
            Slider slider = (Slider)uiComponent;
            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
            String clientId = uiComponent.getClientId();
            //If I am a source of event?
            if (clientId.equals(source)) {
                try {
                    int value = slider.getValue();
                    int submittedValue = Integer.parseInt((String.valueOf(requestParameterMap.get(clientId+"_value"))));
                    //if there is a value change, queue a valueChangeEvent    
                    if (value != submittedValue) { 
                        uiComponent.queueEvent(new ValueChangeEvent (uiComponent, 
                                       new Integer(value), new Integer(submittedValue)));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        String clientId = uiComponent.getClientId(facesContext);
        ResponseWriter writer = facesContext.getResponseWriter();        
        Slider slider = (Slider)uiComponent;
        //YUI3 slider requires a div, where it renders slider component.
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);      
        String styleClass = slider.getStyleClass();
        if (styleClass != null) {
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, HTML.CLASS_ATTR);
        }
        String style = slider.getStyle();
        if (style != null) {
            writer.writeAttribute(HTML.STYLE_ATTR, style, HTML.STYLE_ATTR);
        }
        writer.endElement(HTML.DIV_ELEM);  
        
        //make a call to YUI3 helper API to update 
        writer.startElement(HTML.SCRIPT_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "script", HTML.ID_ATTR); 
        StringBuilder call= new StringBuilder();
        call.append("ice.yui.slider.updateProperties('");
        call.append(clientId);
        call.append("', ");
        //pass through YUI slider properties 
        call.append("{");
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
        call.append("'},");
        //pass JSF component specific properties that would help in slider configuration 
        call.append("{");
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
        call.append("});");
        writer.write(call.toString());
        writer.endElement(HTML.SCRIPT_ELEM);  
                
    }

}
