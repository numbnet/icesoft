package org.icefaces.demo;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.util.Date;

@ManagedBean
@SessionScoped
public class Calendar {
    private java.util.Calendar calendar = java.util.Calendar.getInstance();
    private Date selectedDate;

    public Calendar() {
        calendar.set(2009, 10, 18, 20, 38);
        selectedDate = calendar.getTime();
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }
}
