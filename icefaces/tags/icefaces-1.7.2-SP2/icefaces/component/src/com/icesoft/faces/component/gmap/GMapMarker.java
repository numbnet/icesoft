package com.icesoft.faces.component.gmap;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.icesoft.faces.context.effects.JavascriptContext;

public class GMapMarker extends UIPanel{
	public static final String COMPONENET_TYPE = "com.icesoft.faces.GMapMarker";
	private Boolean draggable;
    private String longitude;
    private String latitude;	
    private List point = new ArrayList();
    
	public GMapMarker() {
		setRendererType(null);
	}
	
    public String getComponentType() {
        return COMPONENET_TYPE;
    }
    
    public boolean getRendersChildren() {
    	return true;
    }
    public void encodeBegin(FacesContext context) throws IOException {
    	setRendererType(null);
    }
    
    public void encodeChildren(FacesContext context) throws IOException {
	     Iterator kids = getChildren().iterator();
	     while (kids.hasNext()) {
		    UIComponent kid = (UIComponent) kids.next();
  
		    	kid.encodeBegin(context);
			    if (kid.getRendersChildren()) {
			    	kid.encodeChildren(context);
			    }
			    kid.encodeEnd(context);
			    if (kid instanceof GMapLatLng) {
			    	String call = kid.getAttributes().get("latLngScript").toString();
                    //if dynamically changed then remove the previous one
			    	if (call.endsWith("changed") || !kid.isRendered() || !isRendered()) {
			    	    call = call.substring(0, call.length() - "changed".length());
			    	    JavascriptContext.addJavascriptCall(context, 
			    	            "Ice.GoogleMap.removeOverlay('"+ this.getParent()
			    	            .getClientId(context)+"', '"+ kid.getClientId(context)+"');");
			    	} 
			    	if (!kid.isRendered() || !isRendered()) continue;
			    	JavascriptContext.addJavascriptCall(context, "Ice.GoogleMap." +
			    			"addOverlay('"+ this.getParent().getClientId(context)+
			    			"', '"+ kid.getClientId(context)+"', 'new GMarker("+ call +")');");
			    } else if(kid instanceof GMapLatLngs) {
			        //The list of GMapLatLngs can be dynamic so first remove previously 
			        //added markers
			        Iterator it = point.iterator();
			        while (it.hasNext()) {
			            JavascriptContext.addJavascriptCall(context, "Ice.GoogleMap." +
			            		"removeOverlay('"+ this.getParent()
			            		.getClientId(context)+"', '"+ it.next() +"');");		            
			        }
			        point.clear();
			        if (!kid.isRendered() || !isRendered()) continue;
			        //now add the fresh list of the markers
			        StringTokenizer st = new StringTokenizer(kid.getAttributes()
			                .get("latLngsScript").toString(), ";");
			    	while(st.hasMoreTokens()) {
			    		String[] scriptInfo =st.nextToken().split("kid-id");
			    		String call = scriptInfo[0];
			    		String latLngId = scriptInfo[1];
			    		point.add(latLngId);
			    		JavascriptContext.addJavascriptCall(context, "Ice.GoogleMap." +
			    				"addOverlay('"+ this.getParent().getClientId(context)+
			    				"', '"+ latLngId +"', 'new GMarker("+ call +")');");
			    	}
			    }
	     }
    }
    
    
	public boolean isDraggable() {
        if (draggable != null) {
            return draggable.booleanValue();
        }
        ValueBinding vb = getValueBinding("draggable");
        return vb != null ?
               ((Boolean) vb.getValue(getFacesContext())).booleanValue() :
               false;
	}

	public void setDraggable(boolean draggable) {
		this.draggable = new Boolean(draggable);
	}
	
	public String getLongitude() {
       if (longitude != null) {
            return longitude;
        }
        ValueBinding vb = getValueBinding("longitude");
        return vb != null ? (String) vb.getValue(getFacesContext()) : "-122.1419";
	}

	public String getLatitude() {
       if (latitude != null) {
            return latitude;
        }
        ValueBinding vb = getValueBinding("latitude");
        return vb != null ? (String) vb.getValue(getFacesContext()) : "37.4419";
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
}
