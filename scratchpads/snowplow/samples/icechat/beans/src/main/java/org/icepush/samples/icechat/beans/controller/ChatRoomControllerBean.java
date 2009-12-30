package org.icepush.samples.icechat.beans.controller;

import java.io.Serializable;
import java.util.logging.Logger;

import org.icepush.PushContext;
import org.icepush.samples.icechat.IPushRequestContext;
import org.icepush.samples.icechat.controller.IChatRoomController;
import org.icepush.samples.icechat.model.ChatRoom;
import org.icepush.samples.icechat.model.UserChatSession;
import org.icepush.samples.icechat.service.IChatService;

public class ChatRoomControllerBean implements Serializable, IChatRoomController{
	
	private static final long serialVersionUID = 3046754615536057774L;

	private IChatService chatService;
	
	private IPushRequestContext pushRequestContext;
	
	public void setPushRequestContext(IPushRequestContext pushRequestContext) {
		this.pushRequestContext = pushRequestContext;
	}

	private static Logger log;
			
	public UserChatSession createNewChatRoom(String chatRoomName, String userName, String password){
		UserChatSession session = chatService.createNewChatRoom(chatRoomName, userName, password);
		PushContext pushContext = pushRequestContext.getPushContext();
        pushContext.addGroupMember(chatRoomName,pushRequestContext.getCurrentPushId());
        pushContext.push(chatRoomName);
        log.info("created new chat session: " + session);
		return session;
	}
	
	public UserChatSession openChatSession(String chatRoomName, String userName, String password){
		UserChatSession session = null;
		for( ChatRoom room : chatService.getChatRooms() ){
			if( room.getName().equals(chatRoomName)){
				session = chatService.loginToChatRoom(chatRoomName, userName, password);
				PushContext pushContext = pushRequestContext.getPushContext();
		        pushContext.addGroupMember(chatRoomName,pushRequestContext.getCurrentPushId());
		        pushContext.push(chatRoomName);
			}
		}		
		return session;
	}
	
	public void sendNewMessage(String chatRoomName, String newMessage, String userName, String password){
		chatService.sendNewMessage(chatRoomName, userName, password,	newMessage);
		PushContext pushContext = pushRequestContext.getPushContext();
        pushContext.push(chatRoomName);
	}
	
	public void setChatService(IChatService chatService){
		this.chatService = chatService;
	}

}
