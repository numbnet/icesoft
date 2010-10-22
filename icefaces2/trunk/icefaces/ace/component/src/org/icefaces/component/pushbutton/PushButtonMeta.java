  package org.icefaces.component.pushbutton;

import javax.faces.component.UIComponent;
import javax.el.MethodExpression;
import javax.faces.event.ActionListener;
import org.icefaces.component.annotation.*;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.component.baseMeta.UICommandMeta;

@Component(
        tagName ="pushButton",
        componentClass ="org.icefaces.component.pushbutton.PushButton",
        rendererClass ="org.icefaces.component.pushbutton.PushButtonRenderer",
        componentType = "org.icefaces.PushButton", 
        rendererType = "org.icefaces.PushButtonRenderer",            
        extendsClass = "javax.faces.component.UICommand", 
        generatedClass = "org.icefaces.component.pushbutton.PushButtonBase",
		componentFamily="com.icesoft.faces.PushButton",
	    tlddoc = "This component allows entry of a complete form or just itself. " +
	         "It has athe same functionality of a regular jsf command button " +
	         "but without having to add extra attributes other than determining singleSubmit " +
	         "to be true or false"
        )
		
@ResourceDependencies({
    @ResourceDependency(name="yui/yui-min.js",library="yui/3_1_1"),    
    @ResourceDependency(library = "yui/2_8_1", name = "yahoo-dom-event/yahoo-dom-event.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "element/element-min.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "button/button-min.js"),
	@ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),	
    @ResourceDependency(name="pushbutton.js",library="org.icefaces.component.pushbutton"),
	@ResourceDependency(name="pushbutton.css",library="org.icefaces.component.pushbutton")
})
        
public class PushButtonMeta extends UICommandMeta {
    
    @Property(tlddoc=" A localized user presentable name for this component. Used by aria.") 
    private String label;

	@Property(defaultValue="false",
			tlddoc="When singleSubmit is true, triggering an action on this component will submit" +
			" and execute this component only. Equivalent to <f:ajax execute='@this' render='@all'>." +
			" When singleSubmit is false, triggering an action on this component will submit and execute " +
			" the full form that this component is contained within." +
			" The default value is false.")
    private boolean singleSubmit;
	
    @Property (defaultValue="false",
            tlddoc="disabled property. If true no input may be submitted via this" +
    				"component.  Is required by aria specs")
    private boolean disabled;
    
    @Property (defaultValue="0", tlddoc="tabindex of the component")
    private int tabindex;  
  
    @Property(tlddoc="style class of the component, rendered on the div root of the component")
    private String styleClass;  

    @Property(tlddoc="style of the component, rendered on the div root of the component")
    private String style;
}
