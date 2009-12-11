package org.icefaces.component.selectinputdate;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

import java.util.Date;

@Component(componentClass = "org.icefaces.component.selectinputdate.SelectInputDate2",
        tagName = "selectInputDate2",
        extendsClass ="javax.faces.component.UIInput",
        componentFamily ="javax.faces.Input",
        componentType = "com.icesoft.faces.SelectInputDate2",
        rendererClass ="org.icefaces.component.selectinputdate.SelectInputDate2Renderer",
        rendererType ="com.icesoft.faces.Calendar2",
        generatedClass = "org.icefaces.component.selectinputdate.SelectInputDate2Base"
        )
public class SelectInputDate2Meta {

    @Property
    private Date selectedDate;

}