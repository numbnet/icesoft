package org.icepush.samples.icechat.gwt.server;

import org.icepush.samples.icechat.gwt.client.UserService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.icepush.samples.icechat.gwt.client.User;
import org.icepush.samples.icechat.gwt.client.Credentials; 


public class UserServiceImpl extends RemoteServiceServlet implements UserService{

    public Credentials register(User user) {
        System.out.println("Name: " + user.getUsername());
    
        Credentials credentials = new Credentials();
        credentials.setNickName(user.getNickname());
        credentials.setUserName(user.getUsername());
        return credentials;
    }
}