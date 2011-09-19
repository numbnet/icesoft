/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */

package org.icemobile.samples.mobileshowcase.view.navigation;

import org.icemobile.samples.mobileshowcase.view.examples.input.button.ButtonBean;
import org.icemobile.samples.mobileshowcase.view.examples.input.flipSwitch.FlipSwitchBean;
import org.icemobile.samples.mobileshowcase.view.examples.input.geolocation.GeolocationBean;
import org.icemobile.samples.mobileshowcase.view.examples.input.input.InputBean;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.MenuLink;
import org.icemobile.samples.mobileshowcase.view.metadata.context.Menu;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

/**
 * Menu items for input and selection components
 */
@org.icemobile.samples.mobileshowcase.view.metadata.annotation.Menu(
        title = "menu.input.title",
        menuLinks = {
                @MenuLink(title = "menu.input.button.title",
                        exampleBeanName = ButtonBean.BEAN_NAME),
                @MenuLink(title = "menu.input.flipswitch.title",
                        exampleBeanName = FlipSwitchBean.BEAN_NAME),
                @MenuLink(title = "menu.input.input.title",
                        exampleBeanName = InputBean.BEAN_NAME),
                @MenuLink(title = "menu.input.geolocation.title",
                        exampleBeanName = GeolocationBean.BEAN_NAME)
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