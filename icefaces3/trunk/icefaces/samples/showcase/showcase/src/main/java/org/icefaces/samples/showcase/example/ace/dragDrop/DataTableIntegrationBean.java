
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

package org.icefaces.samples.showcase.example.ace.dragDrop;

import org.icefaces.samples.showcase.metadata.annotation.*;
import org.icefaces.samples.showcase.metadata.context.ComponentExampleImpl;

import javax.faces.bean.CustomScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.faces.event.ActionEvent;
import org.icefaces.ace.event.DragDropEvent;
import org.icefaces.samples.showcase.example.compat.dragdrop.DragDropItem;

@ComponentExample(
        parent = DragDropOverviewBean.BEAN_NAME,
        title = "example.ace.dataTableIntegration.title",
        description = "example.ace.dataTableIntegration.description",
        example = "/resources/examples/ace/dragDrop/dataTableIntegration.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="dataTableIntegration.xhtml",
                    resource = "/resources/examples/ace/dragDrop/dataTableIntegration.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="DataTableIntegration.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/dragDrop/DataTableIntegrationBean.java")
        }
)
@ManagedBean(name= DataTableIntegrationBean.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class DataTableIntegrationBean extends ComponentExampleImpl<DataTableIntegrationBean> implements Serializable
{
   public static final String BEAN_NAME = "dataTableIntegrationBean";
   
   private List<DragDropItem> availableItems;
   private List<DragDropItem> purchasedItems;
   
   public DataTableIntegrationBean()
   {
       super(DataTableIntegrationBean.class);
       initializeData();
   }
    
   private void initializeData() 
   {
        availableItems = new ArrayList<DragDropItem>();
        availableItems.add(new DragDropItem(1, "Laptop", "/resources/css/images/dragdrop/laptop.png", "electronic device", 999.99d, 1));
        availableItems.add(new DragDropItem(2, "Smartphone", "/resources/css/images/dragdrop/pda.png", "electronic device", 299.99d, 1));
        availableItems.add(new DragDropItem(3, "Monitor", "/resources/css/images/dragdrop/monitor.png", "electronic device", 259.99d, 1));
        availableItems.add(new DragDropItem(4, "Desktop", "/resources/css/images/dragdrop/desktop.png", "electronic device", 2499.99d, 1));
        
        Collections.shuffle(availableItems);
        
        purchasedItems = new ArrayList<DragDropItem>();
    }
   
   
   public void handleDrop(DragDropEvent e)
   {
       DragDropItem item = (DragDropItem)e.getData();
       purchasedItems.add(item);
       availableItems.remove(item);
   }
   
   public void resetShoppingCart(ActionEvent e)
   {
       initializeData();
   }
   

    public List<DragDropItem> getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(List<DragDropItem> availableItems) {
        this.availableItems = availableItems;
    }

    public List<DragDropItem> getPurchasedItems() {
        return purchasedItems;
    }

    public void setPurchasedItems(List<DragDropItem> purchasedItems) {
        this.purchasedItems = purchasedItems;
    }
}
