package org.icefaces.component.selectinputdate;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

import java.util.Date;

@Component(componentClass = "org.icefaces.component.selectinputdate.SelectInputDate1", 
        tagName = "selectInputDate1",
        extendsClass ="javax.faces.component.UIInput",
        componentFamily ="javax.faces.Input",
        componentType = "com.icesoft.faces.SelectInputDate1",
        rendererClass ="org.icefaces.component.selectinputdate.SelectInputDate1Renderer",
        rendererType ="com.icesoft.faces.Calendar1"
        )
public class SelectInputDate1Meta {

    @Property
    private Date selectedDate;

}
