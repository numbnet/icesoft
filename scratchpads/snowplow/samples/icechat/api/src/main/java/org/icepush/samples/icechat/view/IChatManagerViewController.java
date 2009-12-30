package org.icepush.samples.icechat.view;

import org.icepush.samples.icechat.controller.ILoginController;
import org.icepush.samples.icechat.controller.model.ICurrentChatSessionHolderBean;
import org.icepush.samples.icechat.controller.model.INewChatRoomBean;
import org.icepush.samples.icechat.controller.model.INewChatRoomMessageBean;

public interface IChatManagerViewController {
	
	public void setLoginController(ILoginController loginController) ;

	public void setNewChatRoomBean(INewChatRoomBean newChatRoomBean);

	public void setCurrentChatSessionHolder(
			ICurrentChatSessionHolderBean currentChatSessionHolder);

	public void setNewChatRoomMessageBean(
			INewChatRoomMessageBean newChatRoomMessageBean);

	public void createNewChatRoom();
	
	public void openChatSession(String chatRoom);
	
	public void sendNewMessage();


}
