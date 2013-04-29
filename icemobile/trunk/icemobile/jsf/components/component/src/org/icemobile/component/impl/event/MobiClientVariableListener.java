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
package org.icemobile.component.impl.event;

import java.util.Map;

import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

import org.icefaces.mobi.utils.MobiJSFUtils;

public class MobiClientVariableListener implements SystemEventListener{
    
    private static final String KEY = "mobiClient";

    public boolean isListenerForSource(Object source) {
        return source instanceof UIViewRoot;
    }

    public void processEvent(SystemEvent evt) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot viewRoot = context.getViewRoot();
        Map viewMap = viewRoot.getViewMap();
        viewMap.put(KEY, MobiJSFUtils.getClientDescriptor());
    }


}
