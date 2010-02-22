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
import java.util.ArrayList;
import java.util.List;

/**
 * This is an immutabe handle to a chat room.
 */
public class ChatRoomHandle implements Serializable{

    private String chatRoomName;
    private int messageIndex;
    

    private List<ChatRoomMessage> nextMessages = new ArrayList<ChatRoomMessage>();

    protected void initialize(String name){
        this.chatRoomName = name;
        this.messageIndex = 0;
    }

    public String getName(){
        return this.chatRoomName;
    }

    protected void updateMessageIndex(int index){
        this.messageIndex = index;
    }

    public int getMessageIndex(){
        return this.messageIndex;
    }

    public void setNextMessages(List<ChatRoomMessage> messages){
        this.nextMessages = messages;
    }

    public List<ChatRoomMessage> getNextMessages(){
        return this.nextMessages;
    }


}
