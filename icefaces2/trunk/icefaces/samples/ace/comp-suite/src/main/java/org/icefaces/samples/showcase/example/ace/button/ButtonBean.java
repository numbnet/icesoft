package org.icefaces.samples.showcase.example.ace.button;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/*
 * ICESOFT COMMERCIAL SOURCE CODE LICENSE V 1.1
 *
 * The contents of this file are subject to the ICEsoft Commercial Source
 * Code License Agreement V1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the
 * License at
 * http://www.icesoft.com/license/commercial-source-v1.1.html
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * Copyright 2009-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 */

@ComponentExample(
        title = "example.ace.button.title",
        description = "example.ace.button.description",
        example = "/resources/examples/ace/button/button.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="button.xhtml",
                    resource = "/resources/examples/ace/button/button.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="ButtonBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/button/ButtonBean.java")
        }
)
@Menu(
	title = "menu.ace.button.subMenu.title",
	menuLinks = {
	        @MenuLink(title = "menu.ace.button.subMenu.main",
	                isDefault = true,
                    exampleBeanName = ButtonBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.button.subMenu.checkbox",
                exampleBeanName = CheckboxButtonBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.button.subMenu.link",
                exampleBeanName = LinkButtonBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.button.subMenu.push",
                exampleBeanName = PushButtonBean.BEAN_NAME)
    }
)
@ManagedBean(name= ButtonBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class ButtonBean extends ComponentExampleImpl<ButtonBean> implements
    Serializable {

    public static final String BEAN_NAME = "buttonBean";

    private Date pushDate = null;
    private String lastPushClientId = null;
    private String exampleString = "The last clicked button was: None at N/A";
    private DateFormat formatter = new SimpleDateFormat("K:m:s a");

    public String getLastPushClientId() {
        return lastPushClientId;
    }

    public void setLastPushClientId(String lastPushClientId) {
        this.lastPushClientId = lastPushClientId;
    }

    public Date getPushDate() {
        return pushDate;
    }

    public void setPushDate(Date pushDate) {
        this.pushDate = pushDate;
    }

    public void clickActionListener(ActionEvent e) {
        updateClickInfoListener(e);
    }

    public void clickChangeListener(ValueChangeEvent e) {
        updateClickInfoListener(e);
    }

    public void updateClickInfoListener(FacesEvent e) {
        lastPushClientId = e.getComponent().getClientId();
        pushDate = new Date();
        exampleString = "The last clicked button was: "+lastPushClientId+" at "+
                        formatter.format(pushDate);
    }

    public String getExampleString() {
        if (pushDate != null && lastPushClientId != null) return exampleString;
        else return "The last clicked button was: None at N/A";
    }

    public void setExampleString(String exampleString) {
        this.exampleString = exampleString;
    }


    public ButtonBean() {
        super(ButtonBean.class);
    }
}