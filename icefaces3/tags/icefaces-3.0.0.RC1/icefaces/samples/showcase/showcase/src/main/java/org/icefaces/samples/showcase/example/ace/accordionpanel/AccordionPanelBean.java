/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.samples.showcase.example.ace.accordionpanel;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
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
    
    public AccordionPanelBean() 
    {
        super(AccordionPanelBean.class);
        items = initializeListWithItems();
        imageLocation = "/resources/css/images/rainbowCalgary.png";
    }
    
    private ArrayList<Item> initializeListWithItems() 
   {
        ArrayList<Item> list = new ArrayList<Item>();
        list.add(new Item(1, "Aubergine", "/resources/css/images/dragdrop/aubergine.png", "Fruits and Vegetables", 1.99d, 10));
        list.add(new Item(2, "Capsicum", "/resources/css/images/dragdrop/capsicum.png", "Fruits and Vegetables", 0.99d, 4));
        list.add(new Item(3, "Chilli", "/resources/css/images/dragdrop/chilli.png", "Oriental", 3.25, 2));
        list.add(new Item(4, "Eggs", "/resources/css/images/dragdrop/egg.png", "Dairy", 5.99, 40));
        list.add(new Item(5, "Orange", "/resources/css/images/dragdrop/orange.png", "Fruits and Vegetables", 9.99d, 15));
        
        Collections.shuffle(list);
        
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
    
}