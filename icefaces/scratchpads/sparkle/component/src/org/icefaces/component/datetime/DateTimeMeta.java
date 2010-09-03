package org.icefaces.component.datetime;


import javax.faces.component.UIComponent;

import org.icefaces.component.annotation.*;

import org.icefaces.component.baseMeta.UIOutputMeta;

@Component(
        tagName ="dateTime",
        componentClass ="org.icefaces.component.datetime.DateTime",
        rendererClass ="org.icefaces.component.datetime.DateTimeRenderer", 
        componentType = "org.icefaces.DateTime", 
        rendererType = "org.icefaces.DateTimeRenderer",            
        extendsClass = "javax.faces.component.UIOutput", 
        generatedClass = "org.icefaces.component.datetime.DateTimeBase"
        )
        
public class DateTimeMeta extends UIOutputMeta {
    
    @Property
    private String format;

    @Property (inherit=Inherit.SUPERCLASS_PROPERTY)
    private String id;
    
    @Property (inherit=Inherit.SUPERCLASS_PROPERTY)
    private Boolean rendered;
    
    @Property (inherit=Inherit.SUPERCLASS_PROPERTY)
    private UIComponent binding;    

}
