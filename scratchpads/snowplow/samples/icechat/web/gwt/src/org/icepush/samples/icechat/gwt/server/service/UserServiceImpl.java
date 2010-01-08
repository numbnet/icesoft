package org.icepush.samples.icechat.gwt.server.service;

import org.icepush.samples.icechat.beans.controller.BaseLoginControllerBean;
import org.icepush.samples.icechat.gwt.client.service.UserService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import java.util.HashMap;
import org.icepush.samples.icechat.gwt.client.User;
import org.icepush.samples.icechat.gwt.client.Credentials; 
import org.icepush.samples.icechat.gwt.server.service.beans.LoginControllerBean;
import org.icepush.samples.icechat.service.exception.LoginFailedException;


public class UserServiceImpl extends RemoteServiceServlet implements UserService{

	
    /**
	 * 
	 */
	private static final long serialVersionUID = -6564158411144975771L;

	public Credentials register(User user) {
		try{
    	LoginControllerBean loginController = LoginControllerBean.getInstance(this.getServletContext());
    	loginController.register(user.getUsername(), user.getNickname(), user.getPassword());
        Credentials credentials = new Credentials();
        credentials.setNickName(user.getNickname());
        credentials.setUserName(user.getUsername());

        return credentials;
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
    }

    public Credentials login(String username, String password) {
    	LoginControllerBean loginController = LoginControllerBean.getInstance(this.getServletContext());
    	
    	try{
    		loginController.login(username, password);
    		
    		org.icepush.samples.icechat.model.User user = loginController.getCurrentUser();
    		
    		/*wrap for the client side */
    		Credentials credentials = new Credentials();
    		credentials.setUserName(user.getUserName());
    		credentials.setNickName(user.getNickName());
    		
    		return credentials;
    		
    	}catch(LoginFailedException ex){
    		ex.printStackTrace();
    		return null;
    	}
        
    }


}