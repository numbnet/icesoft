package org.icepush.samples.icechat.beans.controller;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.icepush.PushContext;
import org.icepush.samples.icechat.beans.model.CredentialsBean;
import org.icepush.samples.icechat.beans.model.PushConstants;
import org.icepush.samples.icechat.beans.model.PushRequestContext;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.service.api.ChatServiceLocal;
import org.icepush.samples.icechat.service.api.exception.LoginFailedException;


@Named
@SessionScoped
public class LoginController implements Serializable{
	
	private static final long serialVersionUID = 3837479253548001859L;

	@EJB
    private ChatServiceLocal chatService;
	
	@Inject
	private CredentialsBean credentialsBean;
	
	@Inject
	private PushRequestContext pushRequestContext;
	

	private User currentUser;

	public User getCurrentUser() {
		return currentUser;
	}
	
	public void login() throws LoginFailedException{
        login(credentialsBean.getUserName(), credentialsBean.getPassword());
        PushContext pushContext = pushRequestContext.getPushContext();
        pushContext.addGroupMember(PushConstants.CHAT_USERS.name(), 
				pushRequestContext.getCurrentPushId());
        pushContext.push(PushConstants.CHAT_USERS.name());
    }
	
	public void login(String userName, String password) throws LoginFailedException{
		currentUser = chatService.login(userName, password);
	}

    public void register(){
    	currentUser = chatService.register(credentialsBean.getUserName(), credentialsBean.getNickName(), 
    			credentialsBean.getPassword());
    }
    
    public void logout(){
    	currentUser = null;
    }

}
