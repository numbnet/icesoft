package org.icefaces.impl.event;

import org.icefaces.util.EnvUtils;

import javax.faces.component.UIForm;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.IOException;

public class FixViewState implements SystemEventListener {
    private static final String ID_SUFFIX = "_fixviewstate";

    public void processEvent(final SystemEvent event) throws AbortProcessingException {
        final UIForm form = (UIForm) ((ComponentSystemEvent) event).getComponent();
        final String id = form.getId() + ID_SUFFIX;

        UIOutput output = new ScriptWriter();
        output.setTransient(true);
        output.setId(id);
        form.getChildren().add(0, output);
    }

    public boolean isListenerForSource(final Object source) {
        return EnvUtils.isICEfacesView(FacesContext.getCurrentInstance()) && source instanceof UIForm;
    }

    private static class ScriptWriter extends UIOutputWriter {
        public void encode(ResponseWriter writer, FacesContext context) throws IOException {
            String clientID = getClientId(context);
            writer.startElement("span", this);
            writer.writeAttribute("id", clientID, null);
            if (context.isPostback()) {
                writer.startElement("script", this);
                writer.writeAttribute("type", "text/javascript", null);
                String viewState = context.getApplication().getStateManager().getViewState(context);
                writer.writeText("ice.fixViewState('" + clientID + "', '" + viewState + "');", null);
                writer.endElement("script");
            }
            writer.endElement("span");
        }
    }
}