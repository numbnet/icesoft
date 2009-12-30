package org.icepush.samples.icechat.controller.model;

import org.icepush.samples.icechat.model.UserChatSession;

public interface ICurrentChatSessionHolderBean {

	public UserChatSession getSession();

	public void setSession(UserChatSession session);

}
