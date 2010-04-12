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
    
    @Property (inherit=true, useTemplate=true)
    private Boolean rendered;
    
    @Property (inherit=true, useTemplate=true)
    private UIComponent binding;    

}
