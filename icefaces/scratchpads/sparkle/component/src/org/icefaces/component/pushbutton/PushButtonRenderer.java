package org.icefaces.component.pushbutton;

import java.io.IOException;
import java.util.*;
import javax.faces.event.ActionEvent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;


import org.icefaces.component.utils.HTML;
import org.icefaces.util.EnvUtils;


public class PushButtonRenderer extends Renderer {

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.containsKey("ice.event.captured")) {
            PushButton pushButton = (PushButton) uiComponent;
            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
            String clientId = pushButton.getClientId();
       	    System.out.println("PBR:decode() button pressed is "+source+" for clientId="+clientId);
             if (clientId.equals(source)) { //won't always be the same ??
                try {
             	   //do I need to check to see if it is disabled first?
             	   //ActionSource2 has a list of ActionListeners available
             	   //will that change this piece of code?
             	   if (!pushButton.isDisabled()){
                        uiComponent.queueEvent(new ActionEvent (uiComponent));

             	   }
                } catch (Exception e) {}
             }
//            else System.out.println("PBR: clientId!=source");
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
    	
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        PushButton pushButton = (PushButton) uiComponent;

		// root element
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_span", null);
		writer.writeAttribute(HTML.CLASS_ATTR, "yui-button yui-min yui-push-button", null);
		
		// first child
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
		writer.writeAttribute(HTML.CLASS_ATTR, "first-child", null);
	 	writer.writeAttribute(HTML.ID_ATTR, clientId+"_s2", null);
	 	
		// button element
		writer.startElement(HTML.BUTTON_ELEM, uiComponent);
		writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
		writer.writeAttribute(HTML.NAME_ATTR, clientId+"_button", null);
		writer.writeAttribute(HTML.ID_ATTR, clientId+"_button", null);		
		// if there's an image, render label manually, don't rely on YUI, since it'd override button's contents
		if (pushButton.getImage() == null) {
			writer.startElement(HTML.SPAN_ELEM, uiComponent);
			writer.write(pushButton.getLabel());
			writer.endElement(HTML.SPAN_ELEM);
		}else {	
			writer.startElement(HTML.SPAN_ELEM, uiComponent);
			writer.writeAttribute(HTML.ID_ATTR, clientId+"_btn_img", null);
			writer.startElement(HTML.IMG_ELEM, uiComponent);
			writer.writeAttribute(HTML.SRC_ATTR, pushButton.getImage(), null);
			writer.endElement(HTML.IMG_ELEM);
			writer.endElement(HTML.SPAN_ELEM);
		}
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
		String clientId = uiComponent.getClientId(facesContext);
		PushButton pushButton = (PushButton) uiComponent;
        writer.endElement(HTML.BUTTON_ELEM);
		writer.endElement(HTML.SPAN_ELEM);
		writer.endElement(HTML.SPAN_ELEM);
	
	    writer.startElement(HTML.SPAN_ELEM, uiComponent);
	    writer.writeAttribute(HTML.ID_ATTR, clientId +"_sp", null); 
		// js call
        StringBuilder call= new StringBuilder();
        call.append("ice.component.pushbutton.updateProperties('");
        call.append(clientId);
        call.append("', ");
        //pass through YUI  properties 
        call.append("{");
        call.append("type:'button'");
        if (pushButton.getImage() == null) {
            call.append(", label:'");  
            call.append(pushButton.getLabel());
            call.append("'");
        }

        call.append("},");
        //pass JSF component specific properties 
        call.append("{");
        call.append("singleSubmit:");
        call.append(pushButton.isSingleSubmit());  
        call.append(", ");        
        call.append("aria:");
        call.append(EnvUtils.isAriaEnabled(facesContext)); 
        call.append(", ");  
        call.append("disabled:");
        call.append(pushButton.isDisabled());  
        call.append(", ");                
        call.append("tabindex:");
        call.append(pushButton.getTabindex());   
        call.append("});");
 
        writer.startElement(HTML.SCRIPT_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "script", HTML.ID_ATTR);             
        writer.write(call.toString());
        writer.endElement(HTML.SCRIPT_ELEM);
        
        writer.endElement(HTML.SPAN_ELEM);      
        writer.endElement(HTML.DIV_ELEM);
    }
}
