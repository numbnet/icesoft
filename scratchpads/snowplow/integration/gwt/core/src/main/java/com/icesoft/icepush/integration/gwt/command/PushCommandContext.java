package com.icesoft.icepush.integration.gwt.command;

public class PushCommandContext {

	
	
	private static PushCommandContext instance = null;
	public static PushCommandContext getInstance(){
		if(instance == null)
			instance = new PushCommandContext();
		
		return instance;
	}
	
	
	public static void registerCommandExecuter(Class<? extends ICommand> commandClass, ICommandExecuter handler){
		getInstance().registerExecuter(commandClass, handler);
	}
	
	private void registerExecuter(Class<? extends ICommand> commandClass, ICommandExecuter handler){
		
	}
}
