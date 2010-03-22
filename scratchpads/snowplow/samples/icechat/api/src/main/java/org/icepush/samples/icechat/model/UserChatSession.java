/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icepush.samples.icechat.model;

import java.io.Serializable;
import java.util.Date;
/**
 *
 */
public class UserChatSession implements Serializable{

    
	private Long id;

    private User user;

    private ChatRoom room;

    private Date entered;

    private Date exited;

    private boolean live;
    
    private String currentDraft;

    public String getCurrentDraft() {
		return currentDraft;
	}

	public void setCurrentDraft(String currentDraft) {
		this.currentDraft = currentDraft;
	}

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
    	return "UserChatSession[ user=" + user.getName() + ", room=" + room.getName() + ", entered=" + entered + "]";
    }

}
