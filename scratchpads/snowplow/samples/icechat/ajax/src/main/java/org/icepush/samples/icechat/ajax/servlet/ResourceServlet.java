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

package org.icepush.samples.icechat.ajax.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ResourceServlet extends HttpServlet {

	private String rootResourceDir;
	public static final String ROOT_RESOURCE_DIR_KEY = "ROOT_RESOURCE_DIR";
	public static final String RESOURCE_PARAM_KEY = "res";

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		dispatch(req,resp);
	}

	@Override
	public void init() throws ServletException {
		super.init();
		rootResourceDir = getServletConfig().getServletContext()
				.getInitParameter(ROOT_RESOURCE_DIR_KEY);
	}

	private void dispatch(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {

		try {
			String resource = req.getParameter(RESOURCE_PARAM_KEY);
			if( resource != null && resource.length() > 0 ){
				String path = req.getContextPath() + (resource.startsWith("/") ?  "" : "/") 
				+ rootResourceDir + resource;
				getServletConfig().getServletContext().getRequestDispatcher(path).forward(req,resp);
			}			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
