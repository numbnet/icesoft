/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

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
    private float stepPercent = 10.0f;
    private int value = 50;
    private String valueChangeString = "Event: ";
    private String submitOn = "slideEnd";
    private String axis = "x";
    private boolean clickableRail = true;
    private boolean showLabels = false;

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

    public float getStepPercent() {
        return stepPercent;
    }

    public void setStepPercent(float stepPercent) {
        this.stepPercent = stepPercent;
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
    public void validateStepPercent(FacesContext context, UIComponent component, java.lang.Object newValue) {
        float stepPercent = ((Number) newValue).floatValue();
        if (stepPercent < 0.0f || stepPercent > 100.0f) {
            throw new ValidatorException(new FacesMessage("Step Percent must be between 0 and 100 inclusive, as it is a percent."));
        }
    }

    public boolean isShowLabels() {
        return showLabels;
    }

    public void setShowLabels(boolean showLabels) {
        this.showLabels = showLabels;
    }
}
