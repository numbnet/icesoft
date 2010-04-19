package org.icefaces.component.selectinputdate;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

import java.util.Date;

@Component(componentClass = "org.icefaces.component.selectinputdate.SelectInputDate",
        tagName = "selectInputDate",
        extendsClass = "javax.faces.component.UIInput",
        componentFamily = "javax.faces.Input",
        componentType = "com.icesoft.faces.SelectInputDate",
        rendererClass = "org.icefaces.component.selectinputdate.SelectInputDateRenderer",
        rendererType = "com.icesoft.faces.Calendar",
        generatedClass = "org.icefaces.component.selectinputdate.SelectInputDateBase"
)
public class SelectInputDateMeta {
    @Property(defaultValue = "false", tlddoc = "Whether to render a text input field for a popup calendar.")
    private Boolean renderInputField;

    @Property(defaultValue = "false", tlddoc = "Whether to render the calendar inline or as a popup.")
    private Boolean renderAsPopup;

    @Property(defaultValue = "false", tlddoc = "Whether to use single or full submit.")
    private Boolean singleSubmit;

    @Property(inherit = true, tlddoc = "ID of the component.")
    private String id;

    @Property(inherit = true, tlddoc = "Value of the component as a Date object.")
    private Object value;
}