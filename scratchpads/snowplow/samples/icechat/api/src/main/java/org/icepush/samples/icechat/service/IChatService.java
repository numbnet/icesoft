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

package org.icepush.samples.icechat.service;

import java.util.Collection;
import java.util.List;

import org.icepush.samples.icechat.model.ChatRoom;
import org.icepush.samples.icechat.model.Message;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.model.UserChatSession;
import org.icepush.samples.icechat.service.exception.LoginFailedException;
import org.icepush.samples.icechat.service.exception.UnauthorizedException;

/**
 *
 */
public interface IChatService {

    public List<ChatRoom> getChatRooms();

    public Collection<User> getOnlineUsers();

    @Deprecated
    public UserChatSession loginToChatRoom(String chatRoom, String userName, String password);
    
    public UserChatSession loginToChatRoom(String chatRoom, User user);
    
    public ChatRoom getChatRoom(String roomName);

    @Deprecated
    public void logoutOfChatRoom(String chatRoom, String userName, String password);

    public void logoutOfChatRoom(String chatRoom, User user);
    
    public List<Message> getAllChatRoomMessages(String chatRoom);

    public List<Message> getChatRoomMessagesFromIndex(String chatRoom, int index);

    @Deprecated
    public void createNewChatRoom(String name, String user, String password);

    public void createNewChatRoom(String name, User user);
    
    @Deprecated
    public void sendNewMessage(String chatRoom, String user, String password, String message)
    	throws UnauthorizedException;
    
    public void sendNewMessage(String chatRoom, User user, String message)
    	throws UnauthorizedException;

    @Deprecated
    public void createNewUser(String userName, String nickName, String password);

    public void createNewUser(String username);
    
    @Deprecated
    public void setUserAvatar(String userName, String password, byte[] avatar);

    public void setUserAvator(User user, byte[] avatar);
    
    @Deprecated
    public User login(String userName, String password) throws LoginFailedException;

    @Deprecated
    public User register(String userName, String nickName, String password);
    
    public User register(String username);
    
    @Deprecated
    public List<User> getChatRoomUsers(String chatRoomName, String userName, String password);
    
    public List<User> getChatRoomUsers(String chatRoomName);
    
    @Deprecated
    public void updateCurrentDraft(String draft, String roomName, String userName, String password)
    	throws UnauthorizedException;
    
    public void updateCurrentDraft(String draft, String roomName, User user);
    
    @Deprecated
    public UserChatSession getUserChatSession(String roomName, String userName, String password)
    	throws UnauthorizedException;
    
    public UserChatSession getUserChatSession(String roomName, User user);
    
    public User getSessionUser(String sessionToken);
}
