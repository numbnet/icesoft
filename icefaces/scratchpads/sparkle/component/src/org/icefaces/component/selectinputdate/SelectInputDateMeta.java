package org.icefaces.component.selectinputdate;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

import java.util.Date;

@Component(componentClass = "org.icefaces.component.selectinputdate.SelectInputDate",
        tagName = "selectInputDate",
        extendsClass ="javax.faces.component.UIInput",
        componentFamily ="javax.faces.Input",
        componentType = "com.icesoft.faces.SelectInputDate",
        rendererClass ="org.icefaces.component.selectinputdate.SelectInputDateRenderer",
        rendererType ="com.icesoft.faces.Calendar",
        generatedClass = "org.icefaces.component.selectinputdate.SelectInputDateBase"
        )
public class SelectInputDateMeta {

    @Property
    private Date selectedDate;
    @Property(defaultValue="false")
    private Boolean renderInputField;
    @Property(defaultValue="false")
    private Boolean renderAsPopup;
    @Property(defaultValue="false")
    private Boolean singleSubmit;

}