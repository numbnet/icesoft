package org.icepush.samples.icechat.gwt.server.service;

import org.icepush.samples.icechat.gwt.client.chat.ChatRoomMessage;
import org.icepush.samples.icechat.gwt.client.service.ChatService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.icepush.samples.icechat.gwt.push.adapter.GWTPushRequestContextAdaptor;
import org.icepush.PushContext;


import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import org.icepush.samples.icechat.gwt.client.chat.ChatHandleBuilder;
import org.icepush.samples.icechat.gwt.client.chat.ChatMessageBuilder;
import org.icepush.samples.icechat.gwt.client.chat.ChatRoomHandle;

public class ChatServiceImpl extends RemoteServiceServlet implements ChatService {

    private List<String> chatRooms = new ArrayList<String>();
    private HashMap<String, List<String>> participants = new HashMap<String, List<String>>();

    private HashMap<String, String> participantTextBoxes = new HashMap<String, String>();

    private HashMap<String, List<ChatRoomMessage>> chatMessages  = new HashMap<String, List<ChatRoomMessage>>();

    public ChatRoomHandle createChatRoom(String name) {
        
            this.chatRooms.add(name);
            GWTPushRequestContextAdaptor pushAdaptor = GWTPushRequestContextAdaptor.getInstance(this.getServletContext(), getThreadLocalRequest(), getThreadLocalResponse());
            PushContext pushContext = pushAdaptor.getPushContext();

            ChatHandleBuilder builder = new ChatHandleBuilder();
            ChatRoomHandle handle = builder.createHandle(name);

            pushContext.push("chatRoomIndex");
            return handle;
        
    }

    public List<ChatRoomHandle> getChatRooms() {
        List<ChatRoomHandle> result = new ArrayList<ChatRoomHandle>(this.chatRooms.size());

        for (String chatName : this.chatRooms) {
            ChatHandleBuilder builder = new ChatHandleBuilder();
            ChatRoomHandle handle = builder.createHandle(chatName);
            result.add(handle);
        }
        return result;
    }

    public void joinChatRoom(ChatRoomHandle handle,String username) {
        System.out.println("Joining Chat Room...");
        List<String> participantList = this.participants.get(handle.getName());
        if(participantList == null){
            participantList = new ArrayList<String>();
            this.participants.put(handle.getName(), participantList);
        }

        if(participantList.contains(username)){
            return; //already joined.
        }

        this.participantTextBoxes.put(username + handle.getName(), "");
        
        participantList.add(username);
        PushContext.getInstance(this.getServletContext()).push(handle.getName()+"-participants");


    }

    public List<String> getParticipants(ChatRoomHandle handle) {
        System.out.println("Getting Participants");
        return this.participants.get(handle.getName());
    }

    public void sendMessage(String message, String username, ChatRoomHandle handle) {
        System.out.println("Sending message: '" + message + "'");
        List<ChatRoomMessage> messages = this.chatMessages.get(handle.getName());
        if(messages == null){
            messages = new ArrayList<ChatRoomMessage>();
            this.chatMessages.put(handle.getName(), messages);
        }

        ChatMessageBuilder builder = new ChatMessageBuilder();
        messages.add(builder.createChatMessage(message, username));

        participantTextBoxes.put(username + handle.getName(), "");

        PushContext.getInstance(this.getServletContext()).push(handle.getName());
        PushContext.getInstance(this.getServletContext()).push(username+handle.getName().replaceAll(" ", "_"));
    }

    public ChatRoomHandle getMessages(ChatRoomHandle handle) {
        
        List<ChatRoomMessage> result = new ArrayList<ChatRoomMessage>();
        if(this.chatMessages.get(handle.getName()) == null){
            return handle; //return the unchanged handle.
        }

        for(int i = handle.getMessageIndex(); i < this.chatMessages.get(handle.getName()).size(); i++){
            result.add(this.chatMessages.get(handle.getName()).get(i));
        }

        ChatHandleBuilder builder = new ChatHandleBuilder();
        builder.addMessages(handle, result);
        System.out.println("Getting new messages...found " + handle.getNextMessages().size() + " (new index: " + handle.getMessageIndex() + ")");
        return handle;
    }

    public void sendCharacterNotification(String username, ChatRoomHandle handle, String newText) {
        this.participantTextBoxes.put(username + handle.getName(), newText);

        System.out.println("Push [" + username + handle.getName().replaceAll(" ", "_") + "]");
        PushContext.getInstance(this.getServletContext()).push(username + handle.getName().replaceAll(" ", "_"));

    }

    public String getCurrentCharacters(String username, ChatRoomHandle handle){
        return this.participantTextBoxes.get(username + handle.getName());
    }

 
}
