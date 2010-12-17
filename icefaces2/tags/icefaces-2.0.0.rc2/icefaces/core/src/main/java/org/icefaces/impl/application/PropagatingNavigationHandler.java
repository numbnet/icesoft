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

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.faces.application.ConfigurableNavigationHandler;
import javax.faces.application.NavigationCase;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;

import org.icefaces.util.EnvUtils;
import org.icefaces.bean.ViewRetained;
import org.icefaces.impl.context.DOMResponseWriter;

/**
 * <p>
 *   The <code>PropagatingNavigationHandler</code>  ensures that objects in
 *   View Scope are available across navigation.
 * </p>
 */

public class PropagatingNavigationHandler extends ConfigurableNavigationHandler {
    private static Logger log = Logger.getLogger(PropagatingNavigationHandler.class.getName());
    NavigationHandler wrapped;

    public PropagatingNavigationHandler(NavigationHandler wrapped)  {
        this.wrapped = wrapped;
    }
    
    public void handleNavigation(FacesContext context, String fromAction, String outcome)  {
        if (!EnvUtils.isICEfacesView(context))  {
            wrapped.handleNavigation(context, fromAction, outcome);
            return;
        }

        UIViewRoot viewRoot;
        Map viewMap;

        viewRoot = context.getViewRoot();
        String fromViewId = viewRoot.getViewId();
        viewMap = viewRoot.getViewMap();
        HashMap propagated = new HashMap(viewMap);
        Iterator keys = propagated.keySet().iterator();
        while (keys.hasNext())  {
            Object key = keys.next();
            if ( !propagated.get(key).getClass()
                .isAnnotationPresent(ViewRetained.class) )  {
                keys.remove();
            } else {
                if (log.isLoggable(Level.FINE)) {
                    log.log(Level.FINE, "Propagating ViewScoped bean " + key);
                }
            }
        }
        Object oldDOM = viewMap.get(DOMResponseWriter.OLD_DOM);
        
        wrapped.handleNavigation(context, fromAction, outcome);
        
        viewRoot = context.getViewRoot();
        if (viewRoot.getViewId().equals(fromViewId))  {
            viewMap = viewRoot.getViewMap();
            viewMap.putAll(propagated);
            if (null != oldDOM)  {
                viewMap.put(DOMResponseWriter.OLD_DOM, oldDOM);
            }
        }
    }

    @Override
    public NavigationCase getNavigationCase(FacesContext context, String fromAction, String outcome) {
        if( wrapped instanceof ConfigurableNavigationHandler){
            return ((ConfigurableNavigationHandler)wrapped).getNavigationCase(context,fromAction,outcome);
        } else {
            log.warning( wrapped.toString() + " is not a ConfigurableNavigationHandler");
        }

        return null;
    }

    @Override
    public Map<String, Set<NavigationCase>> getNavigationCases() {
        if( wrapped instanceof ConfigurableNavigationHandler){
            return ((ConfigurableNavigationHandler)wrapped).getNavigationCases();
        } else {
            log.warning( wrapped.toString() + " is not a ConfigurableNavigationHandler");
        }
        return null;
    }
}
