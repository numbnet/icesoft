package org.icepush.integration.common.notify;

import org.icepush.PushContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServlet;
import javax.servlet.ServletContext; 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationServlet extends HttpServlet {
    ServletContext servletContext;

    public void init(ServletConfig servletConfig) throws ServletException {
	this.servletContext = servletConfig.getServletContext();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	// Find the bean;
	final String notifier = (String)request.getParameter("notifier");
	BasicGroupNotifier notifierBean = (BasicGroupNotifier)request.getAttribute(notifier);
	if (notifierBean == null) {
	    HttpSession session = request.getSession();
	    notifierBean = (BasicGroupNotifier)session.getAttribute(notifier);
	    if (notifierBean == null) {
		notifierBean = (BasicGroupNotifier)(servletContext.getAttribute(notifier));
	    } 
	} 

	if (notifierBean == null) {
	    throw( new ServletException("Could not find notifier bean " + notifier));
	} 

	// Notifier builds the response;
	notifierBean.processRequest(request,response);
    }

    public void destroy() {

    }
}
