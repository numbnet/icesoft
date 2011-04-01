package org.icepush.samples.icechat.icefaces;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.icefaces.application.PushRenderer;
import org.icepush.samples.icechat.PushConstants;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.service.IChatService;
import org.icepush.samples.icechat.service.exception.ConfigurationException;

@ManagedBean(name="loginController")
@SessionScoped
public class LoginController implements Serializable{
	
	
	private String userName;
	private User currentUser;
	@ManagedProperty(value = "#{chatService}")
	private IChatService chatService;
	
	public void login(ActionEvent evt){
		login();
	}
	
	public void setChatService(IChatService chatService) {
		this.chatService = chatService;
	}

	public User getCurrentUser() {
		return currentUser;
	}
	
	public void login(){
        login(userName);
        PushRenderer.addCurrentView(PushConstants.CHAT_USERS.name());
        PushRenderer.render(PushConstants.CHAT_USERS.name());
    }
	
	public void login(String userName){
		if(chatService==null)
			throw new ConfigurationException(this.getClass(), "chatService", "null.  Please initialize before calling login()");
		currentUser = chatService.login(userName);
	}

    public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
    public void logout(){
    	currentUser = null;
    }
    
   
}
