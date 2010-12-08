package org.icefaces.component.pushbutton;

import java.io.IOException;
import java.util.*;
import javax.faces.event.ActionEvent;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;


import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.JSONBuilder;
import org.icefaces.component.utils.ScriptWriter;
import org.icefaces.component.utils.Utils;
import org.icefaces.util.EnvUtils;
import org.icefaces.render.MandatoryResourceComponent;


@MandatoryResourceComponent("org.icefaces.component.pushbutton.PushButton")
public class PushButtonRenderer extends Renderer {

    List <UIParameter> uiParamChildren;

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
    	Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
            PushButton pushButton = (PushButton) uiComponent;
            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
            String clientId = pushButton.getClientId();
  
             if (clientId.equals(source)) { 
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

          // capture any children UIParameter (f:param) parameters.
        uiParamChildren = Utils.captureParameters( pushButton );

		// root element
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
		writer.writeAttribute(HTML.CLASS_ATTR, "ice-pushbutton", null);
        
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_span", null);
        String yuiBaseClass= "yui-button yui-min yui-push-button";
        Object styleClass = pushButton.getStyleClass();
        if (null!=styleClass){
             yuiBaseClass +=  " " + String.valueOf(styleClass);
        }
		writer.writeAttribute(HTML.STYLE_CLASS_ATTR, yuiBaseClass, null);
        String style = pushButton.getStyle();
        if (style != null && style.trim().length() > 0) {
            writer.writeAttribute(HTML.STYLE_ATTR, style, HTML.STYLE_ATTR);
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
		Object oVal = pushButton.getValue();
		if (null!=oVal) writer.writeText(String.valueOf(oVal), null);
		else{
            String label = pushButton.getLabel();
            if (label != null) {
                writer.writeText(label, null);
            }
		}
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
	    String ariaLabel = "";
	    String yuiLabel="";
	    String builder = "";
	    //if there is a value, then it goes to yui-label property
	    //otherwise, see if there is a label property.  If both exist then send 
	    //separately.
	    Object oVal=pushButton.getValue();
	    if (null!=oVal) yuiLabel = String.valueOf(oVal);
	    Object oLab=pushButton.getLabel();
	    if (null!=oLab) ariaLabel=String.valueOf(oLab);
	    if (yuiLabel.equals(""))yuiLabel=ariaLabel;
	    if (ariaLabel.equals(""))ariaLabel=yuiLabel;
	    	    
	   // need to worry if label isn't set?
	    builder = JSONBuilder.create().beginMap().
	    entry("type", "button").
        entry("disabled", pushButton.isDisabled()).
        entry("tabindex", pushButton.getTabindex()).
	    entry("label", yuiLabel).endMap().toString();


        JSONBuilder jBuild = JSONBuilder.create().
                                beginMap().
                entry("singleSubmit", pushButton.isSingleSubmit()).
                entry("ariaLabel", ariaLabel).
                entry("ariaEnabled", EnvUtils.isAriaEnabled(facesContext));

        if (uiParamChildren != null) {
            jBuild.entry("postParameters",  Utils.asStringArray(uiParamChildren) );
        }

	    String params = "'" + clientId + "'," +
        builder
           + "," + jBuild.endMap().toString();
 //         System.out.println("params = " + params);	    

        String finalScript = "ice.component.pushbutton.updateProperties(" + params + ");";
        ScriptWriter.insertScript(facesContext, uiComponent,finalScript);
            
        writer.endElement(HTML.DIV_ELEM);
    }
}
