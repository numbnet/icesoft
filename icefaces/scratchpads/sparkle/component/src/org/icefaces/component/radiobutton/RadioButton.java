package org.icefaces.component.radiobutton;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIOutput;
import javax.faces.model.SelectItemGroup;
import javax.faces.model.SelectItem;

import java.io.IOException;

@ResourceDependencies({
    @ResourceDependency(name = "sam/button/button.css", library = "org.icefaces.component.sprites"),
    @ResourceDependency(library = "yui/2_8_1", name = "yahoo-dom-event/yahoo-dom-event.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "element/element-min.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "button/button-min.js"),
	@ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),	
    @ResourceDependency(name="radiobutton.js",library="org.icefaces.component.radiobutton")    
})
public class RadioButton extends RadioButtonBase {



}
