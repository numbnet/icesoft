package com.icesoft.util;

import com.icesoft.faces.context.BridgeFacesContext;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Need to ensure thread local variables are nulled out on outbound code paths
 * (servlet and server push + wherever else!) to allow GC. ThreadLocal references can hold onto
 * (Server thread Count * 2 + Server push Render executor pool thread count )
 * objects each with their own UIComponent tree in memory.
 *
 * This utility class just allows clients to check without having compile time
 * includes to core classes, and really only needs to be done at development time.
 *
 */
public class ThreadLocalUtility {

    private static final Log log = LogFactory.getLog(ThreadLocalUtility.class);

    /**
     * Check to see if applicable ThreadLocals are cleared. Very verbose, so
     * call only if at suitable log levels.
     * 
     * @param location an indication where test is being performed
     */
    public static void checkThreadLocals(String location) {

        if (!BridgeFacesContext.isThreadLocalNull()) {
            log.error("BridgeFacesContext ThreadLocal is NON-NULL: " + location);
        }  else {
            log.debug("BridgeFacesContext ThreadLocal is OK: " + location );
        }

        if (!PersistentFacesState.isThreadLocalNull()) {
            log.error("PersistentFacesState ThreadLocal is NON-NULL: " + location);
        } else {
            log.debug("PersistentFacesState ThreadLocal is OK: " + location );
        }
    } 
}
