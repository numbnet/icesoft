package org.icepush.samples.icechat.gwt.client.chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This is an immutabe handle to a chat room.
 */
public class ChatRoomHandle implements Serializable{

    private String chatRoomName;
    private int messageIndex;

    private List<ChatRoomMessage> nextMessages = new ArrayList<ChatRoomMessage>();

    protected void initialize(String name){
        this.chatRoomName = name;
    }

    public String getName(){
        return this.chatRoomName;
    }

    protected void updateMessageIndex(int index){
        this.messageIndex = index;
    }

    public int getMessageIndex(){
        return this.messageIndex;
    }

    public void setNextMessages(List<ChatRoomMessage> messages){
        this.nextMessages = messages;
    }

    public List<ChatRoomMessage> getNextMessages(){
        return this.nextMessages;
    }


}
