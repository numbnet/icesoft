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

@ManagedBean(name="panelConfirmation")
@ViewScoped
public class PanelConfirmationBean implements Serializable {
    private String message ="original";
    private String submitMesage="....working";
    private String acceptLabel="accept";
    private String cancelLabel="cancel";
    private boolean disabled = false;
    private String type="both";
    private int count = 0;
    private String label = "not triggered";
    private boolean menuButtonDisabled= false;
    private String submitMessage="...working";
   
    public PanelConfirmationBean(){

    }
    public void incrementCount(ActionEvent event) {
        count++;
    }

    public int getCount() {
        return count;
    }
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAcceptLabel() {
        return acceptLabel;
    }

    public void setAcceptLabel(String acceptLabel) {
        this.acceptLabel = acceptLabel;
    }

    public String getCancelLabel() {
        return cancelLabel;
    }

    public void setCancelLabel(String cancelLabel) {
        this.cancelLabel = cancelLabel;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubmitMesage() {
        return submitMesage;
    }

    public void setSubmitMesage(String submitMesage) {
        this.submitMesage = submitMesage;
    }

    public boolean isMenuButtonDisabled() {
        return menuButtonDisabled;
    }

    public void setMenuButtonDisabled(boolean menuButtonDisabled) {
        this.menuButtonDisabled = menuButtonDisabled;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public void actionMethod2(ActionEvent ae){
        try{
            Thread.sleep(5000);
            this.label="After actionMethod2";
        }  catch (Exception e){
           System.out.println("exception in actionMethod2") ;
        }
    }

}
