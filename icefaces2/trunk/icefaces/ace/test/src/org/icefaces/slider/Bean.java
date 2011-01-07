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

package org.icefaces.slider;

import javax.faces.bean.SessionScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ValueChangeEvent;

@ManagedBean (name="sliderBean")
@SessionScoped
public class Bean {
    private int value1;
    private int value2;
    private int value3;
    private int value4;
    private int value5;
    private int value6;
    private int value7;        
    private String valueChangeString = "Event: ";
    
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
        System.out.println("Slider value changed to: " + event.getNewValue() );
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
    
}
