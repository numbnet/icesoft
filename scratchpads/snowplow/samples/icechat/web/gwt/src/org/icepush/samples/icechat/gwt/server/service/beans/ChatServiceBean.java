package org.icepush.samples.icechat.gwt.server.service.beans;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.icepush.samples.icechat.beans.service.BaseChatServiceBean;
import org.icepush.samples.icechat.model.ChatRoom;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.model.UserChatSession;

public class ChatServiceBean extends BaseChatServiceBean{

	private ChatServiceBean(){}
	
	public static ChatServiceBean getInstance(ServletContext context){
		
		if(context.getAttribute(ChatServiceBean.class.getName()) == null){
			context.setAttribute(ChatServiceBean.class.getName(), new ChatServiceBean());
		}
		
		return (ChatServiceBean) context.getAttribute(ChatServiceBean.class.getName());
	}
	
	public List<User> getChatRoomParticipants(String chatRoom){
		List<User> result = new ArrayList<User>();
		
		ChatRoom room = this.chatRooms.get(chatRoom);
		for(UserChatSession session: room.getUserChatSessions()){
			result.add(session.getUser());
		}
		return result;
	}
}
