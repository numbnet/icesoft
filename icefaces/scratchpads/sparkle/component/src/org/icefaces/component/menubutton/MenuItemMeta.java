package org.icefaces.component.menubutton;

import javax.faces.component.UIComponent;
import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Properties;
import org.icefaces.component.annotation.Property;

import javax.el.MethodExpression;

@Component(tagName ="menuitem",
        componentClass ="org.icefaces.component.menubutton.MenuItem",
        rendererClass ="org.icefaces.component.menubutton.MenuItemRenderer", 
        componentType = "org.icefaces.MenuItem",
        extendsClass = "javax.faces.component.UICommand", 
        rendererType = "org.icefaces.MenuItemRenderer", 
        generatedClass = "org.icefaces.component.menubutton.MenuItemBase",
        componentFamily="com.icesoft.faces.MenuButton"
        )
public class MenuItemMeta {
        @Property(inherit=true)   
        private String id;
        
	    @Property  
	    private String label;
	    
	    @Property   
	    private Object value;
	    @Property   
	    private Boolean disabled;    

	    @Property(inherit=true)   
	    private Boolean rendered;
	    
	    @Property(inherit=true, isMethodExpression=true)
	    private MethodExpression actionListener;
	 
	    @Property(isMethodExpression=true, inherit=true	)
	    private MethodExpression action;
	    
	    @Property(defaultValue="false",inherit=true, useTemplate=true)
	    private Boolean immediate;
}



 