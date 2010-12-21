package org.icefaces.calendar;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.util.List;
import java.util.ArrayList;

@ManagedBean
@SessionScoped
public class CalendarTableBean {
    private List calendars = new ArrayList();

    public CalendarTableBean() {
        calendars.add(new CalendarBean());
        calendars.add(new CalendarBean());
        calendars.add(new CalendarBean());
    }

    public List getCalendars() {
        return calendars;
    }

    public void setCalendars(List calendars) {
        this.calendars = calendars;
    }
}