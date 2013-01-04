package com.icesoft.icefaces.tutorial.component.dateTimeEntry.yearRange;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@ManagedBean
@SessionScoped
public class DateTimeEntryBean {
    private Date selectedDate;
    private String yearRange = "c-5:c+5";

    public DateTimeEntryBean() throws ParseException {
        selectedDate = new SimpleDateFormat("yyyy-M-d H:m z").parse("2010-4-30 13:9 Pacific Daylight Time");
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public String getYearRange() {
        return yearRange;
    }

    public void setYearRange(String yearRange) {
        this.yearRange = yearRange;
    }
}
