/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
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
