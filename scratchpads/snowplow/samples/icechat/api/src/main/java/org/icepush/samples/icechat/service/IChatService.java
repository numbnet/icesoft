/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
 * @author pbreau
 */
public interface IChatService {

    public List<ChatRoom> getChatRooms();

    public Collection<User> getOnlineUsers();

    public UserChatSession loginToChatRoom(String chatRoom, String userName, String password);
    
    public ChatRoom getChatRoom(String roomName);

    public void logoutOfChatRoom(String chatRoom, String userName, String password);

    public List<Message> getAllChatRoomMessages(String chatRoom);

    public List<Message> getChatRoomMessagesFromIndex(String chatRoom, int index);

    public void createNewChatRoom(String name, String user, String password);

    public void sendNewMessage(String chatRoom, String user, String password, String message)
    	throws UnauthorizedException;

    public void createNewUser(String userName, String nickName, String password);

    public void setUserAvatar(String userName, String password, byte[] avatar);

    public User login(String userName, String password) throws LoginFailedException;

    public User register(String userName, String nickName, String password);
    
    public List<User> getChatRoomUsers(String chatRoomName, String userName, String password);
    
    public void updateCurrentDraft(String draft, String roomName, String userName, String password)
    	throws UnauthorizedException;
    
    public UserChatSession getUserChatSession(String roomName, String userName, String password)
    	throws UnauthorizedException;
}
