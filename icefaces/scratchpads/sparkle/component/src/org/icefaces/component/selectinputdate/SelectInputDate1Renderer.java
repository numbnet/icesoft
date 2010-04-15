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

public class SelectInputDate1Renderer extends Renderer {
    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        System.out.println("\nSelectInputDate1Renderer.encodeBegin");
        super.encodeBegin(context, component);
        ResponseWriter writer = context.getResponseWriter();
        String clientId = component.getClientId(context);
        writer.startElement(HTML.DIV_ELEM, component);
        writer.writeAttribute(HTML.ID_ATTR, clientId, "clientId");
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        System.out.println("\nSelectInputDate1Renderer.encodeEnd");
        super.encodeEnd(context, component);
        ResponseWriter writer = context.getResponseWriter();
        SelectInputDate1 selectInputDate = (SelectInputDate1) component;
        String clientId = component.getClientId(context);
        writer.endElement(HTML.DIV_ELEM);

        Date value = (Date) selectInputDate.getValue();
//        Date value = selectInputDate.getSelectedDate();
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
        writer.startElement(HTML.SCRIPT_ELEM, component);
        writer.write("Calendar.init('" + clientId + "','" + pageDate + "','" + selectedDate + "'," + selectedHour + "," + selectedMinute + ");");
//        writer.writeAttribute(HTML.ID_ATTR, clientId + "_js", null);
//        writer.writeText("Calendar.init('" + clientId + "','" + pageDate + "','" + selectedDate + "'," + selectedHour + "," + selectedMinute + ");", null);
//        writer.writeText("YAHOO.example.calendar.init();", null);
        writer.endElement(HTML.SCRIPT_ELEM);
    }

    @Override
    public void decode(FacesContext context, UIComponent component) {
        System.out.println("\nSelectInputDate1Renderer.decode");
        super.decode(context, component);
        SelectInputDate1 selectInputDate = (SelectInputDate1) component;
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
        SelectInputDate1 selectInputDate = (SelectInputDate1) component;
        return selectInputDate.getConverter().getAsObject(context, component, (String) submittedValue);
    }
}
