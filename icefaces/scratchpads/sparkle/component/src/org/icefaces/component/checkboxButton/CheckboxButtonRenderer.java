package org.icefaces.component.checkboxButton;



import java.io.IOException;
import java.util.*;


import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.faces.render.Renderer;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.JSONBuilder;
import org.icefaces.component.utils.ScriptWriter;

import org.icefaces.util.EnvUtils;

public class CheckboxButtonRenderer extends Renderer {

	public CheckboxButtonRenderer(){
		super();
	}

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
	        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
	        CheckboxButton checkbox = (CheckboxButton) uiComponent;
	        String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
	        String clientId = uiComponent.getClientId();
	        //update with hidden field
		    String hiddenValue = String.valueOf(requestParameterMap.get(clientId+"_hidden"));
		//	System.out.println("\t\tRenderer:-  HIDDEN value ONLY="+hiddenValue);
			boolean submittedValue = isChecked(hiddenValue);
			checkbox.setSubmittedValue(submittedValue);           	
    }


	public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        CheckboxButton checkbox = (CheckboxButton) uiComponent;

		// root element
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_span", null);      

		writer.writeAttribute(HTML.CLASS_ATTR, "yui-button yui-checkboxButton-button", null);
		
		// first child
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
		writer.writeAttribute(HTML.CLASS_ATTR, "first-child", null);
	 	writer.writeAttribute(HTML.ID_ATTR, clientId+"_s2", null);
	 	//labelling  should be either a label or an image

		String label=this.findCheckboxLabel(checkbox);
		String labelPosition = this.findCheckboxLabelPosition(checkbox);
			
			// should be either a label or an image as a minimum
		if (labelPosition.equals("left")){
			writer.startElement(HTML.LABEL_ELEM, uiComponent);
    		writer.writeAttribute(HTML.FOR_ATTR, clientId+"_button", null );
            writer.writeText(label,null);
	   		writer.endElement("label");		
		}
	
		System.out.println(" CBR: label="+label);	 	
	 	
		// button element
		writer.startElement(HTML.BUTTON_ELEM, uiComponent);
		writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
		writer.writeAttribute(HTML.NAME_ATTR, clientId+"_button", null);
		writer.writeAttribute(HTML.ID_ATTR, clientId+"_button", null);
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
		String clientId = uiComponent.getClientId(facesContext);
		CheckboxButton checkbox = (CheckboxButton) uiComponent;
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
	    boolean isChecked = this.isChecked(boxValue);
        String builder="";
	    JSONBuilder.create().beginMap().entry("nothing", label).toString();
	    if (labelPosition.equals("on")){
	        builder = JSONBuilder.create().beginMap().
	        entry("type", "checkbox").
	        entry("checked", isChecked).
            entry("disabled", checkbox.isDisabled()).
            entry("tabindex", checkbox.getTabindex()).
	        entry("label", label).endMap().toString();
	    }
	    else {
	    	System.out.println("LABEL NOT ON");
	        builder = JSONBuilder.create().beginMap().
	        entry("type", "checkbox").
	        entry("checked", isChecked).
            entry("disabled", checkbox.isDisabled()).
            entry("tabindex", checkbox.getTabindex()).
	        entry("label", "").endMap().toString();  
	    }
	    String params = "'" + clientId + "'," +
        builder
           + "," +
           JSONBuilder.create().
           beginMap().
               entry("singleSubmit", checkbox.isSingleSubmit()).
               entry("ariaEnabled", EnvUtils.isAriaEnabled(facesContext)).
           endMap().toString();
  //        System.out.println("params = " + params);	    

        String finalScript = "ice.component.checkboxButton.updateProperties(" + params + ");";
        ScriptWriter.insertScript(facesContext, uiComponent,finalScript);        		  
        
       writer.endElement(HTML.DIV_ELEM);
    }
    
    private String findCheckboxLabel(CheckboxButton checkbox){
    	String label="";
        String checkLabel = checkbox.getLabel();
		if (null!=checkLabel && !checkLabel.equals("")){
			label=checkLabel;
		}
		return label;
    }
    private String findCheckboxLabelPosition(CheckboxButton checkbox){
    	String labelPosition="";
        String checkLabelPos = checkbox.getLabelPosition();
		if (null!=checkLabelPos && !checkLabelPos.equals("")){
			labelPosition=checkLabelPos.trim().toLowerCase();
		}
		return labelPosition;
    }
 
    /**
     * support similar return values as jsf component
     * so can use strings true/false, on/off, yes/no to
     * support older browsers
     * @param hiddenValue
     * @return
     */
    private boolean isChecked(String hiddenValue) {
		return hiddenValue.equalsIgnoreCase("on") ||
		       hiddenValue.equalsIgnoreCase("yes") ||
		       hiddenValue.equalsIgnoreCase("true");
	}
    
    //forced converter support. It's either a boolean or string.   
    @Override
    public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent,
    		                        Object submittedValue) throws ConverterException{
    	if (submittedValue instanceof Boolean)return submittedValue;
    	else return Boolean.valueOf(submittedValue.toString());
    }
    
}
