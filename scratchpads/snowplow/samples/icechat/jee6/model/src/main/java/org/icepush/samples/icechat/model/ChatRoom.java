/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author pbreau
 */
@Entity
@Table(name="chat_rooms", schema="icepush_chat")
public class ChatRoom implements Serializable{

    private static final long serialVersionUID = -6076794430884852218L;

	@Id
    @Column(name="name")
    private String name;

    @OneToMany(mappedBy="room", orphanRemoval=true, cascade=CascadeType.ALL)
    private Collection<UserChatSession> userChatSessions;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="created")
    private Date created;

    @OneToMany(mappedBy = "chatRoom", cascade = {CascadeType.ALL})
    private List<Message> messages = new ArrayList<Message>();

    public ChatRoom(){
        created = new Date();
        userChatSessions = new ArrayList<UserChatSession>();
        messages = new ArrayList<Message>();
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the participants
     */
    public Collection<UserChatSession> getUserChatSessions() {
        return userChatSessions;
    }

    /**
     * @param participants the participants to set
     */
    public void setUserChatSessions(Collection<UserChatSession> userChatSessions) {
        this.userChatSessions = userChatSessions;
    }

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
     * @return the messages
     */
    public List<Message> getMessages() {
        return messages;
    }

    /**
     * @param messages the messages to set
     */
    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
    
    public boolean hasUserSession(String userName){
    	for( UserChatSession userSession : userChatSessions ){
    		if( userSession.getUser().getUserName().equals(userName))
    			return true;
    	}
    	return false;
    }


}
