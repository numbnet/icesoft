package org.icefaces.mobile;

import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.List;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;
import javax.faces.validator.ValidatorException;
import javax.faces.application.FacesMessage;

@ManagedBean(name="date")
@ViewScoped
public class DateBean implements Serializable{
    private Date selectedDate;
    private Date date2;
    private boolean singleSubmit;
    private boolean required;
  //  private boolean valid;
    private String pattern;
    private boolean rendered;
    private String eventString="none";
    private List<DateNtry> dateList = new  ArrayList <DateNtry>();

    public DateBean() {
        dateList.add(new DateNtry("dateOne"));
        dateList.add(new DateNtry("dateTwo"));
        dateList.add(new DateNtry("dateThree"));
        try{
            this.pattern = "MM-dd-yyyy";
            selectedDate = new SimpleDateFormat("yyyy-M-d H:m z").parse("2008-4-30 13:9 Pacific Daylight Time");
        } catch (Exception e){
            System.out.println("PROBLEM PARSING DATE SO SETTING TO TODAY!");
            selectedDate = new Date();
        }
    }

    public Date getSelectedDate() {
        return selectedDate;
    }

    public void setSelectedDate(Date selectedDate) {
        this.selectedDate = selectedDate;
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
        private String title;

        public DateNtry (String title){
             this.title = title;
            adate = new Date();
        }
        public Date getAdate(){
            return adate;
        }
        public void setAdate(Date dateIn){
            this.adate = dateIn;
        }
        public String getTitle(){
            return this.title;
        }
        public void setTitle(String title){
            this.title = title;
        }

    }
}