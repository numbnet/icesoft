package org.icepush.samples.icechat.gwt.server.service.beans;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.service.exception.LoginFailedException;

public class AuthenticationProvider {
	
	HttpSession session = null;
	
	
	public AuthenticationProvider(HttpServletRequest req){
		session = req.getSession();
	}
	
	/**
	 * This method can be used to authenticate the current request.  This method will store the credentials in the request session.
	 * @param username
	 * @param password
	 * @return  a boolean indicating whether the authentication was successful or not.
	 *
	 */
	@Deprecated
	public boolean authenticateThread(String username, String password){
		ChatServiceBean bean = ChatServiceBean.getInstance(session.getServletContext());
		try {
			User user = bean.login(username, password);
			session.setAttribute(User.class.getName(), user);
			return true;
		} catch (LoginFailedException e) {
			// authentication failed...
			return false;
		}
		
	}
	
	/**
	 * This method is a utility to test the validity of the provided username and password
	 * @param username
	 * @param password
	 * @return a boolean indicating whether the authentication was successful or not.
	 */
	@Deprecated
	public boolean isValidCredentials(String username, String password){
		ChatServiceBean bean = ChatServiceBean.getInstance(session.getServletContext());
		try {
			bean.login(username, password);
			return true;
		} catch (LoginFailedException e) {
			// authentication failed...
			return false;
		}
	}
	
	
	/**
	 * get the current user for the request.  
	 * @param req the current HTTP Servlet Request.
	 * @return
	 */
	@Deprecated
	public static User getSessionUser(HttpServletRequest req){
		if(req.getSession().getAttribute(User.class.getName()) == null){
			return null;
		}
		return (User) req.getSession().getAttribute(User.class.getName());
		
	}
	
	
	
	public static User getSessionUser(ServletContext context, String sessionToken){
		return ChatServiceBean.getInstance(context)
				.getSessionUser(sessionToken);			
	}
	
	
}
