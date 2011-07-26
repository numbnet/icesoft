/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.ace.component.datetimeentry;

import org.icefaces.ace.util.Utils;
import org.icefaces.impl.util.Util;
import javax.faces.convert.DateTimeConverter;
import javax.faces.convert.Converter;
import javax.faces.context.FacesContext;
import java.util.TimeZone;
import java.util.Locale;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class DateTimeEntry extends DateTimeEntryBase {
    
    // Copied from 1.8.2
    /**
     * To properly function, dateTimeEntry needs to use the same timezone
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

    // Copied from 1.8.2
    /**
     * This method is necesary since DateTimeConverter.getDateFormat(Locale) is private  
     */
    public static String getDateTimeConverterPattern(DateTimeConverter converter) {
        Locale locale = converter.getLocale();
        String pattern = converter.getPattern();
        String type = converter.getType();
        String dateStyle = converter.getDateStyle();
        String timeStyle = converter.getTimeStyle();
        
        DateFormat df;
        if (pattern != null) {
            df = new SimpleDateFormat(pattern, locale);
        } else if (type.equals("both")) {
            df = DateFormat.getDateTimeInstance
                 (getDateTimeConverterStyle(dateStyle), getDateTimeConverterStyle(timeStyle), locale);
        } else if (type.equals("date")) {
            df = DateFormat.getDateInstance(getDateTimeConverterStyle(dateStyle), locale);
        } else if (type.equals("time")) {
            df = DateFormat.getTimeInstance(getDateTimeConverterStyle(timeStyle), locale);
        } else {
            // PENDING(craigmcc) - i18n
            throw new IllegalArgumentException("Invalid type: " + type);
        }
        df.setLenient(false);
        
        // In the underlying code, it is always a SimpleDateFormat
        if (df instanceof SimpleDateFormat) {
            return ((SimpleDateFormat)df).toPattern();
        }
        return "";
    }
    
    // Copied from 1.8.2
    /**
     * This method is necesary since DateTimeConverter.getStylet(String) is private  
     */
    private static int getDateTimeConverterStyle(String name) {
        if      ("short".equals(name))  return DateFormat.SHORT;
        else if ("medium".equals(name)) return DateFormat.MEDIUM;
        else if ("long".equals(name))   return DateFormat.LONG;
        else if ("full".equals(name))   return DateFormat.FULL;
        else                            return DateFormat.DEFAULT;
    }

    public boolean isSingleSubmit() {
        return Utils.superValueIfSet(this, getStateHelper(), PropertyKeys.singleSubmit.name(), super.isSingleSubmit(), Util.withinSingleSubmit(this));
    }
}
