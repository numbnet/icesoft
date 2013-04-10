/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package org.icefaces.impl.event;

import org.icefaces.util.EnvUtils;
import org.icefaces.util.JavaScriptRunner;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.List;
import java.util.Map;

public class SessionExpiryWarning implements SystemEventListener {
    private long intervalBeforeExpiry;

    public SessionExpiryWarning() {
        intervalBeforeExpiry = EnvUtils.getWarnBeforeExpiryInterval(FacesContext.getCurrentInstance()) * 1000;
    }

    public void processEvent(final SystemEvent event) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        Map session = externalContext.getSessionMap();

        long lastAccessTime;
        Object o = session.get(SessionExpiryWarning.class.getName());
        if (o == null) {
            lastAccessTime = System.currentTimeMillis();
            saveLastAccessTime(session);
        } else {
            lastAccessTime = (Long) o;
        }

        long maxInactiveInterval = externalContext.getSessionMaxInactiveInterval() * 1000;
        long currentTime = System.currentTimeMillis();
        long deltaTime =  lastAccessTime + maxInactiveInterval - intervalBeforeExpiry - currentTime;

        UIComponent bodyComponent = (UIComponent) event.getSource();
        String code = EnvUtils.isPushRequest(context) ? "" : ("ice.resetSessionExpiryTimeout(" + deltaTime + "," + intervalBeforeExpiry + ");");
        UIComponent c = new ScriptWriter(bodyComponent, code, "reset_session_expiry_timeout");
        bodyComponent.getChildren().add(c);

        saveLastAccessTime(session);
    }

    public boolean isListenerForSource(final Object source) {
        FacesContext context = FacesContext.getCurrentInstance();
        return EnvUtils.isICEfacesView(context) &&
                "javax.faces.Body".equals(((UIComponent) source).getRendererType()) &&
                intervalBeforeExpiry > -1;
    }

    private static void saveLastAccessTime(Map session) {
        session.put(SessionExpiryWarning.class.getName(), System.currentTimeMillis());
    }
}
