package org.icefaces.component.checkbox;

import java.io.IOException;
import java.util.*;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.component.utils.HTML;


public class CheckboxRenderer extends Renderer {

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.containsKey("ice.event.captured")) {
            Checkbox checkbox = (Checkbox) uiComponent;
            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
            String clientId = uiComponent.getClientId();
            if (clientId.equals(source)) {
                try {
                    Boolean value = (Boolean) checkbox.getValue();
                    Boolean submittedValue = Boolean.valueOf((String.valueOf(requestParameterMap.get(clientId+"_value"))));
                    if (value.booleanValue() != submittedValue.booleanValue()) {
						checkbox.setSubmittedValue(submittedValue);
                        uiComponent.queueEvent(new ValueChangeEvent (uiComponent, value, submittedValue));
                    }
                } catch (Exception e) {
				}
            }
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Checkbox checkbox = (Checkbox) uiComponent;

		// generate markup according to YUI button checkbox example
		// root element
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
		writer.writeAttribute(HTML.CLASS_ATTR, "yui-button yui-checkbox-button", null);
		
		// first child
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
		writer.writeAttribute(HTML.CLASS_ATTR, "first-child", null);
		
		// button element
		writer.startElement(HTML.BUTTON_ELEM, uiComponent);
		writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
		writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
		String clientId = uiComponent.getClientId(facesContext);
		Checkbox checkbox = (Checkbox) uiComponent;
        writer.endElement(HTML.BUTTON_ELEM);
		writer.endElement(HTML.SPAN_ELEM);
		writer.endElement(HTML.SPAN_ELEM);
		
		// js call
        String javascriptCall = "Ice.component.checkbox.updateProperties('"+ clientId + "', {type: 'checkbox',"
			+ "label: '" + checkbox.getLabel() + "', checked: " + checkbox.getValue() + "});";

        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "call", HTML.ID_ATTR); 
        writer.writeAttribute(HTML.STYLE_ATTR, "display:none", HTML.STYLE_ATTR);         
        writer.write(javascriptCall);
        writer.endElement(HTML.SPAN_ELEM);
     
        String execute = "Ice.component.checkbox.execute('" + clientId + "');";
        writer.startElement(HTML.SCRIPT_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "script", HTML.ID_ATTR);             
        writer.write(execute);
        writer.endElement(HTML.SCRIPT_ELEM);
    }
}
