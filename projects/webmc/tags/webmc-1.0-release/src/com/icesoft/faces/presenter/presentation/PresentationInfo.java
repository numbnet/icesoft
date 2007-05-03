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
 * Class for managing the basic information about a presentation This doesn't
 * deal with manipulating or displaying the presentation, which another class
 * should do
 */
public class PresentationInfo {
    protected String name;
    protected String prefix;
    protected String password;
    protected Participant moderator;
    protected List participants = Collections.synchronizedList(new ArrayList());
    protected int maxParticipants =
            -1; // number of participants allowed, -1 = no limit
    protected MessageLog messageLog = new MessageLog();
    protected boolean autoPlay = false;

    public PresentationInfo() {
    }

    public PresentationInfo(String name, String password, Participant moderator,
                            int maxParticipants) {
        this.name = name;
        this.password = password;
        this.moderator = moderator;
        this.maxParticipants = maxParticipants;
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

    /**
     * Method to get the name of the presentation
     *
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * Method to get the password of the presentation
     *
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * Method to get the moderator of the presentation
     *
     * @return moderator
     */
    public Participant getModerator() {
        return moderator;
    }

    /**
     * Method to get the participants of the presentation
     *
     * @return participants list
     */
    public List getParticipants() {
        return participants;
    }

    /**
     * Method to get the maximum number of participants allowed
     *
     * @return maxParticipants
     */
    public int getMaxParticipants() {
        return maxParticipants;
    }

    /**
     * Method to get the chat message log
     *
     * @return messageLog
     */
    public MessageLog getMessageLog() {
        return messageLog;
    }

    /**
     * Method to get the autoplay status
     *
     * @return autoPlay
     */
    public boolean getAutoPlay() {
        return autoPlay;
    }

    /**
     * Method to set the name of the presentation
     *
     * @param name new
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Method to set the password of the presentation
     *
     * @param password new
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Method to set the moderator of the presentation
     *
     * @param moderator new
     */
    public void setModerator(Participant moderator) {
        this.moderator = moderator;
    }

    /**
     * Method to set the participants of the presentation
     *
     * @param participants list new
     */
    public void setParticipants(List participants) {
        this.participants = participants;
    }

    /**
     * Method to set the maximum number of participants allowed
     *
     * @param maxParticipants new
     */
    public void setMaxParticipants(int maxParticipants) {
        this.maxParticipants = maxParticipants;
    }

    /**
     * Method to set the chat message log
     *
     * @param messageLog new
     */
    public void setMessageLog(MessageLog messageLog) {
        this.messageLog = messageLog;
    }

    /**
     * Method to set the autoplay status
     *
     * @param autoPlay new
     */
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
     * Convience method to get the default information slide
     *
     * @return information slide
     */
    public Slide getInfoSlide() {
        return Slide.getInfoSlide();
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
     * Convience method to determine if any spaces are left for new participants
     * to be added to the presentation
     *
     * @return true if the participant can join
     */
    public boolean hasSlotsLeft() {
        if (maxParticipants != -1) {
            if (participants.size() >= maxParticipants) {
                return false;
            }
        }

        return true;
    }

    /**
     * Convience method to get the number of chat participants as a readable
     * string
     *
     * @return string label of the participants
     */
    public String getNumberChatParticipants() {
        return "Number of Participants: " + participants.size();
    }
}