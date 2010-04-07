package org.icefaces.event;

import org.icefaces.application.WindowScopeManager;
import org.icefaces.push.servlet.SessionExpiredException;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WindowScopeSetup implements PhaseListener {
    private static Logger log = Logger.getLogger(WindowScopeSetup.class.getName());

    public PhaseId getPhaseId() {
        return PhaseId.RESTORE_VIEW;
    }

    public void beforePhase(PhaseEvent event) {
        final FacesContext context = FacesContext.getCurrentInstance();

        if (context.getExternalContext().getRequestMap().containsKey(SessionExpiredException.class.getName())) {
            throw new SessionExpiredException("User session has expired");
        }

        try {
            WindowScopeManager.lookup(context).determineWindowID(context);
        } catch (Exception e) {
            log.log(Level.WARNING, "Unable to set up WindowScope ", e);
        }
    }

    public void afterPhase(PhaseEvent event) {
    }
}
