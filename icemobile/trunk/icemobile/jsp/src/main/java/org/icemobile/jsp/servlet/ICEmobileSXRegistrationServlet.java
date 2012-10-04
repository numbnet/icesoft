package org.icemobile.jsp.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.icemobile.util.Constants;
import org.icemobile.util.Utils;

@WebServlet(name="ICEmobileSXRegistrationServlet", urlPatterns = {"/icemobile/*"}, loadOnStartup=1)
public class ICEmobileSXRegistrationServlet extends HttpServlet {

    
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
	    System.out.println("*******************************");
	    String userAgent = Utils.getUserAgent(request);
	    System.out.println("ua="+userAgent);
	    System.out.println("***servlet "+request.getSession().getId());
	    if( request.getCookies() != null ){
	        for( Cookie cookie : request.getCookies()){
	            System.out.println("cookie: "+cookie.getName()+"="+cookie.getValue());
	        }
	    }
	    
	    if( userAgent != null && userAgent.contains(Constants.USER_AGENT_SX_PART)){
	        request.getSession().setAttribute(Constants.SESSION_KEY_SX,Boolean.TRUE);
	        request.getSession().setAttribute(Constants.USER_AGENT_COOKIE, Constants.USER_AGENT_COOKIE);
	        System.out.println("*********** setting sx session key");
	        String cloudPushId = request.getParameter(Constants.CLOUD_PUSH_KEY);
	        if( cloudPushId != null ){
	            request.getSession().setAttribute(Constants.CLOUD_PUSH_KEY, cloudPushId);
	            System.out.println("*** setting cloud push id="+cloudPushId);
	        }
	    }
	    else{
	        System.out.println("ua does not contain sx");
	    }
	    System.out.println("*******************************");
	    
	}

}
