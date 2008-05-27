/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 *
 */

package com.icesoft.faces.webapp.xmlhttp;

import com.icesoft.faces.context.BridgeFacesContext;
import com.icesoft.faces.context.ViewListener;
import com.icesoft.faces.webapp.http.common.Configuration;
import com.icesoft.faces.webapp.http.core.SessionExpiredException;
import com.icesoft.faces.webapp.parser.ImplementationUtil;

import edu.emory.mathcs.backport.java.util.concurrent.Executors;
import edu.emory.mathcs.backport.java.util.concurrent.ExecutorService;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;

import javax.faces.FactoryFinder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The {@link PersistentFacesState} class allows an application to initiate
 * rendering asynchronously and independently of user interaction.
 * <p/>
 * Typical use is to obtain a {@link PersistentFacesState} instance in a managed
 * bean constructor and then use that instance for any relevant rendering
 * requests.
 * <p/>
 * Applications should obtain the <code>PersistentFacesState</code> instance
 * using the public static getInstance() method.  The recommended approach is to
 * call this method from a mangaged-bean constructor and use the instance
 * obtained for any {@link #render} requests.
 * <p/>
 * Application writers that make calls to the <code>execute</code> and
 * <code>render</code> methods should be aware of the potential for deadlock
 * conditions if this code is coupled with a third party framework
 * (eg. Spring Framework) that does its own resource locking.
 *
 */
public class PersistentFacesState implements Serializable {
    private static final Log log = LogFactory.getLog(PersistentFacesState.class);
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static InheritableThreadLocal localInstance = new InheritableThreadLocal();
    private BridgeFacesContext facesContext;
    private Lifecycle lifecycle;

    private ClassLoader renderableClassLoader = null;
    private boolean synchronousMode;
    private Collection viewListeners;
    private boolean disposed = false;

    public PersistentFacesState(BridgeFacesContext facesContext, Collection viewListeners, Configuration configuration) {
        //JIRA case ICE-1365
        //Save a reference to the web app classloader so that server-side
        //render requests work regardless of how they are originated.
        renderableClassLoader = Thread.currentThread().getContextClassLoader();

        this.facesContext = facesContext;
        this.viewListeners = viewListeners;
        this.synchronousMode = configuration.getAttributeAsBoolean("synchronousUpdate", false);
        LifecycleFactory factory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        this.lifecycle = factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        this.setCurrentInstance();
    }

    public void dispose() {
        disposed = true;
    }

    public void setCurrentInstance() {
        localInstance.set(this);
    }

     public static boolean isThreadLocalNull() {
        return localInstance.get() == null;
    } 

    /**
     * Obtain the <code>PersistentFacesState</code> instance appropriate for the
     * current context.  This is managed through InheritableThreadLocal
     * variables.  The recommended approach is to call this method from a
     * mangaged-bean constructor and use the instance obtained for any {@link
     * #render} requests.
     *
     * @return the PersistentFacesState appropriate for the calling Thread
     */
    public static PersistentFacesState getInstance() {
        return (PersistentFacesState) localInstance.get();
    }

    /**
     * Obtain the {@link PersistentFacesState} instance keyed by viewNumber from
     * the specified sessionMap. This API is not intended for application use.
     *
     * @param sessionMap session-scope parameters
     * @return the PersistentFacesState
     * @deprecated
     */
    public static PersistentFacesState getInstance(Map sessionMap) {
        return getInstance();
    }

    /**
     * Return the FacesContext associated with this instance.
     *
     * @return the FacesContext for this instance
     */
    public FacesContext getFacesContext() {
        return facesContext;
    }

    //todo: try to remove this method in the future
    public void setFacesContext(BridgeFacesContext facesContext) {
        this.facesContext = facesContext;
    }

    /**
     * Render the view associated with this <code>PersistentFacesState</code>.
     * The user's browser will be immediately updated with any changes.
     */
    public void render() throws RenderingException {
        if (disposed) {
            if (log.isDebugEnabled()) {
                log.debug("fatal render failure for viewNumber "
                        + facesContext.getViewNumber());
            }
            throw new FatalRenderingException(
                    "fatal render failure for viewNumber "
                            + facesContext.getViewNumber());
        }
        warn();
        facesContext.setCurrentInstance();
        setCurrentInstance();
        facesContext.setFocusId("");
        synchronized (facesContext) {
            try {
                lifecycle.render(facesContext);
                facesContext.release();
            } catch (Exception e) {
                Throwable throwable = e;
                while (throwable != null) {
                    if (throwable instanceof IllegalStateException ||
                        throwable instanceof SessionExpiredException) {

                        if (log.isDebugEnabled()) {
                            log.debug("fatal render failure for viewNumber "
                                    + facesContext.getViewNumber(), e);
                        }
                        throw new FatalRenderingException(
                                "fatal render failure for viewNumber "
                                        + facesContext.getViewNumber(), e);
                    } else {
                        throwable = throwable.getCause();
                    }
                }
                if (log.isDebugEnabled()) {
                    log.debug("transient render failure for viewNumber "
                            + facesContext.getViewNumber(), e);
                }
                throw new TransientRenderingException(
                        "transient render failure for viewNumber "
                                + facesContext.getViewNumber(), e);
            } finally {
                localInstance.set(null);
                facesContext.resetCurrentInstance();
            } 
        }
    }

    /**
     * Render the view associated with this <code>PersistentFacesState</code>.
     * This takes place on a separate thread to guard against potential deadlock
     * from calling {@link #render} during view rendering.
     */
    public void renderLater() {
        warn();
        executorService.execute(new RenderRunner());
    }

    public void renderLater(long miliseconds) {
        warn();
        executorService.execute(new RenderRunner(miliseconds));
    }

    /**
     * Redirect browser to a different URI. The user's browser will be
     * immediately redirected without any user interaction required.
     *
     * @param uri the relative or absolute URI.
     */
    public void redirectTo(String uri) {
        warn();
        try {
            facesContext.setCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            externalContext.redirect(uri);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Redirect browser to a different page. The redirect page is selected based
     * on the navigation rule. The user's browser will be immediately redirected
     * without any user interaction required.
     *
     * @param outcome the 'from-outcome' field in the navigation rule.
     */
    public void navigateTo(String outcome) {
        warn();
        try {
            facesContext.setCurrentInstance();
            facesContext.getApplication().getNavigationHandler()
                    .handleNavigation(facesContext,
                            facesContext.getViewRoot().getViewId(),
                            outcome);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Threads that used to server request/response cycles in an application
     * server are generally pulled from a pool.  Just before the thread is done
     * completing the cycle, we should clear any local instance variables to
     * ensure that they are not hanging on to any session references, otherwise
     * the session and their resources are not released.
     */
    public void release() {
        localInstance.set(null);
    }

    /**
     * Execute  the view associated with this <code>PersistentFacesState</code>.
     * This is typically followed immediatly by a call to
     * {@link PersistentFacesState#render}.
     * <p/>
     * This method obtains and releases the monitor on the FacesContext object.
     * If starting a JSF lifecycle causes 3rd party frameworks to perform locking
     * of their resources, releasing this monitor between the call to this method
     * and the call to {@link PersistentFacesState#render} can allow deadlocks
     * to occur. Use {@link PersistentFacesState#executeAndRender} instead
     */
    public void execute() throws RenderingException {
        if (disposed) {
            if (log.isDebugEnabled()) {
                log.debug("fatal render failure for viewNumber "
                        + facesContext.getViewNumber());
            }
            throw new FatalRenderingException(
                    "fatal render failure for viewNumber "
                            + facesContext.getViewNumber());
        }
        facesContext.setCurrentInstance();
        setCurrentInstance();
        synchronized (facesContext) {
            try {
                if (ImplementationUtil.isJSF12()) {
                    //facesContext.renderResponse() skips phase listeners
                    //in JSF 1.2, so do a full execute with no stale input
                    //instead
                    facesContext.getExternalContext()
                            .getRequestParameterMap().clear();
                } else {
                    facesContext.renderResponse();
                }
                lifecycle.execute(facesContext);

                // ICE-2478 JSF 1.2 will set this flag because our requestParameter
                // map is empty. We don't actually want to execute a lifecycle, we
                // just want the restoreView phase listeners to be executed for Seam.
                // However, we need to reset the 'just set' responseComplete flag
                // or our server push renders will not occur. 
                if (ImplementationUtil.isJSF12()) {
                    facesContext.resetResponseComplete();
                }
            } catch (Exception e) {
                Throwable throwable = e;
                while (throwable != null) {
                    if (throwable instanceof IllegalStateException ||
                        throwable instanceof SessionExpiredException) {

                        if (log.isDebugEnabled()) {
                            log.debug("fatal render failure for viewNumber "
                                    + facesContext.getViewNumber(), e);
                        }
                        throw new FatalRenderingException(
                                "fatal render failure for viewNumber "
                                        + facesContext.getViewNumber(), e);
                    } else {
                        throwable = throwable.getCause();
                    }
                }
                if (log.isDebugEnabled()) {
                    log.debug("transient render failure for viewNumber "
                            + facesContext.getViewNumber(), e);
                }
                throw new TransientRenderingException(
                        "transient render failure for viewNumber "
                                + facesContext.getViewNumber(), e);
            }  finally {
                localInstance.set(null);
                facesContext.resetCurrentInstance();
            }
        }
    }

    /**
     * Execute the JSF lifecycle (essentially calling <code> execute()</code> and
     * <code>render()</code> ) without releasing the FacesContext monitor
     * at any point between.
     * @since 1.7
     * 
     * @exception RenderingException if there is an exception rendering the View
     */
    public void executeAndRender() throws RenderingException {
        synchronized (facesContext) {
            execute();
            render();
        }
    }


    public ClassLoader getRenderableClassLoader() {
        return renderableClassLoader;
    }

    /**
     * @deprecated use {@link com.icesoft.faces.context.DisposableBean} interface instead
     */
    public void addViewListener(ViewListener listener) {
        if (!viewListeners.contains( listener ) ) {
            viewListeners.add(listener);
        }
    }

    private class RenderRunner implements Runnable {
        private long delay = 0;

        public RenderRunner() {
        }

        public RenderRunner(long miliseconds) {
            delay = miliseconds;
        }

        /**
         * <p>Not for application use. Entry point for {@link
         * PersistentFacesState#renderLater}.</p>
         */
        public void run() {
            try {
                Thread.sleep(delay);
                // JIRA #1377 Call execute before render.
                // #2459 use fully synchronized version internally.
                executeAndRender();
            } catch (RenderingException e) {
                if (log.isDebugEnabled()) {
                    log.debug("renderLater failed ", e);
                }
            } catch (InterruptedException e) {
                //ignore
            }
        }
    }

    private void warn() {
        if (synchronousMode) {
            log.warn("Running in 'synchronous mode'. The page updates were queued but not sent.");
        }
    }
}






