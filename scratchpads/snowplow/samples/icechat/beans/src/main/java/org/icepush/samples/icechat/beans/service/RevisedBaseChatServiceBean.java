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
public abstract class RevisedBaseChatServiceBean implements IChatService{

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
				if (chatSession.isLive()) {
					onlineUsers.add(user);
				}
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
	
	
	public void createNewChatRoom(String chatRoomName, User user) {
		if (user != null) {
			if (chatRooms.get(chatRoomName) == null) {
				ChatRoom newRoom = new ChatRoom();
				newRoom.setName(chatRoomName);
				newRoom.setCreated(new Date());
				chatRooms.put(chatRoomName, newRoom);
			} else {
				LOG.warning("not creating chat room name=" + chatRoomName
						+ " already exists");
			}
		} else {
			LOG.warning("Can't create chatRoom with null user.");
		}
	}

	
	public void createNewUser(String username) {
		User user = new User();
		user.setUserName(username);
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
				if (room.isUserInRoom(user)) {
					for (UserChatSession ucs : user.getChatSessions()) {
						if (ucs.getRoom().getName().equals(chatRoom)){
							ucs.setExited(null);
							ucs.setEntered(new Date());
							ucs.setLive(true);
							session = ucs;
						}
					}
				} else {
					session = new UserChatSession();
					session.setLive(true);
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
				for (UserChatSession chatSession : user.getChatSessions()) {
					if (chatSession.getRoom().getName().equals(chatRoom)) {
						chatSession.setExited(new Date());
						chatSession.setLive(false);
					}
				}
			}else{
				LOG.warning("error leaving chatroom: user can't be null");
			}
		}else{
			LOG.warning("error leaving chatroom: not room with name " + chatRoom + " could be found");
		}
		
	}


	public User register(String username) {
		
			User user = new User();
			user.setUserName(username);
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


	public void setUserAvator(User user, byte[] avatar) {
		if (user != null) {
			user.setAvatar(avatar);
		}else{
			LOG.warning("can't set an avitor on a null user");
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
