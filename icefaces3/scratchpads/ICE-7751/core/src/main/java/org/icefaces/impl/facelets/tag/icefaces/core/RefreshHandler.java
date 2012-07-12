package org.icefaces.impl.facelets.tag.icefaces.core;

import org.icefaces.impl.event.BridgeSetup;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PreRenderComponentEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.TagAttribute;
import javax.faces.view.facelets.TagConfig;
import javax.faces.view.facelets.TagHandler;
import java.io.IOException;

public class RefreshHandler extends TagHandler {
    private long interval;
    private long duration;
    private boolean disabled;
    private boolean receivingPreRenderEvents;

    public RefreshHandler(TagConfig config) {
        super(config);

        TagAttribute intervalAttribute = getAttribute("interval");
        TagAttribute durationAttribute = getAttribute("duration");
        TagAttribute disabledAttribute = getAttribute("disabled");

        interval = intervalAttribute == null ? 10000 : (Long.valueOf(intervalAttribute.getValue()) * 1000);//seconds
        duration = durationAttribute == null ? -1 : (Long.valueOf(durationAttribute.getValue()) * 60 * 1000);//minutes
        disabled = disabledAttribute == null ? false : Boolean.parseBoolean(disabledAttribute.getValue());
        disabled = duration == 0 ? true : disabled;
    }

    public void apply(FaceletContext ctx, UIComponent parent) throws IOException {
        if (!disabled && !receivingPreRenderEvents) {
            ctx.getFacesContext().getApplication().subscribeToEvent(PreRenderComponentEvent.class, new RefreshSetup());
            receivingPreRenderEvents = true;
        }
    }

    private class RefreshSetup implements SystemEventListener {
        public void processEvent(SystemEvent event) throws AbortProcessingException {
            FacesContext context = FacesContext.getCurrentInstance();
            UIOutput refreshSetup = new RefreshSetupOutput();
            UIViewRoot viewRoot = context.getViewRoot();
            refreshSetup.setTransient(true);
            refreshSetup.setId(viewRoot.createUniqueId(context, "_setupRefresh"));
            viewRoot.addComponentResource(context, refreshSetup, "body");
        }

        public boolean isListenerForSource(Object source) {
            return EnvUtils.isICEfacesView(FacesContext.getCurrentInstance()) && source instanceof UIViewRoot;
        }
    }

    private class RefreshSetupOutput extends UIOutput {
        public void encodeBegin(FacesContext context) throws IOException {
            ResponseWriter writer = context.getResponseWriter();
            writer.startElement("script", this);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeText("ice.setupRefresh('", null);
            writer.writeText(BridgeSetup.getViewID(context.getExternalContext()), null);
            writer.writeText("', ", null);
            writer.writeText(interval, null);
            writer.writeText(", ", null);
            writer.writeText(duration, null);
            writer.writeText(");", null);
            writer.endElement("script");
        }
    }
}
