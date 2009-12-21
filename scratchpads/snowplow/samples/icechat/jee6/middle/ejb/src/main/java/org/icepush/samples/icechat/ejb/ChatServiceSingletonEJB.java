/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.icepush.samples.icechat.ejb;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.ejb.Singleton;
import org.icepush.samples.icechat.model.ChatRoom;
import org.icepush.samples.icechat.model.ChatServiceLocal;
import org.icepush.samples.icechat.model.Message;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.model.UserChatSession;

/**
 *
 * @author pbreau
 */
@Singleton
public class ChatServiceSingletonEJB implements ChatServiceLocal {

    private Map<String, ChatRoom> chatRooms;
    private Map<String, User> users;

    public Collection<ChatRoom> getChatRooms() {
        return chatRooms.values();
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

    public void loginToChatRoom(String chatRoom, String userName, String password) {
        ChatRoom room = chatRooms.get(chatRoom);
        if (room != null) {
            User user = users.get(userName);
            if (user != null && user.getPassword().equals(password)) {
                UserChatSession chatSession = new UserChatSession();
                chatSession.setLive(true);
                chatSession.setRoom(room);
                chatSession.setUser(user);
                room.getUserChatSessions().add(chatSession);
                user.getChatSessions().add(chatSession);
            }
        }
    }

    public void logoutOfChatRoom(String chatRoom, String userName, String password) {
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
        ChatRoom room = chatRooms.get("chatRoom");
        if (room != null) {
            return room.getMessages();
        } else {
            return null;
        }
    }

    public List<Message> getChatRoomMessagesFromIndex(String chatRoom, int index) {
        ChatRoom room = chatRooms.get("chatRoom");
        if (room != null) {
            return room.getMessages().size() >= index
                    ? room.getMessages().subList(index, room.getMessages().size() - 1) : null;
        } else {
            return null;
        }
    }

    public void createNewChatRoom(String name, String userName, String password) {
        User user = users.get(userName);
        if (user != null && user.getPassword().equals(password)) {
            if( chatRooms.get(name) == null ){
                ChatRoom room = new ChatRoom();
                room.setName(name);
                room.setCreated(new Date());
                chatRooms.put(name, room);

                UserChatSession chatSession = new UserChatSession();
                chatSession.setUser(user);
                chatSession.setRoom(room);
                chatSession.setLive(true);
                chatSession.setEntered(new Date());
                room.getUserChatSessions().add(chatSession);
                user.getChatSessions().add(chatSession);
            }
        }

    }

    public void sendNewMessage(String chatRoom, String userName, String password, String message) {
        User user = users.get(userName);
        if (user != null && user.getPassword().equals(password)) {
            for( UserChatSession chatSession : user.getChatSessions() ){
                if( chatSession.getRoom().getName().equals(chatRoom)){
                    Message msg = new Message();
                    msg.setChatRoom(chatSession.getRoom());
                    msg.setCreated(new Date());
                    msg.setMessage(message);
                    msg.setUserChatSession(chatSession);
                    chatSession.getRoom().getMessages().add(msg);
                }
            }
        }
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

    public User login(String userName, String password) {
        User user = users.get(userName);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        else
            return null;
    }

    public User register(String userName, String nickName, String password) {
        if( users.get(userName) == null ){
            User user = new User();
            user.setUserName(userName);
            user.setNickName(nickName);
            user.setPassword(password);
            return user;
        }
        else{
            return null;
        }
    }
}
