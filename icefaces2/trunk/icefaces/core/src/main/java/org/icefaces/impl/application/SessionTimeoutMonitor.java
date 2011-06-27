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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package org.icefaces.impl.application;

import org.icefaces.util.EnvUtils;

import javax.faces.application.ResourceHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class SessionTimeoutMonitor extends SessionAwareResourceHandlerWrapper {
    private static final Logger Log = Logger.getLogger(SessionTimeoutMonitor.class.getName());
    private ResourceHandler handler;

    public SessionTimeoutMonitor(ResourceHandler handler) {
        this.handler = handler;
    }

    public ResourceHandler getWrapped() {
        return handler;
    }

    public boolean isSessionAwareResourceRequest(FacesContext context) {
        if (!EnvUtils.isStrictSessionTimeout(context)) {
            return handler.isResourceRequest(context);
        }

        ExternalContext externalContext = context.getExternalContext();
        Map sessionMap = externalContext.getSessionMap();
        Long lastAccessTime = (Long) sessionMap.get(SessionTimeoutMonitor.class.getName());
        boolean isPushRelatedRequest = EnvUtils.isPushRequest(context);
        if (lastAccessTime == null || !isPushRelatedRequest) {
            lastAccessTime = System.currentTimeMillis();
            sessionMap.put(SessionTimeoutMonitor.class.getName(), System.currentTimeMillis());
        }

        Object session = externalContext.getSession(false);
        int maxInactiveInterval;
        if (EnvUtils.instanceofPortletSession(session)) {
            maxInactiveInterval = ((javax.portlet.PortletSession) session).getMaxInactiveInterval();
        } else {
            maxInactiveInterval = ((javax.servlet.http.HttpSession) session).getMaxInactiveInterval();
        }

        if (System.currentTimeMillis() - lastAccessTime > maxInactiveInterval * 1000) {
            sessionMap.remove(SessionTimeoutMonitor.class.getName());
            externalContext.invalidateSession();
        }

        return handler.isResourceRequest(context);
    }

    public void handleSessionAwareResourceRequest(FacesContext context) throws IOException {
        handler.handleResourceRequest(context);
    }
}
