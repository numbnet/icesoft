package org.icefaces.component.selectinputdate;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

import javax.faces.component.UIComponentBase;
import java.util.Date;

@Component(component_class = "org.icefaces.component.selectinputdate.SelectInputDate", 
        tag_name = "selectInputDate",
        extends_class="javax.faces.component.UIComponentBase",
        component_family="javax.faces.Input",
        component_type= "com.icesoft.faces.SelectInputDate",
        renderer_class="org.icefaces.component.selectinputdate.SelectInputDateRenderer",
        renderer_type="com.icesoft.faces.Calendar"
        )
public class SelectInputDateMeta {
    public static final String COMPONENT_FAMILY = "";
    public static final String COMPONENT_TYPE = "";

    @Property
    protected Date selectedDate;

}
