/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.wicket.controller;

import java.io.Serializable;

import org.icepush.samples.icechat.IPushRequestContext;
import org.icepush.samples.icechat.beans.controller.BaseChatRoomControllerBean;
import org.icepush.samples.icechat.service.IChatService;

public class ChatRoomController extends BaseChatRoomControllerBean
	implements Serializable{

	@Override
	public void setChatService(IChatService chatService){
		super.setChatService(chatService);
	}

	@Override
	public void setPushRequestContext(IPushRequestContext pushRequestContext){
		super.setPushRequestContext(pushRequestContext);
	}

}
