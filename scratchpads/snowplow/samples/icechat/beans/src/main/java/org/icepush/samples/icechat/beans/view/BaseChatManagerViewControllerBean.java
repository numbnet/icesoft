package org.icepush.samples.icechat.beans.view;

import java.io.Serializable;

import org.icepush.samples.icechat.beans.controller.BaseChatRoomControllerBean;
import org.icepush.samples.icechat.controller.ILoginController;
import org.icepush.samples.icechat.controller.model.ICurrentChatSessionHolderBean;
import org.icepush.samples.icechat.controller.model.INewChatRoomBean;
import org.icepush.samples.icechat.controller.model.INewChatRoomMessageBean;
import org.icepush.samples.icechat.model.UserChatSession;
import org.icepush.samples.icechat.view.IChatManagerViewController;

public abstract class BaseChatManagerViewControllerBean extends BaseChatRoomControllerBean 
	implements Serializable, IChatManagerViewController{
	
	private ILoginController loginController;
	
	private INewChatRoomBean newChatRoomBean;
	
	private ICurrentChatSessionHolderBean currentChatSessionHolder;
	
	private INewChatRoomMessageBean newChatRoomMessageBean;;
	
	public void setLoginController(ILoginController loginController) {
		this.loginController = loginController;
	}

	public void setNewChatRoomBean(INewChatRoomBean newChatRoomBean) {
		this.newChatRoomBean = newChatRoomBean;
	}

	public void setCurrentChatSessionHolder(
			ICurrentChatSessionHolderBean currentChatSessionHolder) {
		this.currentChatSessionHolder = currentChatSessionHolder;
	}

	public void setNewChatRoomMessageBean(
			INewChatRoomMessageBean newChatRoomMessageBean) {
		this.newChatRoomMessageBean = newChatRoomMessageBean;
	}

	public void createNewChatRoom(){
		UserChatSession newChatSession = createNewChatRoom(newChatRoomBean.getName(), 
				loginController.getCurrentUser().getUserName(),
				loginController.getCurrentUser().getPassword());
		if( newChatSession != null )
			currentChatSessionHolder.setSession(newChatSession);
	}
	
	public void openChatSession(String chatRoom){
		UserChatSession session = openChatSession(chatRoom, 
				loginController.getCurrentUser().getUserName(),
				loginController.getCurrentUser().getPassword());
		if( session != null )
			currentChatSessionHolder.setSession(session);
	}
	
	public void sendNewMessage(){
		if( currentChatSessionHolder.getSession() != null ){
			sendNewMessage(currentChatSessionHolder.getSession().getRoom().getName(), 
					newChatRoomMessageBean.getMessage(),
					loginController.getCurrentUser().getUserName(),
					loginController.getCurrentUser().getPassword());
			newChatRoomMessageBean.setMessage(null);
		}
		
	}
	

}
