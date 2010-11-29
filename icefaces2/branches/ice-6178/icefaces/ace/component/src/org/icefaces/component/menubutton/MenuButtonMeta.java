  package org.icefaces.component.menubutton;

import javax.faces.component.UIComponent;

import org.icefaces.component.annotation.*;

import javax.el.MethodExpression;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.component.baseMeta.UISelectOneMeta;

@Component(
        tagName ="menubutton",
        componentClass ="org.icefaces.component.menubutton.MenuButton",
        rendererClass ="org.icefaces.component.menubutton.MenuButtonRenderer", 
        componentType = "org.icefaces.MenuButton", 
        rendererType = "org.icefaces.MenuButtonRenderer",            
        extendsClass = "javax.faces.component.UISelectOne", 
        generatedClass = "org.icefaces.component.menubutton.MenuButtonBase",
		componentFamily="com.icesoft.faces.MenuButton"
        )
        
@ResourceDependencies({
    @ResourceDependency(name="yui/yui-min.js",library="yui/3_2_0"),
    @ResourceDependency(library = "yui/2_8_1", name="yuiloader/yuiloader-min.js"),
    @ResourceDependency(library = "yui/2_8_1", name="dom/dom-min.js"),   
    @ResourceDependency(library = "yui/2_8_1", name = "event/event-min.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "container/container_core-min.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "menu/menu-min.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "element/element-min.js"),   
    @ResourceDependency(library = "yui/2_8_1", name = "button/button-min.js"),
	@ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),	
    @ResourceDependency(name="menubutton.js",library="org.icefaces.component.menubutton"),
	@ResourceDependency(name="menubutton.css",library="org.icefaces.component.menubutton")
})

public class MenuButtonMeta extends UISelectOneMeta {  
    
    @Property   
    private String label;

	@Property
    private String image;
    
    @Property (defaultValue="false",
    		tlddoc="if overlay is true can use menuitems with html markup")
    private boolean overlay;
   
//    @Property (inherit=true)
//    private String value;
    
    @Property (tlddoc="Need a mnu name to bind the yui widget to the markup")
    private String menuName;
    
//    @Property (tlddoc="need to know how many menuitems in the menu")
//    private int numMenuItems;
    
    @Property (tlddoc="basic selection is within this string indicator")
    private String selectedMenuItem;
    
//    @Property (tlddoc="use this to create a group of buttons")
//    private SelectItemGroup selectItemGroup;
//    
//    @Property (tlddoc="the selected Split button")
//    private SelectItem selectedItem;
    @Property (defaultValue="false",
    		tlddoc="disabled property is required by aria specs")
    private boolean disabled;
    
    @Property (defaultValue="0", tlddoc="tabindex of the component")
    private int tabindex;  
    
	@Property(defaultValue="false",
			tlddoc="Default is false, means uses full submit")
    private boolean singleSubmit;
	
    @Property(expression=Expression.METHOD_EXPRESSION, methodExpressionArgument="javax.faces.event.ValueChangeEvent",
            tlddoc="on menu item change value change event can be captured with this listener")
    private MethodExpression menuSelectionListener;
}
