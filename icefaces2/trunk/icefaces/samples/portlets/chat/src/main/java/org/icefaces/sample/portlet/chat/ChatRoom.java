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

import org.icefaces.application.PushRenderer;
import org.icefaces.sample.portlet.chat.resources.ResourceUtil;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.util.*;
import java.util.logging.Logger;

/**
 * The ChatRoom class is the hub of the application.  It keeps track of
 * Participants (adding and removing) as well as the message history.
 * It is also responsible for firing Ajax Push requests when the state
 * of the application has changed.
 */
@ManagedBean(name = "chatRoom")
@ApplicationScoped
public class ChatRoom {

    private static Logger log = Logger.getLogger(Participant.class.getName());

    public static final String ROOM_RENDERER_NAME = "all";

    private Map participants = Collections.synchronizedMap(new HashMap());
    private LinkedList messages = new LinkedList();


    public ChatRoom() {
    }

    public void addParticipant(Participant participant) {
        if (hasParticipant(participant)) {
            return;
        }
        participants.put(participant.getHandle(), participant);
        PushRenderer.addCurrentSession(ChatRoom.ROOM_RENDERER_NAME);
        String localizedMessage = ResourceUtil.getLocalizedMessage("joined", participant.getHandle());
        addMessage(participant, localizedMessage);
    }

    public void removeParticipant(Participant participant) {
        if (!hasParticipant(participant)) {
            return;
        }
        participants.remove(participant.getHandle());
        String localizedMessage = ResourceUtil.getLocalizedMessage("left", participant.getHandle());
        addMessage(participant, localizedMessage);
        PushRenderer.removeCurrentSession(ROOM_RENDERER_NAME);
    }

    public String[] getHandles() {
        return (String[]) participants.keySet().toArray(new String[participants.size()]);
    }

    public int getNumberOfParticipants() {
        return participants.size();
    }

    public List getMessages() {
        return messages;
    }

    public int getNumberOfMessages() {
        return messages.size();
    }

    protected void addMessage(Message message) {
        messages.addFirst(message.getFormattedMessage());
    }

    public void addMessage(Participant participant, String message) {
        if (participant != null && participant.getHandle() != null) {
            addMessage(new Message(participant, message));
            PushRenderer.render(ROOM_RENDERER_NAME);
        }
    }

    public boolean hasParticipant(Participant participant) {
        return participants.containsKey(participant.getHandle());
    }

    public List getMessages(int start, int number) {

        if (start > messages.size()) {
            start = 0;
        }

        if ((start + number) > messages.size()) {
            number = messages.size() - start;
        }

        return messages.subList(start, start + number);
    }
}
