/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.wicket.facade;

import java.io.Serializable;
import org.icepush.samples.icechat.service.IChatService;

public class ChatManagerFacadeBean
	extends org.icepush.samples.icechat.beans.facade.BaseChatManagerFacadeBean implements Serializable{
	
	@Override
	public void setChatService(IChatService chatService) {
		super.setChatService(chatService);
	}



}
