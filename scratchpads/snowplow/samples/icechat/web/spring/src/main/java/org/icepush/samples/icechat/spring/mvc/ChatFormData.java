package org.icepush.samples.icechat.spring.mvc;

import org.icepush.samples.icechat.spring.impl.BaseLoginController;
import org.icepush.samples.icechat.spring.impl.BaseChatManagerFacade;
import org.icepush.samples.icechat.spring.impl.BaseChatRoom;
import org.icepush.samples.icechat.spring.impl.BaseChatManagerViewController;
import org.icepush.samples.icechat.spring.impl.BasePushRequestContext;
import org.icepush.samples.icechat.spring.impl.BaseCurrentChatSessionHolder;
import org.icepush.samples.icechat.spring.impl.BaseChatMessage;

public class ChatFormData {
    private BasePushRequestContext pushRequestContext;

    private BaseLoginController loginController;
    private BaseChatManagerFacade chatManagerFacade;
    private BaseChatManagerViewController chatManagerViewController;
    private BaseCurrentChatSessionHolder currentChatSessionHolder;
    private BaseChatRoom newChatRoom;
    private BaseChatMessage newMessage;

    public ChatFormData() {
        newChatRoom = new BaseChatRoom();
        newMessage = new BaseChatMessage();
    }

    public BasePushRequestContext getPushRequestContext() {
        return pushRequestContext;
    }

    public void setPushRequestContext(BasePushRequestContext pushRequestContext) {
        this.pushRequestContext = pushRequestContext;

        if ((loginController != null) && (loginController.getPushRequestContext() == null)) {
            loginController.setPushRequestContext(this.pushRequestContext);
        }

        if (chatManagerViewController != null) {
            chatManagerViewController.setPushRequestContext(this.pushRequestContext);
        }
    }

    public BaseLoginController getLoginController() {
        return loginController;
    }

    public void setLoginController(BaseLoginController loginController) {
        this.loginController = loginController;
    }

    public BaseChatManagerFacade getChatManagerFacade() {
        return chatManagerFacade;
    }

    public void setChatManagerFacade(BaseChatManagerFacade chatManagerFacade) {
        this.chatManagerFacade = chatManagerFacade;
    }

    public BaseChatManagerViewController getChatManagerViewController() {
        return chatManagerViewController;
    }

    public void setChatManagerViewController(BaseChatManagerViewController chatManagerViewController) {
        this.chatManagerViewController = chatManagerViewController;
    }

    public BaseCurrentChatSessionHolder getCurrentChatSessionHolder() {
        return currentChatSessionHolder;
    }

    public void setCurrentChatSessionHolder(BaseCurrentChatSessionHolder currentChatSessionHolder) {
        this.currentChatSessionHolder = currentChatSessionHolder;
    }

    public BaseChatRoom getNewChatRoom() {
        return newChatRoom;
    }

    public void setNewChatRoom(BaseChatRoom newChatRoom) {
        this.newChatRoom = newChatRoom;
    }

    public BaseChatMessage getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(BaseChatMessage newMessage) {
        this.newMessage = newMessage;
    }
}
