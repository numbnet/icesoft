/*
 *
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 *
 */
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
