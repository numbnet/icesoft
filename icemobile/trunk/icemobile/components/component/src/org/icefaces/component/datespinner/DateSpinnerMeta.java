package org.icefaces.component.datespinner;

import org.icefaces.component.annotation.*;

import org.icefaces.component.baseMeta.UIInputMeta;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;

@Component(
        tagName = "dateSpinner",
        componentClass = "org.icefaces.component.datespinner.DateSpinner",
        generatedClass = "org.icefaces.component.datespinner.DateSpinnerBase",
        extendsClass = "javax.faces.component.UIInput",
        rendererClass = "org.icefaces.component.datespinner.DateSpinnerRenderer",
        componentFamily = "org.icefaces.component.DateSpinner",
        componentType = "org.icefaces.component.DateSpinner",
        rendererType = "org.icefaces.component.DateSpinnerRenderer",
        tlddoc = "DateSpinner is an input component to provide a date for mobile components. ")
@ResourceDependencies({
   //    @ResourceDependency(library = "org.icefaces.component.datespinner", name = "dateSpinner.js")
})
public class DateSpinnerMeta extends UIInputMeta {


    @Property(defaultValue = "yyyy-M-d H:m", tlddoc = "DateFormat pattern for localization")
    private String pattern;

    @Property(defaultValue = "2000", tlddoc="yearStart is the first year to appear in the dateScroller")
    private int yearStart;

    @Property(defaultValue = "2020", tlddoc="yearEbd is the last year to appear in the dateScroller")
    private int yearEnd;

    @Property(tlddoc = "Locale to be used for labels and conversion.")
    private Object locale;

    @Property(tlddoc = "String or a java.util.TimeZone instance to specify the timezone used for date " +
            "conversion, defaults to TimeZone.getDefault()")
    private Object timeZone;

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
