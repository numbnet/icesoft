/*
 * Version: MPL 1.1
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package com.icesoft.applications.faces.auctionMonitor;

import java.util.ArrayList;

/**
 * Class used to add chat log functionality to the standard ArrayList Some
 * examples are timestamps, colored nicknames, etc.
 */
public class MessageLog extends ArrayList {
    private static final String DEFAULT_COLOR = "Black";

    public MessageLog(int size) {
        super(size);
    }

    public String getMessageAt(int index) {
        return (((Message) get(index)).get());
    }

    /**
     * Override method to add a chat message, with a colored username
     *
     * @param nick nickname of sender, message to use, and color of nickname
     * @return true (as per the general contract of Collection.add)
     */
    public boolean addMessage(String nick, String message, String color) {
        if (color == null) {
            color = DEFAULT_COLOR;
        }

        add(new Message(nick, message, color));

        return (true);
    }
}
