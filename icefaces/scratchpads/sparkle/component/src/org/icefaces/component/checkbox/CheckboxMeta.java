package org.icefaces.component.checkbox;


import javax.faces.component.UIComponent;
import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;



@Component(
        tagName ="checkbox",
        componentClass ="org.icefaces.component.checkbox.Checkbox",
        rendererClass ="org.icefaces.component.checkbox.CheckboxRenderer", 
        componentType = "org.icefaces.Checkbox", 
        rendererType = "org.icefaces.CheckboxRenderer",            
        extendsClass = "javax.faces.component.UISelectBoolean", 
        generatedClass = "org.icefaces.component.checkbox.CheckboxBase",
		componentFamily="com.icesoft.faces.Checkbox"
        )
        

public class CheckboxMeta {
    
    @Property   
    private String label;

    @Property (inherit=true, useTemplate=true)
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
    
    @Property (inherit=true, useTemplate=true)
    private Boolean rendered;
    
    @Property (defaultValue="false",
    		tlddoc="disabled property is required by aria specs")
    private Boolean disabled;
    
    @Property (inherit=true, useTemplate=true)
    private UIComponent binding;    

}
