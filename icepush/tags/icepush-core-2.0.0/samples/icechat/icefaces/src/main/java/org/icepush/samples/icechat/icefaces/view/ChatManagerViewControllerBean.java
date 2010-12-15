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
package org.icepush.samples.icechat.icefaces.view;


import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;

import org.icefaces.application.PushRenderer;
import org.icepush.samples.icechat.icefaces.LoginController;
import org.icepush.samples.icechat.model.ChatRoom;
import org.icepush.samples.icechat.model.User;
import org.icepush.samples.icechat.model.UserChatSession;
import org.icepush.samples.icechat.service.IChatService;
import org.icepush.samples.icechat.service.exception.UnauthorizedException;

@ManagedBean(name="chatManagerVC")
@SessionScoped
public class ChatManagerViewControllerBean{
	
	
	@ManagedProperty(value = "#{loginController}")
	private LoginController loginController;
	
	@ManagedProperty(value= "#{chatService}")
	private IChatService chatService;
	
	private String newChatRoomName;
	private String newChatRoomMessage;
	
	private UserChatSession session;

	public UserChatSession getSession() {
		return session;
	}
	
	public String getNewChatRoomMessage() {
		return newChatRoomMessage;
	}

	public void setNewChatRoomMessage(String newChatRoomMessage) {
		this.newChatRoomMessage = newChatRoomMessage;
	}

	public void setLoginController(LoginController loginController) {
		this.loginController = loginController;
	}
	
	public void setChatService(IChatService chatService){
		this.chatService = chatService;
	}
	
	public void createNewChatRoom(){
		createNewChatRoom(newChatRoomName);
		openChatSession(newChatRoomName);
	}
	
	public String getNewChatRoomName() {
		return newChatRoomName;
	}

	public void setNewChatRoomName(String newChatRoomName) {
		this.newChatRoomName = newChatRoomName;
	}

	public void openChatSession(String chatRoom){
		session = openChatSession(chatRoom, loginController.getCurrentUser());
	}
	
	public void sendNewMessage(){
		if( session != null ){
			try{
				sendNewMessage(session.getRoom().getName(), 
						newChatRoomMessage,
						loginController.getCurrentUser());
				newChatRoomMessage = "";
			}
			catch(UnauthorizedException e){
				e.printStackTrace();
			}
		}
		
	}
	
	public void createNewChatRoom(String chatRoomName){
        chatService.createNewChatRoom(chatRoomName);
        PushRenderer.render(chatRoomName);
        PushRenderer.addCurrentView(chatRoomName);        
	}
	
	public UserChatSession openChatSession(String chatRoomName, User user){
		UserChatSession session = null;
		for( ChatRoom room : chatService.getChatRooms() ){
			if( room.getName().equals(chatRoomName)){
				session = chatService.loginToChatRoom(chatRoomName, user);
				PushRenderer.render(chatRoomName);
				PushRenderer.addCurrentView(chatRoomName);
			}
		}		
		return session;
	}
	
	public void sendNewMessage(String chatRoomName, String newMessage, User user)
		throws UnauthorizedException{
		chatService.sendNewMessage(chatRoomName, user, newMessage);
		PushRenderer.render(chatRoomName);
	}


}
