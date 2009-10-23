package com.icesoft.net.messaging;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class DefaultMessageServiceContextListener implements ServletContextListener {

    private static DefaultMessageService messageService;

    public DefaultMessageServiceContextListener() {
        System.out.println("DefaultMessageServiceContextListener: created " + this);
    }

    public static void setMessageService(DefaultMessageService messageService) {
        System.out.println("DefaultMessageServiceContextListener.setMessageService: " + messageService);
        DefaultMessageServiceContextListener.messageService = messageService;
    }

    public void contextInitialized(ServletContextEvent servletContextEvent) {
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        if (messageService != null) {
            System.out.println("DefaultMessageServiceContextListener.contextDestroyed: requesting stop...");
            messageService.close();
            System.out.println("DefaultMessageServiceContextListener.contextDestroyed: stopped");
        }
    }
}