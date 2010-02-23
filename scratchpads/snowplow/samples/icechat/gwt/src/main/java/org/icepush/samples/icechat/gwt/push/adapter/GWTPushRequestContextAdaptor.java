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

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class GWTPushRequestContextAdaptor extends AbstractPushRequestContext{
	
	private GWTPushRequestContextAdaptor(ServletContext context, HttpServletRequest req, HttpServletResponse resp){
		this.initialize(context,req, resp);
	}

	public void initialize(ServletContext context, HttpServletRequest req, HttpServletResponse resp){
		this.intializePushContext(context,req,resp);
		req.getSession().setAttribute("pushContext", this);
	}
	
	public static GWTPushRequestContextAdaptor getInstance(ServletContext context, HttpServletRequest request, HttpServletResponse response){
	
		GWTPushRequestContextAdaptor instance = (GWTPushRequestContextAdaptor)request.getSession().getAttribute("pushContext");
		
		if(instance == null){
			instance = new GWTPushRequestContextAdaptor(context, request, response);
		}
		
		return instance;
	}
	
}