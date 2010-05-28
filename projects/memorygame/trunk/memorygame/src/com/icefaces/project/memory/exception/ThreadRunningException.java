package com.icefaces.project.memory.exception;

/**
 * Exception thrown when a method is accessed, but currently has a running thread
 *  doing the same process as the method
 * For example we'd throw this if a user tries to click on a card while we are
 *  waiting for a thread to complete that ends the turn and flips the cards over
 */
public class ThreadRunningException extends Exception {
	private static final long serialVersionUID = 6872531712936335658L;

	public ThreadRunningException() {
        super();
    }
    
    public ThreadRunningException(String message) {
        super(message);
    }
    
    public ThreadRunningException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public ThreadRunningException(Throwable cause) {
        super(cause);
    }
}
