package org.icepush.samples.icechat.gwt.client.chat;

import java.io.Serializable;

/**
 * This is an immutabe handle to a chat room.
 */
public class ChatRoomHandle implements Serializable{

    private String chatRoomName;

    protected void initialize(String name){
        this.chatRoomName = name;
    }

    public String getName(){
        return this.chatRoomName;
    }

}
