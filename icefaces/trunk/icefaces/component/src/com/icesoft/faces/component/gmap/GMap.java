package com.icesoft.faces.component.gmap;

import java.io.IOException;
import java.util.Map;

import javax.faces.component.UICommand;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.w3c.dom.Element;

import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;

public class GMap extends UICommand{
	public static final String COMPONENET_TYPE = "com.icesoft.faces.GMap";
    public static final String DEFAULT_RENDERER_TYPE = "com.icesoft.faces.GMapRenderer";
    private String longitude;
    private String latitude;
    private Integer zoomLevel;
    private Boolean locateAddress ;
    private boolean initilized = false;
    private String address;
    private String type;

    public String getRendererType() {
        return DEFAULT_RENDERER_TYPE;
    }
    
    public String getComponentType() {
        return COMPONENET_TYPE;
    }
    
    public GMap() {
        String key = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("com.icesoft.faces.gmapKey");
        if(key != null) {
        	JavascriptContext.includeLib("http://maps.google.com/maps?file=api&v=2&key="+ key, FacesContext.getCurrentInstance());
        } else {
        	//log you must need to define googlemap key in web.xml
        }
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
	
    public void decode(FacesContext facesContext) {
    	Map map = facesContext.getExternalContext().getRequestParameterMap();
    	String clientId = getClientId(facesContext);
    	System.out.println("gmap decode " + map.get(clientId+ "event"));
    	if (map.get(clientId+ "event") != null && map.get(clientId+ "event")
    						.toString().length() > 0) {
	    	 if (map.containsKey(clientId + "lat")) {
				 setLatitude(String.valueOf(map.get(clientId + "lat")));
			 }
			 if(map.containsKey(clientId + "lng")){
				 setLongitude(String.valueOf(map.get(clientId + "lng")));				 
			 }
			 if(map.containsKey(clientId + "zoom")){
				 setZoomLevel(Integer.valueOf(String.
						 valueOf(map.get(clientId + "zoom"))).intValue());				 
			 }
			 if(map.containsKey(clientId + "type")){
				 setType(String.valueOf(map.get(clientId + "type")));				 
			 }			 
    	}
    	
    }
    
    public void encodeBegin(FacesContext context) throws IOException {
    	super.encodeBegin(context);
    	if ((isLocateAddress() || !initilized) && (getAddress() != null 
    			&& getAddress().length() > 2)) {
        	JavascriptContext.addJavascriptCall(context, 
        			"Ice.GoogleMap.locateAddress('"+ getClientId(context)+"', '"+ 
        				getAddress() +"');");
        	initilized = true;
    	} else {
    		if (isLocatedByGeocoder(context)) {
    			JavascriptContext.addJavascriptCall(context, 
    					"Ice.GoogleMap.getGMapWrapper('"+ getClientId(context)+
    					"').getRealGMap().setZoom("+ getZoomLevel() +");");
    		} else { System.out.println("REnderint passing zoom "+ getZoomLevel());
    			JavascriptContext.addJavascriptCall(context, 
    					"Ice.GoogleMap.getGMapWrapper('"+ getClientId(context)+
    					"').getRealGMap().setCenter(new GLatLng("+ getLatitude() 
    					+", "+ getLongitude()+"), "+ getZoomLevel() +");");
    		}
    	}
    	JavascriptContext.addJavascriptCall(context, 
    			"Ice.GoogleMap.setMapType('"+ getClientId(context)+"', '"+ 
    			getType() +"');");
    }
    
    public void encodeEnd(FacesContext context) throws IOException {
    	System.out.println("GMAP encode end");
    }

	public int getZoomLevel() {
       if (zoomLevel != null) {
            return zoomLevel.intValue();
        }
        ValueBinding vb = getValueBinding("zoomLevel");
        return vb != null ? ((Integer) vb.getValue(getFacesContext())).intValue() : 13;
	}

	public void setZoomLevel(int zoomLevel) {
		this.zoomLevel = new Integer(zoomLevel);
	}
	
	private boolean isLocatedByGeocoder(FacesContext context) {
		Object event = context.getExternalContext().getRequestParameterMap()
		.get(getClientId(context)+ "event");
		if (event != null && "geocoder".equals(event)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isLocateAddress() {
        if (locateAddress != null) {
            return locateAddress.booleanValue();
        }
        ValueBinding vb = getValueBinding("locateAddress");
        return vb != null ?
               ((Boolean) vb.getValue(getFacesContext())).booleanValue() :
               false;
	}

	public void setLocateAddress(boolean locateAddress) {
		this.locateAddress = new Boolean(locateAddress);
	}

	public String getAddress() {
        if (address != null) {
            return address;
        }
        ValueBinding vb = getValueBinding("address");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	public void setAddress(String address) {
		this.address = address;
	}
    
	public String getType() {
        if (type != null) {
            return type;
        }
        ValueBinding vb = getValueBinding("type");
        return vb != null ? (String) vb.getValue(getFacesContext()) : "Map";
	}

	public void setType(String type) {
		this.type = type;
	}	
}

