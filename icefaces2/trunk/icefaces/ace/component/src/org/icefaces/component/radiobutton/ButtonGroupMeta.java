  package org.icefaces.component.radiobutton;

import javax.faces.component.UIComponent;

import org.icefaces.component.annotation.*;

import org.icefaces.component.annotation.Field;
import javax.faces.model.SelectItem;
import javax.faces.model.SelectItemGroup;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.component.baseMeta.UISelectOneMeta;

@Component(
        tagName ="radioButtonGroup",
        componentClass ="org.icefaces.component.radiobutton.ButtonGroup",
        rendererClass ="org.icefaces.component.radiobutton.ButtonGroupRenderer", 
        componentType = "org.icefaces.ButtonGroup", 
        rendererType = "org.icefaces.ButtonGroupnRenderer",            
        extendsClass = "javax.faces.component.UISelectOne", 
        generatedClass = "org.icefaces.component.radiobutton.ButtonGroupBase",
		componentFamily="com.icesoft.faces.RadioButton"
        )
        
@ResourceDependencies({
    @ResourceDependency(name="yui/yui-min.js",library="yui/3_2_0"),
    @ResourceDependency(library = "yui/2_8_1", name = "yahoo-dom-event/yahoo-dom-event.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "element/element-min.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "button/button-min.js"),
	@ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),	
    @ResourceDependency(name="radiobutton.js",library="org.icefaces.component.radiobutton"),
	@ResourceDependency(name="radiobutton.css",library="org.icefaces.component.radiobutton")
})

public class ButtonGroupMeta extends UISelectOneMeta {
    
    @Property   
    private String label;

    //not sure this is needed as the string id of the 
    //selected radio button in the group is stored
    //in the value property.
    @Property
    protected String selectedItemId;
    
	@Property(defaultValue="false",
			tlddoc="Default is false, means uses full submit")
    private boolean singleSubmit;
	  
	@Property(tlddoc="style of the component")
	private String style;
		    
    @Property(tlddoc="style class of the component, the renderer doesn't render any default class.")		
    private String styleClass;     
	 
	@Property (defaultValue="0", tlddoc="tabindex of the component")
    private int tabindex;  
	    
	@Property (defaultValue="false",
			tlddoc="disabled property is required by aria specs")
	private boolean disabled;
}
