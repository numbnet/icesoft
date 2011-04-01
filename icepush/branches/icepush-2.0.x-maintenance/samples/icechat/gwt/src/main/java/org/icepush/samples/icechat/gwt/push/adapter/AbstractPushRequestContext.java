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
 * The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icepush.samples.icechat.gwt.push.adapter;

import java.io.Serializable;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icepush.PushContext;

public abstract class AbstractPushRequestContext implements Serializable{

	private static final long serialVersionUID = -6769963604096352169L;
	
	private String currentPushId;
	private transient PushContext pushContext;
	
	public String getCurrentPushId() {
		return currentPushId;
	}
	
	public PushContext getPushContext(){
		return pushContext;
	}

	protected void intializePushContext(ServletContext context, HttpServletRequest request, HttpServletResponse response){
		pushContext = PushContext.getInstance(context);
		currentPushId = pushContext.createPushId(request, response);
	}
	
}
