package org.icepush.samples.icechat.controller;

import org.icepush.samples.icechat.IPushRequestContext;
import org.icepush.samples.icechat.model.UserChatSession;
import org.icepush.samples.icechat.service.IChatService;
import org.icepush.samples.icechat.service.exception.UnauthorizedException;

public interface IChatRoomController {

	public void setPushRequestContext(IPushRequestContext pushRequestContext);

	public void createNewChatRoom(String chatRoomName,
			String userName, String password);

	public UserChatSession openChatSession(String chatRoomName,
			String userName, String password);

	public void sendNewMessage(String chatRoomName, String newMessage,
			String userName, String password) throws UnauthorizedException;

	public void setChatService(IChatService chatService);

}
