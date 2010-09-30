package org.icefaces.component.datetimeselector;

import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.JSONBuilder;
import org.icefaces.component.utils.ScriptWriter;
import org.icefaces.component.utils.Utils;
import org.icefaces.component.datetimeselector.DateTimeSelector;
import org.icefaces.component.animation.ClientBehaviorContextImpl;
import org.icefaces.component.animation.AnimationBehavior;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorContext.Parameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.convert.ConverterException;
import javax.faces.convert.DateTimeConverter;
import java.io.IOException;
import java.util.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.text.DateFormat;
import java.text.FieldPosition;

public class DateTimeSelectorRenderer extends Renderer {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);
        writer.startElement(HTML.DIV_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, clientId, "clientId");
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
        System.out.println("\nDateTimeSelectorRenderer.encodeEnd");
//        printParams();
        super.encodeEnd(context, component);
        ResponseWriter writer = context.getResponseWriter();
        DateTimeSelector dateTimeSelector = (DateTimeSelector) component;
        String clientId = component.getClientId(context);

        DateTimeConverter converter = dateTimeSelector.resolveDateTimeConverter(context);
        TimeZone tz = dateTimeSelector.resolveTimeZone(context);
        Locale currentLocale = dateTimeSelector.resolveLocale(context);
        SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, currentLocale);
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
        Date date;
        if ("ice.ser".equals(paramMap.get("ice.submit.type"))) {
            date = (Date) converter.getAsObject(context, component, (String) dateTimeSelector.getSubmittedValue());
        } else {
            date = (Date) dateTimeSelector.getValue();
        }
        if (date == null) {
            Calendar calendar = Calendar.getInstance(tz, currentLocale);
            date = calendar.getTime();
        }
        String dateStr = converter.getAsString(context, dateTimeSelector, date);
        formatter.setTimeZone(tz);
        formatter.applyPattern("MM/yyyy");
        String pageDate = formatter.format(date);
        formatter.applyPattern("MM/dd/yyyy");
        String selectedDate = formatter.format(date);

//        System.out.println("DateTimeSelector.getDateTimeConverterPattern(converter) = " + DateTimeSelector.getDateTimeConverterPattern(converter));
        formatter.applyPattern(DateTimeSelector.getDateTimeConverterPattern(converter));
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
        String[] amPmStrings = formatter.getDateFormatSymbols().getAmPmStrings();
        
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
                    entry("selectedHour", selectedHour).
                    entry("selectedMinute", selectedMinute).
                    entry("hourField", hourField).
                    entry("amPmStr", amPmStr).
                    entry("amStr", amPmStrings[0]).
                    entry("pmStr", amPmStrings[1]).
                    entry("renderAsPopup", dateTimeSelector.isRenderAsPopup()).
                    entry("renderInputField", dateTimeSelector.isRenderInputField()).
                    entry("singleSubmit", dateTimeSelector.isSingleSubmit()).
                    entry("ariaEnabled", EnvUtils.isAriaEnabled(context)).                    
                endMap().toString();
        System.out.println("params = " + params);
        final UIComponent cal = component;
        final StringBuilder effect = new StringBuilder();
        Utils.iterateEffects(new AnimationBehavior.Iterator(component) {
			public void next(String event, AnimationBehavior effectBehavior) {
				effectBehavior.encodeBegin(FacesContext.getCurrentInstance(), cal);
				effect.append(effectBehavior.getScript(new ClientBehaviorContextImpl(this.getUIComponent(), "transition"), false));	
			}
		});   
        System.out.println(effect.toString());
        effect.append(";");
        effect.append("ice.component.calendar.updateProperties(");
        effect.append(params);
        effect.append(");");
       // ScriptWriter.insertScript(context, component, effect.toString());    
        
        ScriptWriter.insertScript(context, component, effect.toString());
        
        writer.endElement(HTML.DIV_ELEM);
     
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        System.out.println("\nDateTimeSelectorRenderer.decode");
        printParams();
        super.decode(context, component);
        DateTimeSelector dateTimeSelector = (DateTimeSelector) component;
        String clientId = component.getClientId(context);
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
        String dateString = paramMap.get(clientId + "_value");
        
        if (null == dateString) return;
        DateTimeConverter converter = dateTimeSelector.resolveDateTimeConverter(context);
        SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT, dateTimeSelector.resolveLocale(context));
        formatter.setTimeZone(dateTimeSelector.resolveTimeZone(context));
        formatter.applyPattern("yyyy-M-d H:m");
        try {
//            System.out.println("formatter.toPattern() = " + formatter.toPattern());
//            System.out.println("formatter.toLocalizedPattern() = " + formatter.toLocalizedPattern());
//            System.out.println("DateTimeSelector.getDateTimeConverterPattern(converter) = " + DateTimeSelector.getDateTimeConverterPattern(converter));
            dateString = converter.getAsString(context, dateTimeSelector, formatter.parse(dateString));
        } catch (ParseException e) {
//            e.printStackTrace();
        }
        dateTimeSelector.setSubmittedValue(dateString);
        if ("ice.ser".equals(paramMap.get("ice.submit.type"))) {
            System.out.println("Skip to renderResponse()");
            context.renderResponse();
        }
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        System.out.println("\nDateTimeSelectorRenderer.getConvertedValue");
        System.out.println("submittedValue = " + submittedValue);
        super.getConvertedValue(context, component, submittedValue);
        DateTimeSelector dateTimeSelector = (DateTimeSelector) component;
        return dateTimeSelector.resolveDateTimeConverter(context).getAsObject(context, component, (String) submittedValue);
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
}