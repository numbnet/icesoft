/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.model;

import java.io.Serializable;
import java.util.Date;
/**
 *
 * @author pbreau
 */
public class UserChatSession implements Serializable{

    
	private Long id;

    private User user;

    private ChatRoom room;

    private Date entered;

    private Date exited;

    private boolean live;

    public UserChatSession(){
        entered = new Date();
    }

    /**
     * @return the user
     */
    public User getUser() {
        return user;
    }

    /**
     * @param user the user to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * @return the room
     */
    public ChatRoom getRoom() {
        return room;
    }

    /**
     * @param room the room to set
     */
    public void setRoom(ChatRoom room) {
        this.room = room;
    }

    /**
     * @return the entered
     */
    public Date getEntered() {
        return entered;
    }

    /**
     * @param entered the entered to set
     */
    public void setEntered(Date entered) {
        this.entered = entered;
    }

    /**
     * @return the exited
     */
    public Date getExited() {
        return exited;
    }

    /**
     * @param exited the exited to set
     */
    public void setExited(Date exited) {
        this.exited = exited;
    }

    /**
     * @return the live
     */
    public boolean isLive() {
        return live;
    }

    /**
     * @param live the live to set
     */
    public void setLive(boolean live) {
        this.live = live;
    }
    
    public String toString(){
    	return "UserChatSession[ user=" + user.getUserName() + ", room=" + room.getName() + ", entered=" + entered + "]";
    }

}
