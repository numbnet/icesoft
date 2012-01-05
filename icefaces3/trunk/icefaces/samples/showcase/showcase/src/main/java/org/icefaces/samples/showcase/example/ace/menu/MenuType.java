/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

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

@ComponentExample(
        parent = MenuBean.BEAN_NAME,
        title = "example.ace.menu.type.title",
        description = "example.ace.menu.type.description",
        example = "/resources/examples/ace/menu/menuType.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="menuType.xhtml",
                    resource = "/resources/examples/ace/menu/menuType.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MenuType.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/menu/MenuType.java")
        }
)
@ManagedBean(name= MenuType.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuType extends ComponentExampleImpl<MenuType> implements Serializable {
    public static final String BEAN_NAME = "menuType";
    
    private static final SelectItem[] availableTypes = { new SelectItem("plain", "Plain"),
                                                         new SelectItem("tiered", "Tiered"),
                                                         new SelectItem("sliding", "Sliding") };
    
    private String type = availableTypes[0].getValue().toString();
    
    public MenuType() {
        super(MenuType.class);
    }
    
    public SelectItem[] getAvailableTypes() { return availableTypes; }
    public String getType() { return type; }
    
    public void setType(String type) { this.type = type; }
}
