package org.icepush.samples.icechat.beans.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.icepush.samples.icechat.model.ChatRoom;
import org.icepush.samples.icechat.model.Message;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.model.UserChatSession;
import org.icepush.samples.icechat.service.IChatService;
import org.icepush.samples.icechat.service.exception.LoginFailedException;
import org.icepush.samples.icechat.service.exception.UnauthorizedException;

/**
 * @TODO This class does not look threadsafe....
 * 
 */
public abstract class BaseChatServiceBean implements Serializable, IChatService {

	protected Map<String, ChatRoom> chatRooms = new HashMap<String, ChatRoom>();
	protected Map<String, User> users = new HashMap<String, User>();

	private static Logger LOG = Logger.getLogger(BaseChatServiceBean.class
			.getName());

	public BaseChatServiceBean() {
		chatRooms = new HashMap<String, ChatRoom>();
		users = new HashMap<String, User>();
	}

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

	public UserChatSession loginToChatRoom(String chatRoom, String userName,
			String password) {
		ChatRoom room = chatRooms.get(chatRoom);
		UserChatSession session = null;
		if (room != null) {
			User user = users.get(userName);
			if (user != null && user.getPassword().equals(password)) {
				if (room.hasUserSession(userName)) {
					for (UserChatSession ucs : user.getChatSessions()) {
						if (ucs.getRoom().getName().equals(chatRoom))
							session = ucs;
					}
				} else {
					session = new UserChatSession();
					session.setLive(true);
					session.setRoom(room);
					session.setUser(user);
					room.getUserChatSessions().add(session);
					user.getChatSessions().add(session);
				}
			}
		}
		return session;
	}

	public void logoutOfChatRoom(String chatRoom, String userName,
			String password) {
		ChatRoom room = chatRooms.get(chatRoom);
		if (room != null) {
			User user = users.get(userName);
			if (user != null && user.getPassword().equals(password)) {
				for (UserChatSession chatSession : user.getChatSessions()) {
					if (chatSession.getRoom().getName().equals(chatRoom)) {
						chatSession.setExited(new Date());
						chatSession.setLive(false);
					}
				}
			}
		}
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
			
			
			//TODO this seems incorrect...
//			return room.getMessages().size() >= index ? room.getMessages()
//					.subList(index, room.getMessages().size() - 1) : null;
		} else {
			return null;
		}
	}
	


	public void createNewChatRoom(String name, String userName,
			String password) {
		User user = users.get(userName);
		UserChatSession chatSession = null;
		if (user != null && user.getPassword().equals(password)) {
			if (chatRooms.get(name) == null) {
				ChatRoom newRoom = new ChatRoom();
				newRoom.setName(name);
				newRoom.setCreated(new Date());
				chatRooms.put(name, newRoom);
			} else {
				LOG.warning("not creating chat room name=" + name
						+ " already exists");
			}
		} else {
			LOG.warning("not creating chat room name="
					+ name
					+ ", user="
					+ user
					+ (user != null ? ", password correct="
							+ user.getPassword().equals(password) : ""));
		}
	}

	public ChatRoom getChatRoom(String roomName) {
		return chatRooms.get(roomName);
	}

	public void sendNewMessage(String chatRoom, String userName,
			String password, String message) throws UnauthorizedException {
		User user = validateUser(userName, password);
		ChatRoom room = getChatRoom(chatRoom);
		if (room != null) {
			if( !room.hasUserSession(userName))
				throw new UnauthorizedException("user '" + userName + "' not in room '" + chatRoom + "', ignoring message");
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

	@Override
	public void updateCurrentDraft(String draft, String roomName,
			String userName, String password) throws UnauthorizedException {
		UserChatSession session = getUserChatSession(roomName, userName, password);
		if( session != null ){
			session.setCurrentDraft(draft);
		}
	}

	@Override
	public UserChatSession getUserChatSession(String roomName, String userName,
			String password) throws UnauthorizedException {
		User user = validateUser(userName, password);
		for( UserChatSession s : user.getChatSessions() ){
			if( s.getRoom().getName().equals(roomName)){
				return s;
			}
		}
		return null;
	}

	public User validateUser(String userName, String password)
			throws UnauthorizedException {
		User user = users.get(userName);
		if (user == null || !user.getPassword().equals(password)) {
			throw new UnauthorizedException();
		}
		return user;
	}

	public void createNewUser(String userName, String nickName, String password) {
		User user = new User();
		user.setUserName(userName);
		user.setNickName(nickName);
		user.setPassword(password);
		users.put(userName, user);

	}

	public void setUserAvatar(String userName, String password, byte[] avatar) {
		User user = users.get(userName);
		if (user != null && user.getPassword().equals(password)) {
			user.setAvatar(avatar);
		}
	}

	public User login(String userName, String password)
			throws LoginFailedException {
		User user = users.get(userName);
		if (user != null && user.getPassword().equals(password)) {
			return user;
		} else
			throw new LoginFailedException(
					"The provided username and password are not recognized.");
	}

	public User register(String userName, String nickName, String password) {
		if (users.get(userName) == null) {
			User user = new User();
			user.setUserName(userName);
			user.setNickName(nickName);
			user.setPassword(password);
			users.put(userName, user);
			return user;
		} else {
			return null;
		}
	}

	public List<User> getChatRoomUsers(String chatRoomName, String userName,
			String password) {
		List<User> userList = null;
		ChatRoom room = chatRooms.get(chatRoomName);
		if (room != null) {
			User user = users.get(userName);
			if (user != null && user.getPassword().equals(password)) {
				Iterator<UserChatSession> sessions = room.getUserChatSessions()
						.iterator();
				userList = new ArrayList<User>();
				while (sessions.hasNext()) {
					userList.add(sessions.next().getUser());
				}
			}
		}
		return userList;
	}

}
