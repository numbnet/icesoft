package org.icefaces.component.radiobutton;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.JSONBuilder;
import org.icefaces.component.utils.ScriptWriter;
import org.icefaces.util.EnvUtils;
import org.icefaces.render.MandatoryResourceComponent;


@MandatoryResourceComponent("org.icefaces.component.radiobutton.RadioButton")
public class RadioButtonRenderer extends Renderer {
	private final static Logger log = Logger.getLogger(RadioButtonRenderer.class.getName());
    
	public void decode(FacesContext facesContext, UIComponent uiComponent) {
		System.out.println("RBR");
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        if (requestParameterMap.containsKey("ice.event.captured")) {
            RadioButton radioButton = (RadioButton) uiComponent;
            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
            String clientId = uiComponent.getClientId();
          	System.out.println("radioButtonRenderer: clientId="+clientId+" source="+source);
            //update the checked value from parent:-
          	ButtonGroup bg = getGroup(facesContext, uiComponent, clientId);
          	
          	///what if bg is null?  Need to do something for this check
          	String bgClientId = bg.getClientId(facesContext);
          	System.out.println("bgClientId="+bgClientId);
    		String hiddenValue = String.valueOf(requestParameterMap.get(bgClientId+"_hidden"));
			System.out.println("\t\tRenderer:-  HIDDEN value ONLY="+hiddenValue);
			if (hiddenValue.equals(clientId)){
				System.out.println(" WHOOPEE ITS ME");
          	   radioButton.setChecked(true);
          	   bg.setValue(radioButton, facesContext);
			}
			else {
				radioButton.setChecked(false);
				System.out.println(clientId + " is not the one");
			}
   
//       /*     	Need to get the selectedItem passed and set it to the radioButton
//				SelectItem submittedValue = Boolean.valueOf((String.valueOf(requestParameterMap.get(clientId+"_value"))));
//				radioButton.setSelectedItem(); */
            
        }
    }

	@Override
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {

        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        RadioButton radioButton = (RadioButton) uiComponent;
        ButtonGroup bg = getGroup(facesContext, uiComponent, clientId);
		String bgid=bg.getClientId(facesContext);
		//check to see if this button is the one checked in case app changed it
		// if so, then update button group's selectedItemId 
		if (radioButton.isChecked()){
			System.out.println("RBR:encodeBegin() I'm checked! do I need to set the BG?");
			bg.setSelectedItemId(clientId);
		}else{// make sure I'm not in the hidden field
			System.out.println("RBR:encodeBegin() radioButton="+clientId+" not checked");
//     	    if (bg.getSelectedItemId() !=null && bg.getSelectedItemId().equals(clientId)){
//     	    	bg.setSelectedItemId(null);
//     	    }
     	}
        // root element div is with buttongroup class
        String label="none";
        if (null != radioButton.getLabel())label=radioButton.getLabel();
        else if (null != radioButton.getValue())label=String.valueOf(radioButton.getValue());
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null); 
        //see if this button is the one selected
    	System.out.println("RBR: encodeBegin for"+clientId+" checked="+radioButton.isChecked());
        if (radioButton.isChecked())
		   writer.writeAttribute(HTML.CLASS_ATTR, "yui-button yui-radio-button yui-button-checked", null);
        else{
           writer.writeAttribute(HTML.CLASS_ATTR, "yui-button yui-radio-button", null);
       }
		// first child		
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
		writer.writeAttribute(HTML.CLASS_ATTR, "first-child", null);
		//label
		writer.startElement(HTML.LABEL_ELEM, uiComponent);
		writer.writeAttribute(HTML.FOR_ATTR, clientId+"_button", null );
		writer.write(label);
		writer.endElement(HTML.LABEL_ELEM);
		// button element
		writer.startElement(HTML.BUTTON_ELEM, uiComponent);
		writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
		writer.writeAttribute(HTML.ID_ATTR, clientId+"_button", null);
		
		//yui sample markup for the buttongroup shows
		//name is same for all buttons in the group and no id is rendered
		writer.writeAttribute("name", radioButton.getGroup(), null);
		writer.writeAttribute(HTML.VALUE_ATTR, radioButton.getValue(), null);
	  	if (radioButton.isChecked()){
 //    		writer.writeAttribute(HTML.CHECKED_ATTR, HTML.CHECKED_ATTR, null);
	  		writer.write(HTML.CHECKED_ATTR);
     	}
		
		// if there's an image, render label manually, don't rely on YUI, since it'd override button's contents
		if (radioButton.getImage() != null) {
//			writer.startElement(HTML.SPAN_ELEM, uiComponent);
//			writer.write(radioButton.getLabel());
//			writer.endElement(HTML.SPAN_ELEM);
			
			writer.startElement(HTML.IMG_ELEM, uiComponent);
			writer.writeAttribute(HTML.SRC_ATTR, radioButton.getImage(), null);
			writer.endElement(HTML.IMG_ELEM);
		}
		
    }
    
    /*
     * not sure yet how detailed this will be with radiobuttons 
     * in datatable, but keeping it simple for now
     */
    private ButtonGroup getGroup(FacesContext facesContext,
			UIComponent uiComponent, String clientId) {
    	UIComponent parent = uiComponent.getParent();
    	RadioButton rb = (RadioButton)uiComponent;
    	if (parent instanceof ButtonGroup) {
    	 // need to make sure it's the correct buttongroup
    	 //   if (rb.getGroup().equals(anObject)
    		return (ButtonGroup)parent;
    	}
    	else//keep going up 
    	{
//    	   UIViewRoot root = facesContext.getViewRoot();
    	   UIComponent bgroup = findComponent(facesContext.getViewRoot(), rb.getGroup());
    	   if (null!=bgroup && bgroup instanceof ButtonGroup){
    		   return (ButtonGroup)bgroup;
    	   }else System.out.println("didn't find bgroup for id="+rb.getGroup()+" bgroup is class="+bgroup.getClass().getName());
    	}
		return null;
	}
//could augment this class to include the class that the parent should be??
    private UIComponent findComponent(UIComponent parent, String id){
    	if (id.equals(parent.getId())){
    		return parent;
    	}
    	Iterator<UIComponent> children = parent.getFacetsAndChildren();
    	while(children.hasNext()){
    		UIComponent child = (UIComponent)children.next();
    		UIComponent uicomp = findComponent(child, id);
    		if (uicomp!=null){
    			System.out.println("found buttongroup id="+id);
    			return uicomp;
    		}
    	}
    	return null;
    }
    

    @Override
	public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
		String clientId = uiComponent.getClientId(facesContext);
		System.out.println("RB:encodeEnd() for clientId="+clientId);
		RadioButton radioButton = (RadioButton) uiComponent;
	    writer.endElement(HTML.BUTTON_ELEM);
		writer.endElement(HTML.SPAN_ELEM); 
		String groupId = radioButton.getGroup();
		// js call
		String params = "'" + clientId + "'," +
	           JSONBuilder.create().
	           beginMap().
	               entry("name", groupId).
	           endMap().toString() 
	           + "," +
	           JSONBuilder.create().
	           beginMap().
	               entry("disabled", radioButton.isDisabled()).
	               entry("tabindex", radioButton.getTabindex()).
	               entry("singleSubmit", radioButton.isSingleSubmit()).
	               entry("checked", radioButton.isChecked()).
	               entry("ariaEnabled", EnvUtils.isAriaEnabled(facesContext)).
	           endMap().toString();
//	          System.out.println("params = " + params);	    

	        String finalScript = "ice.component.radio.updateProperties(" + params + ");";
	        ScriptWriter.insertScript(facesContext, uiComponent, finalScript);		    		
         writer.endElement(HTML.SPAN_ELEM);
    }
}
