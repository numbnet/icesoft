package com.icesoft.faces.component.gmap;

import java.io.IOException;

import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

public class GMapLatLng extends UIPanel{
	public static final String COMPONENET_TYPE = "com.icesoft.faces.GMapLatLng";
    private String longitude;
    private String latitude;
    private String localeLng;
    private String localeLat;
    public GMapLatLng() {
		setRendererType(null);
	}
	
	public GMapLatLng(String latitude, String longitude) {
		this();
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
    public String getComponentType() {
        return COMPONENET_TYPE;
    }
    
    public void encodeBegin(FacesContext context) throws IOException {
    	setRendererType(null);
    	generateLatLngScript();
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

		public  void setLongitude(String longitude) {
			this.longitude = longitude;
		}

		public  void setLatitude(String latitude) {
			this.latitude = latitude;
		}

		public void generateLatLngScript() {
            String currentLat = getLatitude();
            String currentLng = getLongitude();
            String changed = "";
            if (localeLat != null && !localeLat.equals(currentLat)) {
                changed = "changed";
            }
            if (localeLng != null && !localeLng.equals(currentLng)) {
                changed = "changed";
            }
            localeLat = currentLat;
            localeLng = currentLng;
			String script = "new GLatLng("+ getLatitude() + ","+ getLongitude() +")"+ changed;
			getAttributes().put("latLngScript", script);
		}

}
