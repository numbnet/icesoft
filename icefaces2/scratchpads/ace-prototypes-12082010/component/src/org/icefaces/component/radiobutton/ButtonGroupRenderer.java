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
import org.icefaces.component.utils.JSONBuilder;
import org.icefaces.component.utils.ScriptWriter;
import org.icefaces.util.EnvUtils;
import org.icefaces.render.MandatoryResourceComponent;


@MandatoryResourceComponent("org.icefaces.component.radiobutton.ButtonGroup")
public class ButtonGroupRenderer extends Renderer {
	private final static Logger log = Logger.getLogger(ButtonGroupRenderer.class.getName());
    
	public void decode(FacesContext facesContext, UIComponent uiComponent) {
		System.out.println("BGR: in decode()");
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.containsKey("ice.event.captured")) {
            ButtonGroup buttonGroup = (ButtonGroup) uiComponent;
            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
            String clientId = uiComponent.getClientId();
            System.out.println("\t\t source="+source+" clientId="+clientId);
            //may not always be submitting itself so lose next line
//            if (clientId.equals(source)) {
//			String hiddenValue = String.valueOf(requestParameterMap.get(clientId+"_hidden"));
//			System.out.println("\t\tRenderer:-  HIDDEN value ONLY="+hiddenValue);
//			//find radio button this corresponds to
//			setSubmittedValue(uiComponent, hiddenValue);
//			buttonGroup.setSelectedItemId(hiddenValue);   
        }
    }

	@Override
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
    	System.out.println("BGR: encodeBegin");
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        ButtonGroup buttonGroup = (ButtonGroup) uiComponent;
		// root element
        writer.startElement(HTML.DIV_ELEM, uiComponent);        
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_div", null);
        writer.writeAttribute(HTML.CLASS_ATTR,"yui-buttongroup", null);  
		//if using sam-skin, then should use class yui-buttongroup
//        //make sure submittedValue is set
//        String selectedvalue="none";
//        if (null != buttonGroup.getSelectedItemId()){ //should this be getConvertedValue()????
//            selectedvalue =String.valueOf(buttonGroup.getSelectedItemId());
//            System.out.println("selectedvalue="+String.valueOf(selectedvalue));
//        }
//        else {
//        	//set selectedvalue to  first value in list
//		    List children = uiComponent.getChildren();
//		    Iterator iter = children.iterator();
// 		    while(iter.hasNext()){
//		    	UIComponent rb = (UIComponent)iter.next();	
// 		    	if (rb instanceof RadioButton){
// 		    		selectedvalue=rb.getClientId();
// 		    		System.out.println("set rbr selectedvalue to first item in list id="+selectedvalue);
// 		    		return;
// 		    	}
// 		    }
// 		    buttonGroup.setSelectedItemId(selectedvalue);
//        }
        
    }
    
	@Override
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
		String clientId = uiComponent.getClientId(facesContext);
        ButtonGroup buttonGroup = (ButtonGroup) uiComponent;
        System.out.println(" BGR encodeEnd version of buttonGroup="+buttonGroup+" id="+clientId);
        writer.endElement(HTML.SPAN_ELEM);
			//hidden input for single submit=false
        String selectedvalue = String.valueOf(buttonGroup.getSelectedItemId());
        System.out.println("BGR EncodeEnd: *****selectedValue="+selectedvalue);
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
		writer.writeAttribute(HTML.ID_ATTR, clientId+"_hs",null);
		writer.startElement("input", uiComponent);
		writer.writeAttribute("type", "hidden", null);
	    writer.writeAttribute("name",clientId+"_hidden", null);
		writer.writeAttribute("id",clientId+"_hidden", null);
		writer.writeAttribute("value", selectedvalue, null);
		writer.endElement("input");
		writer.endElement(HTML.DIV_ELEM);
		//can't have null for any parameters in the JSONBuilder class
        if (null==selectedvalue)selectedvalue="none";
		// js call
	    String params = "'" + clientId + "'," +
           JSONBuilder.create().
           beginMap().
               entry("name", clientId).
           endMap().toString() 
           + "," +
           JSONBuilder.create().
           beginMap().
               entry("disabled", buttonGroup.isDisabled()).
               entry("tabindex", buttonGroup.getTabindex()).
               entry("singleSubmit", buttonGroup.isSingleSubmit()).
               entry("selectedItemId", selectedvalue).
               entry("ariaEnabled", EnvUtils.isAriaEnabled(facesContext)).
           endMap().toString();
//          System.out.println("params = " + params);	    

        String finalScript = "ice.component.radiobutton.updateProperties(" + params + ");";
        ScriptWriter.insertScript(facesContext, uiComponent, finalScript);
	 	writer.endElement(HTML.DIV_ELEM);
    }
}
