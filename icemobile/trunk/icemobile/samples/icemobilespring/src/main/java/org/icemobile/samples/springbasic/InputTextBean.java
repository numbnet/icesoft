package org.icemobile.samples.springbasic;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.SessionAttributes;

import java.util.Date;

/**
 * This is a sample backing bean for the MVC supported state
 * The properties should be the same
 */
@SessionAttributes("inputTextBean")
public class InputTextBean {

    // One property for each switch on the page
    private String text;
    private double number;
    private String inputTextArea;
    private String password;
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private Date date;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public double getNumber() {
        return number;
    }

    public void setNumber(double number) {
        this.number = number;
    }

    public String getInputTextArea() {
        return inputTextArea;
    }

    public void setInputTextArea(String inputTextArea) {
        this.inputTextArea = inputTextArea;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
