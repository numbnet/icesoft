package org.icefaces.component.datetime;


import javax.faces.component.UIComponent;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

@Component(
        tag_name="dateTime",
        component_class="org.icefaces.component.datetime.DateTime",
        renderer_class="org.icefaces.component.datetime.DateTimeRenderer", 
        component_type = "org.icefaces.DateTime", 
        renderer_type = "org.icefaces.DateTimeRenderer",            
        extends_class = "javax.faces.component.UIOutput", 
        generated_class = "org.icefaces.component.datetime.DateTimeBase"
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
