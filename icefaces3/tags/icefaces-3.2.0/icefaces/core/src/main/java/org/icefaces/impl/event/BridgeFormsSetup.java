package org.icefaces.impl.event;

import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.util.List;

public class BridgeFormsSetup implements SystemEventListener {
    public void processEvent(final SystemEvent event) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        UIComponent c = (UIComponent) event.getSource();
        String viewID = BridgeSetup.getViewID(context.getExternalContext());
        //add the form used by ice.retrieveUpdate function to retrieve the updates
        //use viewID and '-retrieve-update' suffix as element ID
        addNewTransientForm(viewID + "-retrieve-update", c.getChildren());
        //add the form used by ice.singleSubmit function for submitting event data
        //use viewID and '-single-submit' suffix as element ID
        addNewTransientForm(viewID + "-single-submit", c.getChildren());
    }

    public boolean isListenerForSource(final Object source) {
        return EnvUtils.isICEfacesView(FacesContext.getCurrentInstance()) && (source instanceof UIComponent) &&
                "javax.faces.Body".equals(((UIComponent) source).getRendererType());
    }

    private static void addNewTransientForm(String id, List<UIComponent> parent) {
        UIForm uiForm = new ShortIdForm();
        uiForm.setTransient(true);
        uiForm.setId(id);
        parent.add(uiForm);
    }

    public static class ShortIdForm extends UIForm {
        //ID is assigned uniquely by ICEpush so no need to prepend
        public String getClientId(FacesContext context) {
            return getId();
        }
    }
}
