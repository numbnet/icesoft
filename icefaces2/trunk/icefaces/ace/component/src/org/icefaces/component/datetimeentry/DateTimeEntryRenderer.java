package org.icefaces.component.datetimeentry;

import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.JSONBuilder;
import org.icefaces.component.utils.ScriptWriter;
import org.icefaces.component.utils.Utils;
import org.icefaces.component.animation.ClientBehaviorContextImpl;
import org.icefaces.component.animation.AnimationBehavior;
import org.icefaces.util.EnvUtils;
import org.icefaces.render.MandatoryResourceComponent;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;
import java.io.IOException;
import java.text.*;
import java.util.*;

@MandatoryResourceComponent("org.icefaces.component.datetimeentry.DateTimeEntry")
public class DateTimeEntryRenderer extends Renderer {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);
        writer.startElement(HTML.DIV_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, clientId, "clientId");
        DateTimeEntry dateTimeEntry = (DateTimeEntry) component;
        String style = dateTimeEntry.getStyle();
        if (style != null && style.trim().length() != 0) {
            writer.writeAttribute(HTML.STYLE_ATTR, style, HTML.STYLE_ATTR);
        }
        String styleClass = dateTimeEntry.getStyleClass();
        if (styleClass != null && styleClass.trim().length() != 0) {
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, HTML.CLASS_ATTR);
        }
        writer.writeAttribute(HTML.TABINDEX_ATTR, dateTimeEntry.getTabindex(), HTML.TABINDEX_ATTR);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        // ignore children
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
//        System.out.println("\nDateTimeEntryRenderer.encodeEnd");
//        printParams();
        super.encodeEnd(context, component);
        ResponseWriter writer = context.getResponseWriter();
        DateTimeEntry dateTimeEntry = (DateTimeEntry) component;
        String clientId = component.getClientId(context);

        DateTimeConverter converter = dateTimeEntry.resolveDateTimeConverter(context);
        TimeZone tz = dateTimeEntry.resolveTimeZone(context);
        Locale currentLocale = dateTimeEntry.resolveLocale(context);
//        currentLocale = Locale.CANADA_FRENCH;
//        currentLocale = Locale.TAIWAN;
//        currentLocale = Locale.GERMANY;
//        currentLocale = new Locale("es", "MX");
        SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, currentLocale);
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
        Date date;
        if ("ice.ser".equals(paramMap.get("ice.submit.type"))) {
            date = (Date) converter.getAsObject(context, component, (String) dateTimeEntry.getSubmittedValue());
        } else {
            date = (Date) dateTimeEntry.getValue();
        }
        if (date == null) {
            Calendar calendar = Calendar.getInstance(tz, currentLocale);
            date = calendar.getTime();
        }
        String dateStr = converter.getAsString(context, dateTimeEntry, date);
        formatter.setTimeZone(tz);
        formatter.applyPattern("MM/yyyy");
        String pageDate = formatter.format(date);
        formatter.applyPattern("MM/dd/yyyy");
        String selectedDate = formatter.format(date);
        formatter.applyPattern("yyyy-M-d H:m");
        String hiddenValue = formatter.format(date);

//        System.out.println("DateTimeEntry.getDateTimeConverterPattern(converter) = " + DateTimeEntry.getDateTimeConverterPattern(converter));
        formatter.applyPattern(DateTimeEntry.getDateTimeConverterPattern(converter));
        StringBuffer stringBuffer = new StringBuffer();
        DateFormat.Field[] hourFields = {DateFormat.Field.HOUR0, DateFormat.Field.HOUR1,
                DateFormat.Field.HOUR_OF_DAY0, DateFormat.Field.HOUR_OF_DAY1};
        String[] hourFieldNames = {"HOUR0", "HOUR1", "HOUR_OF_DAY0", "HOUR_OF_DAY1"};
        FieldPosition fieldPosition;
        int beginIndex;
        int endIndex;
        String hourField = "";
        int savedBeginIndex = Integer.MAX_VALUE, savedEndIndex = Integer.MAX_VALUE;
        for (int i = 0; i < hourFields.length; i++) {
            stringBuffer.setLength(0);
            fieldPosition = new FieldPosition(hourFields[i]);
            formatter.format(date, stringBuffer, fieldPosition);
            beginIndex = fieldPosition.getBeginIndex();
            endIndex = fieldPosition.getEndIndex();
            if (beginIndex < endIndex && beginIndex < savedBeginIndex) {
                hourField = hourFieldNames[i];
                savedBeginIndex = beginIndex;
                savedEndIndex = endIndex;
            }
        }
        String selectedHour = "";
        if (!hourField.equals("")) {
            selectedHour = stringBuffer.substring(savedBeginIndex, savedEndIndex);
        }
        String selectedMinute = String.valueOf(date.getMinutes());

        formatter.applyPattern("a");
        String amPmStr = formatter.format(date);
        DateFormatSymbols dateFormatSymbols = formatter.getDateFormatSymbols();
        String[] amPmStrings = dateFormatSymbols.getAmPmStrings();
        String[] longMonths = mapMonths(dateFormatSymbols);
        String[] shortWeekdays = mapWeekdays(dateFormatSymbols);
        StringBuffer unicodeLongMonths = new StringBuffer(), unicodeShortWeekdays = new StringBuffer();
        for (String longMonth : longMonths) {
            unicodeLongMonths.append(",\"").append(convertToEscapedUnicode(longMonth)).append("\"");
        }
        unicodeLongMonths.replace(0, 1, "[").append("]");
        for (String shortWeekday : shortWeekdays) {
            unicodeShortWeekdays.append(",\"").append(convertToEscapedUnicode(shortWeekday)).append("\"");
        }
        unicodeShortWeekdays.replace(0, 1, "[").append("]");

        String params = "'" + clientId + "'," +
                JSONBuilder.create().
                beginMap().
                    entry("pageDate", pageDate).
                    entry("selectedDate", selectedDate).
                endMap().toString()
                + "," +
                JSONBuilder.create().
                beginMap().
                    entry("dateStr", dateStr).
                    entry("hiddenValue", hiddenValue).
                    entry("selectedHour", selectedHour).
                    entry("selectedMinute", selectedMinute).
                    entry("hourField", hourField).
                    entry("amPmStr", amPmStr).
                    entry("amStr", amPmStrings[0]).
                    entry("pmStr", amPmStrings[1]).
                    entry("renderAsPopup", dateTimeEntry.isRenderAsPopup()).
                    entry("renderInputField", dateTimeEntry.isRenderInputField()).
                    entry("singleSubmit", dateTimeEntry.isSingleSubmit()).
                    entry("ariaEnabled", EnvUtils.isAriaEnabled(context)).
                    entry("disabled", dateTimeEntry.isDisabled()).
                    entry("longMonths", unicodeLongMonths.toString(), true).
                    entry("shortWeekdays", unicodeShortWeekdays.toString(), true).
                endMap().toString();
//        System.out.println("params = " + params);
        final UIComponent cal = component;
        final StringBuilder effect = new StringBuilder();
        Utils.iterateEffects(new AnimationBehavior.Iterator(component) {
			public void next(String event, AnimationBehavior effectBehavior) {
				effectBehavior.encodeBegin(FacesContext.getCurrentInstance(), cal);
				effect.append(effectBehavior.getScript(new ClientBehaviorContextImpl(this.getUIComponent(), "transition"), false));	
			}
		});   
//        System.out.println(effect.toString());
        effect.append(";");
        effect.append("ice.component.calendar.updateProperties(");
        effect.append(params);
        effect.append(");");
       // ScriptWriter.insertScript(context, component, effect.toString());

//        System.out.println("effect = " + effect);
        ScriptWriter.insertScript(context, component, effect.toString());
        
        writer.endElement(HTML.DIV_ELEM);
     
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
//        System.out.println("\nDateTimeEntryRenderer.decode");
//        printParams();
        super.decode(context, component);
        DateTimeEntry dateTimeEntry = (DateTimeEntry) component;
        String clientId = component.getClientId(context);
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
        String dateString = paramMap.get(clientId + "_value");
        
        if (null == dateString) return;
        DateTimeConverter converter = dateTimeEntry.resolveDateTimeConverter(context);
        SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT, dateTimeEntry.resolveLocale(context));
        formatter.setTimeZone(dateTimeEntry.resolveTimeZone(context));
        formatter.applyPattern("yyyy-M-d H:m");
        try {
//            System.out.println("formatter.toPattern() = " + formatter.toPattern());
//            System.out.println("formatter.toLocalizedPattern() = " + formatter.toLocalizedPattern());
//            System.out.println("DateTimeEntry.getDateTimeConverterPattern(converter) = " + DateTimeEntry.getDateTimeConverterPattern(converter));
            dateString = converter.getAsString(context, dateTimeEntry, formatter.parse(dateString));
        } catch (ParseException e) {
//            e.printStackTrace();
        }
        dateTimeEntry.setSubmittedValue(dateString);
        if ("ice.ser".equals(paramMap.get("ice.submit.type"))) {
//            System.out.println("Skip to renderResponse()");
            context.renderResponse();
        }
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
//        System.out.println("\nDateTimeEntryRenderer.getConvertedValue");
//        System.out.println("submittedValue = " + submittedValue);
        super.getConvertedValue(context, component, submittedValue);
        DateTimeEntry dateTimeEntry = (DateTimeEntry) component;
        return dateTimeEntry.resolveDateTimeConverter(context).getAsObject(context, component, (String) submittedValue);
    }

    private void printParams() {
        Map<String, String[]> paramValuesMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterValuesMap();
        String key;
        String[] values;
        for (Map.Entry<String, String[]> entry : paramValuesMap.entrySet()) {
            key = entry.getKey();
            values = entry.getValue();
            System.out.print(key);
            System.out.print(" = ");
            for (String value : values) {
                System.out.print(value);
                System.out.print(", ");
            }
            System.out.println();
        }
    }

    public static String convertToEscapedUnicode(String s) {
        char[] chars = s.toCharArray();
        String hexStr;
        StringBuffer stringBuffer = new StringBuffer(chars.length * 6);
        String[] leadingZeros = {"0000", "000", "00", "0", ""};
        for (int i = 0; i < chars.length; i++) {
            hexStr = Integer.toHexString(chars[i]).toUpperCase();
            stringBuffer.append("\\u");
            stringBuffer.append(leadingZeros[hexStr.length()]);
//            stringBuffer.append("0000".substring(0, 4 - hexStr.length()));
            stringBuffer.append(hexStr);
        }
        return stringBuffer.toString();
    }

    // Copied from 1.8.2
    /**
     * @param symbols
     * @return months - String[] containing localized month names
     */
    public static String[] mapMonths(DateFormatSymbols symbols) {
        String[] months = new String[12];

        String[] localeMonths = symbols.getMonths();

        months[0] = localeMonths[Calendar.JANUARY];
        months[1] = localeMonths[Calendar.FEBRUARY];
        months[2] = localeMonths[Calendar.MARCH];
        months[3] = localeMonths[Calendar.APRIL];
        months[4] = localeMonths[Calendar.MAY];
        months[5] = localeMonths[Calendar.JUNE];
        months[6] = localeMonths[Calendar.JULY];
        months[7] = localeMonths[Calendar.AUGUST];
        months[8] = localeMonths[Calendar.SEPTEMBER];
        months[9] = localeMonths[Calendar.OCTOBER];
        months[10] = localeMonths[Calendar.NOVEMBER];
        months[11] = localeMonths[Calendar.DECEMBER];

        return months;
    }

    // Copied from 1.8.2. Modified indexes.
    private static String[] mapWeekdays(DateFormatSymbols symbols) {
        String[] weekdays = new String[7];

        String[] localeWeekdays = symbols.getShortWeekdays();

        weekdays[0] = localeWeekdays[Calendar.SUNDAY];
        weekdays[1] = localeWeekdays[Calendar.MONDAY];
        weekdays[2] = localeWeekdays[Calendar.TUESDAY];
        weekdays[3] = localeWeekdays[Calendar.WEDNESDAY];
        weekdays[4] = localeWeekdays[Calendar.THURSDAY];
        weekdays[5] = localeWeekdays[Calendar.FRIDAY];
        weekdays[6] = localeWeekdays[Calendar.SATURDAY];

        return weekdays;
    }
}