package org.icepush.samples.icechat.beans.facade;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.icepush.samples.icechat.model.ChatRoom;
import org.icepush.samples.icechat.model.Message;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.service.api.ChatServiceLocal;

@Named
@SessionScoped
public class ChatManagerFacade implements Serializable{
	
	@EJB
    private ChatServiceLocal chatService;

	public List<Message> getAllChatRoomMessages(String chatRoom) {
		return chatService.getAllChatRoomMessages(chatRoom);
	}

	public List<Message> getChatRoomMessagesFromIndex(String chatRoom, int index) {
		return chatService.getChatRoomMessagesFromIndex(chatRoom, index);
	}

	public List<ChatRoom> getChatRooms() {
		return chatService.getChatRooms();
	}

	public Collection<User> getOnlineUsers() {
		return chatService.getOnlineUsers();
	}
	
	
	
}
