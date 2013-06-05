/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

package org.icemobile.samples.mobileshowcase.view.examples.input.menubutton;

import org.icefaces.mobi.component.menubutton.MenuButtonItem;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.event.ActionEvent;

import java.io.Serializable;
import java.util.ArrayList;

@Destination(
        title = "example.input.menuButton.destination.title.short",
        titleExt = "example.input.menuButton.destination.title.long",
        titleBack = "example.input.menuButton.destination.title.back"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/input/menubutton-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/input/menubutton-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "menubutton-example.xhtml",
                        resource = "/WEB-INF/includes/examples/input/menubutton-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "ButtonBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/input/menubutton/MenuButtonBean.java")
        }
)

@ManagedBean(name = MenuButtonBean.BEAN_NAME)
@SessionScoped
public class MenuButtonBean extends ExampleImpl<MenuButtonBean> implements
        Serializable {

    public static final String BEAN_NAME = "menuButton";

    private ArrayList<MenuButtonItemModel> dynamicMenuButton;

    private String executedCommand;
    
    private String selTitle="Select";
    

    public MenuButtonBean() {
        super(MenuButtonBean.class);
        initDynamicMenuButton();
    }

    private void initDynamicMenuButton() {
        // build out the menu commands and fill in the MenuButtonItemModel.
        dynamicMenuButton = new ArrayList<MenuButtonItemModel>(4);
//        dynamicMenuButton.add(new MenuButtonItemModel("Action Menu",null));
        dynamicMenuButton.add(new MenuButtonItemModel(" &#160; Create Record",
                new CreateCommand()));
        dynamicMenuButton.add(new MenuButtonItemModel(" &#160; Update Record",
                new UpdateCommand()));
        dynamicMenuButton.add(new MenuButtonItemModel(" &#160; Delete Record",
                new DeleteCommand()));
    }

    public ArrayList<MenuButtonItemModel> getDynamicMenuButton() {
        return dynamicMenuButton;
    }

    public void setExecutedCommand(String executedCommand) {
        this.executedCommand = executedCommand;
    }

    public String getExecutedCommand() {
        return executedCommand;
    }
    
    public String getSelTitle() {
        return this.selTitle;
    }

    public void setSelTitle(String selTitle) {
        this.selTitle = selTitle;
    }

    public void actionMethod(ActionEvent ae){
        UIComponent uic = ae.getComponent();
        if (uic instanceof MenuButtonItem) {
            MenuButtonItem mbi = (MenuButtonItem)uic;
         //   logger.info("Item selected="+mbi.getValue()+" label="+mbi.getLabel());
            this.executedCommand = mbi.getValue().toString();
        }
    }
}
