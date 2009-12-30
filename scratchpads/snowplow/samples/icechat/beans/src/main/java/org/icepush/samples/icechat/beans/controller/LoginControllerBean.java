package org.icepush.samples.icechat.beans.controller;

import java.io.Serializable;

import org.icepush.PushContext;
import org.icepush.samples.icechat.IPushRequestContext;
import org.icepush.samples.icechat.PushConstants;
import org.icepush.samples.icechat.controller.ILoginController;
import org.icepush.samples.icechat.controller.model.ICredentialsBean;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.service.IChatService;
import org.icepush.samples.icechat.service.exception.LoginFailedException;


public class LoginControllerBean implements Serializable, ILoginController{
	
	
	private IChatService chatService;
	
	private ICredentialsBean credentialsBean;
	
	public ICredentialsBean getCredentialsBean() {
		return credentialsBean;
	}

	public void setCredentialsBean(ICredentialsBean credentialsBean) {
		this.credentialsBean = credentialsBean;
	}

	private IPushRequestContext pushRequestContext;

	private User currentUser;

	public IPushRequestContext getPushRequestContext() {
		return pushRequestContext;
	}

	public void setPushRequestContext(IPushRequestContext pushRequestContext) {
		this.pushRequestContext = pushRequestContext;
	}

	public void setChatService(IChatService chatService) {
		this.chatService = chatService;
	}

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
    
    public void register(String userName, String nickName, String password){
    	currentUser = chatService.register(userName, nickName, password);
    }
    
    public void logout(){
    	currentUser = null;
    }

}
