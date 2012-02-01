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

package org.icefaces.samples.showcase.example.ace.accordionpanel;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

@ComponentExample(
        title = "example.ace.accordionpanel.title",
        description = "example.ace.accordionpanel.description",
        example = "/resources/examples/ace/accordionpanel/accordionPanel.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="accordionPanel.xhtml",
                    resource = "/resources/examples/ace/accordionpanel/accordionPanel.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="AccordionPanel.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/accordionpanel/AccordionPanelBean.java")
        }
)
@Menu(
	title = "menu.ace.accordionpanel.subMenu.title",
	menuLinks = {
	        @MenuLink(title = "menu.ace.accordionpanel.subMenu.main",
	                isDefault = true,
                    exampleBeanName = AccordionPanelBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.accordionpanel.subMenu.dynamic",
                    exampleBeanName = AccordionPanelDynamicBean.BEAN_NAME),
	        @MenuLink(title = "menu.ace.accordionpanel.subMenu.effect",
                    exampleBeanName = AccordionPanelEffectBean.BEAN_NAME)
    }
)
@ManagedBean(name= AccordionPanelBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class AccordionPanelBean extends ComponentExampleImpl<AccordionPanelBean> implements Serializable {
    public static final String BEAN_NAME = "accordionPanelBean";
    
    private List<Item> items;
    private String imageLocation;
    private LinkedHashMap <String, Integer> toDoList;
    
    public AccordionPanelBean() 
    {
        super(AccordionPanelBean.class);
        items = populateListWithItems();
        toDoList = populateToDoList();
        imageLocation = "/resources/css/images/rainbowCalgary.png";
    }
    
    private ArrayList<Item> populateListWithItems() 
   {
        ArrayList<Item> list = new ArrayList<Item>();
        list.add(new Item(1, "Aubergine", "/resources/css/images/dragdrop/aubergine.png", "Fruits and Vegetables", 1.99d, 10));
        list.add(new Item(2, "Capsicum", "/resources/css/images/dragdrop/capsicum.png", "Fruits and Vegetables", 0.99d, 4));
        list.add(new Item(3, "Chili", "/resources/css/images/dragdrop/chilli.png", "Oriental", 3.25, 2));
        list.add(new Item(4, "Eggs", "/resources/css/images/dragdrop/egg.png", "Dairy", 5.99, 40));
        list.add(new Item(5, "Orange", "/resources/css/images/dragdrop/orange.png", "Fruits and Vegetables", 9.99d, 15));
        
        Collections.shuffle(list);
        
        return list;
    }
    
    private LinkedHashMap <String, Integer> populateToDoList()
    {
        LinkedHashMap <String, Integer> list = new LinkedHashMap <String, Integer>();
        list.put("Buy groceries",1);
        list.put("Review picture of the day",2);
        list.put("Send invitations",3);
        list.put("Call John",4);
        list.put("Check calendar",5);
        return list;
    }
    

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public String getImageLocation() {
        return imageLocation;
    }

    public void setImageLocation(String imageLocation) {
        this.imageLocation = imageLocation;
    }

    public LinkedHashMap <String, Integer> getToDoList() {
        return toDoList;
    }

    public void setToDoList(LinkedHashMap <String, Integer> toDoList) {
        this.toDoList = toDoList;
    }
}