package org.icefaces.application;

import org.icefaces.push.Configuration;

import javax.faces.context.ExternalContext;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WindowScopeManager {
    private static final Logger Log = Logger.getLogger(WindowScopeManager.class.getName());
    private static final CurrentScopeThreadLocal CurrentScope = new CurrentScopeThreadLocal();
    private HashMap windowScopedMaps = new HashMap();
    private LinkedList disposedWindowScopedMaps = new LinkedList();
    private long expirationPeriod;

    public WindowScopeManager(Configuration configuration) {
        expirationPeriod = configuration.getAttributeAsLong("windowScopeExpiration", 600);
    }

    public ScopeMap lookupWindowScope() {
        return CurrentScope.lookup();
    }

    public synchronized String determineWindowID(String id) {
        try {
            for (Object scopeMap : new ArrayList(disposedWindowScopedMaps)) {
                ((ScopeMap) scopeMap).discardIfExpired();
            }
        } catch (Throwable e) {
            Log.log(Level.FINE, "Failed to remove window scope map", e);
        }

        if (id == null) {
            ScopeMap scopeMap = disposedWindowScopedMaps.isEmpty() ? new ScopeMap() : (ScopeMap) disposedWindowScopedMaps.removeFirst();
            CurrentScope.associate(scopeMap);
            windowScopedMaps.put(scopeMap.id, scopeMap);
            return scopeMap.id;
        } else {
            if (windowScopedMaps.containsKey(id)) {
                CurrentScope.associate((ScopeMap) windowScopedMaps.get(id));
                return id;
            } else {
                throw new RuntimeException("Unknown window scope ID: " + id);
            }
        }
    }

    private static String generateID() {
        return String.valueOf(System.currentTimeMillis());
    }

    public void disposeWindow(String id) {
        ((ScopeMap) windowScopedMaps.get(id)).dispose();
    }

    public class ScopeMap extends HashMap {
        private String id = generateID();
        private long timestamp = -1;

        public ScopeMap() {
        }

        private void discardIfExpired() {
            if (System.currentTimeMillis() > timestamp + expirationPeriod) {
                disposedWindowScopedMaps.remove(this);
            }
        }

        private void dispose() {
            timestamp = System.currentTimeMillis();
            disposedWindowScopedMaps.addLast(windowScopedMaps.remove(id));
        }
    }

    public static WindowScopeManager lookup(Map session, ExternalContext externalContext) {
        Object o = session.get(WindowScopeManager.class.getName());
        final WindowScopeManager manager;
        if (o == null) {
            manager = new WindowScopeManager(new ExternalContextConfiguration("org.icefaces", externalContext));
            session.put(WindowScopeManager.class.getName(), manager);
        } else {
            manager = (WindowScopeManager) o;
        }

        return manager;
    }

    public static WindowScopeManager lookup(HttpSession session, Configuration configuration) {
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
        ScopeMap lookup() {
            return (ScopeMap) get();
        }

        void associate(ScopeMap map) {
            set(map);
        }
    }
}
