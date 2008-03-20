package com.icesoft.faces.webapp.http.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.faces.FacesException;
import javax.faces.context.FacesContext;

import com.icesoft.faces.webapp.http.servlet.SpringWebFlowInstantiationServlet;

public abstract class LifecycleExecutor {
    private static Log log = LogFactory.getLog(LifecycleExecutor.class);
    private static LifecycleExecutor lifecycleExecutor = null;

    public static LifecycleExecutor getLifecycleExecutor()  {
        init();
        return lifecycleExecutor;
    }

    public abstract void apply(FacesContext facesContext);

    private static void init()  {
        if (null != lifecycleExecutor)  {
            return;
        }
        Object flowExecutor = null;
        try {
            flowExecutor = SpringWebFlowInstantiationServlet.getFlowExecutor();
        } catch (Throwable t)  {
            if (log.isDebugEnabled()) {
                log.debug("SpringWebFlow unavailable ");
            }
        }
        if (null != flowExecutor)  {
            lifecycleExecutor = new SwfLifecycleExecutor();
        } else {
            lifecycleExecutor = new JsfLifecycleExecutor();
        }
    }
}