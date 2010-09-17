package org.icefaces.component.checkbox;


import javax.faces.component.UIComponent;
import org.icefaces.component.annotation.*;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.component.baseMeta.UISelectBooleanMeta;

@Component(
        tagName ="checkboxButton",
        componentClass ="org.icefaces.component.checkbox.Checkbox",
        rendererClass ="org.icefaces.component.checkbox.CheckboxRenderer", 
        componentType = "org.icefaces.Checkbox", 
        rendererType = "org.icefaces.CheckboxRenderer",            
        extendsClass = "javax.faces.component.UISelectBoolean", 
        generatedClass = "org.icefaces.component.checkbox.CheckboxBase",
		componentFamily="com.icesoft.faces.Checkbox"
        )
        
@ResourceDependencies({

	    @ResourceDependency(name = "sam/button/button.css", library = "org.icefaces.component.sprites"),
	    @ResourceDependency(library = "yui/2_8_1", name = "yahoo-dom-event/yahoo-dom-event.js"),
	    @ResourceDependency(library = "yui/2_8_1", name = "element/element-min.js"),
	    @ResourceDependency(library = "yui/2_8_1", name = "button/button-min.js"),
		@ResourceDependency(name="util.js",library="org.icefaces.component.util"),
	    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),	
        @ResourceDependency(name="checkbox.js",library="org.icefaces.component.checkbox")    

})

public class CheckboxMeta extends UISelectBooleanMeta {
    
    @Property   
    private String label;

    @Property (implementation=Implementation.EXISTS_IN_SUPERCLASS)
    private String id;
	
	@Property
    private String image;
	
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
    
    @Property (implementation=Implementation.EXISTS_IN_SUPERCLASS)
    private Boolean rendered;
    
    @Property (defaultValue="false",
    		tlddoc="disabled property is required by aria specs")
    private Boolean disabled;
    
    @Property (implementation=Implementation.EXISTS_IN_SUPERCLASS)
    private UIComponent binding;    

}
