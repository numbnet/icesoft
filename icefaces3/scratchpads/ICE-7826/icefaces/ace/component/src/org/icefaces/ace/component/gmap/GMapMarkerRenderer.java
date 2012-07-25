package org.icefaces.ace.component.gmap;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;

//import org.icefaces.ace.component.tabview.Tab;
import org.icefaces.ace.component.ajax.AjaxBehavior;
import org.icefaces.ace.renderkit.CoreRenderer;
import org.icefaces.ace.util.ComponentUtils;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.render.MandatoryResourceComponent;

@MandatoryResourceComponent(tagName="gMap", value="org.icefaces.ace.component.gmap.GMap")
public class GMapMarkerRenderer extends CoreRenderer {


	    public void encodeBegin(FacesContext context, UIComponent component)
	            throws IOException {
            ResponseWriter writer = context.getResponseWriter();
            String clientId = component.getClientId(context);
	        GMapMarker gmap = (GMapMarker) component;
            encodeScript(context, gmap);
	    }

	    protected void encodeScript(FacesContext context, GMapMarker gmapMarker) throws IOException {
	        ResponseWriter writer = context.getResponseWriter();
            String clientId = gmapMarker.getClientId(context);
            writer.startElement("script", null);
            writer.writeAttribute("type", "text/javascript", null);
            writer.write("alert('"+ clientId +"');");
            writer.write("alert('"+ gmapMarker.getParent().getClientId(context) +"');");
            writer.endElement("script");
            /*writer.writeAttribute("src","http://maps.googleapis.com/maps/api/js?key=AIzaSyAATyWVqT2qNusNGmcVTyQ0QmymkpU-B5o&sensor=true",null);
            writer.endElement("script");
            writer.startElement("script", null);
            writer.writeAttribute("type", "text/javascript", null);
            writer.write("var mapOptions = {");
            writer.write("center: new google.maps.LatLng(" + gmap.getLatitude() + "," + gmap.getLongitude() + "),");
            writer.write("zoom:"+ gmap.getZoomLevel() +",");
            if(gmap.getType().equalsIgnoreCase("Map"))
                gmap.setType("ROADMAP");
            writer.write("mapTypeId: google.maps.MapTypeId." + gmap.getType().toUpperCase() );
            if(gmap.getOptions() != null && gmap.getOptions().length() != 0)
                writer.write("," + gmap.getOptions());
            writer.write("};");
            writer.write("var map = new google.maps.Map(document.getElementById('"+ clientId +"'),");
            writer.write("mapOptions);");
            writer.endElement("script");*/
        }
}