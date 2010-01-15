package org.icepush.samples.icechat.gwt.server.service;

import org.icepush.samples.icechat.gwt.client.Credentials;
import org.icepush.samples.icechat.gwt.client.service.UserService;
import org.icepush.samples.icechat.gwt.server.service.beans.ChatServiceBean;
import org.icepush.samples.icechat.model.User;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class UserServiceImpl extends RemoteServiceServlet implements
		UserService {

	@Override
	public Credentials register(String name) {
		// TODO Auto-generated method stub
		ChatServiceBean chatService = ChatServiceBean.getInstance(this.getServletContext());
		
		Credentials result = new Credentials();
		User user = chatService.register(name);
		
		result.setSessionToken(user.getSessionToken());
		result.setUserName(user.getUserName());
		return result;
	}


}