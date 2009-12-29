package org.icepush.samples.icechat.jsf;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.icepush.samples.icechat.beans.controller.LoginController;
import org.icepush.samples.icechat.service.api.exception.LoginFailedException;

@ManagedBean(name="jsfLoginController")
@RequestScoped
public class JSFLoginController {
	
	@ManagedProperty(name="loginController", value="#{loginController}")
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
