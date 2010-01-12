package org.icepush.samples.icechat.ajax.servlet;

import javax.servlet.ServletContextEvent;

public class ServletContextListener implements
		javax.servlet.ServletContextListener {

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	@Override
	public void contextInitialized(ServletContextEvent sce) {

		sce.getServletContext().setAttribute("chatService", new SynchronizedChatServiceBean());

	}

}
