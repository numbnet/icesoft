/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;


@Named(value="login")
@RequestScoped
public class LoginBean {

    private String userName;
    private String password;
    private String nickName;

    @Inject
    private ChatManagerFacadeBean chatManagerFacade;

    @Inject
    private ChatSessionBean chatSession;

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

    public void login(){
        chatSession.setUser(chatManagerFacade.getChatService().login(userName, password));
    }

    public void register(){
        chatSession.setUser(chatManagerFacade.getChatService().register(userName, nickName, password));
    }

    /**
     * @param chatManagerFacade the chatManagerFacade to set
     */
    public void setChatManagerFacade(ChatManagerFacadeBean chatManagerFacade) {
        this.chatManagerFacade = chatManagerFacade;
    }

    /**
     * @param chatSession the chatSession to set
     */
    public void setChatSession(ChatSessionBean chatSession) {
        this.chatSession = chatSession;
    }

    



}
