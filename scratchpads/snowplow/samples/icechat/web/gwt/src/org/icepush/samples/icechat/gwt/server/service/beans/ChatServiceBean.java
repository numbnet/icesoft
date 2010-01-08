package org.icepush.samples.icechat.gwt.server.service.beans;

import javax.servlet.ServletContext;

import org.icepush.samples.icechat.beans.service.BaseChatServiceBean;

public class ChatServiceBean extends BaseChatServiceBean{

	private ChatServiceBean(){}
	
	public static ChatServiceBean getInstance(ServletContext context){
		
		if(context.getAttribute(ChatServiceBean.class.getName()) == null){
			context.setAttribute(ChatServiceBean.class.getName(), new ChatServiceBean());
		}
		
		return (ChatServiceBean) context.getAttribute(ChatServiceBean.class.getName());
	}
}
