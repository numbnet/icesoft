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

package com.icesoft.applications.faces.auctionMonitor;

import java.sql.Time;

/**
 * Class to represent a Message passed between users It stores the sender,
 * timestamp flag, and message content
 */
public class Message {
    private static final String SEPERATOR = ": ";
    private String message;
    private String sender;
    private String color;
    private String timestamp = "";

    public Message(String sender, String message, String color) {
        this.sender = sender;
        this.message = message;
        this.color = color;
        if (ChatState.getInstance().getTimeStampEnabled()) {
            timestamp = (new Time(System.currentTimeMillis()).toString() + " ");
        }
    }

    public String getMessage() {
        return (message);
    }

    public String getColor() {
        return (color);
    }

    public String getTimestamp() {
        return ("<font color=\"#000000\">" + timestamp + "</font>");
    }

    public String getSender() {
        if (color != null) {
            return ("<font color=\"" + color + "\">" + sender + "</font>");
        }
        return (sender);
    }

    /**
     * Method to return a formatted version of this message
     *
     * @return String formatted message
     */
    public String get() {
        if (color != null) {
            return (timestamp + "<font color=\"" + color + "\">" + sender +
                    "</font>" + SEPERATOR + message);
        }

        return (timestamp + sender + "</font>" + SEPERATOR + message);
    }
}
