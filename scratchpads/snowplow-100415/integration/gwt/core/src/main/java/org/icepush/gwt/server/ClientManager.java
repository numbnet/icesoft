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
package org.icepush.gwt.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.icepush.gwt.client.command.ICommand;


public class ClientManager {

	private HashMap<String,SingleClient> clientMap = new HashMap<String, SingleClient>();
	
	/**
	 * this method will return whether a client has been registered to receive a command type.
	 * @param clientKey a UUID that identifies a specific client.
	 * @param command the class of command to test for.
	 * @return
	 */
	public boolean isClientRegisteredToCommand(String clientKey, Class<? extends ICommand> command){
		this.ensureClientInMap(clientKey);
		return this.getClient(clientKey).isRegisteredToCommand(command);
	}
	
	/**
	 * register a client to a start receiving the specified command.
	 * @param clientKey a UUID that identifies a specific client.
	 * @param command the class of command to test for.
	 */
	public void registerClientToCommand(String clientKey, Class<? extends ICommand> command){
		this.ensureClientInMap(clientKey);
		this.getClient(clientKey).registerToCommand(command);
	}
	
	
	/**
	 * unregister a client so they will stop receiving the specified command.
	 * @param clientKey a UUID that identifies a specific client.
	 * @param command the class of command to test for.
	 */
	public void unregisterClientFromCommand(String clientKey, Class<? extends ICommand> command){
		this.ensureClientInMap(clientKey);
		this.getClient(clientKey).unregisterFromCommand(command);
	}
	
	/**
	 * ensures that the specified client key is in the map.
	 * @param clientKey
	 */
	private void ensureClientInMap(String clientKey){
		if(!this.clientMap.containsKey(clientKey)){
			this.clientMap.put(clientKey, new SingleClient());
		}
	}
	
	private SingleClient getClient(String clientKey){
		return this.clientMap.get(clientKey);
	}
	
	/**
	 * broadcast this command to all registered participants
	 * @param command a command object to broadcast.
	 */
	public void broadcastCommand(ICommand command){
		Iterator<String> clients = this.clientMap.keySet().iterator();
		
		while(clients.hasNext()){
			String clientKey = clients.next();
			if(this.getClient(clientKey).isRegisteredToCommand(command.getClass())){
				this.getClient(clientKey).queueCommand(command);
			}
		}
	}
	
	/**
	 * get all the queued commands for the specified client.
	 * @param clientKey a UUID that identifies a specific client.
	 * @return
	 */
	public List<ICommand> getQueuedCommandsForClient(String clientKey){
		return this.getClient(clientKey).getQueuedCommands();
	}
	
	private class SingleClient{
		private HashMap<String, Boolean> registeredCommands = new HashMap<String,Boolean>();
		private List<ICommand> queuedCommands = new ArrayList<ICommand>();
		/**
		 * return whether the client is registered with the command class name.  False if the map entry for this command class is empty or false.
		 * @param command
		 * @return
		 */
		public boolean isRegisteredToCommand(Class<? extends ICommand> command){
			if(this.registeredCommands.containsKey(command.getName())){
				return this.registeredCommands.get(command.getName());
			}else{
				return false;
			}
		}
		
		public void registerToCommand(Class<? extends ICommand> command){
			this.registeredCommands.put(command.getName(), true);
		}
		
		public void unregisterFromCommand(Class<? extends ICommand> command){
			this.registeredCommands.put(command.getName(), false);
		}
		
		public void queueCommand(ICommand command){
			this.queuedCommands.add(command);
		}
		
		public List<ICommand> getQueuedCommands(){
			List<ICommand> result = this.queuedCommands;
			this.queuedCommands = new ArrayList<ICommand>();
			return result;
		}
	}
}
