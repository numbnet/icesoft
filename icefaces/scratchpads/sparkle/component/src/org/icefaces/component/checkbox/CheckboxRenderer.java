package org.icefaces.component.checkbox;

import java.io.IOException;
import java.util.*;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.component.utils.HTML;
import org.icefaces.util.EnvUtils;


public class CheckboxRenderer extends Renderer {

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.containsKey("ice.event.captured")) {
            Checkbox checkbox = (Checkbox) uiComponent;
            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
            String clientId = uiComponent.getClientId();
            if (clientId.equals(source)) {
				Boolean submittedValue = Boolean.valueOf((String.valueOf(requestParameterMap.get(clientId+"_value"))));
				checkbox.setSubmittedValue(submittedValue);
            }
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Checkbox checkbox = (Checkbox) uiComponent;

		// root element
        //writer.startElement(HTML.INPUT_ELEM, uiComponent);
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
		//writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
		//writer.writeAttribute(HTML.TYPE_ATTR, "checkbox", null);
		writer.writeAttribute(HTML.CLASS_ATTR, "yui-button yui-checkbox-button", null);
		
		// first child
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
		writer.writeAttribute(HTML.CLASS_ATTR, "first-child", null);
		
		// button element
		writer.startElement(HTML.BUTTON_ELEM, uiComponent);
		writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
		writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
		
		// if there's an image, render label manually, don't rely on YUI, since it'd override button's contents
		if (checkbox.getImage() != null) {
			writer.startElement(HTML.SPAN_ELEM, uiComponent);
			writer.write(checkbox.getLabel());
			writer.endElement(HTML.SPAN_ELEM);
			
			writer.startElement(HTML.IMG_ELEM, uiComponent);
			writer.writeAttribute(HTML.SRC_ATTR, checkbox.getImage(), null);
			writer.endElement(HTML.IMG_ELEM);
		}
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
		String clientId = uiComponent.getClientId(facesContext);
		Checkbox checkbox = (Checkbox) uiComponent;
        writer.endElement(HTML.BUTTON_ELEM);
		writer.endElement(HTML.SPAN_ELEM);
		writer.endElement(HTML.SPAN_ELEM);
		//writer.endElement(HTML.INPUT_ELEM);
		
		// js call
        StringBuilder call= new StringBuilder();
        call.append("ice.component.checkbox.updateProperties('");
        call.append(clientId);
        call.append("', ");
        //pass through YUI slider properties 
        call.append("{");
        call.append("type:'checkbox'");
        if (checkbox.getImage() == null) {
            call.append(", label:'");  
            call.append(checkbox.getLabel());
            call.append("'");
        }
        call.append(", checked:'");  
        call.append(checkbox.getValue());
        call.append("'},");
        //pass JSF component specific properties that would help in slider configuration 
        call.append("{});");

        writer.startElement(HTML.SCRIPT_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "script", HTML.ID_ATTR);             
        writer.write(call.toString());
        writer.endElement(HTML.SCRIPT_ELEM);
    }
}
