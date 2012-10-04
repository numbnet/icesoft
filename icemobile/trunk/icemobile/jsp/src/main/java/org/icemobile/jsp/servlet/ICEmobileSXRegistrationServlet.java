/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icemobile.jsp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icemobile.util.Constants;
import org.icemobile.util.Utils;

/**
 * Register the ICEmobile-SX auxiliary upload post. This Servlet is automatically 
 * mapped to /icemobile/* for Servlet 3.0 and must be manually mapped to that
 * URL pattern in web.xml for Servlet 2.5 containers. The Servlet will handle
 * the post made by the SX container and set a session attribute that the
 * application can inspect to check if the user has registered SX.  
 *
 */
@WebServlet(name="ICEmobileSXRegistrationServlet", urlPatterns = {"/icemobile/*"}, loadOnStartup=1)
public class ICEmobileSXRegistrationServlet extends HttpServlet {

    
    private static final long serialVersionUID = 9038716950477146607L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ICEmobileSXRegistrationServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//do nothing
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    String userAgent = Utils.getUserAgent(request);
	    
	    if( userAgent != null && userAgent.contains(Constants.USER_AGENT_SX_PART)){
	        request.getSession().setAttribute(Constants.SESSION_KEY_SX_REGISTERED,Boolean.TRUE);
	        request.getSession().setAttribute(Constants.SESSION_KEY_AUX_UPLOAD,Boolean.TRUE);
	        request.getSession().setAttribute(Constants.USER_AGENT_COOKIE, Constants.USER_AGENT_COOKIE);
	        String cloudPushId = request.getParameter(Constants.CLOUD_PUSH_KEY);
	        if( cloudPushId != null ){
	            request.getSession().setAttribute(Constants.CLOUD_PUSH_KEY, cloudPushId);
	        }
	    }
	    	    
	}

}
