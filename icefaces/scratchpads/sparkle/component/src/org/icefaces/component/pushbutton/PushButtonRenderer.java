package org.icefaces.component.pushbutton;

import java.io.IOException;
import java.util.*;
import javax.faces.event.ActionEvent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;


import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.JSONBuilder;
import org.icefaces.component.utils.ScriptWriter;
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
		 
		// js call using JSONBuilder utility ICE-5831 and ScriptWriter ICE-5830
	    String label = "";
	    String ifImage = "";
	    String valueString = String.valueOf(pushButton.getValue());
	    if (null!=pushButton.getLabel())label=pushButton.getLabel();
	    else if (label.equals(""))
	    	label=String.valueOf(pushButton.getValue());
	   // need to worry if label isn't set?
	    if (pushButton.getImage()==null){
	        ifImage = JSONBuilder.create().beginMap().
	        entry("type", "button").
	        entry("label", label).endMap().toString();
	    }
	    else {
	        ifImage = JSONBuilder.create().beginMap().
	        entry("type", "button").endMap().toString();  
	    }
	    String params = "'" + clientId + "'," +
        ifImage
           + "," +
           JSONBuilder.create().
           beginMap().
               entry("disabled", pushButton.isDisabled()).
               entry("tabindex", pushButton.getTabindex()).
               entry("singleSubmit", pushButton.isSingleSubmit()).
               entry("ariaEnabled", EnvUtils.isAriaEnabled(facesContext)).
           endMap().toString();
          System.out.println("params = " + params);	    

        String finalScript = "ice.component.pushbutton.updateProperties(" + params + ");";
        ScriptWriter.insertScript(facesContext, uiComponent,finalScript);
            
        writer.endElement(HTML.DIV_ELEM);
    }
}
