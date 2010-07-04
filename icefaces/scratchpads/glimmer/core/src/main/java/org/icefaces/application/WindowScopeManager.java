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
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WindowScopeManager {
    public static final String ScopeName = "window";
    private static final Logger Log = Logger.getLogger(WindowScopeManager.class.getName());
    private static String seed = Integer.toString(new Random().nextInt(1000), 36);

    public static ScopeMap lookupWindowScope(FacesContext context) {
        String id = lookupAssociatedWindowID(context.getExternalContext().getRequestMap());
        return (ScopeMap) getState(context).windowScopedMaps.get(id);
    }

    public static synchronized String determineWindowID(FacesContext context) {
        State state = getState(context);
        ExternalContext externalContext = context.getExternalContext();
        String id = externalContext.getRequestParameterMap().get("ice.window");
        try {
            for (Object scopeMap : new ArrayList(state.disposedWindowScopedMaps)) {
                ScopeMap map = (ScopeMap) scopeMap;
                if (!map.getId().equals(id)) {
                    map.discardIfExpired(context);
                }
            }
        } catch (Throwable e) {
            Log.log(Level.FINE, "Failed to remove window scope map", e);
        }

        Map requestMap = externalContext.getRequestMap();
        if (id == null) {
            ScopeMap scopeMap;
            if (state.disposedWindowScopedMaps.isEmpty()) {
                scopeMap = new ScopeMap(context);
            } else {
                scopeMap = (ScopeMap) state.disposedWindowScopedMaps.removeFirst();
            }
            scopeMap.activate(state);
            associateWindowID(scopeMap.id, requestMap);
            return scopeMap.id;
        } else {
            if (state.windowScopedMaps.containsKey(id)) {
                associateWindowID(id, requestMap);
                return id;
            } else {
                //this must be a postback request while the window is reloading or redirecting
                for (Object disposedScopeMap : new ArrayList(state.disposedWindowScopedMaps)) {
                    ScopeMap scopeMap = (ScopeMap) disposedScopeMap;
                    if (scopeMap.getId().equals(id)) {
                        scopeMap.activate(state);
                        associateWindowID(id, requestMap);
                        return id;
                    }
                }
                throw new RuntimeException("Unknown window scope ID: " + id);
            }
        }
    }

    private static synchronized String generateID() {
        return seed + Long.toString(System.currentTimeMillis(), 36);
    }

    public static synchronized void disposeWindow(FacesContext context, String id) {
        State state = getState(context);
        state.disactivatedWindowNotifier.notifyObservers(id);
        ScopeMap scopeMap = (ScopeMap) state.windowScopedMaps.get(id);
        //verify if the ScopeMap is present
        //it's possible to have dispose-window request arriving after an application restart or re-deploy
        if (scopeMap != null) {
            scopeMap.disactivate(state);
        }
    }

    public static class ScopeMap extends HashMap {
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
            State state = getState(facesContext);
            new ExternalContextConfiguration("org.icefaces", facesContext.getExternalContext());
            if (System.currentTimeMillis() > timestamp + state.expirationPeriod) {
                boolean processingEvents = facesContext.isProcessingEvents();
                try {
                    facesContext.setProcessingEvents(true);
                    ScopeContext context = new ScopeContext(ScopeName, this);
                    facesContext.getApplication().publishEvent(facesContext, PreDestroyCustomScopeEvent.class, context);
                } finally {
                    state.disposedWindowScopedMaps.remove(this);
                    facesContext.setProcessingEvents(processingEvents);
                }
            }
        }

        private void activate(State state) {
            state.activatedWindowNotifier.notifyObservers(id);
            state.windowScopedMaps.put(id, this);
        }

        private void disactivate(State state) {
            timestamp = System.currentTimeMillis();
            state.disposedWindowScopedMaps.addLast(state.windowScopedMaps.remove(id));
        }
    }

    public static void onActivatedWindow(HttpSession session, Configuration configuration, Observer observer) {
        getState(session, configuration).activatedWindowNotifier.addObserver(observer);
    }

    public static void onDisactivatedWindow(HttpSession session, Configuration configuration, Observer observer) {
        getState(session, configuration).disactivatedWindowNotifier.addObserver(observer);
    }

    private static String lookupAssociatedWindowID(Map requestMap) {
        return (String) requestMap.get(WindowScopeManager.class.getName());
    }

    private static void associateWindowID(String id, Map requestMap) {
        requestMap.put(WindowScopeManager.class.getName(), id);
    }

    private static State getState(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        Map sessionMap = externalContext.getSessionMap();
        State state = (State) sessionMap.get(WindowScopeManager.class.getName());
        if (state == null) {
            ExternalContextConfiguration configuration = new ExternalContextConfiguration("org.icefaces", externalContext);
            state = new State(configuration.getAttributeAsLong("windowScopeExpiration", 1000));
            sessionMap.put(WindowScopeManager.class.getName(), state);
        }

        return state;
    }

    private static State getState(HttpSession session, Configuration configuration) {
        State state = (State) session.getAttribute(WindowScopeManager.class.getName());
        if (state == null) {
            state = new State(configuration.getAttributeAsLong("windowScopeExpiration", 1000));
            session.setAttribute(WindowScopeManager.class.getName(), state);
        }

        return state;
    }

    private static class ReadyObservable extends Observable {
        public synchronized void notifyObservers(Object object) {
            setChanged();
            super.notifyObservers(object);
            clearChanged();
        }
    }

    private static class State implements Externalizable {
        private transient Observable activatedWindowNotifier = new ReadyObservable();
        private transient Observable disactivatedWindowNotifier = new ReadyObservable();
        private HashMap windowScopedMaps = new HashMap();
        private LinkedList disposedWindowScopedMaps = new LinkedList();
        public long expirationPeriod;

        public State() {
        }

        private State(long expirationPeriod) {
            this.expirationPeriod = expirationPeriod;
        }

        public void writeExternal(ObjectOutput out) throws IOException {
            out.writeObject(windowScopedMaps);
            out.writeObject(disposedWindowScopedMaps);
            out.writeLong(expirationPeriod);
        }

        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
            windowScopedMaps = (HashMap) in.readObject();
            disposedWindowScopedMaps = (LinkedList) in.readObject();
            expirationPeriod = in.readLong();

            activatedWindowNotifier = new ReadyObservable();
            disactivatedWindowNotifier = new ReadyObservable();
        }
    }
}
