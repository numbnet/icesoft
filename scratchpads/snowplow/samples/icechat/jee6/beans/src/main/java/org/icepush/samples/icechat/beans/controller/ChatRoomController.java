package org.icepush.samples.icechat.beans.controller;

import java.io.Serializable;
import java.util.logging.Logger;

import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.icepush.PushContext;
import org.icepush.samples.icechat.beans.model.PushRequestContext;
import org.icepush.samples.icechat.model.ChatRoom;
import org.icepush.samples.icechat.model.UserChatSession;
import org.icepush.samples.icechat.service.api.ChatServiceLocal;

@Named
@RequestScoped
public class ChatRoomController implements Serializable{
	
	private static final long serialVersionUID = 2402144917458969630L;

	@EJB
    private ChatServiceLocal chatService;
	
	@Inject
	private PushRequestContext pushRequestContext;
	
	@Inject Logger log;
			
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

}
