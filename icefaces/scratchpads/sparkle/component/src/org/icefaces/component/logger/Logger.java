package org.icefaces.component.Logger;

import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIOutput;

import java.io.IOException;

@ResourceDependencies({
    @ResourceDependency(library = "yui/2_8_1", name = "logger/assets/skins/sam/logger.css"),
    @ResourceDependency(library = "yui/2_8_1", name = "yahoo-dom-event/yahoo-dom-event.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "element/element-min.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "logger/logger-min.js"),
	@ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),	
    @ResourceDependency(name="logger.js",library="org.icefaces.component.logger")
})
   
    public class Logger extends LoggerBase {


}
