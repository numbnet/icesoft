package org.icefaces.component.datetimeselector;

import org.icefaces.component.annotation.Component;
import org.icefaces.component.annotation.Property;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(componentClass = "org.icefaces.component.datetimeselector.DateTimeSelector",
        tagName = "dateTimeSelector",
        extendsClass = "javax.faces.component.UIInput",
        componentFamily = "javax.faces.Input",
        componentType = "com.icesoft.faces.DateTimeSelector",
        rendererClass = "org.icefaces.component.datetimeselector.DateTimeSelectorRenderer",
        rendererType = "com.icesoft.faces.Calendar",
        generatedClass = "org.icefaces.component.datetimeselector.DateTimeSelectorBase"
)

@ResourceDependencies({
        @ResourceDependency(name = "container.css", library = "org.icefaces.component.datetimeselector"),
        @ResourceDependency(library = "yui/2_8_1", name = "yahoo-dom-event/yahoo-dom-event.js"),
//        @ResourceDependency(library = "yui/2_8_1", name = "yahoo/yahoo-debug.js"),
//        @ResourceDependency(library = "yui/2_8_1", name = "dom/dom-debug.js"),
//        @ResourceDependency(library = "yui/2_8_1", name = "event/event-debug.js"),
        @ResourceDependency(library = "yui/2_8_1", name = "element/element-min.js"),
        @ResourceDependency(library = "yui/2_8_1", name = "button/button-min.js"),
        @ResourceDependency(library = "yui/2_8_1", name = "calendar/calendar-min.js"),
        @ResourceDependency(library = "yui/2_8_1", name = "container/container-min.js"),
        @ResourceDependency(library = "yui/2_8_1", name = "selector/selector-min.js"),
        @ResourceDependency(library = "yui/2_8_1", name = "datasource/datasource-min.js"),
        @ResourceDependency(library = "yui/2_8_1", name = "json/json-min.js"),
        @ResourceDependency(name = "util.js", library = "org.icefaces.component.util"),
        @ResourceDependency(name = "component.js", library = "org.icefaces.component.util"),
        @ResourceDependency(name = "calendar.js", library = "org.icefaces.component.datetimeselector")
})

public class DateTimeSelectorMeta {
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