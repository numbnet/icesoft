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

package org.icepush.samples.icechat.gwt.client.service;

import java.util.List;

import org.icepush.samples.icechat.gwt.client.Credentials;
import org.icepush.samples.icechat.gwt.client.chat.ChatRoomDraft;
import org.icepush.samples.icechat.gwt.client.chat.ChatRoomHandle;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;



/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("chat")
public interface ChatService extends RemoteService {
  		public ChatRoomHandle createChatRoom(String name, String sessionToken);
  		public List<ChatRoomHandle> getChatRooms();

        public void joinChatRoom(ChatRoomHandle handle, String sessionToken);
        public List<Credentials> getParticipants(ChatRoomHandle handle);

        public void sendMessage(String message, String sessionToken, ChatRoomHandle handle);
        public ChatRoomHandle getMessages(ChatRoomHandle handle);
        public void sendCharacterNotification(String sessionToken, ChatRoomHandle handle,String newText);
        public String getCurrentCharacters(String sessionToken, ChatRoomHandle handle);
        public List<ChatRoomDraft> getNextDraftUpdate(String userSessionToken,ChatRoomHandle handle);
        
        public void endLongPoll();
}