package org.icefaces.component.checkbox;



import java.io.IOException;
import java.util.*;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.JSONBuilder;
import org.icefaces.component.utils.ScriptWriter;
import org.icefaces.util.EnvUtils;

public class CheckboxRenderer extends Renderer {

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.containsKey("ice.event.captured")) {
            Checkbox checkbox = (Checkbox) uiComponent;
            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
            String clientId = uiComponent.getClientId();
            //as of code review June 16th, just use the hidden field for update in every instance
  /*          if (clientId.equals(source)) {
				Boolean submittedValue = Boolean.valueOf((String.valueOf(requestParameterMap.get(clientId+"_span"+"_value"))));
				String hiddenValue = String.valueOf(requestParameterMap.get(clientId+"_hidden"));
				System.out.println("\t\tRenderer:- submittedValue="+submittedValue+" HIDDEN value="+hiddenValue);
				checkbox.setSubmittedValue(submittedValue);
            }else{ */
            	System.out.println("clientId="+clientId+" not same as source="+source);
            	//update with hidden field
				String hiddenValue = String.valueOf(requestParameterMap.get(clientId+"_hidden"));
				System.out.println("\t\tRenderer:-  HIDDEN value ONLY="+hiddenValue);
				Boolean submittedValue= Boolean.valueOf(hiddenValue);
				checkbox.setSubmittedValue(submittedValue);           	
          //  }
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Checkbox checkbox = (Checkbox) uiComponent;

		// root element
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_span", null);      

		writer.writeAttribute(HTML.CLASS_ATTR, "yui-button yui-checkbox-button", null);
		
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
		//hidden input for single submit=false
		
	    writer.startElement("input", uiComponent);
	    writer.writeAttribute("type", "hidden", null);
	    writer.writeAttribute("name",clientId+"_hidden", null);
	    writer.writeAttribute("id",clientId+"_hidden", null);
	    writer.writeAttribute("value",checkbox.getValue(), null);
	    writer.endElement("input");	
     
		// js call using JSONBuilder utility ICE-5831 and ScriptWriter ICE-5830
	    //note that ScriptWriter takes care of the span tag surrounding the script
	    String label = "";
	    String ifImage = "";
	    if (null!=checkbox.getLabel() || !checkbox.getLabel().equals(""))label=checkbox.getLabel();
	   // need to worry if label isn't set?
	    String boxValue = String.valueOf(checkbox.getValue());
	    boolean isChecked = false;
	    if (boxValue.equals("true") || boxValue.equals("on") || boxValue.equals("yes"))isChecked=true;
	    boolean needLabel = (checkbox.getImage()==null);
	    JSONBuilder.create().beginMap().entry("nothing", label).toString();
	    if (needLabel){
	        ifImage = JSONBuilder.create().beginMap().
	        entry("type", "checkbox").
	        entry("checked", isChecked).
	        entry("label", label).endMap().toString();
	    }
	    else {
	        ifImage = JSONBuilder.create().beginMap().
	        entry("type", "checkbox").
	        entry("checked", isChecked).endMap().toString();  
	    }
	    String params = "'" + clientId + "'," +
        ifImage
           + "," +
           JSONBuilder.create().
           beginMap().
               entry("disabled", checkbox.isDisabled()).
               entry("tabindex", checkbox.getTabindex()).
               entry("singleSubmit", checkbox.isSingleSubmit()).
               entry("ariaEnabled", EnvUtils.isAriaEnabled(facesContext)).
           endMap().toString();
          System.out.println("params = " + params);	    

        String finalScript = "ice.component.checkbox.updateProperties(" + params + ");";
        ScriptWriter.insertScript(facesContext, uiComponent,finalScript);        		  
        
       writer.endElement(HTML.DIV_ELEM);
    }
}
