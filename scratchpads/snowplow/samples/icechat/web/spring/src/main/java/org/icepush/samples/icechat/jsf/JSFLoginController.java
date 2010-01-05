package org.icepush.samples.icechat.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.icepush.samples.icechat.cdi.controller.LoginController;
import org.icepush.samples.icechat.service.exception.LoginFailedException;

public class JSFLoginController {
	
	private LoginController loginController;
	
	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}
	
	public void login(ActionEvent evt){
		try{
			loginController.login();
			
		}
		catch(LoginFailedException e){
			FacesContext.getCurrentInstance()
            	.addMessage(null, new FacesMessage(e.getMessage()));
		}
	}

}
