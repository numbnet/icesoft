package org.icefaces.sliderentry;

import javax.faces.application.FacesMessage;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;
import javax.faces.validator.ValidatorException;
import java.io.Serializable;

@ManagedBean (name="sliderBean")
@SessionScoped
public class Bean implements Serializable {
    private int value1;
    private int value2;
    private int value3;
    private int value4;
    private int value5;
    private int value6;
    private int value7;
    private int minValue = 0;
    private int maxValue = 100;
    private int value = 50;
    private String valueChangeString = "Event: ";
    private String submitOn = "slideEnd";
    private String axis = "x";
    private boolean clickableRail = true;

    public int getValue1() {
        return value1;
    }
    public void setValue1(int value1) {
        this.value1 = value1;
    }
    public int getValue2() {
        return value2;
    }
    public void setValue2(int value2) {
        this.value2 = value2;
    }
    public int getValue3() {
        return value3;
    }
    public void setValue3(int value3) {
        this.value3 = value3;
    }
    public int getValue4() {
        return value4;
    }
    public void setValue4(int value4) {
        this.value4 = value4;
    }

    public void sliderChanged(ValueChangeEvent event) {
//        System.out.println("Slider value changed to: " + event.getNewValue() );
        valueChangeString = ("Slider ValueChangeEvent fired oldValue = "+ event.getOldValue() + " : newValue = " +  event.getNewValue());
    }
    public String getValueChangeString() {
        return valueChangeString;
    }
    public void setValueChangeString(String valueChangeString) {
        this.valueChangeString = valueChangeString;
    }
    public int getValue5() {
        return value5;
    }
    public void setValue5(int value5) {
        this.value5 = value5;
    }
    public int getValue6() {
        return value6;
    }
    public void setValue6(int value6) {
        this.value6 = value6;
    }
    public int getValue7() {
        return value7;
    }
    public void setValue7(int value7) {
        this.value7 = value7;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getSubmitOn() {
        return submitOn;
    }

    public void setSubmitOn(String submitOn) {
        this.submitOn = submitOn;
    }

    public String getAxis() {
        return axis;
    }

    public void setAxis(String axis) {
        this.axis = axis;
    }

    public boolean isClickableRail() {
        return clickableRail;
    }

    public void setClickableRail(boolean clickableRail) {
        this.clickableRail = clickableRail;
    }
    public void validateMinValue(FacesContext context, UIComponent component, java.lang.Object newValue) {
        if (!(maxValue - (Integer) newValue > 3)) {
            throw new ValidatorException(new FacesMessage("Min must be < max by at least 3 units."));
        }
    }
    public void validateMaxValue(FacesContext context, UIComponent component, java.lang.Object newValue) {
        if (!((Integer) newValue - minValue > 3)) {
            throw new ValidatorException(new FacesMessage("Max must be > min by at least 3 units."));
        }
    }
}
