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

package org.icefaces.impl.application;

import org.icefaces.application.SessionExpiredException;
import org.icefaces.util.EnvUtils;
import org.icepush.PushContext;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.ApplicationFactory;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.logging.Logger;

public class SessionExpiredListener implements HttpSessionListener {

    private static Logger log = Logger.getLogger(SessionExpiredListener.class.getName());

    public void sessionCreated(HttpSessionEvent httpSessionEvent) {
    }

    public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {

        FacesContext fc = FacesContext.getCurrentInstance();

        //If there is no FacesContext, then the session likely timed out of it's own accord rather
        //then being invalidated programmatically as part of a JSF lifecycle.  In that case,
        //we can't put an exception into the queue.
        if (fc != null) {
            Application app = fc.getApplication();
            if (app == null) {
                ApplicationFactory factory = (ApplicationFactory) FactoryFinder.getFactory(FactoryFinder.APPLICATION_FACTORY);
                app = factory.getApplication();
            }

            ExceptionQueuedEventContext ctxt =
                    new ExceptionQueuedEventContext(fc, new SessionExpiredException("Session has expired"));
            app.publishEvent(fc, ExceptionQueuedEvent.class, ctxt);
        }

        //If the session is destroyed and ICEpush is available, we can request a push request immediately
        //which should result in a SessionExpiredException being sent to the client.
        if (EnvUtils.isICEpushPresent()) {
            HttpSession session = httpSessionEvent.getSession();
            ServletContext servletContext = session.getServletContext();
            PushContext pushContext = PushContext.getInstance(servletContext);
            pushContext.push(session.getId());
        }
    }

}
