package com.icesoft.icepush.integration.gwt;

public interface IcePushClientLibrary {
	public void init();
	public void addGroupMember(String groupName, String pushId);
	public void removeGroupMember(String groupName, String pushId);
	public void register(String pushId, PushEventListener listener);
	public void deregister(String pushId);
	public void unregisterUserCallbacks(PushEventListener listener);
	public String createPushId();
}
