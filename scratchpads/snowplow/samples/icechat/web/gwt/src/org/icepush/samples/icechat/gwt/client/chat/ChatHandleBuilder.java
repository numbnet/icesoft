package org.icepush.samples.icechat.gwt.client.chat;

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
}
