package org.icepush.samples.icechat.gwt.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

import org.icepush.samples.icechat.gwt.client.chat.ChatRoomDraft;
import org.icepush.samples.icechat.gwt.client.chat.ChatRoomHandle;

public interface ChatServiceAsync {

    void createChatRoom(String name, String sessionToken, AsyncCallback<ChatRoomHandle> callback);

    void getChatRooms(AsyncCallback<List<ChatRoomHandle>> callback);
    
    void joinChatRoom(ChatRoomHandle handle, String sessionToken, AsyncCallback<Void> callback);
    
    void getParticipants(ChatRoomHandle handle, AsyncCallback<List<String>> callback );

    void sendMessage(String message, String sessionToken, ChatRoomHandle handle, AsyncCallback<Void> callback);

    void getMessages(ChatRoomHandle handle, AsyncCallback<ChatRoomHandle> callback);

    void sendCharacterNotification(String sessionToken, ChatRoomHandle handle, String newText, AsyncCallback<Void> callback);

    void getCurrentCharacters(String sessionToken, ChatRoomHandle handle, AsyncCallback<String> callback);
    
    void getNextDraftUpdate(ChatRoomHandle handle, AsyncCallback<ChatRoomDraft> callback);
}
