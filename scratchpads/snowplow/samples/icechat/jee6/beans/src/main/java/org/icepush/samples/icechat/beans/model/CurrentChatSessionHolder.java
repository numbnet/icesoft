package org.icepush.samples.icechat.beans.model;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import org.icepush.samples.icechat.model.UserChatSession;

@Named
@SessionScoped
public class CurrentChatSessionHolder implements Serializable{

	private static final long serialVersionUID = 4127184379390222896L;
	
	private UserChatSession session;

	public UserChatSession getSession() {
		return session;
	}

	public void setSession(UserChatSession session) {
		this.session = session;
	}

	
	

}
