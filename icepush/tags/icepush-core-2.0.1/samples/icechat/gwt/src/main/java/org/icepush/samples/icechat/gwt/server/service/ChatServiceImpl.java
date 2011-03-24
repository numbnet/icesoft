/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icepush.samples.icechat.gwt.server.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import org.icepush.PushContext;
import org.icepush.samples.icechat.gwt.client.Credentials;
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
		chatService.createNewChatRoom(name);
				
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

	public List<Credentials> getParticipants(ChatRoomHandle handle) {
		
		ChatServiceBean chatService = ChatServiceBean.getInstance(this
				.getServletContext());
		
		List<User> users = chatService.getChatRoomParticipants(handle.getName());
		
		List<Credentials> userNames = new ArrayList<Credentials>(users.size());
		for(User u: users){
			Credentials cred = new Credentials();
			cred.setUserName(u.getName());
			cred.setSessionToken(u.getSessionToken());
			
			userNames.add(cred);
		}
		return userNames;
		
	}

	public void sendMessage(String message, String sessionToken,
			ChatRoomHandle handle) {
		
		User currentUser = AuthenticationProvider
			.getSessionUser(getServletContext(), sessionToken);

		ChatServiceBean chatService = ChatServiceBean.getInstance(this
			.getServletContext());
		
		chatService.sendNewMessage(handle.getName(), currentUser, message);

		List<User> allUsers = chatService.getChatRoomParticipants(handle.getName());
		for(User user: allUsers){
			if(this.draftMessages.get(user.getSessionToken()) == null){
				this.draftMessages.put(user.getSessionToken(), new LinkedBlockingQueue<ChatRoomDraft>());
			} 
			ChatRoomDraft userDraft = new ChatRoomDraft();
			userDraft.setText("");
			userDraft.setUserSessionToken(sessionToken);
			this.draftMessages.get(user.getSessionToken()).add(userDraft);
		}
		
		PushContext.getInstance(this.getServletContext())
				.push(handle.getName().replaceAll(" ", "_"));
		
		PushContext.getInstance(this.getServletContext()).push(
				handle.getName().replaceAll(" ", "_")+"-draft");
			
		
	}

	public ChatRoomHandle getMessages(ChatRoomHandle handle) {
		try{
		ChatServiceBean chatService = ChatServiceBean.getInstance(this
			.getServletContext());
		
		List<ChatRoomMessage> result = new ArrayList<ChatRoomMessage>();
		
		List<Message> messages = chatService.getChatRoomMessagesFromIndex(handle.getName(), handle.getMessageIndex());
		
		ChatMessageBuilder messageBuilder = new ChatMessageBuilder();
		for(Message m: messages){
			result.add(messageBuilder.createChatMessage(m.getMessage(), m.getUserChatSession().getUser().getName()));
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
			if(this.draftMessages.get(user.getName()) == null){
				this.draftMessages.put(user.getName(), new LinkedBlockingQueue<ChatRoomDraft>());
			} 
			ChatRoomDraft userDraft = new ChatRoomDraft();
			userDraft.setText(newText);
			userDraft.setUserSessionToken(sessionToken);
			this.draftMessages.get(user.getSessionToken()).add(userDraft);
		}
		
		PushContext.getInstance(this.getServletContext()).push(
				handle.getName().replaceAll(" ", "_") + "-draft");
		

	}

	public String getCurrentCharacters(String username, ChatRoomHandle handle) {
		
		return this.participantTextBoxes.get(username + handle.getName());
	}
	
	public List<ChatRoomDraft> getNextDraftUpdate(String userSessionToken, ChatRoomHandle handle){

		List<ChatRoomDraft> result = new LinkedList<ChatRoomDraft>();
		while(!this.draftMessages.get(userSessionToken).isEmpty()){
			result.add(this.draftMessages.get(userSessionToken).poll());
		}
		return result;
	}
	
	public void endLongPoll(){
		PushContext.getInstance(this.getServletContext()).push("chatRoomIndex");
	}

}
