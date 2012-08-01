package org.icefaces.ace.component.gmap;


import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UIComponent;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.AbortProcessingException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GMapMarker extends GMapMarkerBase {

    public void encodeBegin(FacesContext context, GMapMarker marker) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = getClientId(context);
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("ice.ace.jq(function() {");
        String oldLatitude = "";
        String oldLongitude = "";
        String currentLat = getLatitude();
        String currentLon = getLongitude();
        //create a marker if lat and lon defined on the component itself
        if (currentLat != null &&  currentLon != null
                && currentLat.length() > 0 && currentLon.length() > 0) {
            if (!currentLat.equals(oldLatitude) ||
                    !currentLon.equals(oldLongitude)) {
                //to dynamic support first to remove if any
                writer.write("ice.ace.gMap.removeMarker('"+ getParent().getClientId(context)+"', '"+ clientId +"');");
                if (getOptions() != null)
                    writer.write("ice.ace.gMap.addMarker('" + getParent().getClientId(context) + "', '" + clientId +
                        "', '"+ currentLat + "', '" + currentLon + "', \"" + getOptions() + "\");");
                else
                    writer.write("ice.ace.gMap.addMarker('" + getParent().getClientId(context) + "', '" + clientId +
                        "', '"+ currentLat + "', '" + currentLon + "');");
            }
            oldLatitude = currentLat;
            oldLongitude = currentLon;
        }
        writer.write("});");
        writer.endElement("script");
    }
}