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
        generatedClass = "org.icefaces.component.datetimeentry.DateTimeEntryBase",
        tlddoc = "This component allows entry of a date/time. It can be in inline mode or popup mode. In either mode " +
                "you can enter a date/time by clicking. In popup mode you have the additional option of entering in " +
                "a text input field. The format of the input is determined by the nested &lt;f:convertDateTime&gt; " +
                "tag. For more information, see the " +
                "<a href=\"http://wiki.icefaces.org/display/facesDev/DateTime+Entry\">Wiki doc</a>."
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
    @Property(defaultValue = "false", tlddoc = "When rendered as a popup, you can allow entering the date/time in " +
            "a text input field as well. This attribute flags whether text input is allowed. Format of input is " +
            "determined by the nested &lt;f:convertDateTime&gt; tag.")
    private boolean renderInputField;

    @Property(defaultValue = "false", tlddoc = "Whether to render the calendar inline or as a popup.")
    private boolean renderAsPopup;

    @Property(defaultValue = "false", tlddoc = "When singleSubmit is true, changing the value of this component will " +
            "submit and execute this component only (equivalent to &lt;f:ajax execute=\"@this\" render=\"@all\"&gt;). " +
            "When singleSubmit is false, no submit will occur. The default value is false.")
    private boolean singleSubmit;

    @Property(implementation=Implementation.EXISTS_IN_SUPERCLASS, tlddoc = "Value of the component as a Date object.")
    private Object value;

    @Property (defaultValue = "", tlddoc="style will be rendered on a root element of this component")
    private String style;
    
    @Property (defaultValue = "", tlddoc="style class will be rendered on a root element of this component")
    private String styleClass;

    @Property (tlddoc="If true then this date time entry will be disabled and can not be entered") 
    private boolean disabled;

    @Property (tlddoc="tabindex of the component")
    private int tabindex;
}