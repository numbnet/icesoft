package com.icesoft.faces.application;

import java.util.Iterator;

import javax.faces.FactoryFinder;
import javax.faces.application.Application;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;

import com.icesoft.faces.application.ViewRootStateManagerImpl;
import com.icesoft.faces.application.SingleCopyStateManagerImpl;

public class OverridePhaseListener implements PhaseListener {
	private boolean removed= false;
	private boolean initialized = false;

	public synchronized void afterPhase(PhaseEvent event) {
        if (!removed) {
            LifecycleFactory factory = (LifecycleFactory)
                  FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
            for(Iterator iter = factory.getLifecycleIds(); iter.hasNext(); ) {
                Lifecycle lifecycle = factory.getLifecycle((String) iter.next());
                lifecycle.removePhaseListener(this);
            }
            removed = true;
        }
	}

	public synchronized void beforePhase(PhaseEvent event) {
		if(!initialized){
			FacesContext facesContext = event.getFacesContext();
			Application application = facesContext.getApplication();
			StateManager stateManager = application.getStateManager();
			if(!(stateManager instanceof SingleCopyStateManagerImpl))  {
				application.setStateManager( new SingleCopyStateManagerImpl(
                    new ViewRootStateManagerImpl(stateManager)) );
			}
			initialized = true;
		}
	}


	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

}
