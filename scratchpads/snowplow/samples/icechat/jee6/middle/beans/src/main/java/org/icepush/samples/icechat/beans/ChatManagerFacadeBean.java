/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.beans;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import org.icepush.samples.icechat.model.ChatServiceLocal;

/**
 *
 * @author pbreau
 */
@ManagedBean(name = "chatManagerFacade")
public class ChatManagerFacadeBean {

    @EJB
    private ChatServiceLocal chatService;

    /**
     * @return the chatManager
     */
    public ChatServiceLocal getChatService() {
        return chatService;
    }

    /**
     * @param chatManager the chatManager to set
     */
    public void setChatManager(ChatServiceLocal chatService) {
        this.chatService = chatService;
    }

}
