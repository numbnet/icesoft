package org.icepush.samples.icechat.ajax.servlet;

import javax.servlet.ServletContextEvent;

public class ServletContextListener implements
		javax.servlet.ServletContextListener {

	public void contextDestroyed(ServletContextEvent sce) {
	}

	public void contextInitialized(ServletContextEvent sce) {

		sce.getServletContext().setAttribute("chatService", new SynchronizedChatServiceBean());

	}

}
