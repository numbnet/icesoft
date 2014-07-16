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

package org.icefaces.mobile.input;

import java.io.Serializable;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.faces.context.FacesContext;

@ManagedBean(name="button")
@ViewScoped
public class ButtonBean implements Serializable {
    private String someText;
    private String someText2;
    private String eventString="none";
    private boolean disabled = false;
    private boolean noAjax = false;
    private boolean singleSubmit = false;
    private String buttonType="default";
    private String type;
    private String value;
    private String style;
    private String styleClass;
    private String selectedButton  = "Yes";
    private String orientation = "horizontal";
    private String buttonGroupStyle ;
    private String param1;
    private String param2;
    private String param1Returned;
    private String param2Returned;
    private boolean groupDisabled = false;

    public ButtonBean(){

    }

    public void method(ActionEvent ae){
        this.param1Returned =getRequestParameter("param1");
        this.param2Returned =getRequestParameter("param2");
    }

 /*   public void updateAttribute(ActionEvent ae){
        this.attrTest = (String)ae.getComponent().getAttributes().get("attrib");
    } */
    public void clear() {
         this.eventString="clear";
    }

    public String getEventString() {
        return eventString;
    }
    public void setEventString(String eventString) {
        this.eventString = eventString;
    }

    public static String getRequestParameter(String name){
    	return (String) FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get(name);
    }

    public String getSomeText() {
        return someText;
    }

    public void setSomeText(String someText) {
        this.someText = someText;
    }

    public String getSomeText2() {
        return someText2;
    }

    public void setSomeText2(String someText2) {
        this.someText2 = someText2;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isNoAjax() {
        return noAjax;
    }

    public void setNoAjax(boolean noAjax) {
        this.noAjax = noAjax;
    }

    public boolean isSingleSubmit() {
        return singleSubmit;
    }

    public void setSingleSubmit(boolean singleSubmit) {
        this.singleSubmit = singleSubmit;
    }

    public String getButtonType() {
        return buttonType;
    }

    public void setButtonType(String buttonType) {
        this.buttonType = buttonType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return this.buttonType;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getSelectedButton() {
        return selectedButton;
    }

    public void setSelectedButton(String selectedButton) {
        this.selectedButton = selectedButton;
    }
    public void commandGroup1Selected(ActionEvent event){
        selectedButton = (String)event.getComponent().getAttributes().get("selectedName");
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getButtonGroupStyle() {
        return buttonGroupStyle;
    }

    public void setButtonGroupStyle(String buttonGroupStyle) {
        this.buttonGroupStyle = buttonGroupStyle;
    }

    public String getParam1Returned() {
        return param1Returned;
    }

    public void setParam1Returned(String param1Returned) {
        this.param1Returned = param1Returned;
    }

    public String getParam2Returned() {
        return param2Returned;
    }

    public void setParam2Returned(String param2Returned) {
        this.param2Returned = param2Returned;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public boolean isGroupDisabled() {
        return groupDisabled;
    }

    public void setGroupDisabled(boolean groupDisabled) {
        this.groupDisabled = groupDisabled;
    }
}
