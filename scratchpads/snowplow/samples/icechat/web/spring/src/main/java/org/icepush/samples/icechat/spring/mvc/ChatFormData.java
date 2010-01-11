package org.icepush.samples.icechat.spring.mvc;

import org.icepush.samples.icechat.spring.impl.BaseChatRoom;
import org.icepush.samples.icechat.spring.impl.BaseChatManagerViewController;
import org.icepush.samples.icechat.spring.impl.BasePushRequestContext;
import org.icepush.samples.icechat.spring.impl.BaseCurrentChatSessionHolder;
import org.icepush.samples.icechat.spring.impl.BaseChatMessage;

public class ChatFormData {
    private BasePushRequestContext pushRequestContext;

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

        // Cascade the push request context to child classes that need it
        if (chatManagerViewController != null) {
            chatManagerViewController.setPushRequestContext(this.pushRequestContext);
        }
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
