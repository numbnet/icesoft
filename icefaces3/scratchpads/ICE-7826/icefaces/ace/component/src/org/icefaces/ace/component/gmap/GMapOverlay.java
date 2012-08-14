package org.icefaces.ace.component.gmap;


import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class GMapOverlay extends GMapOverlayBase {

    public void encodeBegin(FacesContext context, GMapOverlay gLayer) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = getClientId(context);
		writer.startElement("span", null);
		writer.writeAttribute("id", clientId + "_overlay", null);
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("ice.ace.jq(function() {");
        if (getPoints() != null && getShape() != null){
            writer.write("ice.ace.gMap.gOverlay('" + this.getParent().getClientId(context) + "', '" + clientId + "' , '" + getShape() + "' , '" + getPoints() + "' , \"" + getOptions() + "\");");
        }
        writer.write("});");
        writer.endElement("script");
		writer.endElement("span");
    }

}