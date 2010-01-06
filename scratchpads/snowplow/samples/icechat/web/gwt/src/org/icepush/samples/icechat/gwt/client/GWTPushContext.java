package org.icepush.samples.icechat.gwt.client;

import com.google.gwt.user.client.Window;
import java.util.Arrays;
import java.util.List;

public class GWTPushContext{

	private static GWTPushContext instance;
	
	/**
		simply initialize the underlying native engine.
	*/
	private GWTPushContext(){
		this.init();
	}
	
	/**
		register a new listener to the specified list of render groups.
	*/
	public void addPushEventListener(PushEventListener listener, List<String> groupNames, String pushId){
		
		listener.setPushId(pushId);
		for(String group: groupNames){
			this.addGroupMember(group, pushId);
		}

                listener.setGroups(groupNames.toArray(new String[]{}));
		
		this.register(pushId,listener);
		
	}
	
	/**
            register a new listener to the specified list of render groups.
	*/
	public void addPushEventListener(PushEventListener listener, String[] groupNames){
		
		String id = this.createPushId();
		listener.setPushId(id);
		for(String group: groupNames){
			this.addGroupMember(group, id);
		}
                listener.setGroups(groupNames);
		
		this.register(id,listener);
		
	}

        /**
         * removes this push event listener from the push notifications
         */
        public void removePushEventListener(PushEventListener listener){
            this.unregisterUserCallbacks(listener);
            this.deregister(listener.getPushId());

            for(String group: listener.getGroups()){
             this.removeGroupMember(group, listener.getPushId());
            }
            
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
		$wnd.ice.push.gwtRootCallback = function(pushIds){
			for(var i = 0; i < $wnd.ice.push.gwtRegisteredCallbacks.length; i++){
                            var listener = $wnd.ice.push.gwtRegisteredCallbacks[i];

                            var listenerId = listener.@org.icepush.samples.icechat.gwt.client.PushEventListener::getPushId()();
                            
                            if(listenerId == pushIds){
                                listener.@org.icepush.samples.icechat.gwt.client.PushEventListener::onPushEvent()();
                            }

                        }
		}
		
		
	}-*/;
	
	/**
         * @param groupName the group name
         * @param pushId the push id to register to this group.
	 *	adds a push id to a server side render group.
	*/
	private native void addGroupMember(String groupName, String pushId)/*-{
		$wnd.ice.push.addGroupMember(groupName, pushId);
	}-*/;

        /**
         *
         * @param groupName the group name
         * @param pushId the push id to unregister from this group.
         *
         * removes a specified push id from a render group.
         */
        private native void removeGroupMember(String groupName, String pushId)/*-{
            $wnd.ice.push.removeGroupMember(groupName, pushId);
        }-*/;
	
	/**
		registers a list of push ids to a callback function.
	*/
	private native void register(String pushId, PushEventListener listener)/*-{
		$wnd.ice.push.gwtRegisteredCallbacks.push(listener)
		$wnd.ice.push.register(new Array(pushId), $wnd.ice.push.gwtRootCallback);
	}-*/;

        private native void deregister(String pushId)/*-{
            $wnd.ice.push.deregister(pushId);
         }-*/;


        private native void unregisterUserCallbacks(PushEventListener listener)/*-{
                var index = -1;
                for(var i = 0; i < $wnd.ice.push.gwtRegisteredCallbacks.length; i++){
                    if($wnd.ice.push.gwtRegisteredCallbacks[i] == listener){
                        index = i;
                    }
                }

                $wnd.ice.push.gwtRegisteredCallbacks.splice(index,1);
                
         }-*/;
	
	/**
		creates a new push id.
	*/
	private native String createPushId()/*-{
		return $wnd.ice.push.createPushId();
	}-*/;
}