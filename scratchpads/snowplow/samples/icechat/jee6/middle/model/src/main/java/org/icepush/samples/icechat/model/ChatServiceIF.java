/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.icepush.samples.icechat.model;

import java.util.Collection;
import java.util.List;

/**
 *
 * @author pbreau
 */
public interface ChatServiceIF {

    public Collection<ChatRoom> getChatRooms();

    public Collection<User> getOnlineUsers();

    public void loginToChatRoom(String chatRoom, String userName, String password);

    public void logoutOfChatRoom(String chatRoom, String userName, String password);

    public List<Message> getAllChatRoomMessages(String chatRoom);

    public List<Message> getChatRoomMessagesFromIndex(String chatRoom, int index);

    public void createNewChatRoom(String name, String user, String password);

    public void sendNewMessage(String chatRoom, String user, String password, String message);

    public void createNewUser(String userName, String nickName, String password);

    public void setUserAvatar(String userName, String password, byte[] avatar);

    public User login(String userName, String password);

    public User register(String userName, String nickName, String password);
}
