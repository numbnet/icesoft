package org.icefaces.component.selectinputdate;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.Converter;
import javax.faces.context.FacesContext;
import java.util.TimeZone;
import java.util.Locale;

@ResourceDependencies({
        @ResourceDependency(name = "container.css", library = "org.icefaces.component.selectinputdate"),
        @ResourceDependency(name = "calendar.css", library = "org.icefaces.component.selectinputdate"),
        @ResourceDependency(name = "util.js", library = "org.icefaces.component.util"),
        @ResourceDependency(name = "component.js", library = "org.icefaces.component.util"),
        @ResourceDependency(name = "calendar.js", library = "org.icefaces.component.selectinputdate")
})
public class SelectInputDate extends SelectInputDateBase {
    // Copied from 1.8.2
    /**
     * To properly function, selectInputDate needs to use the same timezone
     * in the inputText field as well as the calendar, which is accomplished
     * by using a javax.faces.convert.DateTimeConverter, which provides
     * the required Converter behaviours, as we as gives access to its
     * TimeZone object. If developers require a custom Converter, then they
     * must subclass javax.faces.convert.DateTimeConverter.
     *
     * @return DateTimeConverter
     */
    public DateTimeConverter resolveDateTimeConverter(FacesContext context) {
        DateTimeConverter converter = null;
        Converter compConverter = getConverter();
        if (compConverter instanceof DateTimeConverter) {
            converter = (DateTimeConverter) compConverter;
        } else {
            Converter appConverter = context.getApplication().createConverter(
                    java.util.Date.class);
            if (appConverter instanceof DateTimeConverter) {
                converter = (DateTimeConverter) appConverter;
            } else {
                converter = new DateTimeConverter();
            }
        }
        return converter;
    }
    
    // Copied from 1.8.2
    public TimeZone resolveTimeZone(FacesContext context) {
        DateTimeConverter converter = resolveDateTimeConverter(context);
        TimeZone tz = converter.getTimeZone();
        if (tz == null) { // DateTimeConverter should already do this
            tz = TimeZone.getTimeZone("GMT");
        }
        return tz;
    }

    // Copied from 1.8.2
    public Locale resolveLocale(FacesContext context) {
        return context.getViewRoot().getLocale();
    }
}