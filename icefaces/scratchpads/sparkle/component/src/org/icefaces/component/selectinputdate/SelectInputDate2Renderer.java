package org.icefaces.component.selectinputdate;

import org.icefaces.component.utils.HTML;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import javax.faces.convert.ConverterException;
import java.io.IOException;
import java.util.Date;
import java.util.Calendar;
import java.util.Map;

public class SelectInputDate2Renderer extends Renderer {
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
        super.encodeEnd(context, component);
        ResponseWriter writer = context.getResponseWriter();
        SelectInputDate2 selectInputDate = (SelectInputDate2) component;
        String clientId = component.getClientId(context);
        writer.endElement(HTML.DIV_ELEM);

        Date value = (Date) selectInputDate.getValue();
        Calendar timeKeeper = Calendar.getInstance();
        timeKeeper.setTime(value == null ? new Date() : value);
        String pageDate = (timeKeeper.get(Calendar.MONTH) + 1) + "/" + timeKeeper.get(Calendar.YEAR);
        String selectedDate = (timeKeeper.get(Calendar.MONTH) + 1) + "/" + timeKeeper.get(Calendar.DAY_OF_MONTH) + "/" + timeKeeper.get(Calendar.YEAR);
        int selectedHour = timeKeeper.get(Calendar.HOUR_OF_DAY);
        int selectedMinute = timeKeeper.get(Calendar.MINUTE);
        System.out.println("pageDate = " + pageDate);
        System.out.println("selectedDate = " + selectedDate);
        System.out.println("selectedHour = " + selectedHour);
        System.out.println("selectedMinute = " + selectedMinute);
        String params = "{divId:'" + clientId + "'}";
        System.out.println("params = " + params);
        writer.startElement(HTML.SCRIPT_ELEM, component);
        writer.write("YAHOO.icefaces.calendar.init(" + params + ");");
//        writer.write("Calendar.init('" + clientId + "','" + pageDate + "','" + selectedDate + "'," + selectedHour + "," + selectedMinute + ");");
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        System.out.println("\nSelectInputDateRenderer.decode");
        super.decode(context, component);
        SelectInputDate2 selectInputDate = (SelectInputDate2) component;
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
        selectInputDate.setSubmittedValue(paramValuesMap.get(clientId)[0]);
//        selectInputDate.setSubmittedValue("10/23/1988 21:34");
    }

    @Override
    public Object getConvertedValue(FacesContext context, UIComponent component, Object submittedValue) throws ConverterException {
        super.getConvertedValue(context, component, submittedValue);
        SelectInputDate2 selectInputDate = (SelectInputDate2) component;
        return selectInputDate.getConverter().getAsObject(context, component, (String) submittedValue);
    }
}