package org.icefaces.demo;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.util.Date;

@ManagedBean
@SessionScoped
public class Calendar {
    private Date selectedDate;

    public Calendar() {
        selectedDate = java.util.Calendar.getInstance().getTime();
        System.out.println("selectedDate.getTimezoneOffset() = " + selectedDate.getTimezoneOffset());
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }
}
