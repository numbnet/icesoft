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

package org.icefaces.push;

import org.icefaces.push.http.MimeTypeMatcher;
import org.icefaces.push.http.Request;
import org.icefaces.push.http.Server;
import org.icefaces.push.http.standard.PathDispatcherServer;
import org.icefaces.push.servlet.SessionDispatcher;

import javax.servlet.http.HttpSession;
import java.util.Observable;

public class SessionBoundServer implements Server {
    private final PathDispatcherServer dispatcher = new PathDispatcherServer();

    public SessionBoundServer(final HttpSession session, MonitorRunner monitor, final SessionDispatcher.Monitor sessionMonitor, CurrentContext context, Configuration configuration) {
        UpdateNotifier notifier = new UpdateNotifier() {
            public void onUpdate(Observer observer) {
                //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        Observable pingPongNotifier = new Observable() {
            @Override
            public void notifyObservers(Object arg) {
                setChanged();
                super.notifyObservers(arg);
            }
        };
        MimeTypeMatcher mimeTypeMatcher = new MimeTypeMatcher() {
            public String mimeTypeFor(String path) {
                return session.getServletContext().getMimeType(path);
            }
        };
        dispatcher.dispatchOn(".*icefaces\\/send\\-updated\\-views$", new SendUpdatedViews(session, pingPongNotifier, notifier, monitor, configuration));
        dispatcher.dispatchOn(".*icefaces\\/ping$", new ReceivePing(pingPongNotifier));
        dispatcher.dispatchOn(".*icefaces\\/resource\\/.*", new DynamicResourceDispatcher("icefaces/resource/", mimeTypeMatcher, sessionMonitor, session, configuration));
    }

    public void service(Request request) throws Exception {
        dispatcher.service(request);
    }

    public void shutdown() {
    }
}
