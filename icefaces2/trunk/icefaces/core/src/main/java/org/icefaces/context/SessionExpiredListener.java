/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 */

package org.icefaces.context;

import javax.faces.FactoryFinder;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.logging.Logger;
import java.util.logging.Level;

@WebListener
public class SessionExpiredListener implements HttpSessionListener {

    private static Logger log = Logger.getLogger(SessionExpiredListener.class.getName());
    private Application app;

    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
    }

    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

        //If there is no FacesContext, then the session likely timed out of it's own accord rather
        //then being invalidated programmatically.  In that case, we don't handle it here.
        FacesContext fc = FacesContext.getCurrentInstance();
        if (fc == null) {
            if (log.isLoggable(Level.INFO)) {
                log.log(Level.INFO, "session destroyed but FacesContext is not available");
            }
            return;
        }

        if( app == null ){
            ApplicationFactory factory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
            app = factory.getApplication();
        }

        log.log(Level.INFO, "app is " + app);

        ExceptionQueuedEventContext ctxt =
                new ExceptionQueuedEventContext(fc, new SessionExpiredException("session has expired") );
        app.publishEvent(fc, ExceptionQueuedEvent.class, ctxt);

        log.log(Level.INFO, "published " + ctxt);
    }
}
