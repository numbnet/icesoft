/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.mobile;


import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.List;
import java.util.TimeZone;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.util.ClientDescriptor;

@ManagedBean(name="date")
@ViewScoped
public class DateBean implements Serializable{
    private Date selectedDate;
    private Date date2;
    private Date time1;
    private Date time2;
    private String dateString;
    private String dateString2;
    private boolean singleSubmit;
    private boolean required;
  //  private boolean valid;
    private String pattern;
    private boolean isUseNative = true;

    private String timePattern = "hh:mm a";
    private TimeZone zone;
    private boolean rendered;
    private String eventString="none";
    private String timeZone = "America/Edmonton";
    private List<DateNtry> dateList = new  ArrayList <DateNtry>();

    public DateBean() {
        dateList.add(new DateNtry("dateOne"));
        dateList.add(new DateNtry("dateTwo"));
        dateList.add(new DateNtry("dateThree"));
        try{
            this.timeZone = java.util.TimeZone.getDefault().getID();
         //   System.out.println(" timeZone="+timeZone+" id = "+java.util.TimeZone.getDefault().getID());
            this.pattern = "yyyy-MM-dd";
            selectedDate = new SimpleDateFormat("yyyy-M-d H:m z").parse("2008-4-30 13:9 Pacific Daylight Time");

        } catch (Exception e){
            System.out.println("PROBLEM PARSING DATE SO SETTING TO TODAY!");
            selectedDate = new Date();
        }
        try{
            time1 = new SimpleDateFormat("yyyy-M-d H:m z").parse("2010-10-30 13:9 Mountain Standard Time");
        }catch (Exception te){
            System.out.println("PROBLEM Setting TIME");
            time1 = new Date();
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

    public Date getTime1() {
        return time1;
    }

    public void setTime1(Date time1) {
        this.time1 = time1;
    }

    public Date getTime2() {
        return time2;
    }

    public void setTime2(Date time2) {
        this.time2 = time2;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }
    public String getTimePattern() {
        String result = timePattern;
        if (this.isUseNative()){
            ClientDescriptor client = MobiJSFUtils.getClientDescriptor();
            if( client.isIOS5() || client.isBlackBerryOS()){
                result = "HH:mm";
            }
        }
        return result;
    }

    public void setTimePattern(String timePattern) {
        this.timePattern = timePattern;
    }

    public boolean isUseNative() {
        return isUseNative;
    }

    public void setUseNative(boolean useNative) {
        isUseNative = useNative;
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
    public void clearDate(ActionEvent ae){
        this.selectedDate=new Date();
        this.date2 = new Date();
    }
    public void clearTime(ActionEvent ae){
        this.time1 = new Date();
        this.time2 = new Date();
    }

    public Date getDate2() {
        return date2;
    }

    public void setDate2(Date date2) {
        this.date2 = date2;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public String getDateString2() {
        return dateString2;
    }

    public void setDateString2(String dateString2) {
        this.dateString2 = dateString2;
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
