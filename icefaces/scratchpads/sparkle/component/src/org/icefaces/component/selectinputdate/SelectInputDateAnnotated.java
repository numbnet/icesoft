package org.icefaces.component.selectinputdate;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

import javax.faces.component.UIComponentBase;
import java.util.Date;

@Component(component_class = "org.icefaces.component.selectinputdate.SelectInputDate", tag_name = "selectInputDate")
public class SelectInputDateAnnotated extends UIComponentBase {
    public static final String COMPONENT_FAMILY = "javax.faces.Input";
    public static final String COMPONENT_TYPE = "com.icesoft.faces.SelectInputDate";

    @Property
    protected Date selectedDate;

    public SelectInputDateAnnotated() {
        super();
        setRendererType("com.icesoft.faces.Calendar");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }
}
