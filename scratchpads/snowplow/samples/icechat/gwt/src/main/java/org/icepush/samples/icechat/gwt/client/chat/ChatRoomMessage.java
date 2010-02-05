package org.icepush.samples.icechat.gwt.client.chat;

import java.io.Serializable;

/**
 * this class is a light weight transfer object representing a chat message.
 */
public class ChatRoomMessage implements Serializable{

    String text;
    String nickname;

    public ChatRoomMessage(){}


    void initialize(String text, String nickname){
        this.text = text;
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public String getText() {
        return text;
    }


}
