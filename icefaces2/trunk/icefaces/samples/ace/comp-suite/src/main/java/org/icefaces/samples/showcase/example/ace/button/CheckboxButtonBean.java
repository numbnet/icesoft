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
package org.icefaces.samples.showcase.example.ace.button;

import org.icefaces.samples.showcase.example.ace.slider.SliderBasicBean;
import org.icefaces.samples.showcase.example.ace.slider.SliderAsyncBean;
import org.icefaces.samples.showcase.example.ace.slider.SliderAsyncInputBean;

import org.icefaces.samples.showcase.metadata.annotation.Menu;
import org.icefaces.samples.showcase.metadata.annotation.MenuLink;                                                                       
import org.icefaces.samples.showcase.metadata.annotation.ComponentExample;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResource;
import org.icefaces.samples.showcase.metadata.annotation.ExampleResources;
import org.icefaces.samples.showcase.metadata.annotation.ResourceType;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;
import org.icefaces.samples.showcase.util.FacesUtils;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

@ComponentExample(
        parent = ButtonBean.BEAN_NAME,
        title = "example.ace.button.checkbox.title",
        description = "example.ace.button.checkbox.description",
        example = "/resources/examples/ace/button/checkboxbutton.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="checkboxbutton.xhtml",
                    resource = "/resources/examples/ace/button/checkboxbutton.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="CheckboxButtonBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/button/CheckboxButtonBean.java")
        }
)
@ManagedBean(name= CheckboxButtonBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class CheckboxButtonBean extends ComponentExampleImpl<CheckboxButtonBean>
        implements Serializable {

    public static final String BEAN_NAME = "checkboxButton";

    private boolean boxValue = true;

    public boolean isBoxValue() {
        return boxValue;
    }

    public void setBoxValue(boolean boxValue) {
        this.boxValue = boxValue;
    }

    public void boxValueListener(ActionEvent e) {
        boolean isCheckboxedChecked = boxValue; // or e.getComponent().getSelected()

        if (isCheckboxedChecked) {
            FacesUtils.addInfoMessage("The checkbox is selected!");
        } else {
            FacesUtils.addInfoMessage("The checkbox is not selected.");
        }
    }

    public CheckboxButtonBean() {
        super(CheckboxButtonBean.class);
    }
}
