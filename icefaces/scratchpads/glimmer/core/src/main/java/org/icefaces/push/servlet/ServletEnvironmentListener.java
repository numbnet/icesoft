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
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2009 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
*/

package org.icefaces.push.servlet;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class ServletEnvironmentListener implements ServletContextListener, HttpSessionListener {
    //todo: get rid of this li`stener when BROWSERID is introduced
    org.icepush.servlet.ServletEnvironmentListener ICEpushListener = new org.icepush.servlet.ServletEnvironmentListener();

    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ICEpushListener.contextInitialized(servletContextEvent);
    }

    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        ICEpushListener.contextDestroyed(servletContextEvent);
        ICEfacesResourceHandler.notifyContextShutdown(servletContextEvent.getServletContext());
        ICEpushResourceHandler.notifyContextShutdown(servletContextEvent.getServletContext());
    }

    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
        ICEpushListener.sessionCreated(httpSessionEvent);
        SessionDispatcher.notifySessionStartup(httpSessionEvent.getSession());
    }

    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
        ICEpushListener.sessionDestroyed(httpSessionEvent);
        SessionDispatcher.notifySessionShutdown(httpSessionEvent.getSession());
    }
}
