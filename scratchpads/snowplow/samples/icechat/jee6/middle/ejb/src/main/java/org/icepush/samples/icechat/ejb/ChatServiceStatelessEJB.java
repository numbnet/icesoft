/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.ejb;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.icepush.samples.icechat.model.ChatRoom;
import org.icepush.samples.icechat.model.ChatServiceLocal;
import org.icepush.samples.icechat.model.Message;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.model.UserChatSession;

/**
 *
 * @author pbreau
 */
@Stateless
public class ChatServiceStatelessEJB implements ChatServiceLocal {

    @PersistenceContext(unitName = "ChatMgr-ejbPU")
    private EntityManager em;

    public Collection<ChatRoom> getChatRooms() {
        return em.createQuery("select object(o) from ChatRoom as o").getResultList();
    }

    public Collection<User> getOnlineUsers() {
        return em.createQuery("select distinct participants from ChatRoom as o").getResultList();
    }

    public void loginToChatRoom(String chatRoom, String userName, String password) {
        User user = (User)em.createQuery("select object(o) from User as o where o.userName = :userName and o.password = :password")
                .setParameter("userName", userName).setParameter("password", password).getSingleResult();
        if( user != null ){
          ChatRoom room = (ChatRoom)em.createQuery("select object(o) from ChatRoom as o where o.name = :name")
                .setParameter("name", chatRoom).getSingleResult();
          if( room != null ){
              UserChatSession chatSession = new UserChatSession();
              chatSession.setLive(true);
              chatSession.setRoom(room);
              chatSession.setUser(user);
              room.getUserChatSessions().add(chatSession);
              em.persist(chatSession);
          }
        }        
    }

    public void logoutOfChatRoom(String chatRoom, String userName, String password) {
        UserChatSession chatSession = (UserChatSession)em
                .createQuery("select object(o) from UserChatSession as o where o.user.userName = :userName " +
                "and o.user.password = :password and o.room.name = :chatRoom")
                .setParameter("userName", userName).setParameter("password", password).getSingleResult();
        if( chatSession != null ){
            chatSession.setExited(new Date());
            chatSession.setLive(false);
            em.merge(chatSession);
        }

    }

    public List<Message> getAllChatRoomMessages(String chatRoom) {
        ChatRoom room = (ChatRoom)em.createQuery("select object(o) from ChatRoom as o where o.name = :name")
                .setParameter("name", chatRoom).getSingleResult();
        List<Message> messages = null;
        if( room != null ){
            messages = room.getMessages();
        }
        return messages;
    }

    public List<Message> getChatRoomMessagesFromIndex(String chatRoom, int index) {
        return em.createQuery("select o.messages from ChatRoom as o where o.name = :name")
                .setParameter("name", chatRoom).setFirstResult(index).getResultList();
    }

    public void createNewChatRoom(String name, String userName, String password) {
        User user = (User)em.createQuery("select object(o) from User as o where o.userName = :userName and o.password = :password")
                .setParameter("userName", userName).setParameter("password", password).getSingleResult();
        if( user != null ){
            ChatRoom room = new ChatRoom();
            room.setCreated(new Date());
            room.setName(name);
            em.persist(room);

            UserChatSession chatSession = new UserChatSession();
            chatSession.setLive(true);
            chatSession.setUser(user);
            chatSession.setRoom(room);
            em.persist(chatSession);
        }
    }

    public void sendNewMessage(String chatRoom, String userName, String password, String message) {
        UserChatSession chatSession = (UserChatSession)em
                .createQuery("select object(o) from UserChatSession as o where o.user.userName = :userName " +
                "and o.user.password = :password and o.room.name = :chatRoom")
                .setParameter("userName", userName).setParameter("password", password).getSingleResult();
        if( chatSession != null ){
          
              Message msg = new Message();
              msg.setMessage(message);
              msg.setChatRoom(chatSession.getRoom());
              msg.setUserChatSession(chatSession);
              chatSession.getRoom().getMessages().add(msg);
              em.persist(msg);
        }
    }

    public void createNewUser(String userName, String nickName, String password) {
        User user = new User();
        user.setUserName(userName);
        user.setNickName(nickName);
        user.setPassword(password);
        em.persist(user);
    }

    public void setUserAvatar(String userName, String password, byte[] avatar) {
        User user = (User)em.createQuery("select object(o) from User as o where o.userName = :userName and o.password = :password")
                .setParameter("userName", userName).setParameter("password", password).getSingleResult();
        if( user != null ){
            user.setAvatar(avatar);
            em.persist(user);
        }
    }

    public User login(String userName, String password) {
        return (User)em.createQuery("select object(o) from User as o where o.userName = :userName and o.password = :password")
                .setParameter("userName", userName).setParameter("password", password).getSingleResult();
    }

    public User register(String userName, String nickName, String password) {
        User user = new User();
        user.setUserName(userName);
        user.setNickName(nickName);
        user.setPassword(password);
        em.persist(user);
        return user;
    }
    
 
}
