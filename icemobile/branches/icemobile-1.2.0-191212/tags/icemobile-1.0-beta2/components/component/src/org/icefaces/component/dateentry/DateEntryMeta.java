package org.icefaces.component.dateentry;

import org.icefaces.component.annotation.*;

import org.icefaces.component.baseMeta.UIInputMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "dateEntry",
        componentClass = "org.icefaces.component.dateentry.DateEntry",
        generatedClass = "org.icefaces.component.dateentry.DateEntryBase",
        extendsClass = "javax.faces.component.UIInput",
        rendererClass = "org.icefaces.component.dateentry.DateEntryRenderer",
        componentFamily = "org.icefaces.component.DateEntry",
        componentType = "org.icefaces.component.DateEntry",
        rendererType = "org.icefaces.component.DateEntryRenderer",
        tlddoc = "DateEntry is an input component to provide a date in various ways. Other than basic " +
                "features dateentry supports popup")
@ResourceDependencies({
 //       @ResourceDependency(library = "org.icefaces.component.dateentry", name= "tcal.css"),
        @ResourceDependency(library = "org.icefaces.component.dateentry", name = "tcal.js"),
        @ResourceDependency(library = "org.icefaces.component.dateentry", name = "dateentry.js")
})
//@ClientBehaviorHolder(events = {"blur", "change", "valueChange", "click", "focus", "keydown", "keypress", "keyup", "mousedown", "mousemove", "mouseout", "mouseover", "mouseup", "select", "dateSelect"}, defaultEvent = "valueChange")
public class DateEntryMeta extends UIInputMeta {


    @Property(defaultValue = "MM/dd/yyyy", tlddoc = "DateFormat pattern for localization")
    private String pattern;

    @Property(tlddoc = "Locale to be used for labels and conversion.")
    private Object locale;

    @Property(tlddoc = "Enables yearscroll for navigating the year.")
    private boolean yearscroll;

    @Property(tlddoc = "String or a java.util.TimeZone instance to specify the timezone used for date " +
            "conversion, defaults to TimeZone.getDefault()")
    private Object timeZone;

    @Property(defaultValue="0", tlddoc = "determines start of week date 0=Monday, 1 = Tuesday,....6=Saturday")
    private int weekstart;

    @Property(defaultValue = "d-m-Y", tlddoc = " format of input field in client available formats are"+
          "'m/d/Y' or  'd-m-Y' or  'Y-m-d' or 'l, F jS Y'")
    private String format;

    @Property(defaultValue = "tcal", tlddoc = "string prefix for classes for example iphone will be prefix for iphone styles")
    private String stringPrefix;

    @Property(tlddoc = "style will be rendered on a root element of this component")
    private String style;

    @Property(tlddoc = "style class will be rendered on a root element of this component")
    private String styleClass;

    @Property(tlddoc = "If true then this date time entry will be disabled and can not be entered.")
    private boolean disabled;

    @Property(tlddoc = "If true then this date time entry will be read-only and can not be entered.")
    private boolean readonly;

    @Property(tlddoc = "If true then this date time entry will submit itself.")
    private boolean singleSubmit;


}
