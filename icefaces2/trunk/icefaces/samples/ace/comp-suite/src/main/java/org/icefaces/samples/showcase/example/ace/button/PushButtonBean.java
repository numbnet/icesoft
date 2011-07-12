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
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;

@ComponentExample(
        parent = ButtonBean.BEAN_NAME,
        title = "example.ace.button.push.title",
        description = "example.ace.button.push.description",
        example = "/resources/examples/ace/button/pushbutton.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="pushbutton.xhtml",
                    resource = "/resources/examples/ace/button/pushbutton.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PushButtonBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/example/ace/button/PushButtonBean.java")
        }
)
@ManagedBean(name= PushButtonBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PushButtonBean extends ComponentExampleImpl<PushButtonBean>
        implements Serializable {

    public static final String BEAN_NAME = "pushButton";

    private String exampleValue = "";
    private String listenerNavigation = "showcase.jsf?grp=eeMenu";
    private String navActionValue = "overview";

    public void addMessage() {
        FacesUtils.addInfoMessage("This button didn't submit or validate the rest of the form.");
    }

    public void navListener(ActionEvent e) {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            FacesUtils.getExternalContext().dispatch(listenerNavigation);
        } catch (IOException e1) {
            FacesUtils.addErrorMessage("Path \""+listenerNavigation+"\" could not be read.");
            e1.printStackTrace();
        }
    }

    public String getNavAction() { return navAction(); }

    public String navAction() {
        if (navActionValue.equals("overview")) return "buttonBean";
        if (navActionValue.equals("checkbox")) return "checkboxButton";
        if (navActionValue.equals("push")) return "pushButton";
        return "linkButton";
    }

    public String getNavActionValue() {
        return navActionValue;
    }

    public void setNavActionValue(String navActionValue) {
        this.navActionValue = navActionValue;
    }

    public String getListenerNavigation() {
        return listenerNavigation;
    }

    public void setListenerNavigation(String listenerNavigation) {
        this.listenerNavigation = listenerNavigation;
    }

    public String getExampleValue() {
        return exampleValue;
    }

    public void setExampleValue(String exampleValue) {
        this.exampleValue = exampleValue;
    }

    public PushButtonBean() {
        super(PushButtonBean.class);
    }
}
