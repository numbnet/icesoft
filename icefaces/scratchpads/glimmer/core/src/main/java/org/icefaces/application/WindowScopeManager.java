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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package org.icefaces.application;

import org.icefaces.push.Configuration;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.PostConstructCustomScopeEvent;
import javax.faces.event.PreDestroyCustomScopeEvent;
import javax.faces.event.ScopeContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

//todo: factor out window manager that can be used to manage the window scope 
public class WindowScopeManager {
    public static final String ScopeName = "window";
    private static final Logger Log = Logger.getLogger(WindowScopeManager.class.getName());
    private static final CurrentScopeThreadLocal CurrentScope = new CurrentScopeThreadLocal();
    private HashMap windowScopedMaps = new HashMap();
    private LinkedList disposedWindowScopedMaps = new LinkedList();
    private long expirationPeriod;
    private Observable activatedWindowNotifier = new ReadyObservable();
    private Observable disactivatedWindowNotifier = new ReadyObservable();
    private String seed;

    public WindowScopeManager(Configuration configuration) {
        expirationPeriod = configuration.getAttributeAsLong("windowScopeExpiration", 1000);
        seed = Integer.toString(new Random().nextInt(1000), 36);
    }

    public ScopeMap lookupWindowScope() {
        return (ScopeMap) windowScopedMaps.get(CurrentScope.lookup());
    }

    public synchronized String determineWindowID(FacesContext context) {
        String id = context.getExternalContext().getRequestParameterMap().get("ice.window");
        try {
            for (Object scopeMap : new ArrayList(disposedWindowScopedMaps)) {
                ScopeMap map = (ScopeMap) scopeMap;
                if (!map.getId().equals(id)) {
                    map.discardIfExpired(context);
                }
            }
        } catch (Throwable e) {
            Log.log(Level.FINE, "Failed to remove window scope map", e);
        }

        if (id == null) {
            ScopeMap scopeMap;
            if (disposedWindowScopedMaps.isEmpty()) {
                scopeMap = new ScopeMap(context);
            } else {
                scopeMap = (ScopeMap) disposedWindowScopedMaps.removeFirst();
            }
            scopeMap.activate();
            return scopeMap.id;
        } else {
            if (windowScopedMaps.containsKey(id)) {
                CurrentScope.associate(id);
                return id;
            } else {
                //this must be a postback request while the window is reloading or redirecting
                for (Object disposedScopeMap : new ArrayList(disposedWindowScopedMaps)) {
                    ScopeMap scopeMap = (ScopeMap) disposedScopeMap;
                    if (scopeMap.getId().equals(id)) {
                        scopeMap.activate();
                        return id;
                    }
                }
                throw new RuntimeException("Unknown window scope ID: " + id);
            }
        }
    }

    private synchronized String generateID() {
        return seed + Long.toString(System.currentTimeMillis(), 36);
    }

    public synchronized void disposeWindow(String id) {
        disactivatedWindowNotifier.notifyObservers(id);
        ScopeMap scopeMap = (ScopeMap) windowScopedMaps.get(id);
        //verify if the ScopeMap is present
        //it's possible to have dispose-window request arriving after an application restart or re-deploy
        if (scopeMap != null) {
            scopeMap.disactivate();
        }
    }

    public class ScopeMap extends HashMap {
        private String id = generateID();
        private long timestamp = -1;

        public String getId() {
            return id;
        }

        public ScopeMap(FacesContext facesContext) {
            boolean processingEvents = facesContext.isProcessingEvents();
            try {
                facesContext.setProcessingEvents(true);
                ScopeContext context = new ScopeContext(ScopeName, this);
                facesContext.getApplication().publishEvent(facesContext, PostConstructCustomScopeEvent.class, context);
            } finally {
                facesContext.setProcessingEvents(processingEvents);
            }
        }

        private void discardIfExpired(FacesContext facesContext) {
            if (System.currentTimeMillis() > timestamp + expirationPeriod) {
                boolean processingEvents = facesContext.isProcessingEvents();
                try {
                    facesContext.setProcessingEvents(true);
                    ScopeContext context = new ScopeContext(ScopeName, this);
                    facesContext.getApplication().publishEvent(facesContext, PreDestroyCustomScopeEvent.class, context);
                } finally {
                    disposedWindowScopedMaps.remove(this);
                    facesContext.setProcessingEvents(processingEvents);
                }
            }
        }

        private void activate() {
            activatedWindowNotifier.notifyObservers(id);
            CurrentScope.associate(id);
            windowScopedMaps.put(id, this);
        }

        private void disactivate() {
            timestamp = System.currentTimeMillis();
            disposedWindowScopedMaps.addLast(windowScopedMaps.remove(id));
        }
    }

    public void onActivatedWindow(Observer observer) {
        activatedWindowNotifier.addObserver(observer);
    }

    public void onDisactivatedWindow(Observer observer) {
        disactivatedWindowNotifier.addObserver(observer);
    }

    public synchronized static WindowScopeManager lookup(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        //ICE-5281:  We require that a session be available at this point and it may not have
        //           been created otherwise.
        Object session = externalContext.getSession(true);
        Map sessionMap = externalContext.getSessionMap();
        ExternalContextConfiguration configuration = new ExternalContextConfiguration("org.icefaces", externalContext);
        return lookup(sessionMap, configuration);
    }

    public synchronized static WindowScopeManager lookup(Map sessionMap, Configuration configuration) {
        Object o = sessionMap.get(WindowScopeManager.class.getName());
        final WindowScopeManager manager;
        if (o == null) {
            manager = new WindowScopeManager(configuration);
            sessionMap.put(WindowScopeManager.class.getName(), manager);
        } else {
            manager = (WindowScopeManager) o;
        }

        return manager;
    }

    public synchronized static WindowScopeManager lookup(HttpSession session, Configuration configuration) {
        Object o = session.getAttribute(WindowScopeManager.class.getName());
        final WindowScopeManager manager;
        if (o == null) {
            manager = new WindowScopeManager(configuration);
            session.setAttribute(WindowScopeManager.class.getName(), manager);
        } else {
            manager = (WindowScopeManager) o;
        }

        return manager;
    }

    private static class CurrentScopeThreadLocal extends ThreadLocal {
        String lookup() {
            return (String) get();
        }

        void associate(String windowID) {
            set(windowID);
        }
    }

    private static class ReadyObservable extends Observable {
        public synchronized void notifyObservers(Object o) {
            setChanged();
            super.notifyObservers(o);
            clearChanged();
        }
    }
}
