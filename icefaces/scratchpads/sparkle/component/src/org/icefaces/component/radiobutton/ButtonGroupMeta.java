  package org.icefaces.component.radiobutton;

import javax.faces.component.UIComponent;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

import org.icefaces.component.annotation.Field;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName ="radioButtonGroup",
        componentClass ="org.icefaces.component.radiobutton.ButtonGroup",
        rendererClass ="org.icefaces.component.radiobutton.ButtonGroupRenderer", 
        componentType = "org.icefaces.ButtonGroup", 
        rendererType = "org.icefaces.ButtonGroupnRenderer",            
        extendsClass = "javax.faces.component.UISelectOne", 
        generatedClass = "org.icefaces.component.radiobutton.ButtonGroupBase",
		componentFamily="com.icesoft.faces.RadioButton"
        )
        
@ResourceDependencies({
    @ResourceDependency(library = "yui/2_8_1", name = "yahoo-dom-event/yahoo-dom-event.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "element/element-min.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "button/button-min.js"),
	@ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),	
    @ResourceDependency(name="radiobutton.js",library="org.icefaces.component.radiobutton")    
})

public class ButtonGroupMeta {
    
    @Property   
    private String label;

    @Property (inherit=true, useTemplate=true)
    private String id;
    
    //not sure this is needed as the string id of the 
    //selected radio button in the group is stored
    //in the value property.
    @Property
    protected String selectedItemId;
    
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
