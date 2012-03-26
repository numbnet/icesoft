package com.icesoft.spring.security;

import org.springframework.webflow.definition.StateDefinition;
import org.springframework.webflow.execution.FlowExecutionListenerAdapter;
import org.springframework.webflow.execution.RequestContext;
import org.springframework.webflow.execution.View;
import org.icefaces.impl.event.BridgeSetup;
import org.icefaces.impl.application.WindowScopeManager;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *  WebflowListener to check to duplicate PhaseListener junk.
 */
public class WebflowListener extends FlowExecutionListenerAdapter {

    /**
	 * Called when a view is about to render in a view-state, before any render actions are executed.
	 * @param context the current flow request context
	 * @param view the view that is about to render
	 * @param viewState the current view state
	 */
	public void viewRendering(RequestContext context, View view, StateDefinition viewState) {


        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        String viewId = BridgeSetup.getViewID(ec);
        if (viewId == null ) {
            WindowScopeManager.determineWindowID( fc );
            BridgeSetup.assignViewID( ec );
        }
    }
}
