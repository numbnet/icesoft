package org.icefaces.component.inputFiles;

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

@ManagedBean(name="inputFilesLoader", eager=true)
@ApplicationScoped
public class InputFilesLoader {
    public InputFilesLoader() {
//System.out.println("InputFilesLoader");
        Application application = FacesContext.getCurrentInstance().getApplication();
        application.subscribeToEvent(PostAddToViewEvent.class, null, new InputFilesFormSubmit());

        PhaseListener phaseListener = new InputFilesPhaseListener();
        LifecycleFactory lifecycleFactory = (LifecycleFactory)
            FactoryFinder.getFactory(FactoryFinder.LIFECYCLE_FACTORY);
        for (Iterator ids = lifecycleFactory.getLifecycleIds(); ids.hasNext();) {
            Lifecycle lifecycle = lifecycleFactory.getLifecycle(
                (String) ids.next());
            lifecycle.addPhaseListener(phaseListener);
//System.out.println("InputFilesLoader  lifecycle: " + lifecycle);
        }
    }
    
    @ManagedProperty(value="name")
    private String name;
    
    public void setName(String n) {
        name = n;
//System.out.println("InputFilesLoader.setName()  name: " + name);
    }
    
    public String getName() {
        return name;
    }
}
