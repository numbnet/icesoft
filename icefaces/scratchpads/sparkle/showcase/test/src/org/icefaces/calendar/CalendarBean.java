package org.icefaces.calendar;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

@ManagedBean
@SessionScoped
public class CalendarBean {
    private Date selectedDate;

    public CalendarBean() throws ParseException {
//        selectedDate = new SimpleDateFormat("yyyy-M-d H:m z").parse("2008-4-30 13:9 Pacific Daylight Time");
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }
}