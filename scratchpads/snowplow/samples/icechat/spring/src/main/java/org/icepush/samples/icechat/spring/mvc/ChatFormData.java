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

package org.icepush.samples.icechat.spring.mvc;

import org.icepush.samples.icechat.spring.impl.BaseChatRoom;
import org.icepush.samples.icechat.spring.impl.BaseChatManagerViewController;
import org.icepush.samples.icechat.spring.impl.BaseCurrentChatSessionHolder;
import org.icepush.samples.icechat.spring.impl.BaseChatMessage;

public class ChatFormData {
    private BaseChatManagerViewController chatManagerViewController;
    private BaseCurrentChatSessionHolder currentChatSessionHolder;
    private BaseChatRoom newChatRoom;
    private BaseChatMessage newMessage;

    public ChatFormData() {
        newChatRoom = new BaseChatRoom();
        newMessage = new BaseChatMessage();
    }

    public BaseChatManagerViewController getChatManagerViewController() {
        return chatManagerViewController;
    }

    public void setChatManagerViewController(BaseChatManagerViewController chatManagerViewController) {
        this.chatManagerViewController = chatManagerViewController;
    }

    public BaseCurrentChatSessionHolder getCurrentChatSessionHolder() {
        return currentChatSessionHolder;
    }

    public void setCurrentChatSessionHolder(BaseCurrentChatSessionHolder currentChatSessionHolder) {
        this.currentChatSessionHolder = currentChatSessionHolder;
    }

    public BaseChatRoom getNewChatRoom() {
        return newChatRoom;
    }

    public void setNewChatRoom(BaseChatRoom newChatRoom) {
        this.newChatRoom = newChatRoom;
    }

    public BaseChatMessage getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(BaseChatMessage newMessage) {
        this.newMessage = newMessage;
    }
}
