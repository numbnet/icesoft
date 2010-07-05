  package org.icefaces.component.radiobutton;

import javax.faces.component.UIComponent;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;


@Component(
        tagName ="buttongroup",
        componentClass ="org.icefaces.component.radiobutton.ButtonGroup",
        rendererClass ="org.icefaces.component.radiobutton.ButtonGroupRenderer", 
        componentType = "org.icefaces.ButtonGroup", 
        rendererType = "org.icefaces.ButtonGroupnRenderer",            
        extendsClass = "javax.faces.component.UISelectOne", 
        generatedClass = "org.icefaces.component.radiobutton.ButtonGroupBase",
		componentFamily="com.icesoft.faces.RadioButton"
        )
        
public class ButtonGroupMeta {
    
    @Property   
    private String label;

    @Property (inherit=true, useTemplate=true)
    private String id;
    
    @Property
    private String selectedRadioId;
    
    @Property (inherit=true, useTemplate=true)
    private UIComponent binding;  
	@Property(defaultValue="false",
			tlddoc="Default is false, means uses full submit")
    private Boolean singleSubmit;
	  
	@Property(tlddoc="style of the component")
	private String style;
		    
    @Property(tlddoc="style class of the component, the renderer doesn't render any default class.")		
    private String styleClass;     
	 
	@Property (defaultValue="0", tlddoc="tabindex of the component")
    private Integer tabindex;  
	    
	@Property(defaultValue="false")
    private Boolean immediate;
	    
	@Property (inherit=true, useTemplate=true)
    private Boolean rendered;
	    
	@Property (defaultValue="false",
			tlddoc="disabled property is required by aria specs")
	private Boolean disabled;
}
