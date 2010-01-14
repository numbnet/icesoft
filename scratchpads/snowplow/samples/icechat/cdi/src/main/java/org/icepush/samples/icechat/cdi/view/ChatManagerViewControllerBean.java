package org.icepush.samples.icechat.cdi.view;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.icepush.samples.icechat.IPushRequestContext;
import org.icepush.samples.icechat.controller.ILoginController;
import org.icepush.samples.icechat.controller.model.ICurrentChatSessionHolderBean;
import org.icepush.samples.icechat.controller.model.INewChatRoomBean;
import org.icepush.samples.icechat.controller.model.INewChatRoomMessageBean;
import org.icepush.samples.icechat.service.IChatService;
import org.icepush.samples.icechat.view.IChatManagerViewController;

@Named(value="chatManagerVC")
@RequestScoped
public class ChatManagerViewControllerBean extends
		org.icepush.samples.icechat.beans.view.BaseChatManagerViewControllerBean
		implements IChatManagerViewController {
	
	@Inject
	@Override
	public void setLoginController(ILoginController loginController) {
		super.setLoginController(loginController);
	}
	
	@Inject
	@Override
	public void setNewChatRoomBean(INewChatRoomBean newChatRoomBean) {
		super.setNewChatRoomBean(newChatRoomBean);
	}
	
	@Inject 
	@Override
	public void setCurrentChatSessionHolder(
			ICurrentChatSessionHolderBean currentChatSessionHolder) {
		super.setCurrentChatSessionHolder(currentChatSessionHolder);
	}
	
	@Inject
	@Override
	public void setNewChatRoomMessageBean(
			INewChatRoomMessageBean newChatRoomMessageBean) {
		super.setNewChatRoomMessageBean(newChatRoomMessageBean);
	}
	
	@Inject
	@Override
	public void setChatService(IChatService chatService){
		super.setChatService(chatService);
	}
	
	@Inject
	@Override
	public void setPushRequestContext(IPushRequestContext pushRequestContext) {
		super.setPushRequestContext(pushRequestContext);
	}
}
