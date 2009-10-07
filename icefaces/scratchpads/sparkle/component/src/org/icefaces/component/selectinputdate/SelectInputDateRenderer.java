package org.icefaces.component.selectinputdate;

import org.icefaces.component.utils.HTML;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Date;
import java.util.Calendar;

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
        super.encodeEnd(context, component);
        ResponseWriter writer = context.getResponseWriter();
        SelectInputDate selectInputDate = (SelectInputDate) component;
        String clientId = component.getClientId(context);
        writer.endElement(HTML.DIV_ELEM);

        Date value = selectInputDate.getSelectedDate();
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
        writer.endElement(HTML.SCRIPT_ELEM);
    }
}
