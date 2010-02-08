package org.icepush.gwt.client.command;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("commandPushService")
public interface PushCommandService extends RemoteService {

	public List<ICommand> getQueuedCommands(String clientKey);
	
	public void registerForCommand(String clientKey, String commandClass);
	public void deregisterFromCommand(String clientKey, String commandClass);
	
}
