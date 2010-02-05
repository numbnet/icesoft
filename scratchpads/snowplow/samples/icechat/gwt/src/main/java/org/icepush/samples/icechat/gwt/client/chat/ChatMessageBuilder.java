package org.icepush.samples.icechat.gwt.client.chat;

/**
 * this is a builder class used to create immutable ChatRoomMessage objects
 */
public class ChatMessageBuilder {

    public ChatRoomMessage createChatMessage(String text, String nickname){
        ChatRoomMessage message = new ChatRoomMessage();

        message.initialize(text, nickname);

        return message;
    }

}
