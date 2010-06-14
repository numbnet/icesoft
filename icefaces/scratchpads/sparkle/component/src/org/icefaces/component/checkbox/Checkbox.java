package org.icefaces.component.checkbox;



import javax.faces.context.FacesContext;

import javax.faces.context.ResponseWriter;

import javax.faces.application.ResourceDependencies;

import javax.faces.application.ResourceDependency;

import javax.faces.component.UIOutput;



import java.io.IOException;



@ResourceDependencies({

	    @ResourceDependency(name = "sam/button/button.css", library = "org.icefaces.component.sprites"),
	    @ResourceDependency(name = "sam/button/button-skin.css", library = "org.icefaces.component.sprites"),
	    @ResourceDependency(library = "yui/2_8_1", name = "yahoo-dom-event/yahoo-dom-event.js"),
	    @ResourceDependency(library = "yui/2_8_1", name = "element/element-min.js"),
	    @ResourceDependency(library = "yui/2_8_1", name = "button/button-min.js"),
		@ResourceDependency(name="util.js",library="org.icefaces.component.util"),
	    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),	
        @ResourceDependency(name="checkbox.js",library="org.icefaces.component.checkbox")    

})

public class Checkbox extends CheckboxBase {

	// return false if current value is null

//    public Object getValue() {
//	    System.out.println("Checkbox component value="+super.getValue());	
//		Object value = super.getValue();
//		if (value == null) {
//		System.out.println("CHECKBOX IS NULL!!!!!!!!");
//			return Boolean.valueOf(false);
//		}
//		return value;
//	}

}

