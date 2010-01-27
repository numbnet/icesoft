package org.icepush.samples.icechat.beans.model;

import java.io.Serializable;

import org.icepush.samples.icechat.controller.model.ICurrentChatSessionHolderBean;
import org.icepush.samples.icechat.model.UserChatSession;

public abstract class BaseCurrentChatSessionHolderBean implements ICurrentChatSessionHolderBean, Serializable {

	private UserChatSession session;

	public UserChatSession getSession() {
		return session;
	}

	public void setSession(UserChatSession session) {
		this.session = session;
	}

	
	

}
