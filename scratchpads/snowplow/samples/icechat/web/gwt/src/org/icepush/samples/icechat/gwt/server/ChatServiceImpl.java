package org.icepush.samples.icechat.gwt.server;

import org.icepush.samples.icechat.gwt.client.UserService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.icepush.samples.icechat.gwt.push.adapter.GWTPushRequestContextAdaptor;
import org.icepush.PushContext;

import org.icepush.samples.icechat.gwt.client.*;

import java.util.List;
import java.util.ArrayList;



public class ChatServiceImpl extends RemoteServiceServlet implements ChatService{

	private List<String> chatRooms = new ArrayList<String>();
	
    public void createChatRoom(String name) {
    try{
       	this.chatRooms.add(name);
       	GWTPushRequestContextAdaptor pushAdaptor = GWTPushRequestContextAdaptor.getInstance(this.getServletContext(),getThreadLocalRequest(),getThreadLocalResponse());
       	PushContext pushContext = pushAdaptor.getPushContext();
       	
       	//pushContext.addGroupMember(name,pushAdaptor.getCurrentPushId());
       	//System.out.println("adding " + pushAdaptor.getCurrentPushId() + " to group: '" + name+ "'");
       	pushContext.push(name);
       	
       	System.out.println("pushing to group: " + name);
       }catch(Exception e){
       		e.printStackTrace();
       }
    }
    
    public List<String> getChatRooms(){
    	return this.chatRooms;
    }
}