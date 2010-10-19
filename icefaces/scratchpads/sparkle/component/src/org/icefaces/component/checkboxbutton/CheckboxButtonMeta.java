package org.icefaces.component.checkboxbutton;


import javax.faces.component.UIComponent;
import org.icefaces.component.annotation.*;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.component.baseMeta.UISelectBooleanMeta;

@Component(
        tagName ="checkboxButton",
        componentClass ="org.icefaces.component.checkboxbutton.CheckboxButton",
        rendererClass ="org.icefaces.component.checkboxbutton.CheckboxButtonRenderer",
        componentType = "org.icefaces.CheckboxButton",
        rendererType = "org.icefaces.CheckboxButtonRenderer",
        extendsClass = "javax.faces.component.UISelectBoolean", 
        generatedClass = "org.icefaces.component.checkboxbutton.CheckboxButtonBase",
		componentFamily="org.icefaces.CheckboxButton",
		tlddoc="This component allows entry of a selection button"+
		       "supports browsers which see checkbox as true or false,"+
		       "yes or no, on or off.  LabelPosition property allows label "+
		       "to be placed on the button-in case of sam style, or to the left "+
		       "of the button - in the case of rime style."
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
			tlddoc= "When singleSubmit is true, changing the value of this component" +
					" will submit and execute this component only. Equivalent to " +
					"<f:ajax execute='@this' render='@all'>. " +
					"When singleSubmit is false, no submit will occur. " +
					"The default value is false.")
    private boolean singleSubmit;
	    
    @Property(tlddoc="style of the component, rendered on the root div of the component")
	private String style;
	    
    @Property(tlddoc="style class of the component, rendered on the root div of the component.")
	private String styleClass;     
 
    @Property (defaultValue="0", tlddoc="tabindex of the component")
    private int tabindex;  
    
    @Property (defaultValue="false",
    		tlddoc="disabled property. If true no input may be submitted via this" +
    				"component.  Is required by aria specs")
    private boolean disabled;
}
