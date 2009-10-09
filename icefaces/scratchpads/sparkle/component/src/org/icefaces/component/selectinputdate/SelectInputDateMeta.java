package org.icefaces.component.selectinputdate;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

import java.util.Date;

@Component(componentClass = "org.icefaces.component.selectinputdate.SelectInputDate", 
        tagName = "selectInputDate",
        extendsClass ="javax.faces.component.UIComponentBase",
        componentFamily ="javax.faces.Input",
        componentType = "com.icesoft.faces.SelectInputDate",
        rendererClass ="org.icefaces.component.selectinputdate.SelectInputDateRenderer",
        rendererType ="com.icesoft.faces.Calendar"
        )
public class SelectInputDateMeta {

    @Property
    private Date selectedDate;

}
