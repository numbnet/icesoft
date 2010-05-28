/*
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
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/
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
