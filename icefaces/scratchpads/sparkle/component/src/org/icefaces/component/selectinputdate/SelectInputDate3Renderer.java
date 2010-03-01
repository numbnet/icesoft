package org.icefaces.component.selectinputdate;

import org.icefaces.component.utils.HTML;

import javax.faces.component.UIComponent;
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

public class SelectInputDate3Renderer extends Renderer {
    private SimpleDateFormat formatter;

    {
        formatter = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.CANADA);
        formatter.setLenient(false);
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        System.out.println("\nSelectInputDate3Renderer.encodeBegin");
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);
        writer.startElement(HTML.DIV_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, clientId, "clientId");
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        System.out.println("SelectInputDate3Renderer.encodeEnd");
        super.encodeEnd(context, component);
        ResponseWriter writer = context.getResponseWriter();
        SelectInputDate3 selectInputDate = (SelectInputDate3) component;
        String clientId = component.getClientId(context);
        writer.endElement(HTML.DIV_ELEM);

        DateTimeConverter converter = (DateTimeConverter) selectInputDate.getConverter();
        Date date = (Date) selectInputDate.getValue();
        if (date == null) {
            Calendar calendar = Calendar.getInstance(converter.getTimeZone(), converter.getLocale());
            date = calendar.getTime();
        }
        String dateStr = converter.getAsString(context, selectInputDate, date);
        formatter.setTimeZone(converter.getTimeZone());
        formatter.applyPattern("MM/yyyy");
        String pageDate = formatter.format(date);
        formatter.applyPattern("MM/dd/yyyy");
        String selectedDate = formatter.format(date);

        formatter.applyPattern(converter.getPattern());
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

        String minDate = selectInputDate.getMinDate();
        String maxDate = selectInputDate.getMaxDate();
        String disabledDates = selectInputDate.getDisabledDates();
        String highlightUnit = selectInputDate.getHighlightUnit();
        String highlightValue = selectInputDate.getHighlightValue();
        String highlightClass = selectInputDate.getHighlightClass();
        boolean renderAsPopup = selectInputDate.isRenderAsPopup();
        boolean renderInputField = selectInputDate.isRenderInputField();

        String params = "{divId:'" + clientId + "',dateStr:'" + dateStr + "',pageDate:'" + pageDate +
                "',selectedDate:'" + selectedDate + "',selectedHour:'" + selectedHour +
                "',selectedMinute:'" + selectedMinute + "',hourField:'" + hourField + "',amPmStr:'" + amPmStr +
                "',amStr:'" + amPmStrings[0] + "',pmStr:'" + amPmStrings[1] + "',minDate:'" + minDate +
                "',maxDate:'" + maxDate + "',disabledDates:'" + disabledDates + "',highlightUnit:'" + highlightUnit +
                "',highlightValue:'" + highlightValue + "',highlightClass:'" + highlightClass +
                "',renderAsPopup:" + renderAsPopup + ",renderInputField:" + renderInputField + "}";
        System.out.println("params = " + params);
        writer.startElement(HTML.SCRIPT_ELEM, component);
        writer.write("YAHOO.icefaces.calendar.init(" + params + ");");
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        System.out.println("\nSelectInputDate3Renderer.decode");
        super.decode(context, component);
        SelectInputDate3 selectInputDate = (SelectInputDate3) component;
        String clientId = component.getClientId(context);
//        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
        Map<String, String[]> paramValuesMap = context.getExternalContext().getRequestParameterValuesMap();
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
        String dateString = paramValuesMap.get(clientId)[0];
        DateTimeConverter converter = (DateTimeConverter) selectInputDate.getConverter();
        formatter.setTimeZone(converter.getTimeZone());
//        formatter.setTimeZone(TimeZone.getDefault());
        formatter.applyPattern("yyyy-M-d H:m");
        try {
            dateString = converter.getAsString(context, selectInputDate, formatter.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        selectInputDate.setSubmittedValue(dateString);
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        System.out.println("\nSelectInputDate3Renderer.getConvertedValue");
        System.out.println("submittedValue = " + submittedValue);
        super.getConvertedValue(context, component, submittedValue);
        SelectInputDate3 selectInputDate = (SelectInputDate3) component;
        return selectInputDate.getConverter().getAsObject(context, component, (String) submittedValue);
    }
}