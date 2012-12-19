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

package org.icefaces.mobile;

import java.io.Serializable;

import org.icefaces.application.PushRenderer;
import org.icefaces.application.PushMessage;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import javax.faces.context.FacesContext;

import org.icefaces.util.EnvUtils;


/**
 * <p>A NotificationBean for use in various sample pages.</p>
 */
@ManagedBean(name="notificationBean")
@SessionScoped

public class NotificationBean implements Serializable {
    public static final String GROUP = "main";
    private String subject = "subject";
    private String message = "message";
//    @ManagedProperty(value = "#{messagingHub}")
//    private MessagingHub messagingHub;

    public NotificationBean() {
        PushRenderer.addCurrentSession(GROUP);
    }

    public void sendNotification() {
        PushRenderer.render(GROUP);
//        messagingHub.setMessage(message);
    }

    public void sendImportantNotification() {
        PushRenderer.render(GROUP, new PushMessage(subject, message));
//        messagingHub.setMessage(message);
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

//    public MessagingHub getMessagingHub() {
//        return messagingHub;
//    }
//
//    public void setMessagingHub(MessagingHub messagingHub) {
//        this.messagingHub = messagingHub;
//    }
}
