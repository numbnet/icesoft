package org.icepush.gwt.server;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.icepush.PushContext;

/**
 * The listener that initializes the ICEpush Command Queue framework.
 * @author Patrick Wilson
 *
 */
public class PushCommandContextListener implements ServletContextListener{

	public void contextDestroyed(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		context.removeAttribute(ClientManager.class.getName());
	}

	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		ClientManager clientManager = new ClientManager();
		
		context.setAttribute(ClientManager.class.getName(), clientManager);
		ServerPushCommandContext commandContext = new ServerPushCommandContext(clientManager);
		context.setAttribute(ServerPushCommandContext.class.getName(), commandContext);
		
	}
}
