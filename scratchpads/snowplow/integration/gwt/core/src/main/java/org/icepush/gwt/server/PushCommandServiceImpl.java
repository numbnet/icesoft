package org.icepush.gwt.server;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.icepush.gwt.client.command.CommandFrameworkException;
import org.icepush.gwt.client.command.ICommand;
import org.icepush.gwt.client.command.PushCommandService;


import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * this is the GWT remote procedure servlet for the command API.  Modification of this class is not necessary to use the API.
 * @author Patrick Wilson
 *
 */
public class PushCommandServiceImpl extends RemoteServiceServlet implements PushCommandService {

	Logger log = Logger.getLogger(PushCommandServiceImpl.class.getName());
	
	public List<ICommand> getQueuedCommands(String clientKey) {
		try{
			ClientManager manager = (ClientManager) this.getServletContext().getAttribute(ClientManager.class.getName());
			
			return manager.getQueuedCommandsForClient(clientKey);
		}catch(Throwable t){
			logError(t);
			return null;
		}
		
	}
	


	@SuppressWarnings("unchecked")
	
	public void deregisterFromCommand(String clientKey,
			String commandClassName) {
		try{
			Class<? extends ICommand> commandClass = (Class<? extends ICommand>) Class.forName(commandClassName);
		
			ClientManager manager = (ClientManager) this.getServletContext().getAttribute(ClientManager.class.getName());
			manager.unregisterClientFromCommand(clientKey, commandClass);
		}catch(Throwable t){
			logError(t);
		}
	}

	@SuppressWarnings("unchecked")
	
	public void registerForCommand(String clientKey,
			String commandClassName) {
		try{
			Class<? extends ICommand> commandClass = (Class<? extends ICommand>) Class.forName(commandClassName);
			ClientManager manager = (ClientManager) this.getServletContext().getAttribute(ClientManager.class.getName());
			manager.registerClientToCommand(clientKey, commandClass);
		}catch(Throwable t){
			logError(t);
		}
		
	}
	
	private void logError(Throwable t) throws CommandFrameworkException {
		log.warning("An error occured: " + t.getMessage());
		if(log.getLevel() == Level.WARNING || log.getLevel() == Level.SEVERE){
			t.printStackTrace();
		}
		throw new CommandFrameworkException("there was an error getting the queued commands: caused by: " + t.getMessage(), t);
	}
	
	
	

}
