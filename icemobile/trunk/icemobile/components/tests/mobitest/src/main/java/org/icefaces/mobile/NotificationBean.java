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

package org.icefaces.mobile;

import java.io.Serializable;

import org.icefaces.application.PushRenderer;
import org.icefaces.application.PushRendererMessage;

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
    @ManagedProperty(value = "#{messagingHub}")
    private MessagingHub messagingHub;

    public NotificationBean() {
        PushRenderer.addCurrentSession(GROUP);
    }

    public void sendNotification() {
        PushRenderer.render(GROUP);
        messagingHub.setMessage(message);
    }

    public void sendImportantNotification() {
        PushRendererMessage pushMessage = new PushRendererMessage();
        pushMessage.setProperty("subject", subject);
        pushMessage.setProperty("body", message);
        PushRenderer.render(GROUP, pushMessage);
        messagingHub.setMessage(message);
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

    public MessagingHub getMessagingHub() {
        return messagingHub;
    }

    public void setMessagingHub(MessagingHub messagingHub) {
        this.messagingHub = messagingHub;
    }
}
