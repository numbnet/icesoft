package org.icepush.samples.icechat.gwt.server.service.beans;

import javax.servlet.ServletContext;

import org.icepush.samples.icechat.beans.controller.BaseLoginControllerBean;

public class LoginControllerBean extends BaseLoginControllerBean{


	private static final long serialVersionUID = 1L;

	private LoginControllerBean(ServletContext context){
		this.chatService = ChatServiceBean.getInstance(context);
	}
	
	public static LoginControllerBean getInstance(ServletContext context){
		if(context.getAttribute(LoginControllerBean.class.getName()) == null){
			context.setAttribute(LoginControllerBean.class.getName(), new LoginControllerBean(context));
		}
		
		return (LoginControllerBean)context.getAttribute(LoginControllerBean.class.getName());
	}
}
