package org.icepush.samples.icechat.gwt.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import org.icepush.samples.icechat.gwt.client.chat.ChatRoomHandle;
import org.icepush.samples.icechat.gwt.client.chat.ChatRoomMessage;


/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("chat")
public interface ChatService extends RemoteService {
  	public ChatRoomHandle createChatRoom(String name);
   	public List<ChatRoomHandle> getChatRooms();

        public void joinChatRoom(ChatRoomHandle handle, String username);
        public List<String> getParticipants(ChatRoomHandle handle);

        public void sendMessage(String message, String username, ChatRoomHandle handle);
        public ChatRoomHandle getMessages(ChatRoomHandle handle);
}