package org.icefaces.component.selectinputdate;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

import java.util.Date;

@Component(componentClass = "org.icefaces.component.selectinputdate.SelectInputDate3",
        tagName = "selectInputDate3",
        extendsClass ="javax.faces.component.UIInput",
        componentFamily ="javax.faces.Input",
        componentType = "com.icesoft.faces.SelectInputDate3",
        rendererClass ="org.icefaces.component.selectinputdate.SelectInputDate3Renderer",
        rendererType ="com.icesoft.faces.Calendar3",
        generatedClass = "org.icefaces.component.selectinputdate.SelectInputDate3Base"
        )
public class SelectInputDate3Meta {

    @Property
    private Date selectedDate;
    @Property(defaultValue="")
    private String minDate;
    @Property(defaultValue="")
    private String maxDate;
    @Property(defaultValue="")
    private String disabledDates;
    @Property(defaultValue="")
    private String highlightUnit;
    @Property(defaultValue="")
    private String highlightValue;
    @Property(defaultValue="")
    private String highlightClass;
    @Property(defaultValue="false")
    private Boolean renderInputField;
    @Property(defaultValue="false")
    private Boolean renderAsPopup;
    @Property(defaultValue="false")
    private Boolean singleSubmit;

}