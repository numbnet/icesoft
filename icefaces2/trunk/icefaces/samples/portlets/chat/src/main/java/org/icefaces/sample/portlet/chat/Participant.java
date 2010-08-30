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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.sample.portlet.chat;

import javax.annotation.PreDestroy;
import javax.faces.event.ActionEvent;
import java.util.logging.Logger;

/**
 * The Participant class stores information about an individual participant
 * in the chat room. Since this is a fairly simple example, it also stores
 * information about the state of the current conversation for the the user
 * (e.g. what part of the chat history they are currently viewing).  In a
 * more sophisticated application, this could potentially held in a separate
 * bean.
 * <p/>
 */
public class Participant {

    private static Logger log = Logger.getLogger(Participant.class.getName());

    private String handle;
    private ChatRoom chatRoom;
    private String message;

    public Participant() {
    }

    public String getHandle() {
        return handle;
    }

    public void setHandle(String handle) {
        this.handle = handle.trim();
    }

    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

    public String getMessage() {
        return "";
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void login(ActionEvent event) {
        if (handle != null && handle.trim().length() > 0) {
            if (!chatRoom.hasParticipant(this)) {
                chatRoom.addParticipant(this);
            } else {
//                ResourceUtil.addMessage("alreadyRegistered", handle);
                handle = null;
            }
        } else {
//            ResourceUtil.addMessage("badHandle");
        }
    }

    public void sendMessage(ActionEvent event) {
        if (!chatRoom.hasParticipant(this) || message == null || message.trim().length() < 1) {
            return;
        }
        chatRoom.addMessage(this, message);
    }

    @PreDestroy
    public void logout(ActionEvent event) {
        chatRoom.removeParticipant(this);
        handle = null;
    }

    public boolean isRegistered() {
        return chatRoom.hasParticipant(this);
    }

    public void setRegistered(boolean registered) {
    }

    public String toString() {
        return super.toString() + " [" + handle + "]";
    }

}
