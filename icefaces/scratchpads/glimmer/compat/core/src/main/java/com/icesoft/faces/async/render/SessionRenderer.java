package com.icesoft.faces.async.render;

import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.logging.Logger;

public class SessionRenderer extends PushRenderer {
    private static Logger log = Logger.getLogger(SessionRenderer.class.getName());
    //avoid referencing PortableRenderer class in static context so that application still works when ICEpush not present
    private static Object portableRenderer;

    public static void render(String groupName) {
        if (portableRenderer != null) {
            if (FacesContext.getCurrentInstance() == null) {
                ((PortableRenderer) portableRenderer).render(groupName);
            } else {
                PushRenderer.render(groupName);
            }
        }
    }

    public static class StartupListener implements SystemEventListener {
        public void processEvent(SystemEvent event) throws AbortProcessingException {
            try {
                portableRenderer = PushRenderer.getPortableRenderer(FacesContext.getCurrentInstance());
            } catch (NoClassDefFoundError e) {
                log.info("ICEpush library missing. Cannot enable push functionality.");
            }
        }

        public boolean isListenerForSource(Object source) {
            return true;
        }
    }
}
