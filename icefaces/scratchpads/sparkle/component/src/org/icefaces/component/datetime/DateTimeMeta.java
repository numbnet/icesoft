package org.icefaces.component.datetime;


import javax.faces.component.UIComponent;

import org.icefaces.component.annotation.*;

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

    @Property (inherit=Inherit.SUPERCLASS_PROPERTY, useTemplate=true)
    private String id;
    
    @Property (inherit=Inherit.SUPERCLASS_PROPERTY, useTemplate=true)
    private Boolean rendered;
    
    @Property (inherit=Inherit.SUPERCLASS_PROPERTY, useTemplate=true)
    private UIComponent binding;    

}
