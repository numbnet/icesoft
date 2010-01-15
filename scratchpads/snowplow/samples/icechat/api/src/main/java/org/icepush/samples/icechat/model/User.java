/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 *
 * @author pbreau
 */
public class User implements Serializable{

    private String userName;

    private String nickName;

    private String password;
    
    private Collection<UserChatSession> chatSessions = new ArrayList<UserChatSession>();

    private String sessionToken;
    private Date lastTouch = new Date();
    
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

    public String toString(){
    	return nickName != null ? nickName + " (" + userName + ")" : userName;
    }

    
    public void touchUser(){
    	this.lastTouch = new Date();
    }

	public String getSessionToken() {
		return sessionToken;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}

	public Date getLastTouch() {
		return lastTouch;
	}
    
    
}
