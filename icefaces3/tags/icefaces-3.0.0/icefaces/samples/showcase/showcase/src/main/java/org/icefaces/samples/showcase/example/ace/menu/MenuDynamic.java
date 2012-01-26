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
import javax.faces.event.ActionEvent;
import javax.faces.component.UIComponent;
import java.io.Serializable;
import java.util.List;

import org.icefaces.ace.model.MenuModel;
import org.icefaces.ace.model.DefaultMenuModel;
import org.icefaces.ace.component.submenu.Submenu;
import org.icefaces.ace.component.menuitem.MenuItem;
import org.icefaces.samples.showcase.util.FacesUtils;

@ComponentExample(
        parent = MenuBean.BEAN_NAME,
        title = "example.ace.menu.dynamic.title",
        description = "example.ace.menu.dynamic.description",
        example = "/resources/examples/ace/menu/menuDynamic.xhtml"
)
@ExampleResources(
        resources ={
            // xhtml
            @ExampleResource(type = ResourceType.xhtml,
                    title="menuDynamic.xhtml",
                    resource = "/resources/examples/ace/menu/menuDynamic.xhtml"),
            // Java Source
            @ExampleResource(type = ResourceType.java,
                    title="MenuDynamic.java",
                    resource = "/WEB-INF/classes/org/icefaces/samples/showcase"+
                    "/example/ace/menu/MenuDynamic.java")
        }
)
@ManagedBean(name= MenuDynamic.BEAN_NAME)
@CustomScoped(value = "#{window}")
public class MenuDynamic extends ComponentExampleImpl<MenuDynamic> implements Serializable {
    public static final String BEAN_NAME = "menuDynamic";
    
    private MenuModel menuModel;
    private int itemCount = 1;
    
    public MenuDynamic() 
    {
        super(MenuDynamic.class);
        menuModel = new DefaultMenuModel();
    }
    
    public MenuModel getMenuModel() { return menuModel; }
    
    public void setMenuModel(MenuModel menuModel) { this.menuModel = menuModel; }
    
    /**
     * Necessary method because a menu model that is dynamically modified at the bean
     *  level will not display properly on the page without a refresh/reload
     */
    private void forceRedirectWorkaround() {
        FacesUtils.redirectBrowser(".");
    }
    
    private Submenu generateItems(Submenu sub) {
        sub.getChildren().clear();
        
        MenuItem toAdd = null;
        for (int i = 0; i < itemCount; i++) {
            toAdd = new MenuItem();
            toAdd.setId(sub.getId() + "_mmi_" + (i+1));
            toAdd.setValue("Value " + (i+1));
            sub.getChildren().add(toAdd);
        }
        
        return sub;
    }
    
    private void refreshItems() {
        for (UIComponent currentMenu : menuModel.getSubmenus()) {
            generateItems((Submenu)currentMenu);
        }
    }
    
    public void addSubmenu(ActionEvent event) {
        int nextCount = menuModel.getSubmenus() != null ? menuModel.getSubmenus().size() : 0;
        
        Submenu newSubmenu = new Submenu();
        newSubmenu.setId("sm_" + (nextCount+1));
        newSubmenu.setLabel("Submenu " + (nextCount+1));
        generateItems(newSubmenu);
        
        menuModel.addSubmenu(newSubmenu);
        
        forceRedirectWorkaround();
    }
    
    public void removeSubmenu(ActionEvent event) {
        List<UIComponent> submenus = menuModel.getSubmenus();
        
        // Ensure we don't remove the last submenu
        if ((submenus != null) && (submenus.size() > 1)) {
            submenus.remove(submenus.size()-1);
        }
        
        forceRedirectWorkaround();
    }
    
    public void addItem(ActionEvent event) {
        itemCount++;
        
        refreshItems();
        
        forceRedirectWorkaround();
    }
    
    public void removeItem(ActionEvent event) {
        if (itemCount > 1) {
            itemCount--;
        }
        
        refreshItems();
        
        forceRedirectWorkaround();
    }
}
