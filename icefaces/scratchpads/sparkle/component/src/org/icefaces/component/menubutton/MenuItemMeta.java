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
	    @Property  
	    private String label;
	    
	    @Property   
	    private boolean disabled;    
}



 