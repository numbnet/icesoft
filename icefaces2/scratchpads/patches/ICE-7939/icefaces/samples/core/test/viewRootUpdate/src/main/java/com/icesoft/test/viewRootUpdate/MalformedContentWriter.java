package com.icesoft.test.viewRootUpdate;

import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import java.io.IOException;

public class MalformedContentWriter implements SystemEventListener {

    public void processEvent(SystemEvent systemEvent) throws AbortProcessingException {
        FacesContext context = FacesContext.getCurrentInstance();
        UIViewRoot root = context.getViewRoot();
        root.addComponentResource(context, new MalformedMarkup(), "body");
    }

    public boolean isListenerForSource(Object o) {
        return true;
    }

    private static class MalformedMarkup extends UIOutput {
        private MalformedMarkup() {
            setTransient(true);
        }

        public void encodeBegin(FacesContext context) throws IOException {
            ResponseWriter writer = context.getResponseWriter();
            writer.write("<br>");
        }
    }
}
