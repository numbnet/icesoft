package org.icepush.samples.icechat.icefaces;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.icefaces.application.PushRenderer;
import org.icepush.samples.icechat.PushConstants;
import org.icepush.samples.icechat.beans.model.BaseCredentialsBean;
import org.icepush.samples.icechat.controller.ILoginController;
import org.icepush.samples.icechat.controller.model.ICredentialsBean;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.service.IChatService;
import org.icepush.samples.icechat.service.exception.ConfigurationException;
import org.icepush.samples.icechat.service.exception.LoginFailedException;

@ManagedBean(name="loginController")
@ViewScoped
public class LoginController{
	
	
	private ICredentialsBean credentialsBean = new BaseCredentialsBean(){};
	private User currentUser;
	@ManagedProperty(value = "#{chatService}")
	private IChatService chatService;
	
	public void login(ActionEvent evt){
		try{
			login();
			
		}
		catch(LoginFailedException e){
			FacesContext.getCurrentInstance()
            	.addMessage(null, new FacesMessage(e.getMessage()));
		}
	}
	
	public ICredentialsBean getCredentialsBean() {
		return credentialsBean;
	}

	public void setCredentialsBean(ICredentialsBean credentialsBean) {
		this.credentialsBean = credentialsBean;
	}

	public void setChatService(IChatService chatService) {
		this.chatService = chatService;
	}

	public User getCurrentUser() {
		return currentUser;
	}
	
	public void login() throws LoginFailedException{
        login(credentialsBean.getUserName(), credentialsBean.getPassword());
        PushRenderer.addCurrentView(PushConstants.CHAT_USERS.name());
        PushRenderer.render(PushConstants.CHAT_USERS.name());
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
    
   
}
