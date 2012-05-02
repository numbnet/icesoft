package org.icefaces.mobi.event;

import org.icefaces.mobi.component.accordion.Accordion;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;


public class AccordionPaneChangeEvent extends FacesEvent {

    private Accordion tab;
    public AccordionPaneChangeEvent(UIComponent uiComponent, Accordion tab){
        super(uiComponent);
        this.tab = tab;
    }
    @Override
    public boolean isAppropriateListener(FacesListener faceslistener) {
        return false;
    }

    @Override
    public void processListener(FacesListener faceslistener) {
        throw new UnsupportedOperationException();
    }

    public Accordion getTab(){
        return tab;
    }
}
