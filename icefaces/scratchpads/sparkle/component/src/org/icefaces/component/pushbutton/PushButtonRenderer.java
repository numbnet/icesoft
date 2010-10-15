package org.icefaces.component.PushButton;

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
            PushButton pushButton = (PushButton) uiComponent;
            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
            String clientId = pushButton.getClientId();
    //   	    System.out.println("PBR:decode() button pressed is "+source+" for clientId="+clientId);
             if (clientId.equals(source)) { //won't always be the same ??
                try {
             	   if (!pushButton.isDisabled()){
                        uiComponent.queueEvent(new ActionEvent (uiComponent));

             	   }
                } catch (Exception e) {}
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
        String yuiBaseClass= "yui-button yui-min yui-push-button";
        Object styleClass = pushButton.getStyleClass();
        if (null!=styleClass){
             yuiBaseClass = yuiBaseClass + " " + String.valueOf(styleClass);
        }
		writer.writeAttribute(HTML.CLASS_ATTR, yuiBaseClass, null);
        Object style = pushButton.getStyle();
        if (null!=style && !style.equals("")){
            writer.writeAttribute(HTML.STYLE_ATTR, String.valueOf(style), null);
        }		
		// first child
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
		writer.writeAttribute(HTML.CLASS_ATTR, "first-child", null);
	 	writer.writeAttribute(HTML.ID_ATTR, clientId+"_s2", null);
	 	
		// button element
		writer.startElement(HTML.BUTTON_ELEM, uiComponent);
		writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
		writer.writeAttribute(HTML.NAME_ATTR, clientId+"_button", null);
		writer.writeAttribute(HTML.ID_ATTR, clientId+"_button", null);		
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
		writer.write(pushButton.getLabel());
		writer.endElement(HTML.SPAN_ELEM);
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
	    String builder = "";
	    String valueString = String.valueOf(pushButton.getValue());
	    if (null!=pushButton.getLabel())label=pushButton.getLabel();
	    else if (label.equals(""))
	    	label=String.valueOf(pushButton.getValue());
	   // need to worry if label isn't set?
	    builder = JSONBuilder.create().beginMap().
	    entry("type", "button").
        entry("disabled", pushButton.isDisabled()).
        entry("tabindex", pushButton.getTabindex()).
	    entry("label", label).endMap().toString();

	    String params = "'" + clientId + "'," +
        builder
           + "," +
           JSONBuilder.create().
           beginMap().
               entry("singleSubmit", pushButton.isSingleSubmit()).
               entry("ariaEnabled", EnvUtils.isAriaEnabled(facesContext)).
           endMap().toString();
 //         System.out.println("params = " + params);	    

        String finalScript = "ice.component.PushButton.updateProperties(" + params + ");";
        ScriptWriter.insertScript(facesContext, uiComponent,finalScript);
            
        writer.endElement(HTML.DIV_ELEM);
    }
}
