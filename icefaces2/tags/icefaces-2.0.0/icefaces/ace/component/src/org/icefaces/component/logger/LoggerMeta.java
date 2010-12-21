package org.icefaces.component.logger;

import javax.faces.component.UIComponent;
import javax.el.MethodExpression;
import javax.faces.event.ActionListener;
import org.icefaces.component.annotation.*;
import java.util.List;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.component.baseMeta.UIComponentBaseMeta;

@Component(
        tagName ="logger",
        componentClass ="org.icefaces.component.logger.Logger",
        rendererClass ="org.icefaces.component.logger.LoggerRenderer", 
        componentType = "org.icefaces.Logger", 
        rendererType = "org.icefaces.LoggerRenderer",            
        extendsClass = "javax.faces.component.UIComponentBase", 
        generatedClass = "org.icefaces.component.logger.LoggerBase",
		componentFamily="org.icefaces.Logger"
        )
        
@ResourceDependencies({
    @ResourceDependency(library = "yui/2_8_1", name = "logger/assets/skins/sam/logger.css"),
    @ResourceDependency(library = "yui/2_8_1", name = "yahoo-dom-event/yahoo-dom-event.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "element/element-min.js"),
    @ResourceDependency(library = "yui/2_8_1", name = "logger/logger-min.js"),
	@ResourceDependency(name="util.js",library="org.icefaces.component.util"),
    @ResourceDependency(name="component.js",library="org.icefaces.component.util"),	
    @ResourceDependency(name="logger.js",library="org.icefaces.component.logger")
})

public class LoggerMeta extends UIComponentBaseMeta {
//    
//    @Property   
//    private String label;

	@Property
    private String debugElement;
	
//	@Property
//	private int width;
//	
//	@Property
//	private int height;
//	
//	@Property
//	private boolean footerEnabled;
//	
//	@Property
//	private boolean logReaderEnabled;
	
//other properties not included here are thresholdMax, thresholdMin,
//draggable, outputBuffer, newestOnTop, verboseOutput, entryFormat
	
    @Property (defaultValue="0", tlddoc="tabindex of the component")
    private int tabindex;  
  
    @Property(tlddoc="style class of the component, the renderer doesn't render any default class.")
    private String styleClass;  

    @Property(tlddoc="style of the component")
    private String style;
    
}
