package org.icepush.samples.icechat.cdi.controller;

import java.io.Serializable;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.icepush.samples.icechat.IPushRequestContext;
import org.icepush.samples.icechat.beans.controller.ChatRoomControllerBean;
import org.icepush.samples.icechat.service.IChatService;

@Named
@RequestScoped
public class ChatRoomController extends ChatRoomControllerBean
	implements Serializable{
	
	@Inject
	@Override
	public void setChatService(IChatService chatService){
		super.setChatService(chatService);
	}
    	
	@Inject
	@Override
	public void setPushRequestContext(IPushRequestContext pushRequestContext){
		super.setPushRequestContext(pushRequestContext);
	}

}
