package org.icepush.gwt.client;

public class NativePushLibraryImpl implements IcePushClientLibrary {

	/**
	 * initialize the ICEpush engine.
	 */
	public native void init()/*-{
		
		//initialize a hashmap to map ids to callbacks.
		$wnd.ice.push.gwtRegisteredCallbacks = new Array(); 
		
		//create a function to get called on notification.
		$wnd.ice.push.gwtRootCallback = function(pushIds){
			for(var i = 0; i < $wnd.ice.push.gwtRegisteredCallbacks.length; i++){
            	var listener = $wnd.ice.push.gwtRegisteredCallbacks[i];
				var listenerId = listener.@org.icepush.gwt.client.PushEventListener::getPushId()();
                            
                	if(listenerId == pushIds){
                    	listener.@org.icepush.gwt.client.PushEventListener::onPushEvent()();
                    }
            }
		}
	}-*/;
	
	/**
	 * @param groupName
	 *            the group name
	 * @param pushId
	 *            the push id to register to this group. adds a push id to a
	 *            server side render group.
	 */
	public native void addGroupMember(String groupName, String pushId)/*-{
		$wnd.ice.push.addGroupMember(groupName, pushId);
	}-*/;

	/**
	 * 
	 * @param groupName
	 *            the group name
	 * @param pushId
	 *            the push id to unregister from this group.
	 * 
	 *            removes a specified push id from a render group.
	 */
	public native void removeGroupMember(String groupName, String pushId)/*-{
    	$wnd.ice.push.removeGroupMember(groupName, pushId);
    }-*/;
	
	/**
		registers a list of push ids to a callback function.
	*/
	public native void register(String pushId, PushEventListener listener)/*-{
		$wnd.ice.push.gwtRegisteredCallbacks.push(listener)
		$wnd.ice.push.register(new Array(pushId), $wnd.ice.push.gwtRootCallback);
	}-*/;

    public native void deregister(String pushId)/*-{
        $wnd.ice.push.deregister(pushId);
    }-*/;


    public native void unregisterUserCallbacks(PushEventListener listener)/*-{
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
	public native String createPushId()/*-{
		return $wnd.ice.push.createPushId();
	}-*/;
	
	/**
	 * invoke native push api to send a notification.
	 */
	public native void push(String group)/*-{
		$wnd.ice.push.notify(group);
	}-*/;
}
