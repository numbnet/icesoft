/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import test.User;

/**
 *
 * @author pbreau
 */
@Named(value="chatSession")
@RequestScoped
public class ChatSessionBean {

    private User user;

    @Inject
    private ChatManagerFacadeBean chatManagerFacade;

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
    

    public void logout(){
        //chatManagerFacade.getChatManager().logoutOfChatRoom(null, null, null);
        user = null;
    }

    /**
     * @param chatManagerFacade the chatManagerFacade to set
     */
    public void setChatManagerFacade(ChatManagerFacadeBean chatManagerFacade) {
        this.chatManagerFacade = chatManagerFacade;
    }
}
