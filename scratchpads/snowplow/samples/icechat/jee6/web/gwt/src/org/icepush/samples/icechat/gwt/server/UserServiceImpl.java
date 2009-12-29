package org.icepush.samples.icechat.gwt.server;

import org.icepush.samples.icechat.gwt.client.UserService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.icepush.samples.icechat.gwt.client.User;
import javax.ejb.EJB;
import org.icepush.samples.icechat.service.api.ChatServiceLocal;


public class UserServiceImpl extends RemoteServiceServlet implements UserService{

    @EJB
    private ChatServiceLocal chatService;

	public void register(User user){
		System.out.println("Name: " + user.getUsername());
		if(chatService == null){
			throw new RuntimeException("EJB injection Failed...");
		}
		chatService.register(user.getUsername(), user.getNickname(), user.getPassword());
	}
}