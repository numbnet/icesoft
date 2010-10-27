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

import org.icefaces.impl.push.SessionViewManager;
import org.icefaces.util.EnvUtils;
import org.icefaces.bean.WindowDisposed;

import javax.annotation.PreDestroy;
import java.lang.reflect.Method;
import javax.faces.FactoryFinder;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ResourceHandlerWrapper;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.component.UIViewRoot;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.event.PostConstructCustomScopeEvent;
import javax.faces.event.PreDestroyCustomScopeEvent;
import javax.faces.event.ScopeContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.http.HttpSession;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Observable;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WindowScopeManager extends ResourceHandlerWrapper implements PhaseListener {
    public static final String ScopeName = "window";
    private static final Logger log = Logger.getLogger(WindowScopeManager.class.getName());
    private static String seed = Integer.toString(new Random().nextInt(1000), 36);

    private ResourceHandler wrapped;
    private PhaseListener invokeBeforeSelf;

    public WindowScopeManager(ResourceHandler wrapped) {
        this.wrapped = wrapped;
        //insane bit of code to call addPhaseListener(this)
        //if determineWindowID can run lazily, this PhaseListener approach
        //may not be necessary
        LifecycleFactory factory = (LifecycleFactory)
                FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle = factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        lifecycle.addPhaseListener(this);
    }

    public ResourceHandler getWrapped() {
        return wrapped;
    }

    public void setInvokeBeforeSelf(PhaseListener invoke) {
        invokeBeforeSelf = invoke;
    }
    
    public void handleResourceRequest(FacesContext facesContext) throws IOException {
        //a null session cannot have any window scope beans
        if (null == facesContext.getExternalContext().getSession(false))  {
            wrapped.handleResourceRequest(facesContext);
            return;
        }
        ExternalContext externalContext = facesContext.getExternalContext();
        Map parameters = externalContext.getRequestParameterMap();
        if (isDisposeWindowRequest(parameters)) {
            String windowID = (String) parameters.get("ice.window");
            disposeWindow(facesContext, windowID);
            if (EnvUtils.isICEpushPresent()) {
                try {
                    String[] viewIDs = externalContext.getRequestParameterValuesMap().get("ice.view");
                    for (int i = 0; i < viewIDs.length; i++) {
                        SessionViewManager.removeView(facesContext, viewIDs[i]);
                    }
                } catch (RuntimeException e) {
                    //missing ice.view parameters means that none of the views within the page
                    //was registered with PushRenderer before page unload
                    log.log(Level.FINE, "Exception during dispose-window ", e);
                }
            }
        } else {
            wrapped.handleResourceRequest(facesContext);
        }
    }

    public boolean isResourceRequest(FacesContext facesContext) {
        ExternalContext externalContext = facesContext.getExternalContext();
        Map parameters = externalContext.getRequestParameterMap();
        if (isDisposeWindowRequest(parameters)) {
            return true;
        }
        return wrapped.isResourceRequest(facesContext);
    }

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    public void beforePhase(PhaseEvent event) {
        if (invokeBeforeSelf != null) {
            invokeBeforeSelf.beforePhase(event);
        }
        
        final FacesContext context = FacesContext.getCurrentInstance();

        try {
            ExternalContext externalContext = context.getExternalContext();
            //ICE-5281:  We require that a session be available at this point and it may not have
            //           been created otherwise.
            //WindowScope should not cause session creation until the time objects need to be stored
            //in window scope
            //Object session = externalContext.getSession(true);
            WindowScopeManager.determineWindowID(context);
        } catch (Exception e) {
            log.log(Level.FINE, "Unable to set up WindowScope ", e);
        }
    }

    public void afterPhase(PhaseEvent event) {
        if (invokeBeforeSelf != null) {
            invokeBeforeSelf.afterPhase(event);
        }
    }

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
            log.log(Level.FINE, "Failed to remove window scope map", e);
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

    private static boolean isDisposeWindowRequest(Map parameters) {
        return "ice.dispose.window".equals(parameters.get("ice.submit.type"));
    }

    private static synchronized String generateID() {
        return seed + Long.toString(System.currentTimeMillis(), 36);
    }

    public static synchronized void disposeWindow(FacesContext context, String id) {
        State state = getState(context);
        //TODO remove Servlet dependency
        ScopeMap scopeMap = (ScopeMap) state.windowScopedMaps.get(id);
        //verify if the ScopeMap is present
        //it's possible to have dispose-window request arriving after an application restart or re-deploy
        if (scopeMap != null) {
            scopeMap.disactivate(state);
        }
        disposeViewScopeBeans(context);
    }

    private static void disposeViewScopeBeans(FacesContext facesContext)  {
        //Unfortunately we must execute the lifecycle to get to the viewMap
        LifecycleFactory factory = (LifecycleFactory)
                FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle = factory.getLifecycle(
                LifecycleFactory.DEFAULT_LIFECYCLE );
        lifecycle.execute(facesContext);

        UIViewRoot viewRoot = facesContext.getViewRoot();
        if (null == viewRoot)  {
            return;
        }
        Map viewMap = viewRoot.getViewMap();
        Iterator keys = viewMap.keySet().iterator();
        while (keys.hasNext())  {
            Object key = keys.next();
            Object object = viewMap.get(key);
            if ( object.getClass().isAnnotationPresent(WindowDisposed.class) )  {
                keys.remove();
                callPreDestroy(object);
                if (log.isLoggable(Level.FINE)) {
                    log.log(Level.FINE, "Closing window disposed ViewScoped bean " + key);
                }
            } 
        }
    }

    private static void callPreDestroy(Object object)  {
        Class theClass = object.getClass();
        try {
            while (null != theClass)  {
                Method[] methods = object.getClass().getDeclaredMethods();
                for (Method method : methods)  {
                    if (method.isAnnotationPresent(PreDestroy.class))  {
                            method.setAccessible(true);
                            method.invoke(object);
                            return;
                    }
                }
                theClass = theClass.getSuperclass();
            }
        } catch (Exception e)  {
            log.log(Level.WARNING, "Failed to invoke PreDestroy on " + theClass, e);
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
            state.windowScopedMaps.put(id, this);
        }

        private void disactivate(State state) {
            timestamp = System.currentTimeMillis();
            state.disposedWindowScopedMaps.addLast(state.windowScopedMaps.remove(id));
        }
    }

    public static String lookupAssociatedWindowID(Map requestMap) {
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
            state = new State(EnvUtils.getWindowScopeExpiration(context));
            sessionMap.put(WindowScopeManager.class.getName(), state);
        }

        return state;
    }

    private static State getState(HttpSession session) {
        State state = (State) session.getAttribute(WindowScopeManager.class.getName());
        if (state == null) {
            state = new State(EnvUtils.getWindowScopeExpiration(FacesContext.getCurrentInstance()));
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
