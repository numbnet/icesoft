/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author pbreau
 */
@Entity
@Table(name="users", schema="icepush_chat")
public class User implements Serializable{

    @Id
    @Column(name="user_name")
    private String userName;

    @Column(name="nick_name")
    private String nickName;

    @Column(name="password", nullable=false)
    private String password;
    
    @OneToMany(mappedBy = "user", cascade = {CascadeType.ALL})
    private Collection<UserChatSession> chatSessions = new ArrayList<UserChatSession>();

    @Column(name="avatar")
    @Lob
    private byte[] avatar;

    public User(){
        chatSessions = new ArrayList<UserChatSession>();
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the nickName
     */
    public String getNickName() {
        return nickName;
    }

    /**
     * @param nickName the nickName to set
     */
    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    /**
     * @param messages the messages to set
     */
    public void setMessages(Collection<Message> messages) {
        this.setMessages(messages);
    }

    /**
     * @return the chatRooms
     */
    public Collection<UserChatSession> getChatSessions() {
        return chatSessions;
    }

    /**
     * @param chatRooms the chatRooms to set
     */
    public void setChatRooms(Collection<ChatRoom> chatRooms) {
        this.setChatRooms(chatRooms);
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return the avatar
     */
    public byte[] getAvatar() {
        return avatar;
    }

    /**
     * @param avatar the avatar to set
     */
    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }
    
    public String getDisplayName(){
    	return nickName != null ? nickName : userName;
    }


}
