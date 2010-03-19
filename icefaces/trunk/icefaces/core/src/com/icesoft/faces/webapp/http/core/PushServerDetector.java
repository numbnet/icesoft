/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */

package com.icesoft.faces.webapp.http.core;

import com.icesoft.faces.async.common.PushServerAdaptingServlet;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.common.Request;
import com.icesoft.faces.webapp.http.common.Server;
import com.icesoft.faces.webapp.http.servlet.CoreMessageService;
import com.icesoft.util.MonitorRunner;

import java.util.Collection;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PushServerDetector
implements Server {
    private static final Log LOG = LogFactory.getLog(PushServerDetector.class);
    private static final Object LOCK = new Object();

    private static ServerFactory factory;
    private static ServerFactory fallbackFactory;

    private Server server;

    public PushServerDetector(
        final HttpSession httpSession, final String icefacesID,
        final Collection synchronouslyUpdatedViews,
        final ViewQueue allUpdatedViews, final MonitorRunner monitorRunner,
        final Configuration configuration,
        final CoreMessageService coreMessageService,
        final PageTest pageTest) {

        if (factory == null) {
            synchronized (LOCK) {
                if (factory == null) {
                    if (coreMessageService != null) {
                        factory = new PushServerAdaptingServletFactory();
                        fallbackFactory = new SendUpdatedViewsFactory();
                    } else {
                        factory = new SendUpdatedViewsFactory();
                    }
                }
            }
        }
        server =
            factory.newServer(
                httpSession, icefacesID, synchronouslyUpdatedViews,
                allUpdatedViews, monitorRunner, configuration,
                coreMessageService, pageTest);
    }

    public void service(final Request request) throws Exception {
        server.service(request);
    }

    public void shutdown() {
        server.shutdown();
    }

    private static interface ServerFactory {
        public Server newServer(
            final HttpSession httpSession, final String icefacesID,
            final Collection synchronouslyUpdatedViews,
            final ViewQueue allUpdatedViews,
            final MonitorRunner monitorRunner,
            final Configuration configuration,
            final CoreMessageService coreMessageService,
            final PageTest pageTest);
    }

    private static class PushServerAdaptingServletFactory
    implements ServerFactory {
        public Server newServer(
            final HttpSession httpSession, final String icefacesID,
            final Collection synchronouslyUpdatedViews,
            final ViewQueue allUpdatedViews,
            final MonitorRunner monitorRunner,
            final Configuration configuration,
            final CoreMessageService coreMessageService,
            final PageTest pageTest) {

            try {
                return
                    new PushServerAdaptingServlet(
                        httpSession,
                        icefacesID,
                        synchronouslyUpdatedViews,
                        allUpdatedViews,
                        configuration,
                        coreMessageService);
            } catch (Exception exception) {
                // Possible exceptions: MessageServiceException
                LOG.warn(
                    "Failed to adapt to Push Server environment. Falling " +
                        "back to Push environment.",
                    exception);
                synchronized (LOCK) {
                    factory = fallbackFactory;
                    fallbackFactory = null;
                }
                return
                    factory.newServer(
                        httpSession, icefacesID, synchronouslyUpdatedViews,
                        allUpdatedViews, monitorRunner, configuration,
                        coreMessageService, pageTest);
            }
        }
    }

    private static class SendUpdatedViewsFactory
    implements ServerFactory {
        public Server newServer(
            final HttpSession httpSession, final String icefacesID,
            final Collection synchronouslyUpdatedViews,
            final ViewQueue allUpdatedViews,
            final MonitorRunner monitorRunner,
            final Configuration configuration,
            final CoreMessageService coreMessageService,
            final PageTest pageTest) {

            return
                new SendUpdatedViews(
                    icefacesID, synchronouslyUpdatedViews, allUpdatedViews,
                    monitorRunner, configuration, pageTest);
        }
    }
}
