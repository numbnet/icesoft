/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.model;

import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author pbreau
 */
@Entity
@Table(name="user_chat_sessions", schema="icepush_chat")
public class UserChatSession {

    @Id
    @GeneratedValue
    @Column(name="user_chat_session_id")
    private Long id;

    @OneToOne(optional=false, fetch=FetchType.EAGER)
    @JoinColumn(name="user_id", nullable=false)
    private User user;

    @OneToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name="room_id", nullable=false)
    private ChatRoom room;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="entered")
    private Date entered;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="exited")
    private Date exited;

    @Column(name="live")
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

}
