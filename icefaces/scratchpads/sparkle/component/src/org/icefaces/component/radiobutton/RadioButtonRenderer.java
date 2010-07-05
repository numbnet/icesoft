package org.icefaces.component.radiobutton;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.model.SelectItem;
import javax.faces.render.Renderer;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.component.utils.HTML;
import org.icefaces.util.EnvUtils;


public class RadioButtonRenderer extends Renderer {
	private final static Logger log = Logger.getLogger(RadioButtonRenderer.class.getName());
    
	public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.containsKey("ice.event.captured")) {
            RadioButton radioButton = (RadioButton) uiComponent;
            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
            String clientId = uiComponent.getClientId();
            if (clientId.equals(source)) {
            	System.out.println("radioButtonRenderer: clientId="+clientId+" source="+source);
            	log.info("in decode where client="+clientId+" and source="+source);
            	//get parent of this component and the hidden field value??
            	//if spread then each radio button has to have it's own hidden field though??
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
        RadioButton radioButton = (RadioButton) uiComponent;
		// root element div is with buttongroup class
        
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+radioButton.getId(), null);  
        if (radioButton.isChecked())
		   writer.writeAttribute(HTML.CLASS_ATTR, "yui-button yui-radio-button yui-button-checked", null);
        else{
           writer.writeAttribute(HTML.CLASS_ATTR, "yui-button yui-radio-button", null);
        }
		// first child		
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
		writer.writeAttribute(HTML.CLASS_ATTR, "first-child", null);
		// button element
		writer.startElement(HTML.BUTTON_ELEM, uiComponent);
		writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
		
		//yui sample markup for the buttongroup shows
		//name is same for all buttons in the group and no id is rendered
		writer.writeAttribute(HTML.NAME_ATTR, clientId+"_button", null);
		writer.writeAttribute(HTML.ID_ATTR, clientId+"_button", null);
		writer.writeAttribute(HTML.VALUE_ATTR, radioButton.getValue(), null);
	  	if (radioButton.isChecked()){
     		writer.writeAttribute(HTML.CHECKED_ATTR, true, null);
     	}
   
		
		// if there's an image, render label manually, don't rely on YUI, since it'd override button's contents
		if (radioButton.getImage() != null) {
			writer.startElement(HTML.SPAN_ELEM, uiComponent);
			writer.write(radioButton.getLabel());
			writer.endElement(HTML.SPAN_ELEM);
			
			writer.startElement(HTML.IMG_ELEM, uiComponent);
			writer.writeAttribute(HTML.SRC_ATTR, radioButton.getImage(), null);
			writer.endElement(HTML.IMG_ELEM);
		}
		else {
			writer.startElement(HTML.SPAN_ELEM, uiComponent);
			writer.write(radioButton.getLabel());
			writer.endElement(HTML.SPAN_ELEM);	
		}

		
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
		String clientId = uiComponent.getClientId(facesContext);
		RadioButton radioButton = (RadioButton) uiComponent;
	       writer.endElement(HTML.BUTTON_ELEM);
			writer.endElement(HTML.SPAN_ELEM);  
		    
			writer.endElement(HTML.SPAN_ELEM);			

    }
}
