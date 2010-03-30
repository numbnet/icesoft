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
    private boolean renderAsPopup;
    private boolean renderInputField;
    private boolean singleSubmit;

    public CalendarBean() throws ParseException {
//        selectedDate = new SimpleDateFormat("yyyy-M-d H:m z").parse("2008-4-30 13:9 Pacific Daylight Time");
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
    }

    public boolean isRenderAsPopup() {
        return renderAsPopup;
    }

    public void setRenderAsPopup(boolean renderAsPopup) {
        this.renderAsPopup = renderAsPopup;
    }

    public boolean isRenderInputField() {
        return renderInputField;
    }

    public void setRenderInputField(boolean renderInputField) {
        this.renderInputField = renderInputField;
    }

    public boolean isSingleSubmit() {
        return singleSubmit;
    }

    public void setSingleSubmit(boolean singleSubmit) {
        this.singleSubmit = singleSubmit;
    }
}