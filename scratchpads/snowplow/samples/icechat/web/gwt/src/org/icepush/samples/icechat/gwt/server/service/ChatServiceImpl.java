package org.icepush.samples.icechat.gwt.server.service;

import org.icepush.samples.icechat.gwt.client.chat.ChatRoomMessage;
import org.icepush.samples.icechat.gwt.client.service.ChatService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.icepush.samples.icechat.gwt.push.adapter.GWTPushRequestContextAdaptor;
import org.icepush.samples.icechat.gwt.server.service.beans.AuthenticationProvider;
import org.icepush.samples.icechat.gwt.server.service.beans.ChatServiceBean;
import org.icepush.PushContext;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import org.icepush.samples.icechat.gwt.client.chat.ChatHandleBuilder;
import org.icepush.samples.icechat.gwt.client.chat.ChatMessageBuilder;
import org.icepush.samples.icechat.gwt.client.chat.ChatRoomHandle;
import org.icepush.samples.icechat.model.ChatRoom;
import org.icepush.samples.icechat.model.Message;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.model.UserChatSession;

public class ChatServiceImpl extends RemoteServiceServlet implements
		ChatService {

	private List<String> chatRooms = new ArrayList<String>();
	private HashMap<String, List<String>> participants = new HashMap<String, List<String>>();
	private HashMap<String, String> participantTextBoxes = new HashMap<String, String>();
	private HashMap<String, List<ChatRoomMessage>> chatMessages = new HashMap<String, List<ChatRoomMessage>>();

	public ChatRoomHandle createChatRoom(String name) {

		User currentUser = AuthenticationProvider
				.getSessionUser(getThreadLocalRequest());

		ChatServiceBean chatService = ChatServiceBean.getInstance(this
				.getServletContext());
		UserChatSession session = chatService.createNewChatRoom(name,
				currentUser.getUserName(), currentUser.getPassword());
		//    	
		// this.chatRooms.add(name);
		GWTPushRequestContextAdaptor pushAdaptor = GWTPushRequestContextAdaptor
				.getInstance(this.getServletContext(), getThreadLocalRequest(),
						getThreadLocalResponse());
		PushContext pushContext = pushAdaptor.getPushContext();

		ChatHandleBuilder builder = new ChatHandleBuilder();
		ChatRoomHandle handle = builder.createHandle(session.getRoom()
				.getName());

		pushContext.push("chatRoomIndex");
		return handle;

	}

	public List<ChatRoomHandle> getChatRooms() {

		ChatServiceBean chatService = ChatServiceBean.getInstance(this
				.getServletContext());

		List<ChatRoom> chatRooms = chatService.getChatRooms();
		List<ChatRoomHandle> result = new ArrayList<ChatRoomHandle>(chatRooms
				.size());

		for (ChatRoom chatRoom : chatRooms) {
			ChatHandleBuilder builder = new ChatHandleBuilder();
			ChatRoomHandle handle = builder.createHandle(chatRoom.getName());
			result.add(handle);
		}
		return result;
	}

	public void joinChatRoom(ChatRoomHandle handle, String username) {
		
		User currentUser = AuthenticationProvider
				.getSessionUser(getThreadLocalRequest());

		ChatServiceBean chatService = ChatServiceBean.getInstance(this
				.getServletContext());

		chatService.loginToChatRoom(handle.getName(), currentUser.getUserName(), currentUser.getPassword());
		
		this.participantTextBoxes.put(username + handle.getName(), "");

		PushContext.getInstance(this.getServletContext()).push(
				handle.getName() + "-participants");

	}

	public List<String> getParticipants(ChatRoomHandle handle) {
		
		ChatServiceBean chatService = ChatServiceBean.getInstance(this
				.getServletContext());
		
		List<User> users = chatService.getChatRoomParticipants(handle.getName());
		
		List<String> userNames = new ArrayList<String>(users.size());
		for(User u: users){
			userNames.add(u.getUserName());
		}
		return userNames;
		
	}

	public void sendMessage(String message, String username,
			ChatRoomHandle handle) {
		
		User currentUser = AuthenticationProvider
			.getSessionUser(getThreadLocalRequest());

		ChatServiceBean chatService = ChatServiceBean.getInstance(this
			.getServletContext());
		
		chatService.sendNewMessage(handle.getName(), currentUser.getUserName(), currentUser.getPassword(), message);

		participantTextBoxes.put(username + handle.getName(), "");

		PushContext.getInstance(this.getServletContext())
				.push(handle.getName());
		PushContext.getInstance(this.getServletContext()).push(
				username + handle.getName().replaceAll(" ", "_"));
	}

	public ChatRoomHandle getMessages(ChatRoomHandle handle) {
		try{
		ChatServiceBean chatService = ChatServiceBean.getInstance(this
			.getServletContext());
		
		List<ChatRoomMessage> result = new ArrayList<ChatRoomMessage>();
		
		List<Message> messages = chatService.getChatRoomMessagesFromIndex(handle.getName(), handle.getMessageIndex());
		
		ChatMessageBuilder messageBuilder = new ChatMessageBuilder();
		for(Message m: messages){
			result.add(messageBuilder.createChatMessage(m.getMessage(), m.getUserChatSession().getUser().getUserName()));
		}

		

		ChatHandleBuilder builder = new ChatHandleBuilder();
		builder.addMessages(handle, result);
		return handle;
		}catch(RuntimeException e){
			e.printStackTrace();
			throw e;
		}
	}

	public void sendCharacterNotification(String username,
			ChatRoomHandle handle, String newText) {
		this.participantTextBoxes.put(username + handle.getName(), newText);

		PushContext.getInstance(this.getServletContext()).push(
				username + handle.getName().replaceAll(" ", "_"));

	}

	public String getCurrentCharacters(String username, ChatRoomHandle handle) {
		return this.participantTextBoxes.get(username + handle.getName());
	}

}
