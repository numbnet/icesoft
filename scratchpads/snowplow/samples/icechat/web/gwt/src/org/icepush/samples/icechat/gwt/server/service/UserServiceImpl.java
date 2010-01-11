package org.icepush.samples.icechat.gwt.server.service;

import org.icepush.samples.icechat.gwt.client.Credentials;
import org.icepush.samples.icechat.gwt.client.User;
import org.icepush.samples.icechat.gwt.client.service.UserService;
import org.icepush.samples.icechat.gwt.server.service.beans.AuthenticationProvider;
import org.icepush.samples.icechat.gwt.server.service.beans.LoginControllerBean;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class UserServiceImpl extends RemoteServiceServlet implements
		UserService {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6564158411144975771L;

	public Credentials register(User user) {
		try {

			LoginControllerBean loginController = LoginControllerBean
					.getInstance(this.getServletContext());
			loginController.register(user.getUsername(), user.getNickname(),
					user.getPassword());
			Credentials credentials = new Credentials();
			credentials.setNickName(user.getNickname());
			credentials.setUserName(user.getUsername());

			// now we must login the new user.
			AuthenticationProvider authProvider = new AuthenticationProvider(
					getThreadLocalRequest());
			// assume this will succeed as the user was just registered...
			authProvider.authenticateThread(user.getUsername(), user
					.getPassword());

			return credentials;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Credentials login(String username, String password) {

		AuthenticationProvider authProvider = new AuthenticationProvider(
				getThreadLocalRequest());

		if (!authProvider.authenticateThread(username, password)) {
			return null; // invalid login.
		}

		org.icepush.samples.icechat.model.User user = AuthenticationProvider
				.getSessionUser(getThreadLocalRequest());

		/* wrap for the client side */
		Credentials credentials = new Credentials();
		credentials.setUserName(user.getUserName());
		credentials.setNickName(user.getNickName());

		return credentials;

	}

}