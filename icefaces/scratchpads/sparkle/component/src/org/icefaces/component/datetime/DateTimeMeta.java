package org.icefaces.component.datetime;


import javax.faces.component.UIComponent;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

@Component(
        tagName ="dateTime",
        componentClass ="org.icefaces.component.datetime.DateTime",
        rendererClass ="org.icefaces.component.datetime.DateTimeRenderer", 
        componentType = "org.icefaces.DateTime", 
        rendererType = "org.icefaces.DateTimeRenderer",            
        extendsClass = "javax.faces.component.UIOutput", 
        generatedClass = "org.icefaces.component.datetime.DateTimeBase"
        )
        
public class DateTimeMeta{
    
    @Property
    private String format;

    @Property (inherit=true, useTemplate=true)
    private String id;
    
    @Property (inherit=true, useTemplate=true)
    private Boolean rendered;
    
    @Property (inherit=true, useTemplate=true)
    private UIComponent binding;    

}
