package org.icefaces.mobile;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.faces.application.FacesMessage;

@ManagedBean(name="date")
@SessionScoped
public class DateBean implements Serializable{
    private Date selectedDate;
    private Date date2;
    private boolean renderAsPopup;
    private boolean renderInputField;
    private boolean singleSubmit;
    private boolean required;
  //  private boolean valid;
    private boolean immediate;
    private String pattern = "MMM/dd/yyyy hh:mm a";
    private boolean rendered;
    private String eventString="none";
    private List<DateNtry> dateList = new  ArrayList <DateNtry>();

    public DateBean() {
        dateList.add(new DateNtry());
        dateList.add(new DateNtry());
        dateList.add(new DateNtry());
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

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        System.out.println("\nCalendarBean.setPattern");
        System.out.println("pattern = " + pattern);
        this.pattern = pattern;
    }
    
    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
    

    public boolean isRendered() {
        return rendered;
    }

    public void setRendered(boolean rendered) {
        this.rendered = rendered;
    }


    public void valueChangeListener() {
        System.out.println("CalendarBean.valueChangeListener");
        System.out.println("CurrentPhaseId = " + FacesContext.getCurrentInstance().getCurrentPhaseId());
    }
    public String getEventString() {
        return eventString;
    }
    public void setEventString(String eventString) {
        this.eventString = eventString;
    }
    public void defaultEvent(ActionEvent ae){
        String val = "DefaultEvent:";
        this.setEventString(val);
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public List<DateNtry> getDateList() {
        return dateList;
    }

    public void setDateList(List<DateNtry> dateList) {
        this.dateList = dateList;
    }

    public class DateNtry{
        private Date adate;

        public Date getAdate(){
            return adate;
        }
        public void setAdate(Date dateIn){
            this.adate = dateIn;
        }
    }
}