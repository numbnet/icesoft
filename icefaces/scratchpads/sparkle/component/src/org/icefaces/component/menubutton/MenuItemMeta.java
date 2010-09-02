package org.icefaces.component.menubutton;

import javax.faces.component.UIComponent;
import org.icefaces.component.annotation.*;

import javax.el.MethodExpression;

import org.icefaces.component.baseMeta.UICommandMeta;

@Component(tagName ="menuitem",
        componentClass ="org.icefaces.component.menubutton.MenuItem",
        rendererClass ="org.icefaces.component.menubutton.MenuItemRenderer", 
        componentType = "org.icefaces.MenuItem",
        extendsClass = "javax.faces.component.UICommand", 
        rendererType = "org.icefaces.MenuItemRenderer", 
        generatedClass = "org.icefaces.component.menubutton.MenuItemBase",
        componentFamily="com.icesoft.faces.MenuButton"
        )
public class MenuItemMeta extends UICommandMeta {
        @Property(inherit=Inherit.SUPERCLASS_PROPERTY)   
        private String id;
        
	    @Property  
	    private String label;
	    
	    @Property   
	    private Object value;
	    @Property   
	    private Boolean disabled;    

	    @Property(inherit=Inherit.SUPERCLASS_PROPERTY)   
	    private Boolean rendered;
	    
	    @Property(inherit=Inherit.SUPERCLASS_PROPERTY, isMethodExpression=Expression.METHOD_EXPRESSION)
	    private MethodExpression actionListener;
	 
	    @Property(isMethodExpression=Expression.METHOD_EXPRESSION, inherit=Inherit.SUPERCLASS_PROPERTY	)
	    private MethodExpression action;
	    
	    @Property(defaultValue="false",inherit=Inherit.SUPERCLASS_PROPERTY, useTemplate=true)
	    private Boolean immediate;
}



 