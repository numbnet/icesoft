/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */
package com.icesoft.faces.presenter.presentation;

import com.icesoft.faces.presenter.chat.Message;
import com.icesoft.faces.presenter.chat.MessageLog;
import com.icesoft.faces.presenter.participant.Participant;
import com.icesoft.faces.presenter.slide.Slide;
import com.icesoft.faces.presenter.util.Encrypter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class used to store back end specific information about a Presentation.
 * This means no UI work is done, just core information about a Presentation is 
 * managed and it is meant to be extended by a front end Presentation bean.
 */
public class PresentationInfo {
    protected String name;
    protected String prefix;
    protected String password;
    protected Participant moderator;
    protected List participants = Collections.synchronizedList(new ArrayList());
    protected MessageLog messageLog = new MessageLog();
    protected boolean autoPlay = false;

    public PresentationInfo() {
    }

    public PresentationInfo(String name, String password, Participant moderator) {
        this.name = name;
        this.password = password;
        this.moderator = moderator;
    }

    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Method to get the prefix
     * If the prefix has not been generated yet, it will be before being returned
     *
     * @return prefix
     */
    public String getPrefix() {
        if ((prefix == null) && (name != null)) {
            prefix = Encrypter.md5(name + System.currentTimeMillis());
        }

        return prefix;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Participant getModerator() {
        return moderator;
    }

    public void setModerator(Participant moderator) {
        this.moderator = moderator;
    }

    public List getParticipants() {
        return participants;
    }
    
    public void setParticipants(List participants) {
        this.participants = participants;
    }

    public MessageLog getMessageLog() {
        return messageLog;
    }
    
    public void setMessageLog(MessageLog messageLog) {
        this.messageLog = messageLog;
    }

    public boolean getAutoPlay() {
        return autoPlay;
    }
    
    public void setAutoPlay(boolean autoPlay) {
        this.autoPlay = autoPlay;
    }

    /**
     * Method to get the count of the chat log message
     *
     * @return messageLog size
     */
    public int getMessageLogSize() {
        return messageLog.size();
    }

    /**
     * Method to get the count of the participants
     *
     * @return participants size
     */
    public int getParticipantsSize() {
        return participants.size();
    }

    /**
     * Convenience method to get the default information slide
     *
     * @return information slide
     */
    public Slide getInfoSlide(boolean mobile) {
        return Slide.getInfoSlide(mobile);
    }

    /**
     * Method to get a specific chat message at the passed index
     *
     * @param index of the message
     * @return matching object, or null if out of bounds or invalid
     */
    public Message getMessageAt(int index) {
        if ((index < messageLog.size()) && (index >= 0)) {
            return (Message) messageLog.get(index);
        }
        return null;
    }

    /**
     * Method to get a specific participant at the passed index
     *
     * @param index of the participant
     * @return matching object, or null if out of bounds or invalid
     */
    public Participant getParticipantAt(int index) {
        if ((index < participants.size()) && (index >= 0)) {
            return (Participant) participants.get(index);
        }
        return null;
    }

    /**
     * Convenience method to get the number of chat participants as a readable
     * string
     *
     * @return string label of the participants
     */
    public String getNumberChatParticipants() {
        return " " + participants.size();
    }
}