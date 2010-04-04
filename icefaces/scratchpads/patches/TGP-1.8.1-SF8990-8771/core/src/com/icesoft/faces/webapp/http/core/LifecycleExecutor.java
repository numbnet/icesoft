package com.icesoft.faces.webapp.http.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import com.icesoft.faces.env.SpringWebFlowInstantiationServlet;

import java.util.Map;

public abstract class LifecycleExecutor {
    private static Log log = LogFactory.getLog(LifecycleExecutor.class);

    private static String JSF_EXEC = "JSF Lifecyle Executor";

    public static LifecycleExecutor getLifecycleExecutor(FacesContext context)  {

        // ICE-5212 Don't reuse SwfLifecycleExector objects, they are not threadsafe
        if (!isJsfLifecycle(context))  {
            Map appMap = context.getExternalContext().getApplicationMap();
            if (!appMap.containsKey(JSF_EXEC))  {
                try {
                    LifecycleExecutor executor = new SwfLifecycleExecutor();
                    return executor;
                } catch (Throwable t)  {
                    //ClassNotFound Exception or Error variant, fall back to
                    //standard JSF
                    if (log.isDebugEnabled()) {
                        log.debug("SpringWebFlow unavailable and is disabled for this application ");
                    }
                    appMap.put(JSF_EXEC,JSF_EXEC);
                }
            }
        }
        return  new JsfLifecycleExecutor();
    }

    public abstract void apply(FacesContext facesContext);

    private static boolean isJsfLifecycle( FacesContext facesContext) {
        Object request = facesContext.getExternalContext().getRequest();
        if (request instanceof HttpServletRequest)  {
            String requestURI = ((HttpServletRequest) request).getRequestURI();
            int slashIndex = requestURI.lastIndexOf("/");
            int dotIndex = requestURI.lastIndexOf(".");
            return (slashIndex < dotIndex);
        }
        return false;
    }

    /**
     * This might have a different implementation later.
     * @param facesContext
     * @return A JsfLifecycleExecutor
     */
    public LifecycleExecutor getJsfLifecycleExecutor(FacesContext facesContext) {
        return new JsfLifecycleExecutor( );
    }
}