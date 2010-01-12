/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.icepush.samples.icechat.wicket.view;

import java.io.Serializable;

import org.icepush.samples.icechat.IPushRequestContext;
import org.icepush.samples.icechat.controller.ILoginController;
import org.icepush.samples.icechat.controller.model.ICurrentChatSessionHolderBean;
import org.icepush.samples.icechat.controller.model.INewChatRoomBean;
import org.icepush.samples.icechat.controller.model.INewChatRoomMessageBean;
import org.icepush.samples.icechat.service.IChatService;
import org.icepush.samples.icechat.view.IChatManagerViewController;

public class ChatManagerViewControllerBean extends
		org.icepush.samples.icechat.beans.view.BaseChatManagerViewControllerBean
		implements IChatManagerViewController, Serializable {

	@Override
	public void setLoginController(ILoginController loginController) {
		super.setLoginController(loginController);
	}

	@Override
	public void setNewChatRoomBean(INewChatRoomBean newChatRoomBean) {
		super.setNewChatRoomBean(newChatRoomBean);
	}

	@Override
	public void setCurrentChatSessionHolder(
			ICurrentChatSessionHolderBean currentChatSessionHolder) {
		super.setCurrentChatSessionHolder(currentChatSessionHolder);
	}

	@Override
	public void setNewChatRoomMessageBean(
			INewChatRoomMessageBean newChatRoomMessageBean) {
		super.setNewChatRoomMessageBean(newChatRoomMessageBean);
	}

	@Override
	public void setChatService(IChatService chatService){
		super.setChatService(chatService);
	}

	@Override
	public void setPushRequestContext(IPushRequestContext pushRequestContext) {
		super.setPushRequestContext(pushRequestContext);
	}

        public ICurrentChatSessionHolderBean getCurrentChatSessionHolder(){
            return currentChatSessionHolder;
        }
}
