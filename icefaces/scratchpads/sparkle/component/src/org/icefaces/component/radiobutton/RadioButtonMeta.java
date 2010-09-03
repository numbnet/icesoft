  package org.icefaces.component.radiobutton;

import javax.faces.component.UIComponent;

import org.icefaces.component.annotation.*;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.component.baseMeta.UISelectOneMeta;

@Component(
        tagName ="radioButton",
        componentClass ="org.icefaces.component.radiobutton.RadioButton",
        rendererClass ="org.icefaces.component.radiobutton.RadioButtonRenderer", 
        componentType = "org.icefaces.RadioButton", 
        rendererType = "org.icefaces.RadioButtonRenderer",            
        extendsClass = "javax.faces.component.UISelectOne", 
        generatedClass = "org.icefaces.component.radiobutton.RadioButtonBase",
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

public class RadioButtonMeta extends UISelectOneMeta {
    
    @Property   
    private String label;
    
    @Property
    private String name;
    
    @Property(tlddoc="require groupId that this button belongs to")
    private String group;

    @Property (inherit=Inherit.SUPERCLASS_PROPERTY)
    private String id;
	
	@Property
    private String image;
    
    @Property  (defaultValue="false")
    private Boolean checked;
    
    @Property (inherit=Inherit.SUPERCLASS_PROPERTY)
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
	    
	@Property (inherit=Inherit.SUPERCLASS_PROPERTY)
    private Boolean rendered;
	    
	@Property (defaultValue="false",
			tlddoc="disabled property is required by aria specs")
	private Boolean disabled;
}
