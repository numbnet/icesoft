package org.icefaces.samples.showcase.example.ace.panel;

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
        title = "example.ace.panel.title",
        description = "example.ace.panel.description",
        example = "/resources/examples/ace/panel/panel.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="panel.xhtml",
                    resource = "/resources/examples/ace/panel/panel.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="PanelBean.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/panel/PanelBean.java")
        }
)
@Menu(
	title = "menu.ace.panel.subMenu.title",
	menuLinks = {
	        @MenuLink(title = "menu.ace.panel.subMenu.main",
	                isDefault = true,
                    exampleBeanName = PanelBean.BEAN_NAME),
            @MenuLink(title = "menu.ace.panel.subMenu.header",
                exampleBeanName = PanelHeader.BEAN_NAME),
            @MenuLink(title = "menu.ace.panel.subMenu.toggle",
                exampleBeanName = PanelToggle.BEAN_NAME),
            @MenuLink(title = "menu.ace.panel.subMenu.close",
                exampleBeanName = PanelClose.BEAN_NAME),
            @MenuLink(title = "menu.ace.panel.subMenu.listener",
                exampleBeanName = PanelListener.BEAN_NAME),
            @MenuLink(title = "menu.ace.panel.subMenu.menu",
                exampleBeanName = PanelMenu.BEAN_NAME)
    }
)
@ManagedBean(name= PanelBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class PanelBean extends ComponentExampleImpl<PanelBean> implements Serializable {
    public static final String BEAN_NAME = "panelBean";
    
    private boolean collapsed = false;

    public PanelBean() {
        super(PanelBean.class);
    }
    
    public boolean getCollapsed() { return collapsed; }
    
    public void setCollapsed(boolean collapsed) { this.collapsed = collapsed; }
    
    public void toggleCollapsed(ActionEvent event) {
        collapsed = !collapsed;
    }
}