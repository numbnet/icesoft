package org.icepush.gwt.client.command;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface PushCommandServiceAsync {

	void getQueuedCommands(String clientKey,AsyncCallback<List<ICommand>> callback);
	void registerForCommand(String clientKey, String commandClass, AsyncCallback<Void> callback);
	void deregisterFromCommand(String clientKey, String commandClass, AsyncCallback<Void> callback);
}
