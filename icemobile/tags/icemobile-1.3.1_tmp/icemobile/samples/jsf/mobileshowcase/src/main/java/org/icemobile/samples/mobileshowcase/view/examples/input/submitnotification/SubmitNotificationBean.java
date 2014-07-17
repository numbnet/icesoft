/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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
package org.icemobile.samples.mobileshowcase.view.examples.input.submitnotification;

import org.icemobile.samples.mobileshowcase.view.examples.input.menubutton.MenuButtonItemModel;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Destination(
        title = "example.input.submitNotification.destination.title.short",
        titleExt = "example.input.submitNotification.destination.title.long",
        titleBack = "example.input.submitNotification.destination.title.back"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/input/submitnotification-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/input/submitnotification-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "submitnotification-example.xhtml",
                        resource = "/WEB-INF/includes/examples/input/submitnotification-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "SubmitNotificationBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/input/submitnotification/SubmitNotificationBean.java")
        }
)

@ManagedBean(name = SubmitNotificationBean.BEAN_NAME)
@SessionScoped
public class SubmitNotificationBean extends ExampleImpl<SubmitNotificationBean> implements
        Serializable {

    public static final String BEAN_NAME = "submitNotificationBean";
    private List<MenuButtonItemModel> menuButtons;

    public SubmitNotificationBean() {
        super(SubmitNotificationBean.class);
        menuButtons = new ArrayList<MenuButtonItemModel>();
        menuButtons.add(new MenuButtonItemModel("Execute Long Task",null));
    }
    
    public void longRunningTask(ActionEvent event){
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public List<MenuButtonItemModel> getMenuButtons() {
        return menuButtons;
    }
}
