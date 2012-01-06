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
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.samples.showcase.example.ace.menu;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;

import java.io.Serializable;
import java.util.LinkedHashMap;
import javax.faces.model.SelectItem;

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
    
    private LinkedHashMap <String, String> availableTypes;
    private String type;
    
    public MenuType() {
        super(MenuType.class);
        availableTypes = populateAvailableTypes();
        type = availableTypes.get("Plain");
    }
    
    private LinkedHashMap <String, String> populateAvailableTypes()
    {
        LinkedHashMap <String, String> list = new LinkedHashMap <String, String>();
        list.put("Plain","plain");
        list.put("Tiered","tiered");
        list.put("Sliding","sliding");
        return list;
    }

    public LinkedHashMap<String, String> getAvailableTypes() { return availableTypes; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
