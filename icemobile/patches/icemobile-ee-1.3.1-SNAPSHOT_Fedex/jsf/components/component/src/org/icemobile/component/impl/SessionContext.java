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
package org.icemobile.component.impl;

import java.util.Map;
import javax.faces.context.FacesContext;
import java.io.Serializable;

public class SessionContext implements Serializable  {
    private static String SESSION_CONTEXT = SessionContext.class.getName();
    private String userAgent;

    public static SessionContext getSessionContext()  {
        Map sessionMap = FacesContext.getCurrentInstance()
                .getExternalContext().getSessionMap();
        Object sessionContext = sessionMap.get(SESSION_CONTEXT);
        if (null == sessionContext)  {
            sessionContext = new SessionContext();
            sessionMap.put(SESSION_CONTEXT, sessionContext);
        }
        return (SessionContext) sessionContext;
    }
    
    public String getUserAgent()  {
        if (null != userAgent)  {
            return userAgent;
        }
        Map headerMap = FacesContext.getCurrentInstance()
                .getExternalContext().getRequestHeaderMap();
        String currentUserAgent = (String) headerMap.get("User-Agent");
        //marker value indicating valid User-Agent
        if (currentUserAgent.contains("Mozilla"))  {
            userAgent = currentUserAgent;
        }
        //must return something
        return currentUserAgent;
    }
    
}
