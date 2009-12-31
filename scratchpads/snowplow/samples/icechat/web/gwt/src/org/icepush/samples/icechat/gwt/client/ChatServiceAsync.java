package org.icepush.samples.icechat.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

public interface ChatServiceAsync{
	void createChatRoom(String name, AsyncCallback<Void> callback);
	
	 void getChatRooms(AsyncCallback<List<String>> callback);
}