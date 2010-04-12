package com.icesoft.faces.async.render;

import org.icefaces.application.PortableRenderer;
import org.icefaces.application.PushRenderer;

import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

public class SessionRenderer extends PushRenderer {
    private static PortableRenderer portableRenderer;

    public static void render(String groupName) {
        if (FacesContext.getCurrentInstance() == null) {
            portableRenderer.render(groupName);
        } else {
            PushRenderer.render(groupName);
        }
    }

    public static class StartupListener implements SystemEventListener {
        public void processEvent(SystemEvent event) throws AbortProcessingException {
            portableRenderer = PushRenderer.getPortableRenderer(FacesContext.getCurrentInstance());
        }

        public boolean isListenerForSource(Object source) {
            return true;
        }
    }
}
