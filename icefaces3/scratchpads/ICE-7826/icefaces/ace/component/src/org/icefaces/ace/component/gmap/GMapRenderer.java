package org.icefaces.ace.component.gmap;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;

import org.icefaces.ace.component.ajax.AjaxBehavior;
import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.render.MandatoryResourceComponent;

@MandatoryResourceComponent(tagName="gMap", value="org.icefaces.ace.component.gmap.GMap")
public class GMapRenderer extends CoreRenderer {


	    public void encodeBegin(FacesContext context, UIComponent component)
	            throws IOException {
            ResponseWriter writer = context.getResponseWriter();
            String clientId = component.getClientId(context);
            GMap gmap = (GMap) component;
            writer.startElement("div", null);
            writer.writeAttribute("id", clientId, null);
            writer.writeAttribute("style", "width: 800px; height: 500px", null);
            writer.endElement("div");
            writer.startElement("script",null);
            writer.writeAttribute("src","http://maps.googleapis.com/maps/api/js?key=AIzaSyAATyWVqT2qNusNGmcVTyQ0QmymkpU-B5o&sensor=true", null);
            writer.endElement("script");
            gmap.encodeBegin(context, gmap);
	    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        if (context == null || component == null) {
            throw new NullPointerException();
        }
        if (component.getChildCount() == 0) return;
        Iterator kids = component.getChildren().iterator();
        while (kids.hasNext()) {
            UIComponent kid = (UIComponent) kids.next();
            kid.encodeBegin(context);
            if (kid.getRendersChildren()) {
                kid.encodeChildren(context);
            }
            kid.encodeEnd(context);
        }

    }

    private void addHiddenField(FacesContext context,
                                String clientId,
                                String name) throws IOException {
        addHiddenField(context, clientId, name, null);
    }


    private void addHiddenField(FacesContext context,
                                String clientId,
                                String name,
                                String value) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", null);
        writer.writeAttribute("id", clientId + name, null);
        writer.writeAttribute("name", clientId + name, null);
        writer.writeAttribute("type", "hidden", null);
        if (value != null) {
            writer.writeAttribute("value", value, null);
        }
        writer.endElement("div");
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}