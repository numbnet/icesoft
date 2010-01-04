package org.icepush.samples.icechat.gwt.server.service;

import org.icepush.samples.icechat.gwt.client.service.ChatService;
import org.icepush.samples.icechat.gwt.client.UserService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.icepush.samples.icechat.gwt.push.adapter.GWTPushRequestContextAdaptor;
import org.icepush.PushContext;

import org.icepush.samples.icechat.gwt.client.*;

import java.util.List;
import java.util.ArrayList;
import org.icepush.samples.icechat.gwt.client.chat.ChatHandleBuilder;
import org.icepush.samples.icechat.gwt.client.chat.ChatRoomHandle;



public class ChatServiceImpl extends RemoteServiceServlet implements ChatService{

	private List<String> chatRooms = new ArrayList<String>();
	
    public void createChatRoom(String name) {
    try{
       	this.chatRooms.add(name);
       	GWTPushRequestContextAdaptor pushAdaptor = GWTPushRequestContextAdaptor.getInstance(this.getServletContext(),getThreadLocalRequest(),getThreadLocalResponse());
       	PushContext pushContext = pushAdaptor.getPushContext();
       	
       	pushContext.push("chatRoomIndex");
       	
       }catch(Exception e){
       		e.printStackTrace();
       }
    }
    
    public List<ChatRoomHandle> getChatRooms(){
        List<ChatRoomHandle> result = new ArrayList<ChatRoomHandle>(this.chatRooms.size());

        for(String chatName: this.chatRooms){
            ChatHandleBuilder builder = new ChatHandleBuilder();
            ChatRoomHandle handle = builder.createHandle(chatName);
            result.add(handle);
        }
    	return result;
    }
}