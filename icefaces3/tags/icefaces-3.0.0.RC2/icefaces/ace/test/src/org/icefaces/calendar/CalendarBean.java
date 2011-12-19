/*
 * Copyright 2010-2011 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.icefaces.calendar;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

@ManagedBean
@SessionScoped
public class CalendarBean implements Serializable {
    private Date selectedDate;
    private boolean renderAsPopup;
    private boolean renderInputField;
    private boolean singleSubmit = false;
    private String pattern = "MMM/dd/yyyy hh:mm a";

//    public CalendarBean() throws ParseException {
    public CalendarBean() {
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
//        System.out.println("\nCalendarBean.setPattern");
//        System.out.println("pattern = " + pattern);
        this.pattern = pattern;
    }
}