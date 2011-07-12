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

import javax.faces.application.NavigationHandler;
import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.Serializable;

@ComponentExample(
        parent = ButtonBean.BEAN_NAME,
        title = "example.ace.button.link.title",
        description = "example.ace.button.link.description",
        example = "/resources/examples/ace/button/linkbutton.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="linkbutton.xhtml",
                    resource = "/resources/examples/ace/button/linkbutton.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="LinkButtonBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase/"+
                                "example/ace/button/LinkButtonBean.java")
        }
)
@ManagedBean(name=LinkButtonBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class LinkButtonBean extends ComponentExampleImpl<LinkButtonBean>
        implements Serializable {

    public static final String BEAN_NAME = "linkButton";

    private String navActionValue = "overview";
    private String staticNavigation = "showcase.jsf?grp=compatMenu";
    private String listenerNavigation = "showcase.jsf?grp=eeMenu";
    private String requiredField =  "";

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

    public String navAction() {
        if (navActionValue.equals("overview")) return "buttonBean";
        if (navActionValue.equals("checkbox")) return "checkboxButton";
        if (navActionValue.equals("push")) return "pushButton";
        return "linkButton";
    }

    public String getNavAction() {
        return navAction();
    }

    public String getRequiredField() {
        return requiredField;
    }

    public void setRequiredField(String requiredField) {
        this.requiredField = requiredField;
    }

    public void setNavAction(String navAction) {
        this.navActionValue = navAction;
    }

    public String getNavActionValue() {
        return navActionValue;
    }

    public void setNavActionValue(String navAction) {
        this.navActionValue = navAction;
    }

    public String getListenerNavigation() {
        return listenerNavigation;
    }

    public void setListenerNavigation(String listenerNavigation) {
        this.listenerNavigation = listenerNavigation;
    }

    public String getStaticNavigation() {
        return staticNavigation;
    }

    public void setStaticNavigation(String staticNavigation) {
        this.staticNavigation = staticNavigation;
    }

    public LinkButtonBean() {
        super(LinkButtonBean.class);
    }
}
