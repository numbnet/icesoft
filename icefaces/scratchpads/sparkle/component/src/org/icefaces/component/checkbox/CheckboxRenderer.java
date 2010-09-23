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
	 	//labelling  should be either a label or an image

		String image=this.findCheckboxImage(checkbox);
		String label=this.findCheckboxLabel(checkbox);
		String labelPosition = this.findCheckboxLabelPosition(checkbox);
			
			// should be either a label or an image as a minimum
		if (labelPosition.equals("left")&& image.equals("")){
			writer.startElement(HTML.LABEL_ELEM, uiComponent);
    		writer.writeAttribute(HTML.FOR_ATTR, clientId+"_button", null );	    		writer.write(label);
	   		writer.endElement("label");		
		}
	
		System.out.println(" CBR: label="+label);	 	
	 	
		// button element
		writer.startElement(HTML.BUTTON_ELEM, uiComponent);
		writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
		writer.writeAttribute(HTML.NAME_ATTR, clientId+"_button", null);
		writer.writeAttribute(HTML.ID_ATTR, clientId+"_button", null);

		if (!image.equals("")){
			writer.startElement(HTML.SPAN_ELEM, uiComponent);
			writer.write(checkbox.getLabel());
			writer.endElement(HTML.SPAN_ELEM);		
			writer.startElement(HTML.IMG_ELEM, uiComponent);
			writer.writeAttribute(HTML.SRC_ATTR, image, null);
			writer.endElement(HTML.IMG_ELEM);
		}

    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
		String clientId = uiComponent.getClientId(facesContext);
		Checkbox checkbox = (Checkbox) uiComponent;
		String image=this.findCheckboxImage(checkbox);
		String label=this.findCheckboxLabel(checkbox);
		String labelPosition = this.findCheckboxLabelPosition(checkbox);
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
	    String boxValue = String.valueOf(checkbox.getValue());
	    boolean isChecked = false;
	    String builder="";
	    if (boxValue.equals("true") || boxValue.equals("on") || boxValue.equals("yes"))isChecked=true;
	    JSONBuilder.create().beginMap().entry("nothing", label).toString();
	    if (labelPosition.equals("on")){
	    	System.out.println("LABEL ON");
	        builder = JSONBuilder.create().beginMap().
	        entry("type", "checkbox").
	        entry("checked", isChecked).
	        entry("label", label).endMap().toString();
	    }
	    else {
	    	System.out.println("LABEL NOT ON");
	        builder = JSONBuilder.create().beginMap().
	        entry("type", "checkbox").
	        entry("checked", isChecked).
	        entry("label", "").endMap().toString();  
	    }
	    String params = "'" + clientId + "'," +
        builder
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
    
    private String findCheckboxLabel(Checkbox checkbox){
    	String label="";
		if (null!=checkbox.getLabel() && !checkbox.getLabel().equals("")){
			label=checkbox.getLabel();
		}
		return label;
    }
    private String findCheckboxLabelPosition(Checkbox checkbox){
    	String labelPosition="";
		if (null!=checkbox.getLabelPosition() && !checkbox.getLabelPosition().equals("")){
			labelPosition=checkbox.getLabelPosition().trim().toLowerCase();
		}
		return labelPosition;
    }
    private String findCheckboxImage(Checkbox checkbox){
    	String image="";
		if (null!=checkbox.getImage() && !checkbox.getImage().equals("")){
			image=checkbox.getImage();
		}
		return image;
    }
    
}
