package org.icepush.samples.icechat.gwt.client.service;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;
import org.icepush.samples.icechat.gwt.client.chat.ChatRoomHandle;


/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("chat")
public interface ChatService extends RemoteService {
  	public void createChatRoom(String name);
   	public List<ChatRoomHandle> getChatRooms();
}