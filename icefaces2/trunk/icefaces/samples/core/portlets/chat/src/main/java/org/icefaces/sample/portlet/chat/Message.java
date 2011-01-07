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

package org.icefaces.sample.portlet.chat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Data bean for timestamping and formatting chat messages.
 */
public class Message {

    private String message;
    private String formattedMessage;
    private Participant participant;
    private Date timestamp;

    private static SimpleDateFormat formatter =
            new SimpleDateFormat("hh:mm:ss");


    public Message() {
        timestamp = new Date();
    }

    public Message(Participant participant, String message) {
        this();
        this.message = message;
        this.participant = participant;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return formatter.format(timestamp);
    }

    public void setTimestamp(Date timestamp) {
        //
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public String getFormattedMessage() {
        if (formattedMessage == null) {
            StringBuffer buff = new StringBuffer();
            buff.append("[");
            buff.append(getTimestamp());
            buff.append(" - ");
            buff.append(participant.getHandle());
            buff.append("] ");
            buff.append(message);
            formattedMessage = buff.toString();
        }
        return formattedMessage;
    }

    public String toString() {
        return getFormattedMessage();
    }
}
