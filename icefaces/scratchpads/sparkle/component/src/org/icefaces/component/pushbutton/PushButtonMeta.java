  package org.icefaces.component.pushbutton;

import javax.faces.component.UIComponent;
import javax.el.MethodExpression;
import javax.faces.event.ActionListener;
import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;
import java.util.List;

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
