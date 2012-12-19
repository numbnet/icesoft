package org.icefaces.mobi.component.gmap;

import java.io.IOException;
import java.util.Iterator;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.mobi.renderkit.CoreRenderer;

public class GMapRenderer extends CoreRenderer {

	public void encodeBegin(FacesContext context, UIComponent component)
			throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = component.getClientId(context);
		GMap gmap = (GMap) component;
		
		writer.startElement("div", null);
		writer.writeAttribute("id", clientId+"_wrp", null);
		if( gmap.getStyle() != null ){
		    writer.writeAttribute("style", gmap.getStyle(), null );
		}
		if( gmap.getStyleClass() != null ){
		    writer.writeAttribute("class", gmap.getStyleClass(), null);
		}
		
		writer.startElement("div", null);
		writer.writeAttribute("id", clientId, null);
		writer.writeAttribute("style", "height: 100%;", null);
		writer.endElement("div");
		writer.startElement("span", null);
		writer.writeAttribute("id", clientId + "_script", null);
		writer.startElement("script", null);
		writer.writeAttribute("type", "text/javascript", null);
		
		StringBuilder sb = new StringBuilder();
		sb.append("if( mobi.gmap.repo['"+ clientId + "'] ){");
		sb.append(    "delete mobi.gmap.repo['"+ clientId + "'];");
		sb.append("}");
		sb.append("mobi.gmap.repo['"+ clientId + "'] = new google.maps.Map(document.getElementById('"+ clientId + "'), {mapTypeId : '"
		        +gmap.getType()+"'});var map = mobi.gmap.repo['"+ clientId + "'];");
		if ((gmap.isLocateAddress() || !gmap.isIntialized())
				&& (gmap.getAddress() != null && gmap.getAddress().length() > 2)) {
			sb.append("new google.maps.Geocoder()");
			sb.append(  ".geocode({'address' : '" + gmap.getAddress() + "'},");
			sb.append(  "function(results, status) {if (status == google.maps.GeocoderStatus.OK) {map.setCenter(results[0].geometry.location);}});");
		} else {
			sb.append("map.setCenter(new google.maps.LatLng("+gmap.getLatitude()+","
			        +gmap.getLongitude()+"));");
		}
		sb.append("map.setZoom("+gmap.getZoomLevel()+");");
		if (gmap.getOptions() != null && gmap.getOptions().length() > 1) {
			sb.append("map.setOptions(eval(({" + gmap.getOptions() + "})));");
		}
		writer.write(sb.toString());
		writer.endElement("script");
		writer.endElement("span");
		writer.endElement("div");
		gmap.setIntialized(true);
	}

	@Override
	public void encodeChildren(FacesContext context, UIComponent component)
			throws IOException {
		if (context == null || component == null) {
			throw new NullPointerException();
		}
		if (component.getChildCount() == 0)
			return;
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