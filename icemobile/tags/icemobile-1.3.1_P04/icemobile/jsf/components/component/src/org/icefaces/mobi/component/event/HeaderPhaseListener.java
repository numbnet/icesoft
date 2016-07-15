/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package org.icefaces.mobi.component.event;

import javax.annotation.PostConstruct;
import javax.faces.FactoryFinder;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.lifecycle.LifecycleFactory;
import javax.servlet.http.HttpServletResponse;

@ManagedBean(eager=true)
@ApplicationScoped
public class HeaderPhaseListener implements PhaseListener {
    
    /*
     * Programmatic initialization as PhaseListener annotation not available in JSF 2.1
     */
    @PostConstruct
    private void init() {
        LifecycleFactory factory = (LifecycleFactory) FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        Lifecycle lifecycle = factory.getLifecycle(LifecycleFactory.DEFAULT_LIFECYCLE);
        lifecycle.addPhaseListener(new HeaderPhaseListener());
    }

    public void afterPhase(PhaseEvent pe) {
    }

    public void beforePhase(PhaseEvent event) { 
        final FacesContext facesContext = event.getFacesContext(); 
        final HttpServletResponse response = (HttpServletResponse) facesContext.getExternalContext().getResponse();
        response.setContentType("text/html; charset=UTF-8");
        response.addHeader("X-UA-Compatible", "IE=edge,chrome=1"); 
    }

    public PhaseId getPhaseId() { 
        return PhaseId.RENDER_RESPONSE; 
    }

}
