package org.icefaces.component.radiobutton;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIOutput;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItemGroup;
import javax.faces.model.SelectItem;

import java.io.IOException;

@ResourceDependencies({
    @ResourceDependency(library = "yui/2_8_1", name = "yahoo-dom-event/yahoo-dom-event.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "element/element-min.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "button/button-min.js"),
	@ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),	
    @ResourceDependency(name="radiobutton.js",library="org.icefaces.component.radiobutton")    
})
public class ButtonGroup extends ButtonGroupBase {

	public void setValue(RadioButton radioButton, FacesContext facesContext){
        Object prevValue = this.getValue();
        String updatedId = radioButton.getClientId(facesContext);
        System.out.println("BUTTONGROUP: verions="+this+" \t Prev value = "+String.valueOf(prevValue));
        this.setSelectedItemId(updatedId);
        setValue(radioButton.getValue());
        if (radioButton.getValue()!=prevValue){
        	System.out.println("BG: queing event");
            this.queueEvent(new ValueChangeEvent (this, 
                   null, radioButton.getValue()));
        }
        /*
        if (prevValue instanceof RadioButton){
        	RadioButton rb = (RadioButton)prevValue;
            String prevId = rb.getClientId(facesContext);
            this.setSelectedItemId(updatedId);
            System.out.println("\t\t prevId="+prevId+" updatedId="+updatedId);
            if (!prevId.equals(updatedId)){
            	this.setValue(radioButton.getValue());
            	System.out.println(" \\ updated value need to fire change event");
            	//fire ValueChangeEvent
            	 this.queueEvent(new ValueChangeEvent (this, 
                         prevValue, radioButton.getValue()));
            }
        }
        */
    }
	
//	protected void setSelectedItemId(String id){
//		this.setSelectedItemId(id);
//	}
//	
//	protected String getSelectedItemId(){
//		return this.getSelectedItemId();
//	}

}
