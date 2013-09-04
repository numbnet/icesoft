/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

@ManagedBean(name="submitNotification")
@ViewScoped
public class SubmitNotificationBean implements Serializable {
    private String name ="name";
    private boolean disabled = false;
    private String label = "not triggered";
    private boolean menuButtonDisabled= false;
    private String submitMessage="...working";
    private String style;
    private String styleClass;
   
    public SubmitNotificationBean(){

    }
    public void testSubmitNotification(ActionEvent ae){
        try{
            Thread.sleep(3000);
            this.name="updateprocesscomplete";
        }  catch (Exception e){

        }
        this.label=("thread Timer finished");
    }

    public String getName() {
        return name;
    }

    public void setName(String message) {
        this.name = message;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isMenuButtonDisabled() {
        return menuButtonDisabled;
    }

    public void setMenuButtonDisabled(boolean menuButtonDisabled) {
        this.menuButtonDisabled = menuButtonDisabled;
    }


    public String getSubmitMessage() {
        return submitMessage;
    }

    public void setSubmitMessage(String submitMessage) {
        this.submitMessage = submitMessage;
    }

    public void actionMethod(ActionEvent ae){
        try{
            Thread.sleep(5000);
            this.label="After actionMethod";
        }  catch (Exception e){
           System.out.println("exception in actionMethod") ;
        }
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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
}
