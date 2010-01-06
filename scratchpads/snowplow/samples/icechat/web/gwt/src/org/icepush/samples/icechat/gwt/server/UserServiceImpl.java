package org.icepush.samples.icechat.gwt.server;

import org.icepush.samples.icechat.gwt.client.UserService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.HashMap;
import org.icepush.samples.icechat.gwt.client.User;
import org.icepush.samples.icechat.gwt.client.Credentials; 


public class UserServiceImpl extends RemoteServiceServlet implements UserService{

    private HashMap<String,Credentials> registry = new HashMap<String, Credentials>();
    public Credentials register(User user) {
        Credentials credentials = new Credentials();
        credentials.setNickName(user.getNickname());
        credentials.setUserName(user.getUsername());

        String hash = user.getUsername() + user.getPassword();
        System.out.println("Hash in:" + hash);
        registry.put(hash, credentials);
        return credentials;
    }

    public Credentials login(String username, String password) {
        String hash = (username + password);
        System.out.println("Hash out:" + hash);
        if(registry.get(hash) == null){
            throw new RuntimeException("Invalid username and password.");
        }
        return registry.get(hash);
    }


}