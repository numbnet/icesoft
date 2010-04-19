package org.icefaces.component.selectinputdate;

import org.icefaces.component.utils.HTML;
import org.icefaces.util.EnvUtils;

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

public class SelectInputDateRenderer extends Renderer {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);
        writer.startElement(HTML.DIV_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, clientId, "clientId");
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        System.out.println("\nSelectInputDateRenderer.encodeEnd");
        super.encodeEnd(context, component);
        ResponseWriter writer = context.getResponseWriter();
        SelectInputDate selectInputDate = (SelectInputDate) component;
        String clientId = component.getClientId(context);
        writer.endElement(HTML.DIV_ELEM);

        DateTimeConverter converter = selectInputDate.resolveDateTimeConverter(context);
        TimeZone tz = selectInputDate.resolveTimeZone(context);
        Locale currentLocale = selectInputDate.resolveLocale(context);
        SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, currentLocale);
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
        Date date;
        if (paramMap.get("formatSubmit") != null) {
            date = (Date) converter.getAsObject(context, component, (String) selectInputDate.getSubmittedValue());
        } else {
            date = (Date) selectInputDate.getValue();
        }
        if (date == null) {
            Calendar calendar = Calendar.getInstance(tz, currentLocale);
            date = calendar.getTime();
        }
        String dateStr = converter.getAsString(context, selectInputDate, date);
        formatter.setTimeZone(tz);
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

        boolean renderAsPopup = selectInputDate.isRenderAsPopup();
        boolean renderInputField = selectInputDate.isRenderInputField();
        boolean singleSubmit = selectInputDate.isSingleSubmit();
        boolean ariaEnabled = EnvUtils.isAriaEnabled(context);

        String params = "{divId:'" + clientId + "',dateStr:'" + dateStr + "',pageDate:'" + pageDate +
                "',selectedDate:'" + selectedDate + "',selectedHour:'" + selectedHour +
                "',selectedMinute:'" + selectedMinute + "',hourField:'" + hourField + "',amPmStr:'" + amPmStr +
                "',amStr:'" + amPmStrings[0] + "',pmStr:'" + amPmStrings[1] +
                "',renderAsPopup:" + renderAsPopup + ",renderInputField:" + renderInputField +
                ",singleSubmit:" + singleSubmit + ",ariaEnabled:" + ariaEnabled + "}";
        System.out.println("params = " + params);
        writer.startElement(HTML.SCRIPT_ELEM, component);
        writer.write("YAHOO.icefaces.calendar.init(" + params + ");");
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        System.out.println("\nSelectInputDateRenderer.decode");
        super.decode(context, component);
        SelectInputDate selectInputDate = (SelectInputDate) component;
        String clientId = component.getClientId(context);
        Map<String, String> paramMap = context.getExternalContext().getRequestParameterMap();
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
        String dateString = paramMap.get(clientId + "_value");
        DateTimeConverter converter = selectInputDate.resolveDateTimeConverter(context);
        SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT,
                DateFormat.SHORT, selectInputDate.resolveLocale(context));
        formatter.setTimeZone(selectInputDate.resolveTimeZone(context));
        formatter.applyPattern("yyyy-M-d H:m");
        try {
            dateString = converter.getAsString(context, selectInputDate, formatter.parse(dateString));
        } catch (ParseException e) {
//            e.printStackTrace();
        }
        selectInputDate.setSubmittedValue(dateString);
        if (paramMap.get("formatSubmit") != null) {
            context.renderResponse();
        }
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        System.out.println("\nSelectInputDateRenderer.getConvertedValue");
        System.out.println("submittedValue = " + submittedValue);
        super.getConvertedValue(context, component, submittedValue);
        SelectInputDate selectInputDate = (SelectInputDate) component;
        return selectInputDate.resolveDateTimeConverter(context).getAsObject(context, component, (String) submittedValue);
    }
}