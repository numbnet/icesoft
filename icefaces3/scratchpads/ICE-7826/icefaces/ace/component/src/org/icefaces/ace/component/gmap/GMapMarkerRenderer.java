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

	        GMapMarker marker = (GMapMarker) component;
            marker.encodeBegin(context, marker);
	    }
}