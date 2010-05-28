package com.icefaces.project.memory.exception;

/**
 * Exception thrown when a user fails to join a game.
 */
public class FailedJoinException extends Exception {
	private static final long serialVersionUID = -6030523484615620517L;

	public FailedJoinException() {
        super();
    }
    
    public FailedJoinException(String message) {
        super(message);
    }
    
    public FailedJoinException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public FailedJoinException(Throwable cause) {
        super(cause);
    }
}
