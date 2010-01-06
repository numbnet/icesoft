package org.icepush.samples.icechat.gwt.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import org.icepush.samples.icechat.gwt.client.chat.ChatRoomHandle;

public interface ChatServiceAsync {

    void createChatRoom(String name, AsyncCallback<ChatRoomHandle> callback);

    void getChatRooms(AsyncCallback<List<ChatRoomHandle>> callback);
    
    void joinChatRoom(ChatRoomHandle handle, String username, AsyncCallback<Void> callback);
    
    void getParticipants(ChatRoomHandle handle, AsyncCallback<List<String>> callback );

    void sendMessage(String message, String username, ChatRoomHandle handle, AsyncCallback<Void> callback);

    void getMessages(ChatRoomHandle handle, AsyncCallback<ChatRoomHandle> callback);

    void sendCharacterNotification(String username, ChatRoomHandle handle, String newText, AsyncCallback<Void> callback);

    void getCurrentCharacters(String username, ChatRoomHandle handle, AsyncCallback<String> callback);
}
