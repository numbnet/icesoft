package org.icepush.samples.icechat.beans.controller;

import java.io.Serializable;

import org.icepush.PushContext;
import org.icepush.samples.icechat.IPushRequestContext;
import org.icepush.samples.icechat.PushConstants;
import org.icepush.samples.icechat.controller.ILoginController;
import org.icepush.samples.icechat.controller.model.ICredentialsBean;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.service.IChatService;
import org.icepush.samples.icechat.service.exception.ConfigurationException;
import org.icepush.samples.icechat.service.exception.LoginFailedException;


public abstract class BaseLoginControllerBean implements Serializable, ILoginController{
	
	
	protected IChatService chatService;
	
	private ICredentialsBean credentialsBean;
	
	public ICredentialsBean getCredentialsBean() {
		return credentialsBean;
	}

	public void setCredentialsBean(ICredentialsBean credentialsBean) {
		this.credentialsBean = credentialsBean;
	}

	protected IPushRequestContext pushRequestContext;

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
        addGroupMember(PushConstants.CHAT_USERS.name(), 
				pushRequestContext.getCurrentPushId());
        push(PushConstants.CHAT_USERS.name());
    }
	
	public void login(String userName, String password) throws LoginFailedException{
		if(chatService==null)
			throw new ConfigurationException(this.getClass(), "chatService", "null.  Please initialize before calling login()");
		currentUser = chatService.login(userName, password);
	}

    public void register(){
    	if(chatService==null)
			throw new ConfigurationException(this.getClass(), "chatService", "null.  Please initialize before calling register()");
    	
    	currentUser = chatService.register(credentialsBean.getUserName(), credentialsBean.getNickName(), 
    			credentialsBean.getPassword());
    }
    
    public void register(String userName, String nickName, String password){
    	if(chatService==null)
			throw new ConfigurationException(this.getClass(), "chatService", "null.  Please initialize before calling register()");
    	
    	currentUser = chatService.register(userName, nickName, password);
    }
    
    public void logout(){
    	currentUser = null;
    }
    
    public void addGroupMember(String name, String pushId){
    	PushContext pushContext = pushRequestContext.getPushContext();
    	pushContext.addGroupMember(name, pushId);
    }
    
    public void push(String name){
    	PushContext pushContext = pushRequestContext.getPushContext();
    	pushContext.push(name);
    }

}
