package org.icepush.samples.icechat.gwt.client;

import java.util.List;
import com.google.gwt.user.client.Window;

public class GWTPushContext{

	private static GWTPushContext instance;
	
	/**
		simply initialize the underlying native engine.
	*/
	private GWTPushContext(){
		Window.alert("Loading context...");
		this.init();
	}
	
	/**
		register a new listener to the specified list of render groups.
	*/
	public void addPushEventListener(PushEventListener listener, List<String> groupNames, String pushId){
		
		/*
		for(String group: groupNames){
			this.addGroupMember(group, pushId);
		}
		*/
		this.register(pushId,listener);
		
	}
	
	/**
		register a new listener to the specified list of render groups.
	*/
	public void addPushEventListener(PushEventListener listener, String[] groupNames, String id){
		/*String id = this.createPushId();
		
		for(String group: groupNames){
			this.addGroupMember(group, id);
		}
		*/
		Window.alert("id:" + id);
		this.register(id,listener);
		
	}
	
	/**
		retrieve the singleton instance of the GWT push context.
	*/
	public static GWTPushContext getInstance(){
		if(instance == null){
			instance = new GWTPushContext();
		}
		
		return instance; 
	}
	
	
	/* wrap the native js API for easier use */
	
	/**
		initialize the ICEpush engine.
	*/
	private native void init()/*-{
		
		//initialize a hashmap to map ids to callbacks.
		$wnd.ice.push.gwtRegisteredCallbacks = new Array(); 
		
		//create a function to get called on notification.
		$wnd.ice.push.gwtRootCallback = function(){
			$wnd.alert('notification recieved');
		}
		
		
		//$wnd.ice.push.register(['chatRoom1'], $wnd.ice.push.gwtRootCallback);
		
		
	}-*/;
	
	/**
		adds a push id to a server side render group.
	*/
	private native void addGroupMember(String groupName, String pushId)/*-{
		$wnd.ice.push.addGroupMember(groupName, pushId);
	}-*/;
	
	/**
		registers a list of push ids to a callback function.
	*/
	private native void register(String pushId, PushEventListener listener)/*-{
		
		//$wnd.ice.push.register(new Array(pushId),listener.@org.icepush.samples.icechat.gwt.client.PushEventListener::onPushEvent());
		$wnd.ice.push.register(new Array(pushId), $wnd.ice.push.gwtRootCallback);
	}-*/;
	
	/**
		creates a new push id.
	*/
	private native String createPushId()/*-{
		return $wnd.ice.push.createPushId();
	}-*/;
}