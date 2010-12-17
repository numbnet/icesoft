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

package org.icepush.samples.jsp.beans;

import java.util.Collection;
import java.util.List;

import org.icepush.samples.icechat.beans.service.BaseChatServiceBean;
import org.icepush.samples.icechat.model.ChatRoom;
import org.icepush.samples.icechat.model.Message;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.model.UserChatSession;
import org.icepush.samples.icechat.service.exception.UnauthorizedException;

public class ChatService extends BaseChatServiceBean {

	public synchronized ChatRoom createNewChatRoom(String name) {
		super.createNewChatRoom(name);
	}

	public synchronized void createNewUser(String userName) {
		super.createNewUser(userName);
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

	public synchronized User login(String userName) {
		return super.login(userName);
	}

	public synchronized UserChatSession loginToChatRoom(String chatRoom, User user) {
		return super.loginToChatRoom(chatRoom, user);
	}

	public synchronized void logoutOfChatRoom(String chatRoom, User user) {
		super.logoutOfChatRoom(chatRoom, user);
	}

	public synchronized void sendNewMessage(String chatRoom, User user, String message)
	throws UnauthorizedException{
		super.sendNewMessage(chatRoom, user, message);
	}

	public synchronized List<User> getChatRoomUsers(String chatRoomName) {
		return super.getChatRoomUsers(chatRoomName);
	}


}
