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

package org.icemobile.samples.mobileshowcase.view.navigation;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.icemobile.samples.mobileshowcase.view.examples.input.button.ButtonBean;
import org.icemobile.samples.mobileshowcase.view.examples.input.dateSpinner.DateBean;
import org.icemobile.samples.mobileshowcase.view.examples.input.flipSwitch.FlipSwitchBean;
import org.icemobile.samples.mobileshowcase.view.examples.input.geolocation.GeoLocationBean;
import org.icemobile.samples.mobileshowcase.view.examples.input.input.InputBean;
import org.icemobile.samples.mobileshowcase.view.examples.input.menubutton.MenuButtonBean;
import org.icemobile.samples.mobileshowcase.view.examples.input.submitnotification.SubmitNotificationBean;
import org.icemobile.samples.mobileshowcase.view.examples.input.gmap.GMapBean;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.MenuLink;
import org.icemobile.samples.mobileshowcase.view.metadata.context.Menu;

/**
 * Menu items for input and selection components
 */
@org.icemobile.samples.mobileshowcase.view.metadata.annotation.Menu(
        title = "menu.input.title",
        menuLinks = {
                @MenuLink(title = "menu.input.button.title",
                        exampleBeanName = ButtonBean.BEAN_NAME,
                        examplePanelId = "button"),
                @MenuLink(title = "menu.input.date.title",
                        exampleBeanName = DateBean.BEAN_NAME,
                        examplePanelId = "date"),
                @MenuLink(title = "menu.input.flipswitch.title",
                        exampleBeanName = FlipSwitchBean.BEAN_NAME,
                        examplePanelId = "flipswitch"),
                @MenuLink(title = "menu.input.geolocation.title",
                        exampleBeanName = GeoLocationBean.BEAN_NAME,
                        examplePanelId = "geolocation"),
                @MenuLink(title = "menu.layout.gmap.title",
                        exampleBeanName = GMapBean.BEAN_NAME,
                        examplePanelId = "gmap",
                        isRedirect=true),
                @MenuLink(title = "menu.input.input.title",
                        exampleBeanName = InputBean.BEAN_NAME,
                        examplePanelId = "input"),
                @MenuLink(title = "menu.input.menuButton.title",
                        exampleBeanName = MenuButtonBean.BEAN_NAME,
                        examplePanelId = "menubutton"),
                @MenuLink(title = "menu.input.submitNotification.title",
                        exampleBeanName = SubmitNotificationBean.BEAN_NAME,
                        examplePanelId = "submitnotification")
                
        
        })
@ManagedBean(name = InputAndSelectionMenu.BEAN_NAME)
@ApplicationScoped
public class InputAndSelectionMenu extends Menu<InputAndSelectionMenu>
        implements Serializable {

    public static final String BEAN_NAME = "inputAndSelectionMenu";

    public InputAndSelectionMenu() {
        super(InputAndSelectionMenu.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getBeanName() {
        return BEAN_NAME;
    }
}