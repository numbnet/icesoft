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
package org.icepush.gwt.client.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.icepush.gwt.client.GWTPushContext;
import org.icepush.gwt.client.PushEventListener;


import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;


public class ClientPushCommandContext {

	/*
	 * 
	 * this is a unique identifier that will be used to identify this client on the server
	 * 
	 **/
	private String key = "client:" + Math.floor(Math.random() * 1000);
	
	PushCommandServiceAsync service = GWT.create(PushCommandService.class);
	VoidCallback defaultVoidCallback = new VoidCallback();
	
	private CommandPushListener listener = new CommandPushListener();
	
	private static ClientPushCommandContext instance = null;
	public static ClientPushCommandContext getInstance(){
		if(instance == null)
			instance = new ClientPushCommandContext();
		
		return instance;
	}
	
	
	/**
	 * a method to register a command executer with a group name.
	 * @param commandClass
	 * @param handler
	 * @param groupName
	 */
	public void registerExecuter(Class<? extends ICommand> commandClass, ICommandExecuter handler){
		GWTPushContext context = GWTPushContext.getInstance();
		
		context.addPushEventListener(listener, commandClass.getName());
		listener.addCommandExecutor(commandClass, handler);
		
		service.registerForCommand(key, commandClass.getName(),defaultVoidCallback);
		
	}
	
	/**
	 * stop handling the specified command class with the specified CommandExecuter.
	 * @param commandClass
	 * @param handler
	 */
	public void deregisterExecuter(Class<? extends ICommand> commandClass, Class< ? extends ICommandExecuter> handler){
		try{
		listener.removeCommandExecuter(commandClass, handler);
		}catch(HandlerRemovalException ex){
			//trying to remove last handler, should call deregisterCommandType instead.
			deregisterCommandType(commandClass);
		}
	}
	
	/**
	 *  stop handling the specified command class with any CommandExecuter.
	 * 
	 * @param commandClass
	 */
	public void deregisterCommandType(Class<? extends ICommand> commandClass){
		GWTPushContext context = GWTPushContext.getInstance();
		context.removePushEventListenerFromGroup(listener, commandClass.getName());
		
		listener.removeAllCommandExecutersForCommand(commandClass);
		service.deregisterFromCommand(key, commandClass.getName(), defaultVoidCallback);
	}

	
	/** 
	 * this class is the push event listener that is used to invoke the proper command executer.
	 *
	 */
	public class CommandPushListener extends PushEventListener implements AsyncCallback<List<ICommand>>{
		
		private HashMap<String, List<ICommandExecuter>> handlers = new HashMap<String, List<ICommandExecuter>>();
		
		public void addCommandExecutor(Class<? extends ICommand> commandClass, ICommandExecuter handler){
			if(!handlers.containsKey(commandClass.getName())){
				handlers.put(commandClass.getName(), new ArrayList<ICommandExecuter>());
			}
			
			handlers.get(commandClass.getName()).add(handler);
		}
		
		public void removeCommandExecuter(Class<? extends ICommand> commandClass, Class<? extends ICommandExecuter> handler){
			if(handlers.containsKey(commandClass.getName())){
				if(this.handlers.get(commandClass.getName()).size() <= 1){
					throw new HandlerRemovalException("Can't remove single remaining executer with this method: use removeAllCommandExecutersForCommand instead");
				}
				ListIterator<ICommandExecuter> handlers = this.handlers.get(commandClass.getName()).listIterator();
				
				while(handlers.hasNext()){
					ICommandExecuter item = handlers.next();
					if(item.getClass().equals(handler)){
						handlers.remove();
					}
				}
			}
		}
		
		public void removeAllCommandExecutersForCommand(Class<? extends ICommand> commandClass){
			if(handlers.containsKey(commandClass.getName())){
				this.handlers.remove(commandClass.getName());
			}
		}
		
		public void onPushEvent() {
			ClientPushCommandContext.this.service.getQueuedCommands(ClientPushCommandContext.this.key, this);
		}

		public void onFailure(Throwable arg0) {
		
			
		}

		public void onSuccess(List<ICommand> commandQueue) {
			for(ICommand command: commandQueue){
				List<ICommandExecuter> handlers = this.handlers.get(command.getClass().getName());
				for(ICommandExecuter handler: handlers){
					handler.execute(command);
				}
			}
			
		}
		
	}
	
	private class HandlerRemovalException extends RuntimeException{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public HandlerRemovalException(String message) {
			super(message);
		}
	}
	
	private class VoidCallback implements AsyncCallback<Void>{

		public void onFailure(Throwable arg0) {

			
		}

		public void onSuccess(Void arg0) {
			//do nothing.
			
		}}
	
}
