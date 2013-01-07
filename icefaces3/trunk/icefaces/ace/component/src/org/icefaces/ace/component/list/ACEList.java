package org.icefaces.ace.component.list;

import org.icefaces.ace.event.ListSelectEvent;

import javax.el.MethodExpression;
import javax.faces.application.NavigationHandler;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;
import javax.faces.model.DataModel;
import java.util.*;

public class ACEList extends ListBase {
    @Override
    protected DataModel getDataModel() {
        return super.getDataModel();
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    @Override
    public Set<Object> getSelections() {
        Set<Object> set = super.getSelections();
        if (set == null) {
            set = new HashSet<Object>();
            setSelections(set);
        }
        return set;
    }

    @Override
    public void broadcast(FacesEvent event) throws AbortProcessingException {
        super.broadcast(event);

        FacesContext context = FacesContext.getCurrentInstance();
        String outcome = null;
        MethodExpression me = null;

        if (event instanceof ListSelectEvent) me = getSelectionListener();

        if (me != null) {
            if (!context.isValidationFailed()) {
                outcome = (String) me.invoke(context.getELContext(), new Object[] {event});
            }

            if (outcome != null) {
                NavigationHandler navHandler = context.getApplication().getNavigationHandler();
                navHandler.handleNavigation(context, null, outcome);
                context.renderResponse();
            }
        }
    }

    // References to immigrant objects are gathered immediately
    List<ImmigrationRecord> immigrants;

    public List<ImmigrationRecord> getImmigrants() {
        return immigrants;
    }

    public void setImmigrants(List<ImmigrationRecord> immigrants) {
        this.immigrants = immigrants;
    }

}
