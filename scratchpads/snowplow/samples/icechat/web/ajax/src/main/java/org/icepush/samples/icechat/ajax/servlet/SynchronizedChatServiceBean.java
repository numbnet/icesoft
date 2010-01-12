package org.icepush.samples.icechat.ajax.servlet;

import java.util.Collection;
import java.util.List;

import org.icepush.samples.icechat.beans.service.BaseChatServiceBean;
import org.icepush.samples.icechat.model.ChatRoom;
import org.icepush.samples.icechat.model.Message;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.model.UserChatSession;
import org.icepush.samples.icechat.service.exception.LoginFailedException;
import org.icepush.samples.icechat.service.exception.UnauthorizedException;

public class SynchronizedChatServiceBean extends BaseChatServiceBean {

	public synchronized void createNewChatRoom(String name,
			String userName, String password) {
		super.createNewChatRoom(name, userName, password);
	}

	public synchronized void createNewUser(String userName, String nickName,
			String password) {
		super.createNewUser(userName, nickName, password);
	}

	public synchronized  List<Message> getAllChatRoomMessages(String chatRoom) {
		return super.getAllChatRoomMessages(chatRoom);
	}

	public synchronized  List<Message> getChatRoomMessagesFromIndex(String chatRoom,
			int index) {
		return super.getChatRoomMessagesFromIndex(chatRoom, index);
	}

	public synchronized  List<ChatRoom> getChatRooms() {
		return super.getChatRooms();
	}

	public synchronized Collection<User> getOnlineUsers() {
		return super.getOnlineUsers();
	}

	public synchronized User login(String userName, String password)
			throws LoginFailedException {
		return super.login(userName, password);
	}

	public synchronized UserChatSession loginToChatRoom(String chatRoom,
			String userName, String password) {
		return super.loginToChatRoom(chatRoom, userName, password);
	}

	public synchronized void logoutOfChatRoom(String chatRoom, String userName,
			String password) {
		super.logoutOfChatRoom(chatRoom, userName, password);
	}

	public synchronized User register(String userName, String nickName,
			String password) {
		return super.register(userName, nickName, password);
	}

	public synchronized void sendNewMessage(String chatRoom, String userName,
			String password, String message) throws UnauthorizedException{
		super.sendNewMessage(chatRoom, userName, password, message);
	}

	public synchronized void setUserAvatar(String userName, String password,
			byte[] avatar) {
		super.setUserAvatar(userName, password, avatar);
	}

	public synchronized List<User> getChatRoomUsers(String chatRoomName, String userName,
			String password) {
		return super.getChatRoomUsers(chatRoomName, userName, password);
	}
	

}
