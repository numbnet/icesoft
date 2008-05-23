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
 * The Original Code is ICEfaces 1.7 open source software code, released
 * April 15, 2007. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2007 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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

package org.icefaces.application;

import java.util.Map;
import java.util.Hashtable;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.lang.reflect.Constructor;

import javax.servlet.http.HttpSession;

import javax.faces.context.FacesContext;
import javax.faces.context.ExternalContext;

import com.icesoft.faces.util.event.servlet.ContextEventRepeater;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import com.icesoft.faces.webapp.http.servlet.MainSessionBoundServlet;
import com.icesoft.faces.webapp.http.servlet.SessionDispatcher;
import com.icesoft.faces.context.View;
import com.icesoft.faces.webapp.xmlhttp.RenderingException;
import com.icesoft.faces.async.render.ContextDestroyedListener;
import com.icesoft.faces.async.render.Disposable;

/**
 * The {@link SessionRenderer} class allows an application to initiate
 * rendering asynchronously and independently of user interaction for
 * a session or group of sessions.  When a session is rendered, all windows
 * or views that a particular user has open in their session will be updated
 * via Ajax Push with current the current application state.  This is a
 * preliminary introduction of this API and it is subject to change.
 * <p/>
 *
 */
public class SessionRenderer  {
    private static Hashtable renderGroups = new Hashtable();
    private static PortableExecutor executor = new PortableExecutor(1);

    //Not yet implemented
    public static String ALL_SESSIONS = "SessionRenderer.ALL_SESSIONS";

    //listeners are held weakly, so need a reference here
    private static ContextDestroyedListener shutdownListener;

    static {
        shutdownListener = new ContextDestroyedListener(
            new SessionRendererDisposer() );
        ContextEventRepeater.addListener(shutdownListener);
    }

    /**
     * Add the current session to the specified group. Groups of sessions
     * are automatically garbage collected when all member sessions have
     * become invalid.
     *
     * @param groupName the name of the group to add the current session to
     */
    public static synchronized void addCurrentSession(String groupName)  {
        Set group = (Set) renderGroups.get(groupName);
        if (null == group)  {
            group = new HashSet();
            renderGroups.put(groupName, group);
        }
        ExternalContext externalContext = FacesContext.getCurrentInstance()
                .getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        group.add(new SessionHolder(session));
    }

    /**
     * Remove the current session from the specified group. 
     *
     * @param groupName the name of the group to remove the current session from
     */
    public static synchronized void removeCurrentSession(String groupName)  {
        Set group = (Set) renderGroups.get(groupName);
        if (null == group)  {
            return;
        }
        ExternalContext externalContext = FacesContext.getCurrentInstance()
                .getExternalContext();
        HttpSession session = (HttpSession) externalContext.getSession(false);
        group.remove(new SessionHolder(session));
        removeGroupIfEmpty(groupName);
    }

/*
    public static void removeSession(Object session, String groupName)  {
    }

    public static void addSession(Object session, String groupName)  {
    }
*/

    /**
     * Render the specified group of sessions by performing the JavaServer Faces
     * execute and render lifecycle phases.  If a FacesContext is in the 
     * scope of the current thread scope, the current view will not be
     * asynchronously rendered
     * (it is already rendered as a result of the user event being 
     * processed).  For more fine-grained control
     * use the RenderManager API.
     *
     * @param groupName the name of the group of sessions to render.
     */
    public static void render(String groupName)  {
        Set group = (Set) renderGroups.get(groupName);
        if (null == group)  {
            return;
        }

        PersistentFacesState suppressedView = PersistentFacesState.getInstance();
        if (null == FacesContext.getCurrentInstance())  {
            //invocation from non-JSF thread should not suppress current view
            suppressedView = null;
        }

        Iterator sessionHolders = group.iterator();
        while (sessionHolders.hasNext())  {
            SessionHolder sessionHolder = (SessionHolder) sessionHolders.next();
            HttpSession session = (HttpSession) sessionHolder.getSession();
            if ( (null != session) && (isValid(session)) )  {
                MainSessionBoundServlet mainServlet = (MainSessionBoundServlet)
                        SessionDispatcher.getSingletonSessionServlet(session);
                Iterator views = mainServlet.getViews().values().iterator();
                
                while (views.hasNext())  {
                    View view = (View) views.next();
                    final PersistentFacesState viewState = 
                            view.getPersistentFacesState();
                    if (viewState != suppressedView)  {
                        executor.execute(new RenderRunner(viewState));
                    }
                }
                
           } else {
                //remove any null or expired sessions
                sessionHolders.remove();
                removeGroupIfEmpty(groupName);
            }
        }
    }

    static boolean isValid(HttpSession session)  {
        try {
            Object test = session.getAttribute("isTheSessionValid?");
            return true;
        } catch (Exception e)  {
        }
        return false;
    }

    //this method will not be called frequently enough to completely remove
    //all empty groups.  We can either run this during notification of session
    //shutdown or have a ReferenceQueue that we poll() in addCurrentSession()
    //(if addCurrentSession() is never called, the leak is not significant)
    static synchronized void removeGroupIfEmpty(String groupName)  {
        Set group = (Set) renderGroups.get(groupName);
        if (null == group)  {
            return;
        }
        if (group.isEmpty())  {
            renderGroups.remove(groupName);
        }
    }

/*  Begin with reserved String names, but we may want to add a SessionGroup
    with add/remove features
    public static void render(SessionGroup group) {
    }
*/
    //these should probably be on the SessionGroup
/*
    public static void removeSession(Object session, String groupName)  {
    }

    public static void addSession(Object session, String groupName)  {
    }
*/
//either introduce the SessionGroup with "cluster" attribute or
//introduce a method that gives the naming scheme for which
//groups are clustered enableClusterGroup("cluster/*")

    static void shutdown()  {
        executor.shutdown();
    }
}

class RenderRunner implements Runnable {
    PersistentFacesState persistentState = null;

    public RenderRunner(PersistentFacesState persistentState)  {
        this.persistentState = persistentState;
    }

    public void run()  {
        try  {
            persistentState.executeAndRender();
        } catch (RenderingException e)  {
            //it's up to our View infrastructure to remove dead views
        }
    }
}

/* An extension of this class should be moved to a common set of wrappers
   that allow us to run without backport-concurrent on JDK 1.5
*/
class PortableExecutor  {
    private int poolSize;
    
    private static Class executorClass;
    private static Constructor executorConstructor;
    private static Method executorExecute;
    private static Method executorShutdown;
    private Object executor = null;
    private static String backportClassName = 
        "edu.emory.mathcs.backport.java.util.concurrent.ScheduledThreadPoolExecutor";
    private static String utilClassName = 
        "java.util.concurrent.ScheduledThreadPoolExecutor";

    static {
        try {
            executorClass = Class.forName(utilClassName);
        } catch (Throwable t)  { }
        if (null == executorClass)  {
            try {
                executorClass = Class.forName(backportClassName);
            } catch (Throwable t)  { }
        }
        try {
            executorConstructor = executorClass
                    .getConstructor(new Class[] {int.class});
            executorExecute = executorClass
                    .getMethod("execute", new Class[] {Runnable.class});
            executorShutdown = executorClass
                    .getMethod("shutdown", new Class[] {});
        } catch (Throwable t)  {
            t.printStackTrace();
        }
        
    }

    public PortableExecutor(int poolSize)  {
        this.poolSize = poolSize;
        try {
            executor = executorConstructor.newInstance(
                    new Object[] {new Integer(poolSize)} );
        } catch (Throwable t)  {
            t.printStackTrace();
        }
    }
    
    public void execute(Runnable job)  {
        try {
            executorExecute.invoke(executor, new Object[] {job});
        } catch (Throwable t)  {
            t.printStackTrace();
        }
    }
    
    public void shutdown()  {
        try {
            executorShutdown.invoke(executor, null);
        } catch (Throwable t)  {
            t.printStackTrace();
        }
    }
}

class SessionHolder   {
    WeakReference sessionReference = null;
    String sessionId = null;

    public SessionHolder(HttpSession session)  {
        sessionReference = new WeakReference(session);
        sessionId = session.getId();
    }
    
    public Object getSession()  {
        return sessionReference.get();
    }

    public String getId()  {
        return sessionId;
    }

    public boolean equals(Object o)  {
        if (o instanceof SessionHolder)  {
            return (sessionId.equals(((SessionHolder) o).getId()));
        }
        return false;
    }
    
    public int hashCode()  {
        return sessionId.hashCode();
    }
}

class SessionRendererDisposer implements Disposable  {
    public void dispose()  {
        SessionRenderer.shutdown();
    }
}
