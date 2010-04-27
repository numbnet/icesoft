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

package org.icepush.samples.icechat.beans.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Logger;

import org.icepush.samples.icechat.model.ChatRoom;
import org.icepush.samples.icechat.model.Message;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.model.UserChatSession;
import org.icepush.samples.icechat.service.IChatService;
import org.icepush.samples.icechat.service.exception.UnauthorizedException;

/**
 * @TODO the name of this class will need to be modified once the deprecated class is deleted.
 * 
 * this class is an implementation of the non deprecated methods in IChatService.  Once the deprecated methods are removed, this will be THE implementation of IChatService.
 *
 */
public abstract class BaseChatServiceBean implements IChatService{

	protected Map<String, ChatRoom> chatRooms = new HashMap<String, ChatRoom>();
	protected Map<String, User> users = new HashMap<String, User>();

	protected static Logger LOG = Logger.getLogger(BaseChatServiceBean.class
			.getName());

	
	
	public List<ChatRoom> getChatRooms() {
		return new ArrayList<ChatRoom>(chatRooms.values());
	}

	
	public Collection<User> getOnlineUsers() {
		Set<User> onlineUsers = new HashSet<User>();
		for (User user : users.values()) {
			for (UserChatSession chatSession : user.getChatSessions()) {
				onlineUsers.add(user);
			}
		}

		return onlineUsers;
	}
	

	
	public List<Message> getAllChatRoomMessages(String chatRoom) {
		ChatRoom room = chatRooms.get(chatRoom);
		if (room != null) {
			return room.getMessages();
		} else {
			return null;
		}
	}

	
	
	public List<Message> getChatRoomMessagesFromIndex(String chatRoom, int index) {
		
		ChatRoom room = chatRooms.get(chatRoom);
		if (room != null) {
			index = Math.max(0, index);
			index = Math.min(index, room.getMessages().size());
			//now index should be bounded properly.
			return room.getMessages().subList(index, room.getMessages().size());
		} else {
			return null;
		}
	}
	
	
	public ChatRoom getChatRoom(String roomName) {
		return chatRooms.get(roomName);
	}
	
	
	public void createNewChatRoom(String chatRoomName) {
		if (chatRooms.get(chatRoomName) == null) {
			ChatRoom newRoom = new ChatRoom();
			newRoom.setName(chatRoomName);
			newRoom.setCreated(new Date());
			chatRooms.put(chatRoomName, newRoom);
		} else {
			LOG.warning("not creating chat room name=" + chatRoomName
					+ " already exists");
		}		
	}

	
	public void createNewUser(String name) {
		User user = new User();
		user.setName(name);
		user.setSessionToken(UUID.randomUUID().toString());
		users.put(user.getSessionToken(), user);
		
	}

	
	public List<User> getChatRoomUsers(String chatRoomName) {
		List<User> userList = null;
		ChatRoom room = chatRooms.get(chatRoomName);
		if (room != null) {
				Iterator<UserChatSession> sessions = room.getUserChatSessions()
						.iterator();
				userList = new ArrayList<User>();
				while (sessions.hasNext()) {
					userList.add(sessions.next().getUser());
				}
		}
		return userList;
	}

	
	public UserChatSession getUserChatSession(String roomName, User user) {
		for (UserChatSession s : user.getChatSessions()) {
			if (s.getRoom().getName().equals(roomName)) {
				return s;
			}
		}
		LOG.warning("no session found for room: " + roomName + " and user " + user.getSessionToken());
		return null;
	}

	
	public UserChatSession loginToChatRoom(String chatRoom, User user) {
		ChatRoom room = chatRooms.get(chatRoom);
		UserChatSession session = null;
		if (room != null) {
			if (user != null) {
				if(!room.isUserInRoom(user)) {
					session = new UserChatSession();
					session.setRoom(room);
					session.setUser(user);
					room.getUserChatSessions().add(session);
					user.getChatSessions().add(session);
				}
			}else{
				LOG.warning("error joining chatroom: user can't be null");
			}
		}else{
			LOG.warning("error joining chatroom: not room with name " + chatRoom + " could be found");
		}
		return session;
	}


	public void logoutOfChatRoom(String chatRoom, User user) {
		ChatRoom room = chatRooms.get(chatRoom);
		if (room != null) {
			
			if (user != null) {
                List<UserChatSession> toRemove = new ArrayList();
				for (UserChatSession chatSession : user.getChatSessions()) {
					if (chatSession.getRoom().getName().equals(chatRoom)) {
                        toRemove.add(chatSession);
					}
				}
                for(UserChatSession chatSession : toRemove){
                   user.getChatSessions().remove(chatSession);
                }
			}else{
				LOG.warning("error leaving chatroom: user can't be null");
			}
		}else{
			LOG.warning("error leaving chatroom: not room with name " + chatRoom + " could be found");
		}
		
	}
	
	private boolean userNameUnused(String name){
		boolean result = true;
		for( User existingUser : users.values() ){
			if( existingUser.getName().equals(name)){
				result = false;
				break;
			}
		}
		return result;
	}


	public User login(String name) {
		
		String selectedName = null;
		if( userNameUnused(name)){
			selectedName = name;
		}
		else{
			String adjustedName = null;
			int counter = 0;
			do{
				adjustedName = name + ++counter;
			}
			while( !userNameUnused(adjustedName));
			selectedName = adjustedName;
		}
		
		User user = new User();
		user.setName(selectedName);
		user.setSessionToken(UUID.randomUUID().toString());
		users.put(user.getSessionToken(), user);
		return user;
		
	}


	public void sendNewMessage(String chatRoom, User user, String message)
			throws UnauthorizedException {
		ChatRoom room = getChatRoom(chatRoom);
		if (room != null) {
			if (!room.isUserInRoom(user))
				throw new UnauthorizedException("user '" + user.getSessionToken()
						+ "' not in room '" + chatRoom + "', ignoring message");
			for (UserChatSession chatSession : user.getChatSessions()) {
				if (chatSession.getRoom().getName().equals(chatRoom)) {
					Message msg = new Message();
					msg.setId(Long.valueOf(chatSession.getRoom().getMessages().size()+1));
					msg.setChatRoom(chatSession.getRoom());
					msg.setCreated(new Date());
					msg.setMessage(message);
					msg.setUserChatSession(chatSession);
					chatSession.getRoom().getMessages().add(msg);
					chatSession.setCurrentDraft(null);
				}
			}
		} else {
			LOG.warning("chat room '" + chatRoom
					+ "' does not exist, ignoring message");
		}
		
	}


	public void updateCurrentDraft(String draft, String roomName, User user) {
		UserChatSession session = getUserChatSession(roomName, user);
		if (session != null) {
			session.setCurrentDraft(draft);
		}
		
	}

	public User getSessionUser(String sessionToken){
		return users.get(sessionToken);
	}

}
