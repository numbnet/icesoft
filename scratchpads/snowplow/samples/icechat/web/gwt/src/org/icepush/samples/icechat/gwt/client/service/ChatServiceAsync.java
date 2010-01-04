package org.icepush.samples.icechat.gwt.client.service;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;
import org.icepush.samples.icechat.gwt.client.chat.ChatRoomHandle;

public interface ChatServiceAsync{
	void createChatRoom(String name, AsyncCallback<Void> callback);
	
	 void getChatRooms(AsyncCallback<List<ChatRoomHandle>> callback);
}