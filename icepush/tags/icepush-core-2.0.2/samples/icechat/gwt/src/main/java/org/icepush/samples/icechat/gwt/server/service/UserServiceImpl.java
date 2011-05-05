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

package org.icepush.samples.icechat.gwt.server.service;

import org.icepush.samples.icechat.gwt.client.Credentials;
import org.icepush.samples.icechat.gwt.client.service.UserService;
import org.icepush.samples.icechat.gwt.server.service.beans.ChatServiceBean;
import org.icepush.samples.icechat.model.User;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class UserServiceImpl extends RemoteServiceServlet implements
		UserService {


	public Credentials register(String name) {
		// TODO Auto-generated method stub
		ChatServiceBean chatService = ChatServiceBean.getInstance(this.getServletContext());
		
		Credentials result = new Credentials();
		User user = chatService.login(name);
		
		result.setSessionToken(user.getSessionToken());
		result.setUserName(user.getName());
		return result;
	}


}