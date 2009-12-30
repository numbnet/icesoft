/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.icepush.samples.icechat.cdi.service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.icepush.samples.icechat.model.ChatRoom;
import org.icepush.samples.icechat.model.Message;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.model.UserChatSession;
import org.icepush.samples.icechat.service.api.ChatServiceLocal;
import org.icepush.samples.icechat.service.exception.LoginFailedException;

/**
 *
 * @author pbreau
 */
@Named(value="chatService")
@ApplicationScoped
public class ChatServiceApplicationBean implements IChatService, Serializable {

    private Map<String, ChatRoom> chatRooms = new HashMap<String,ChatRoom>();
    private Map<String, User> users = new HashMap<String,User>();
    
    public ChatServiceApplicationBean(){
    	chatRooms = new HashMap<String,ChatRoom>();
    	users = new HashMap<String,User>();
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

    public UserChatSession loginToChatRoom(String chatRoom, String userName, String password) {
        ChatRoom room = chatRooms.get(chatRoom);
        UserChatSession session = null;
        if (room != null) {
            User user = users.get(userName);
            if (user != null && user.getPassword().equals(password)) {
            	if( room.hasUserSession(userName)){
            		for( UserChatSession ucs : user.getChatSessions()){
            			if( ucs.getRoom().getName().equals(chatRoom))
            				session = ucs;
            		}
            	}
            	else{
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

    public UserChatSession createNewChatRoom(String name, String userName, String password) {
        User user = users.get(userName);
        UserChatSession chatSession = null;
        if (user != null && user.getPassword().equals(password)) {
            if( chatRooms.get(name) == null ){
                ChatRoom newRoom = new ChatRoom();
                newRoom.setName(name);
                newRoom.setCreated(new Date());
                chatRooms.put(name, newRoom);

                chatSession = new UserChatSession();
                chatSession.setUser(user);
                chatSession.setRoom(newRoom);
                chatSession.setLive(true);
                chatSession.setEntered(new Date());
                newRoom.getUserChatSessions().add(chatSession);
                user.getChatSessions().add(chatSession);
            }
        }
        return chatSession;
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

    public User login(String userName, String password) throws LoginFailedException {
        User user = users.get(userName);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }
        else
            throw new LoginFailedException("The provided username and password are not recognized.");
    }

    public User register(String userName, String nickName, String password) {
        if( users.get(userName) == null ){
            User user = new User();
            user.setUserName(userName);
            user.setNickName(nickName);
            user.setPassword(password);
            users.put(userName, user);
            return user;
        }
        else{
            return null;
        }
    }
}
