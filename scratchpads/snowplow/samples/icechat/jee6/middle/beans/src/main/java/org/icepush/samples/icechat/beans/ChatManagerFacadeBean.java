/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.beans;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import test.ChatManagerLocal;

/**
 *
 * @author pbreau
 */
@ManagedBean(name = "chatManagerFacade")
public class ChatManagerFacadeBean {

    @EJB
    private ChatManagerLocal chatManager;

    /**
     * @return the chatManager
     */
    public ChatManagerLocal getChatManager() {
        return chatManager;
    }

    /**
     * @param chatManager the chatManager to set
     */
    public void setChatManager(ChatManagerLocal chatManager) {
        this.chatManager = chatManager;
    }

}
