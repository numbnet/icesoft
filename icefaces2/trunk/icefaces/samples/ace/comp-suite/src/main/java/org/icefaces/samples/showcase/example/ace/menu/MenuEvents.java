package org.icefaces.samples.showcase.example.ace.menu;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import javax.faces.model.SelectItem;
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
        parent = MenuBean.BEAN_NAME,
        title = "example.ace.menu.events.title",
        description = "example.ace.menu.events.description",
        example = "/resources/examples/ace/menu/menuEvents.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="menuEvents.xhtml",
                    resource = "/resources/examples/ace/menu/menuEvents.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MenuEvents.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/menu/MenuEvents.java")
        }
)
@ManagedBean(name= MenuEvents.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuEvents extends ComponentExampleImpl<MenuEvents> implements Serializable {
    public static final String BEAN_NAME = "menuEvents";
    
    private String message;
    private String currentColor = "black";
    
    public MenuEvents() {
        super(MenuEvents.class);
    }
    
    public String getMessage() { return message; }
    public String getCurrentColor() { return currentColor; }
    
    public void setMessage(String message) { this.message = message; }
    public void setCurrentColor(String currentColor) { this.currentColor = currentColor; }
    
    public String ourAction() {
        System.out.println("Fired Menu Action");
        
        message = "Fired Action.";
        
        return null;
    }
    
    public void ourActionListener(ActionEvent event) {
        System.out.println("Fired Menu Action Listener");
        
        message = "Fired Action Listener.";
    }
    
    public void applyColor(ActionEvent event) {
        currentColor = event.getComponent() != null ? event.getComponent().getId() : "black";
    }
}
