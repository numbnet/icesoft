package org.icepush.samples.icechat.beans.view;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.icepush.samples.icechat.beans.controller.ChatRoomController;
import org.icepush.samples.icechat.beans.controller.LoginController;
import org.icepush.samples.icechat.beans.model.CurrentChatSessionHolder;
import org.icepush.samples.icechat.beans.model.NewChatRoomBean;
import org.icepush.samples.icechat.beans.model.NewChatRoomMessageBean;
import org.icepush.samples.icechat.model.UserChatSession;

@Named(value="chatManagerVC")
@RequestScoped
public class ChatManagerViewController extends ChatRoomController {
	
	private static final long serialVersionUID = 9102430645372034983L;

	@Inject
	private LoginController loginController;
	
	@Inject
	private NewChatRoomBean newChatRoomBean;
	
	@Inject 
	private CurrentChatSessionHolder currentChatSessionHolder;
	
	@Inject
	private NewChatRoomMessageBean newChatRoomMessageBean;;
	
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
