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

package org.icemobile.samples.mobileshowcase.view.examples.input.input;


import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icefaces.util.JavaScriptRunner;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;
import org.icemobile.util.ClientDescriptor;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import java.io.Serializable;
import java.util.Date;

@Destination(
        title = "example.input.input.destination.title.short",
        titleExt = "example.input.input.destination.title.long",
        titleBack = "example.input.input.destination.title.back"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/input/input-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/input/input-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "input-example.xhtml",
                        resource = "/WEB-INF/includes/examples/input/input-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "InputBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/input/input/InputBean.java")
        }
)

@ManagedBean(name = InputBean.BEAN_NAME)
@SessionScoped
public class InputBean extends ExampleImpl<InputBean> implements
        Serializable {

    public static final String BEAN_NAME = "inputBean";

    // simple bean input value bindings

    private String textInput;
    private String passwordInput;
    private String email;
    private String date;
    private String textArea;
    private String urlInput;
    private Double numberInput;
    private Float numberStep = 1.0f;
    private String dateInput;


    public InputBean() {
        super(InputBean.class);
    }

    public String getTextInput() {
        return textInput;
    }

    public void setTextInput(String textInput) {
        this.textInput = textInput;
    }

    public String getPasswordInput() {
        return passwordInput;
    }

    public void setPasswordInput(String passWordInput) {
        this.passwordInput = passWordInput;
    }

    public Double getNumberInput() {
        return numberInput;
    }

    public void setNumberInput(Double numberInput) {
        this.numberInput = numberInput;
    }

    public String getDateInput() {
        return dateInput;
    }

    public void setDateInput(String dateInput) {
        this.dateInput = dateInput;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUrlInput() {
        return urlInput;
    }

    public void setUrlInput(String urlInput) {
        this.urlInput = urlInput;
    }

    public String getTextArea() {
        return textArea;
    }

    public void setTextArea(String textArea) {
        this.textArea = textArea;
    }
    
    public void action(){
        ClientDescriptor client = MobiJSFUtils.getClientDescriptor();
        if( client.isIOS() ){
            JavaScriptRunner.runScript(FacesContext.getCurrentInstance(), "window.scrollTo(0, 0);");
        }
    }

    public Float getNumberStep() {
        return numberStep;
    }

    public void setNumberStep(Float numberStep) {
        this.numberStep = numberStep;
    }
}