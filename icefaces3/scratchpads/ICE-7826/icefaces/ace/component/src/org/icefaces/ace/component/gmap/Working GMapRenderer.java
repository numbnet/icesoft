package org.icefaces.ace.component.gmap;

import java.io.IOException;
import java.util.Iterator;
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
	        encodeScript(context, gmap);
	    }

	    protected void encodeScript(FacesContext context, GMap gmap) throws IOException {
	        ResponseWriter writer = context.getResponseWriter();
            String clientId = gmap.getClientId(context);
            writer.startElement("script", null);
            writer.writeAttribute("type", "text/javascript", null);
            writer.writeAttribute("src","http://maps.googleapis.com/maps/api/js?key=AIzaSyAATyWVqT2qNusNGmcVTyQ0QmymkpU-B5o&sensor=true",null);
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
            if(gmap.getAddress() != null && gmap.getAddress().length() != 0){
                if(gmap.isIntialized() == false || gmap.isLocateAddress() == true){
                    gmap.setIntialized(true);
                    writer.write("var geocoder = new google.maps.Geocoder();");
                    writer.write("geocoder.geocode( {'address':'" + gmap.getAddress() + "'}, function(results, status) {");
                    writer.write("if (status == google.maps.GeocoderStatus.OK) {");
                    writer.write("map.setCenter(results[0].geometry.location);");
                    writer.write("} else {");
                    writer.write("alert('Geocode was not successful for the following reason:'  + status);");
                    writer.write("}");
                    writer.write("});");
                }

            }
            writer.endElement("script");
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

    @Override
    public boolean getRendersChildren() {
        return true;
    }
}