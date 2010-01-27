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