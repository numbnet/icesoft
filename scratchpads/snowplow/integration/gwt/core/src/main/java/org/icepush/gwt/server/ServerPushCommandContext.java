package org.icepush.gwt.server;

import javax.servlet.ServletContext;

import org.icepush.PushContext;
import org.icepush.gwt.client.command.ICommand;

public class ServerPushCommandContext {

	private ClientManager clientManager;
	
	public static ServerPushCommandContext getInstance(ServletContext context){
		return (ServerPushCommandContext) context.getAttribute(ServerPushCommandContext.class.getName());
	}
	
	public ServerPushCommandContext(ClientManager manager){
		this.clientManager = manager;
	}
	
	public void pushCommand(ICommand command, ServletContext context){
		this.clientManager.broadcastCommand(command);
		PushContext.getInstance(context).push(command.getClass().getName());
	}
	
	
}
