package org.icefaces.demo;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.util.Date;

@ManagedBean
@SessionScoped
public class Calendar {
    private Date selectedDate = new Date(97, 6, 23, 12, 38);

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }
}
