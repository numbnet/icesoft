package org.icemobile.samples.springbasic;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;

/**
 * This is a sample backing bean for the MVC supported state
 * The properties should be the same
 */
@SessionAttributes("dateTimeSpinnerBean")
public class DateTimeSpinnerBean {

    private String myDate;

    @ModelAttribute("dateTimeSpinnerBean")
    public DateTimeSpinnerBean createBean() {
        return new DateTimeSpinnerBean();
    }

    public String getDate() {
        return myDate;
    }

    public void setDate(String date) {
        this.myDate = date;
    }
}
