/*
 *
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 *
 */
package org.icepush.gwt.client;

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
	 * removes this push event listener from the push notifications
	 */
	public void removePushEventListenerFromGroup(PushEventListener listener, String group) {
		icePushClient.removeGroupMember(group, listener.getPushId());
	}
	
	/**
	 * send a push notification to the specified group.
	 * @param group a string group name
	 */
	public void push(String group){
		this.icePushClient.push(group);
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
	
	
	/**
	 * retrieve the implementation of the ICEpushNativeAPI.
	 */
	
	public IcePushClientLibrary getIcePushClientLibrary(){
		return this.icePushClient;
	}

}