/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */


package org.icefaces.component.fileentry;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedProperty;
import javax.faces.application.Application;
import javax.faces.event.PostAddToViewEvent;
import javax.faces.event.PhaseListener;
import javax.faces.context.FacesContext;
import javax.faces.lifecycle.LifecycleFactory;
import javax.faces.lifecycle.Lifecycle;
import javax.faces.FactoryFinder;
import java.util.Iterator;

@ManagedBean(name="fileEntryLoader", eager=true)
@ApplicationScoped
public class FileEntryLoader {
    public FileEntryLoader() {
//System.out.println("FileEntryLoader");
        Application application =
            FacesContext.getCurrentInstance().getApplication();
        application.subscribeToEvent(PostAddToViewEvent.class, null,
            new FileEntryFormSubmit());

        PhaseListener phaseListener = new FileEntryPhaseListener();
        LifecycleFactory lifecycleFactory = (LifecycleFactory)
            FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        for (Iterator ids = lifecycleFactory.getLifecycleIds(); ids.hasNext();) {
            Lifecycle lifecycle = lifecycleFactory.getLifecycle(
                (String) ids.next());
            // Remove all other PhaseListeners, and re-add them after
            // FileEntryPhaseListener, since they'll likely rely on it having 
            // setup a valid environment for JSF. Eg: WindowScopeManager.
            PhaseListener[] phaseListeners = lifecycle.getPhaseListeners();
            for (PhaseListener otherPhaseListener : phaseListeners) {
                lifecycle.removePhaseListener(otherPhaseListener);
            }
            lifecycle.addPhaseListener(phaseListener);
            for (PhaseListener otherPhaseListener : phaseListeners) {
                lifecycle.addPhaseListener(otherPhaseListener);
            }
        }
    }
    
    @ManagedProperty(value="name")
    private String name;

    public void setName(String n) {
        name = n;
//System.out.println("FileEntryLoader.setName()  name: " + name);
    }
    
    public String getName() {
        return name;
    }
}
