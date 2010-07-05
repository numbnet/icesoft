package org.icefaces.component.radiobutton;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.component.utils.HTML;
import org.icefaces.util.EnvUtils;


public class ButtonGroupRenderer extends Renderer {
	private final static Logger log = Logger.getLogger(ButtonGroupRenderer.class.getName());
    
	public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.containsKey("ice.event.captured")) {
            ButtonGroup buttonGroup = (ButtonGroup) uiComponent;
            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
            String clientId = uiComponent.getClientId();
            if (clientId.equals(source)) {
            	System.out.println("ButtonGroupRenderer: in decode where client="+clientId+" and source="+source);
//            	Need to get the selectedItem passed and set it to the radioButton
//				SelectItem submittedValue = Boolean.valueOf((String.valueOf(requestParameterMap.get(clientId+"_value"))));
//				radioButton.setSelectedItem();
            }
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        ButtonGroup buttonGroup = (ButtonGroup) uiComponent;
		// root element
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.writeAttribute(HTML.CLASS_ATTR,"yui-buttongroup", null);
        
		
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
		String clientId = uiComponent.getClientId(facesContext);
        ButtonGroup buttonGroup = (ButtonGroup) uiComponent;	
			//hidden input for single submit=false	
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
		writer.writeAttribute(HTML.ID_ATTR, clientId+"_hs",null);
		writer.startElement("input", uiComponent);
		writer.writeAttribute("type", "hidden", null);
	    writer.writeAttribute("name",clientId+"_hidden", null);
		writer.writeAttribute("id",clientId+"_hidden", null);
		writer.writeAttribute("value", buttonGroup.getSelectedRadioId(), null);
		writer.endElement("input");
		writer.endElement(HTML.SPAN_ELEM);
			// js call
	    writer.startElement(HTML.SPAN_ELEM, uiComponent);		

        StringBuilder call= new StringBuilder();
        call.append("ice.component.radiobutton.updateProperties('");
        call.append(clientId);
        call.append("', ");
        //pass through YUI  properties 
        call.append("{");
        call.append("type:'radio'");
        call.append("},");
        //pass JSF component specific properties 
        call.append("{");
        call.append("singleSubmit:");
        call.append(buttonGroup.isSingleSubmit());       
        call.append(", ");        
        call.append("aria:");
        call.append(EnvUtils.isAriaEnabled(facesContext)); 
        call.append(", "); 
        call.append("disabled:");
        call.append(buttonGroup.isDisabled());  
        call.append(", ");      
        call.append("tabindex:");
        call.append(buttonGroup.getTabindex());   
        call.append("});");

        writer.startElement(HTML.SCRIPT_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "script", HTML.ID_ATTR);             
        writer.write(call.toString());
        writer.endElement(HTML.SCRIPT_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
    }
}
