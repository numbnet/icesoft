/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.model;

import java.util.Date;

/**
 *
 * @author pbreau
 */
public class Message {

    private Long id;

    private Date created;

    private String message;

    private UserChatSession userChatSession;

    private ChatRoom chatRoom;

    /**
     * @return the created
     */
    public Date getCreated() {
        return created;
    }

    /**
     * @param created the created to set
     */
    public void setCreated(Date created) {
        this.created = created;
    }

    /**
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the message to set
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return the user
     */
    public UserChatSession getUserChatSession() {
        return userChatSession;
    }

    /**
     * @param user the user to set
     */
    public void setUserChatSession(UserChatSession userChatSession) {
        this.userChatSession = userChatSession;
    }

    /**
     * @return the id
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the chatRoom
     */
    public ChatRoom getChatRoom() {
        return chatRoom;
    }

    /**
     * @param chatRoom the chatRoom to set
     */
    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }

}
