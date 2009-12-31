package org.icepush.samples.icechat.cdi.facade;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.icepush.samples.icechat.service.IChatService;

@Named
@SessionScoped
public class ChatManagerFacadeBean 
	extends org.icepush.samples.icechat.beans.facade.BaseChatManagerFacadeBean{
	
	@Inject
	@Override
	public void setChatService(IChatService chatService) {
		super.setChatService(chatService);
	}
	
	
	
}
