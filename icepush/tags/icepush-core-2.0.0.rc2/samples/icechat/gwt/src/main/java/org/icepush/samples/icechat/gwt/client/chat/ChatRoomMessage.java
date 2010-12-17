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
 * The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icepush.samples.icechat.gwt.client.chat;

import java.io.Serializable;

/**
 * this class is a light weight transfer object representing a chat message.
 */
public class ChatRoomMessage implements Serializable{

    String text;
    String nickname;

    public ChatRoomMessage(){}


    void initialize(String text, String nickname){
        this.text = text;
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getText() {
        return text;
    }


}
