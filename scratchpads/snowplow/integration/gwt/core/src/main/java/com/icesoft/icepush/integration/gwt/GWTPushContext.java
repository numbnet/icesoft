package com.icesoft.icepush.integration.gwt;

import java.util.List;

public class GWTPushContext {

	private static GWTPushContext instance;
	private IcePushClientLibrary icePushClient = null;

	/**
	 * simply initialize the underlying native engine.
	 */
	private GWTPushContext() {
		icePushClient = new NativePushLibraryImpl();
		icePushClient.init();
	}
	
	protected GWTPushContext(IcePushClientLibrary lib){
		icePushClient = lib;
	}

	/**
	 * register a new listener to the specified list of render groups.
	 */
	public void addPushEventListener(PushEventListener listener,
			List<String> groupNames, String pushId) {

		listener.setPushId(pushId);
		for (String group : groupNames) {
			icePushClient.addGroupMember(group, pushId);
		}

		listener.setGroups(groupNames.toArray(new String[] {}));

		icePushClient.register(pushId, listener);

	}

	/**
	 * register a new listener to the specified list of render groups.
	 */
	public void addPushEventListener(PushEventListener listener,
			String[] groupNames) {

		String id = icePushClient.createPushId();
		listener.setPushId(id);
		for (String group : groupNames) {
			icePushClient.addGroupMember(group, id);
		}
		listener.setGroups(groupNames);

		icePushClient.register(id, listener);

	}

	/**
	 * removes this push event listener from the push notifications
	 */
	public void removePushEventListener(PushEventListener listener) {
		icePushClient.unregisterUserCallbacks(listener);
		icePushClient.deregister(listener.getPushId());

		for (String group : listener.getGroups()) {
			icePushClient.removeGroupMember(group, listener.getPushId());
		}

	}

	/**
	 * retrieve the singleton instance of the GWT push context.
	 */
	public static GWTPushContext getInstance() {
		if (instance == null) {
			instance = new GWTPushContext();
		}

		return instance;
	}

}