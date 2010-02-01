package org.icepush.integration.gwt.command;

import java.io.Serializable;

/**
 * a generic exception class to throw when the framework encounters and error.
 * @author Patrick Wilson
 *
 */
public class CommandFrameworkException extends RuntimeException implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public CommandFrameworkException(String message){
		super(message);
	}
	
	public CommandFrameworkException(String message, Throwable t){
		super(message,t);
	}
}
