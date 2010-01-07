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

public class SelectInputDate2Renderer extends Renderer {
    SimpleDateFormat formatter = (SimpleDateFormat) DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.CANADA);

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        System.out.println("\nSelectInputDate2Renderer.encodeBegin");
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);
        writer.startElement(HTML.DIV_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, clientId, "clientId");
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        System.out.println("\nSelectInputDate2Renderer.encodeEnd");
        super.encodeEnd(context, component);
        ResponseWriter writer = context.getResponseWriter();
        SelectInputDate2 selectInputDate = (SelectInputDate2) component;
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
        formatter.applyPattern("H");
        String selectedHour = formatter.format(date);
        formatter.applyPattern("m");
        String selectedMinute = formatter.format(date);
        String params = "{divId:'" + clientId + "',dateStr:'" + dateStr + "',pageDate:'" + pageDate +
                "',selectedDate:'" + selectedDate + "',selectedHour:'" + selectedHour +
                "',selectedMinute:'" + selectedMinute + "'}";
        System.out.println("params = " + params);
        writer.startElement(HTML.SCRIPT_ELEM, component);
        writer.write("YAHOO.icefaces.calendar.init(" + params + ");");
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        System.out.println("\nSelectInputDate2Renderer.decode");
        super.decode(context, component);
        SelectInputDate2 selectInputDate = (SelectInputDate2) component;
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
        System.out.println("\nSelectInputDate2Renderer.getConvertedValue");
        System.out.println("submittedValue = " + submittedValue);
        super.getConvertedValue(context, component, submittedValue);
        SelectInputDate2 selectInputDate = (SelectInputDate2) component;
        return selectInputDate.getConverter().getAsObject(context, component, (String) submittedValue);
    }
}