/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icemobile.impl.application;

import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.icepush.PushContext;

public class PushBackOffPhaseListener implements PhaseListener {
    private static final Logger log = Logger.getLogger(PushBackOffPhaseListener.class.getName());

    public void afterPhase(PhaseEvent phaseEvent)  {
    }

    public void beforePhase(PhaseEvent phaseEvent)  {
        FacesContext facesContext = phaseEvent.getFacesContext();
        ExternalContext externalContext = facesContext.getExternalContext();
        if (PhaseId.RENDER_RESPONSE == phaseEvent.getPhaseId()) {
            String browserID = getBrowserID(facesContext);
            if (null != browserID)  {
                try {
                    PushContext pushContext = PushContext.getInstance(
                        (ServletContext) externalContext.getContext());
                    pushContext.backOff(browserID, 300);
                } catch (Throwable t)  {
                    log.log(Level.FINE, "pushContext.backOff failed ", t);
                }
            }
        }
    }

    private static String getBrowserID(FacesContext facesContext)  {
        ExternalContext externalContext = facesContext.getExternalContext();
        Map cookies = externalContext.getRequestCookieMap();
        if (cookies != null) {
            Cookie cookie = (Cookie) cookies.get("ice.push.browser");
            if (null != cookie)  {
                return cookie.getValue();
            }
        }
        return null;
    }

    public PhaseId getPhaseId() {
        return PhaseId.RENDER_RESPONSE;
    }

}