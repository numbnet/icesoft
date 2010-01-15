package org.icepush.samples.icechat.gwt.server.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.icepush.PushContext;
import org.icepush.samples.icechat.gwt.client.chat.ChatHandleBuilder;
import org.icepush.samples.icechat.gwt.client.chat.ChatMessageBuilder;
import org.icepush.samples.icechat.gwt.client.chat.ChatRoomDraft;
import org.icepush.samples.icechat.gwt.client.chat.ChatRoomHandle;
import org.icepush.samples.icechat.gwt.client.chat.ChatRoomMessage;
import org.icepush.samples.icechat.gwt.client.service.ChatService;
import org.icepush.samples.icechat.gwt.push.adapter.GWTPushRequestContextAdaptor;
import org.icepush.samples.icechat.gwt.server.service.beans.AuthenticationProvider;
import org.icepush.samples.icechat.gwt.server.service.beans.ChatServiceBean;
import org.icepush.samples.icechat.model.ChatRoom;
import org.icepush.samples.icechat.model.Message;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.model.UserChatSession;
import org.icepush.samples.icechat.service.exception.UnauthorizedException;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ChatServiceImpl extends RemoteServiceServlet implements
		ChatService {


	private HashMap<String, String> participantTextBoxes = new HashMap<String, String>();
	
	private HashMap<String,Queue<ChatRoomDraft>> draftMessages = new HashMap<String, Queue<ChatRoomDraft>>();

	public ChatRoomHandle createChatRoom(String name, String sessionToken) {

		User currentUser = AuthenticationProvider
				.getSessionUser(getServletContext(), sessionToken);

		ChatServiceBean chatService = ChatServiceBean.getInstance(this
				.getServletContext());
		chatService.createNewChatRoom(name, currentUser);
				
		UserChatSession session = chatService.loginToChatRoom(name, currentUser);
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

	public void joinChatRoom(ChatRoomHandle handle, String sessionToken) {
		try{
		User currentUser = AuthenticationProvider
				.getSessionUser(getServletContext(),sessionToken);

		ChatServiceBean chatService = ChatServiceBean.getInstance(this
				.getServletContext());

		chatService.loginToChatRoom(handle.getName(), currentUser);
		
//		this.participantTextBoxes.put(username + handle.getName(), "");
		this.draftMessages.put(sessionToken, new LinkedBlockingQueue<ChatRoomDraft>());
		this.draftMessages.get(sessionToken).add(new ChatRoomDraft("",sessionToken));

		PushContext.getInstance(this.getServletContext()).push(
				handle.getName() + "-participants");
		}catch(RuntimeException e){
			e.printStackTrace();
			throw e;
		}

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

	public void sendMessage(String message, String sessionToken,
			ChatRoomHandle handle) {
		
		User currentUser = AuthenticationProvider
			.getSessionUser(getServletContext(), sessionToken);

		ChatServiceBean chatService = ChatServiceBean.getInstance(this
			.getServletContext());
		
		try{
			chatService.sendNewMessage(handle.getName(), currentUser, message);

			List<User> allUsers = chatService.getChatRoomParticipants(handle.getName());
			for(User user: allUsers){
				if(this.draftMessages.get(user.getUserName()) == null){
					this.draftMessages.put(user.getUserName(), new LinkedBlockingQueue<ChatRoomDraft>());
				} 
				ChatRoomDraft userDraft = new ChatRoomDraft();
				userDraft.setText("");
				this.draftMessages.get(user.getUserName()).add(userDraft);
			}
			
			PushContext.getInstance(this.getServletContext())
					.push(handle.getName().replaceAll(" ", "_"));
			
			PushContext.getInstance(this.getServletContext()).push(
					handle.getName().replaceAll(" ", "_")+"-draft");
			
		}
		catch(UnauthorizedException e){
			e.printStackTrace();
		}
		
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

	public void sendCharacterNotification(String sessionToken,
			ChatRoomHandle handle, String newText) {
//		this.participantTextBoxes.put(username + handle.getName(), newText);

		ChatServiceBean chatService = ChatServiceBean.getInstance(this
				.getServletContext());
		
		List<User> allUsers = chatService.getChatRoomParticipants(handle.getName());
		for(User user: allUsers){
			if(this.draftMessages.get(user.getUserName()) == null){
				this.draftMessages.put(user.getUserName(), new LinkedBlockingQueue<ChatRoomDraft>());
			} 
			ChatRoomDraft userDraft = new ChatRoomDraft();
			userDraft.setText(newText);
//			userDraft.setUsername(username);
			this.draftMessages.get(user.getUserName()).add(userDraft);
		}
		
		PushContext.getInstance(this.getServletContext()).push(
				handle.getName().replaceAll(" ", "_") + "-draft");

	}

	public String getCurrentCharacters(String username, ChatRoomHandle handle) {
		
		return this.participantTextBoxes.get(username + handle.getName());
	}
	
	public ChatRoomDraft getNextDraftUpdate(ChatRoomHandle handle){
		User currentUser = AuthenticationProvider
		.getSessionUser(getThreadLocalRequest());
		
		return this.draftMessages.get(currentUser.getUserName()).poll();
	}

}
