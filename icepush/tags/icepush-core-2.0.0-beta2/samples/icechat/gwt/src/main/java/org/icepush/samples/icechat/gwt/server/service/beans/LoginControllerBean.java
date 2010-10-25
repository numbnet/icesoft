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

package org.icepush.samples.icechat.gwt.server.service.beans;

import javax.servlet.ServletContext;

import org.icepush.samples.icechat.beans.controller.BaseLoginControllerBean;
import org.icepush.samples.icechat.model.User;

public class LoginControllerBean extends BaseLoginControllerBean{


	private static final long serialVersionUID = 1L;

	private LoginControllerBean(ServletContext context){
		this.chatService = ChatServiceBean.getInstance(context);
	}
	
	public static LoginControllerBean getInstance(ServletContext context){
		if(context.getAttribute(LoginControllerBean.class.getName()) == null){
			context.setAttribute(LoginControllerBean.class.getName(), new LoginControllerBean(context));
		}
		
		return (LoginControllerBean)context.getAttribute(LoginControllerBean.class.getName());
	}

	
}
