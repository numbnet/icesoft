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
        @Property(implementation=Implementation.EXISTS_IN_SUPERCLASS)   
        private String id;
        
	    @Property  
	    private String label;
	    
	    @Property   
	    private Object value;
	    @Property   
	    private Boolean disabled;    

	    @Property(implementation=Implementation.EXISTS_IN_SUPERCLASS)   
	    private Boolean rendered;
	    
	    @Property(implementation=Implementation.EXISTS_IN_SUPERCLASS, expression=Expression.METHOD_EXPRESSION)
	    private MethodExpression actionListener;
	 
	    @Property(expression=Expression.METHOD_EXPRESSION, implementation=Implementation.EXISTS_IN_SUPERCLASS	)
	    private MethodExpression action;
	    
	    @Property(defaultValue="false",implementation=Implementation.EXISTS_IN_SUPERCLASS)
	    private Boolean immediate;
}



 