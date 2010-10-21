/*
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
 * The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icepush.samples.icechat.gwt.server.service.beans;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import org.icepush.samples.icechat.beans.service.BaseChatServiceBean;
import org.icepush.samples.icechat.model.ChatRoom;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.model.UserChatSession;

public class ChatServiceBean extends BaseChatServiceBean{

	private ChatServiceBean(){}
	
	public static ChatServiceBean getInstance(ServletContext context){
		
		if(context.getAttribute(ChatServiceBean.class.getName()) == null){
			context.setAttribute(ChatServiceBean.class.getName(), new ChatServiceBean());
		}
		
		return (ChatServiceBean) context.getAttribute(ChatServiceBean.class.getName());
	}
	
	public List<User> getChatRoomParticipants(String chatRoom){
		List<User> result = new ArrayList<User>();
		
		ChatRoom room = this.chatRooms.get(chatRoom);
		for(UserChatSession session: room.getUserChatSessions()){
			result.add(session.getUser());
		}
		return result;
	}
}
