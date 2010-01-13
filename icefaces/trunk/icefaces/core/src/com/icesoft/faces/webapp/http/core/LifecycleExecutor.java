package com.icesoft.faces.webapp.http.core;


import javax.servlet.http.HttpServletRequest;
import javax.faces.context.FacesContext;

/**
 * This class refactored for ICE-5212. The SwfLifecycleExector class wasn't threadsafe
 * so don't cache a single copy. 
 */
public abstract class LifecycleExecutor {

    public static LifecycleExecutor getLifecycleExecutor(FacesContext context)  {

        // ICE-5212 Don't reuse SwfLifecycleExector objects, they are not threadsafe
        if (!isJsfLifecycle(context))  {
            return new com.icesoft.faces.webapp.http.core.SwfLifecycleExecutor();
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