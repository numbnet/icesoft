  package org.icefaces.component.pushbutton;

import javax.faces.component.UIComponent;
import javax.el.MethodExpression;
import javax.faces.event.ActionListener;
import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;
import java.util.List;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName ="pushbutton",
        componentClass ="org.icefaces.component.pushbutton.PushButton",
        rendererClass ="org.icefaces.component.pushbutton.PushButtonRenderer", 
        componentType = "org.icefaces.PushButton", 
        rendererType = "org.icefaces.PushButtonRenderer",            
        extendsClass = "javax.faces.component.UICommand", 
        generatedClass = "org.icefaces.component.pushbutton.PushButtonBase",
		componentFamily="com.icesoft.faces.PushButton"
        )
		
@ResourceDependencies({
    @ResourceDependency(name = "sam/button/button.css", library = "org.icefaces.component.sprites"),
    @ResourceDependency(library = "yui/2_8_1", name = "yahoo-dom-event/yahoo-dom-event.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "element/element-min.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "button/button-min.js"),
	@ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),	
    @ResourceDependency(name="pushbutton.js",library="org.icefaces.component.pushbutton")
})
        
public class PushButtonMeta {
    
    @Property   
    private String label;

    @Property (inherit=true, useTemplate=true)
    private String id;
	
	@Property
    private String image;
	
	@Property(defaultValue="false",
			tlddoc="Default is false, means uses full submit")
    private Boolean singleSubmit;
	
    @Property (inherit=true, useTemplate=true)
    private UIComponent binding;  
    
    @Property (defaultValue="false")
    private Boolean disabled;
    
    @Property (inherit=true, defaultValue="true")
    private Boolean rendered;
    
    @Property (defaultValue="0", tlddoc="tabindex of the component")
    private Integer tabindex;  
  
    @Property(tlddoc="style class of the component, the renderer doesn't render any default class.")
    private String styleClass;  

    @Property(tlddoc="style of the component")
    private String style;
    
    @Property(inherit=true, isMethodExpression=true)
    private MethodExpression actionListener;
 
    @Property(isMethodExpression=true, inherit=true	)
    private MethodExpression action;
    
    @Property(defaultValue="false",inherit=true, useTemplate=true)
    private Boolean immediate;
}
