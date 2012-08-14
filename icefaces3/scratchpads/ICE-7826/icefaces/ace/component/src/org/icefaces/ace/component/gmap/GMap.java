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

public class GMap extends GMapBase {

    public void encodeBegin(FacesContext context, GMap gmap) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        String clientId = getClientId(context);
		writer.startElement("span", null);
		writer.writeAttribute("id", clientId + "_script", null);
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("ice.ace.jq(function() {");
        if ((isLocateAddress() || !isIntialized()) && (getAddress() != null && getAddress().length() > 2))
            writer.write("ice.ace.gMap.locateAddress('" + clientId + "', '" + getAddress() + "');");
        else
            writer.write("ice.ace.gMap.getGMapWrapper('" + clientId +"').getRealGMap().setCenter(new google.maps.LatLng("+ getLatitude() + "," + getLongitude() + "));");
        writer.write("ice.ace.gMap.getGMapWrapper('" + clientId +"').getRealGMap().setZoom(" + getZoomLevel() + ");");
        writer.write("ice.ace.gMap.setMapType('" + clientId + "','" + getType().toUpperCase() + "');");
        if (getOptions() != null && getOptions().length() > 1)
            writer.write("ice.ace.gMap.addOptions('" + clientId +"',\"" + getOptions() + "\");");
        writer.write("});");
        writer.endElement("script");
		writer.endElement("span");
    }

}