  package org.icefaces.component.Logger;

import javax.faces.component.UIComponent;
import javax.el.MethodExpression;
import javax.faces.event.ActionListener;
import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;
import java.util.List;

@Component(
        tagName ="logger",
        componentClass ="org.icefaces.component.Logger.Logger",
        rendererClass ="org.icefaces.component.Logger.LoggerRenderer", 
        componentType = "org.icefaces.Logger", 
        rendererType = "org.icefaces.LoggerRenderer",            
        extendsClass = "javax.faces.component.UIComponentBase", 
        generatedClass = "org.icefaces.component.Logger.LoggerBase",
		componentFamily="com.icesoft.faces.Logger"
        )
        
public class LoggerMeta {
//    
//    @Property   
//    private String label;

    @Property (inherit=true, useTemplate=true)
    private String id;
	
	@Property
    private String debugElement;
	
//	@Property
//	private int width;
//	
//	@Property
//	private int height;
//	
//	@Property
//	private Boolean footerEnabled;
//	
//	@Property
//	private Boolean logReaderEnabled;
	
//other properties not included here are thresholdMax, thresholdMin,
//draggable, outputBuffer, newestOnTop, verboseOutput, entryFormat
	
    @Property (inherit=true, defaultValue="true")
    private Boolean rendered;
    
    @Property (defaultValue="0", tlddoc="tabindex of the component")
    private Integer tabindex;  
  
    @Property(tlddoc="style class of the component, the renderer doesn't render any default class.")
    private String styleClass;  

    @Property(tlddoc="style of the component")
    private String style;
    
}
