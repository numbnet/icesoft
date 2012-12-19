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

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;

/**
 * MessageHub is an application scoped bean that stores a shared message between
 * active sessions.  The MessagingBean places a new message string in this bean
 * and initiates a push to share the message in a JSF view.
 * Essentially a very simple chat.
 */
@ManagedBean
@ApplicationScoped
public class MessageHub implements Serializable {

    private String message;

    /**
     * Gets the shared session chat message.
     *
     * @return message string. Can be null.
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message that will be pushed out to other users on receipt of the
     * notification.
     *
     * @param message simple text message.
     */
    public void setMessage(String message) {
        this.message = message;
    }
}
