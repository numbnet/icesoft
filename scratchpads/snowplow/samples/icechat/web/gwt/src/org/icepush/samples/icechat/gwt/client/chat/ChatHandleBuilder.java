package org.icepush.samples.icechat.gwt.client.chat;

import java.util.List;

/**
 *  this class is designed to be used to create immutable ChatHandles.
 * 
 */
public class ChatHandleBuilder {
    public ChatRoomHandle createHandle(String name){
        ChatRoomHandle handle = new ChatRoomHandle();
        handle.initialize(name);
        return handle;
    }

    public void addMessages(ChatRoomHandle handle, List<ChatRoomMessage> messages){
        handle.updateMessageIndex(handle.getMessageIndex() + messages.size());
        handle.setNextMessages(messages);
    }
}
