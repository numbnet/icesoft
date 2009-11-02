package com.icesoft.net.messaging;

import org.apache.commons.logging.LogFactory;
import org.apache.commons.logging.Log;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DefaultMessageServiceContextListener implements ServletContextListener {

    private static Log log = LogFactory.getLog("com.icesoft.net.messaging.DefaultMessageServiceContextListener");

    private static DefaultMessageService messageService;

    public DefaultMessageServiceContextListener() {
    }

    public static void setMessageService(DefaultMessageService messageService) {
        DefaultMessageServiceContextListener.messageService = messageService;
    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (messageService != null) {
            if (log.isDebugEnabled()) {
                log.debug("requesting close of message service");
            }
            messageService.close();
        }
    }
}