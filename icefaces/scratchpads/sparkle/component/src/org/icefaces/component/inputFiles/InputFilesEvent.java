package org.icefaces.component.inputFiles;

import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.component.UIComponent;

public class InputFilesEvent extends FacesEvent {
    public InputFilesEvent(UIComponent src) {
        super(src);
    }
    
    public void processListener(FacesListener facesListener) {
    }

    public boolean isAppropriateListener(FacesListener facesListener) {
        return false;
    }
}
