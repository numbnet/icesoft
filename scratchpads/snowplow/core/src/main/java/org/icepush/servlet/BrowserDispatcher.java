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

package org.icepush.servlet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

public abstract class BrowserDispatcher implements PseudoServlet {
    private static final Logger log = Logger.getLogger(BrowserDispatcher.class.getName());
    private final Map sessionBoundServers = new HashMap();

    public BrowserDispatcher() {
        //todo: discard unused browserIDs by monitoring their activity
    }

    public void service(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String browserID = getBrowserIDFromCookie(request);
        checkSession(browserID);
        lookupServer(browserID).service(request, response);
    }

    public void shutdown() {
        Iterator i = sessionBoundServers.values().iterator();
        while (i.hasNext()) {
            PseudoServlet servlet = (PseudoServlet) i.next();
            servlet.shutdown();
        }
    }

    protected abstract PseudoServlet newServer(String browserID) throws Exception;

    protected void checkSession(String browserID) throws Exception {
        synchronized (sessionBoundServers) {
            if (!sessionBoundServers.containsKey(browserID)) {
                sessionBoundServers.put(browserID, this.newServer(browserID));
            }
        }
    }

    protected PseudoServlet lookupServer(final String browserID) {
        return (PseudoServlet) sessionBoundServers.get(browserID);
    }

    private static String getBrowserIDFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("ice.push.browser".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
