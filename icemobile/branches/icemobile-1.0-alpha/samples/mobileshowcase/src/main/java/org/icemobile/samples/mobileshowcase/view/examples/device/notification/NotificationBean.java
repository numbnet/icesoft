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

package org.icemobile.samples.mobileshowcase.view.examples.device.notification;

import org.icefaces.application.PushRenderer;
import org.icefaces.application.PushRendererMessage;
import org.icemobile.samples.mobileshowcase.view.metadata.annotation.*;
import org.icemobile.samples.mobileshowcase.view.metadata.context.ExampleImpl;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;
import java.io.Serializable;

/**
 * The NotificationBean is responsible for sending a notification message
 * as well as testing the park capabilities of the bridge.  That is to say
 * when a mobile device goes to sleep or the container is paused the ICEfaces
 * bridge must disconnect and reconnect when the device or application become
 * active again.
 * <p/>
 * <ul>
 * <li>The simplest test {@link #sendPushMessage(javax.faces.event.ActionEvent)}
 * sends message string to all active sessions.  </li>
 * <li>The second test {@link #sendNotificationMessage(javax.faces.event.ActionEvent)}
 * pushes a device level notification.  The notification will only be received
 * if the application is running in the the Mobile container. </li>
 * </ul>
 */
@Destination(
        title = "example.device.notification.destination.title.short",
        titleExt = "example.device.notification.destination.title.long",
        titleBack = "example.device.notification.destination.title.back",
        contentPath = "/WEB-INF/includes/examples/device/notification-desc.xhtml"
)
@Example(
        descriptionPath = "/WEB-INF/includes/examples/device/notification-desc.xhtml",
        examplePath = "/WEB-INF/includes/examples/device/notification-example.xhtml",
        resourcesPath = "/WEB-INF/includes/examples/example-resources.xhtml"
)
@ExampleResources(
        resources = {
                // xhtml
                @ExampleResource(type = ResourceType.xhtml,
                        title = "notification-example.xhtml",
                        resource = "/WEB-INF/includes/examples/device/notification-example.xhtml"),
                // Java Source
                @ExampleResource(type = ResourceType.java,
                        title = "NotificationBean.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/device/notification/NotificationBean.java"),
                @ExampleResource(type = ResourceType.java,
                        title = "MessageHub.java",
                        resource = "/WEB-INF/classes/org/icemobile/samples/mobileshowcase" +
                                "/view/examples/device/notification/MessageHub.java")
        }
)
@ManagedBean(name = NotificationBean.BEAN_NAME)
@SessionScoped
public class NotificationBean extends ExampleImpl<NotificationBean> implements
        Serializable {

    public static final String NOTIFICATION_GROUP_NAME = "main";

    public static final String BEAN_NAME = "notificationBean";

    // application scoped common message.
    @ManagedProperty(value = "#{messageHub}")
    private MessageHub messageHub;

    // Simple push notification
    private String pushMessage;

    // Notification message values.
    private String notificationSubject;
    private String notificationMessage;

    public NotificationBean() {
        super(NotificationBean.class);
        // add current session to common render group.
        PushRenderer.addCurrentSession(NOTIFICATION_GROUP_NAME);
    }

    /**
     * Sends/pushes the shared value bound message string in {@link MessageHub}
     *
     * @param event jsf action event
     */
    public void sendPushMessage(ActionEvent event) {
        messageHub.setMessage(pushMessage);
        PushRenderer.render(NOTIFICATION_GROUP_NAME);
    }

    /**
     * Sends a notification api message to active sessions.
     *
     * @param event jsf action event
     */
    public void sendNotificationMessage(ActionEvent event) {
        PushRendererMessage pushMessage = new PushRendererMessage();
        pushMessage.setProperty("subject", notificationSubject);
        pushMessage.setProperty("body", notificationMessage);
        PushRenderer.render(NOTIFICATION_GROUP_NAME, pushMessage);
    }

    public String getNotificationSubject() {
        return notificationSubject;
    }

    public void setNotificationSubject(String notificationSubject) {
        this.notificationSubject = notificationSubject;
    }

    public String getNotificationMessage() {
        return notificationMessage;
    }

    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }

    public void setMessageHub(MessageHub messageHub) {
        this.messageHub = messageHub;
    }

    public String getPushMessage() {
        return pushMessage;
    }

    public void setPushMessage(String pushMessage) {
        this.pushMessage = pushMessage;
    }
}
