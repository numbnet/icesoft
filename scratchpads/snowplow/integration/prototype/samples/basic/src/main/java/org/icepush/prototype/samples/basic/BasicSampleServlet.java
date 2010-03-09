/*
 *
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 *
 */

package org.icepush.prototype.samples.basic;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icepush.PushContext;

public class BasicSampleServlet extends HttpServlet{
	
	private static final String GET_GROUPS = "/groups";
	private static final String GET_TIME = "/time";
	private static final String POST_CREATE_GROUP = "/creategroup";
	
	private static DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss z");
	
	private Timer timer;

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.setContentType("text/html");
		setCacheHeaders(resp);
		
		String url = req.getServletPath();

		if (GET_GROUPS.equals(url)) {
			StringBuffer buff = new StringBuffer();
			buff.append("<div>");
			for( String group: getGroupsList() ){
				buff.append("<div><a href=\"javascript:joinGroup('");
				buff.append(group);
				buff.append("');\">");
				buff.append(group);
				buff.append("</a></div>");
			}
			buff.append("</div>");
			resp.getWriter().print(buff.toString());
		} else if (GET_TIME.equals(url)) {
			resp.getWriter().print(DATE_FORMAT.format(new Date()));
		} 			
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		resp.setContentType("text/html");
		setCacheHeaders(resp);
		
		String url = req.getServletPath();

		if (POST_CREATE_GROUP.equals(url)) {
			String group = req.getParameter("group");
			if( group != null )
				getGroupsList().add(group);
			
			PushContext pushContext = PushContext.getInstance(req.getSession().getServletContext());
			pushContext.push("groups");
		} 
	}
	
	private void setCacheHeaders(HttpServletResponse resp){
		resp.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");//HTTP 1.1
        resp.setHeader("Pragma", "no-cache");//HTTP 1.0
        resp.setHeader("Expires", "0");//prevents proxy caching
	}
	
	public List<String> getGroupsList() {
		List<String> list = (List<String>) this.getServletContext()
				.getAttribute("groups");
		if( list == null ){
			list = new ArrayList<String>();
			this.getServletContext().setAttribute("groups", list);
		}
		return list;
	}

	public void init(final ServletConfig config) throws ServletException {
		
		super.init(config);
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask(){

			@Override
			public void run() {
				if( config != null ){
					PushContext pushContext = PushContext.getInstance(config.getServletContext());
					if( pushContext != null ){
						pushContext.push("time");
					}
					else{
						System.err.println("Can't run ICEpush timer taks, PushContext is null");
					}
				}
				else{
					System.err.println("Cannot run ICEpush timer task, ServletConfig is null");
				}
			}
			
		}, 2000, 1000);
		
	}

	public void destroy() {
		super.destroy();
		if( timer != null )
			timer.cancel();
	}

}
