package org.icefaces.component.datetimeentry;

import org.icefaces.component.annotation.*;
import org.icefaces.component.annotation.ClientBehaviorHolder;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

import org.icefaces.component.baseMeta.UIInputMeta;

@Component(componentClass = "org.icefaces.component.datetimeentry.DateTimeEntry",
        tagName = "dateTimeEntry",
        extendsClass = "javax.faces.component.UIInput",
        componentFamily = "org.icefaces.component.DateTimeEntry",
        componentType = "org.icefaces.DateTimeEntry",
        rendererClass = "org.icefaces.component.datetimeentry.DateTimeEntryRenderer",
        rendererType = "org.icefaces.DateTimeEntry",
        generatedClass = "org.icefaces.component.datetimeentry.DateTimeEntryBase"
)

@ResourceDependencies({
        @ResourceDependency(name = "container.css", library = "org.icefaces.component.datetimeentry"),
        @ResourceDependency(library = "yui/2_8_1", name = "yahoo-dom-event/yahoo-dom-event.js"),
    	@ResourceDependency(name="yui/yui-min.js",library="yui/3_1_1"),
    	@ResourceDependency(name="loader/loader-min.js",library="yui/3_1_1"),
        @ResourceDependency(name ="anim/anim-min.js",library = "yui/3_1_1"),
        @ResourceDependency(name ="plugin/plugin-min.js",library = "yui/3_1_1"),    
        @ResourceDependency(name ="pluginhost/pluginhost-min.js",library = "yui/3_1_1"),      
        @ResourceDependency(name="util.js",library="org.icefaces.component.util"),
        @ResourceDependency(name="component.js",library="org.icefaces.component.util"),    
        @ResourceDependency(name="yui3.js",library="org.icefaces.component.util"),   
        @ResourceDependency(name="animation.js",library="org.icefaces.component.animation"),
        @ResourceDependency(name="animation.css",library="org.icefaces.component.animation"),          
        
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
        @ResourceDependency(name = "calendar.js", library = "org.icefaces.component.datetimeentry")
})
@ClientBehaviorHolder 
public class DateTimeEntryMeta extends UIInputMeta {
    @Property(defaultValue = "false", tlddoc = "Whether to render a text input field for a popup calendar.")
    private boolean renderInputField;

    @Property(defaultValue = "false", tlddoc = "Whether to render the calendar inline or as a popup.")
    private boolean renderAsPopup;

    @Property(defaultValue = "false", tlddoc = "Whether to use single or full submit.")
    private boolean singleSubmit;

    @Property(implementation=Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Value of the component as a Date object.")
    private Object value;

    @Property (defaultValue = "", tlddoc="style will be rendered on a root element of this component")
    private String style;
    
    @Property (defaultValue = "", tlddoc="style class will be rendered on a root element of this component")
    private String styleClass;
}