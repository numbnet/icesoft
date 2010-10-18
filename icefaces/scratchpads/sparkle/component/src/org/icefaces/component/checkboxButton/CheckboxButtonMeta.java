package org.icefaces.component.checkboxButton;


import javax.faces.component.UIComponent;
import org.icefaces.component.annotation.*;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.component.baseMeta.UISelectBooleanMeta;

@Component(
        tagName ="checkboxButton",
        componentClass ="org.icefaces.component.checkboxButton.CheckboxButton",
        rendererClass ="org.icefaces.component.checkboxButton.CheckboxButtonRenderer",
        componentType = "org.icefaces.CheckboxButton",
        rendererType = "org.icefaces.CheckboxButtonRenderer",
        extendsClass = "javax.faces.component.UISelectBoolean", 
        generatedClass = "org.icefaces.component.checkboxButton.CheckboxButtonBase",
		componentFamily="com.icesoft.faces.CheckboxButton"
        )
        
@ResourceDependencies({
    	@ResourceDependency(name="yui/yui-min.js",library="yui/3_1_1"),	
	    @ResourceDependency(library = "yui/2_8_1", name = "yahoo-dom-event/yahoo-dom-event.js"),
	    @ResourceDependency(library = "yui/2_8_1", name = "element/element-min.js"),
	    @ResourceDependency(library = "yui/2_8_1", name = "button/button-min.js"),
		@ResourceDependency(name="util.js",library="org.icefaces.component.util"),
	    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),	
        @ResourceDependency(name="checkboxbutton.js",library="org.icefaces.component.checkboxbutton"),
        @ResourceDependency(name="checkboxbutton.css",library="org.icefaces.component.checkboxbutton")
})

public class CheckboxButtonMeta extends UISelectBooleanMeta {
    
    @Property   
    private String label;
    
    @Property(defaultValue="left",
    		tlddoc="Default is left, Possible values are on, left")
    private String labelPosition;

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
