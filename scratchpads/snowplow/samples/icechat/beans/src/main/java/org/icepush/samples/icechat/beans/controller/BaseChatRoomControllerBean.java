package org.icepush.samples.icechat.beans.controller;

import java.io.Serializable;
import java.util.logging.Logger;

import org.icepush.PushContext;
import org.icepush.samples.icechat.IPushRequestContext;
import org.icepush.samples.icechat.controller.IChatRoomController;
import org.icepush.samples.icechat.model.ChatRoom;
import org.icepush.samples.icechat.model.UserChatSession;
import org.icepush.samples.icechat.service.IChatService;
import org.icepush.samples.icechat.service.exception.UnauthorizedException;

public class BaseChatRoomControllerBean implements Serializable, IChatRoomController{
	
	private static final long serialVersionUID = 3046754615536057774L;

	private IChatService chatService;
	
	protected IPushRequestContext pushRequestContext;
	
	private static Logger log = Logger.getLogger(BaseChatRoomControllerBean.class.getName());
	
	public void setPushRequestContext(IPushRequestContext pushRequestContext) {
		this.pushRequestContext = pushRequestContext;
	}
    
    public IPushRequestContext getPushRequestContext() {
        return pushRequestContext;
    }

	public void createNewChatRoom(String chatRoomName, String userName, String password){
        chatService.createNewChatRoom(chatRoomName, userName, password);
		PushContext pushContext = this.getPushRequestContext().getPushContext();
        pushContext.addGroupMember(chatRoomName,pushRequestContext.getCurrentPushId());
        pushContext.push(chatRoomName);
	}
	
	public UserChatSession openChatSession(String chatRoomName, String userName, String password){
		UserChatSession session = null;
		for( ChatRoom room : chatService.getChatRooms() ){
			if( room.getName().equals(chatRoomName)){
				session = chatService.loginToChatRoom(chatRoomName, userName, password);
				PushContext pushContext = this.getPushRequestContext().getPushContext();
		        pushContext.addGroupMember(chatRoomName,pushRequestContext.getCurrentPushId());
		        pushContext.push(chatRoomName);
			}
		}		
		return session;
	}
	
	public void sendNewMessage(String chatRoomName, String newMessage, String userName, String password)
		throws UnauthorizedException{
		chatService.sendNewMessage(chatRoomName, userName, password, newMessage);
		PushContext pushContext = this.getPushRequestContext().getPushContext();
        pushContext.push(chatRoomName);
	}
	
	public void setChatService(IChatService chatService){
            this.chatService = chatService;
	}

}
