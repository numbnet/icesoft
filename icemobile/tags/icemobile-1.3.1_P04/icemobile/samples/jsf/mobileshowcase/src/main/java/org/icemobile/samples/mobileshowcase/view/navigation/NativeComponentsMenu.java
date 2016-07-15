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

package org.icemobile.samples.mobileshowcase.view.navigation;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import org.icemobile.samples.mobileshowcase.view.examples.device.camcorder.CamcorderBean;
import org.icemobile.samples.mobileshowcase.view.examples.device.camera.CameraBean;
import org.icemobile.samples.mobileshowcase.view.examples.device.contacts.ContactsBean;
import org.icemobile.samples.mobileshowcase.view.examples.device.microphone.MicrophoneBean;
import org.icemobile.samples.mobileshowcase.view.examples.device.open.OpenBean;
import org.icemobile.samples.mobileshowcase.view.examples.device.notification.NotificationBean;
import org.icemobile.samples.mobileshowcase.view.examples.device.qrcode.QrCodeBean;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.MenuLink;

/**
 * Menu items for native integration
 */
@org.icemobile.samples.mobileshowcase.view.metadata.annotation.Menu(
        title = "menu.native.title",
        menuLinks = {
                @MenuLink(title = "menu.native.camcorder.title",
                        exampleBeanName = CamcorderBean.BEAN_NAME,
                        examplePanelId = "camcorder"),
                @MenuLink(title = "menu.native.camera.title",
                        exampleBeanName = CameraBean.BEAN_NAME,
                        examplePanelId = "camera"),
                @MenuLink(title = "menu.native.notification.title",
                        exampleBeanName = NotificationBean.BEAN_NAME,
                        examplePanelId = "notification"),
                /*
                @MenuLink(title = "menu.native.open.title",
                        exampleBeanName = OpenBean.BEAN_NAME,
                        examplePanelId = "open"),*/
                @MenuLink(title = "menu.native.contacts.title",
                        exampleBeanName = ContactsBean.BEAN_NAME,
                        examplePanelId = "contacts"),
                @MenuLink(title = "menu.native.microphone.title",
                        exampleBeanName = MicrophoneBean.BEAN_NAME,
                        examplePanelId = "microphone"),
                @MenuLink(title = "menu.native.qrcode.title",
                        exampleBeanName = QrCodeBean.BEAN_NAME,
                        examplePanelId = "qrcode")
                
                
        })
@ManagedBean(name = NativeComponentsMenu.BEAN_NAME)
@ApplicationScoped
public class NativeComponentsMenu
        extends org.icemobile.samples.mobileshowcase.view.metadata.context.Menu<NativeComponentsMenu>
        implements Serializable {

    public static final String BEAN_NAME = "nativeCompsMenu";

    public NativeComponentsMenu() {
        super(NativeComponentsMenu.class);
    }

    @PostConstruct
    public void initMetaData() {
        super.initMetaData();
    }

    public String getBeanName() {
        return BEAN_NAME;
    }

}
