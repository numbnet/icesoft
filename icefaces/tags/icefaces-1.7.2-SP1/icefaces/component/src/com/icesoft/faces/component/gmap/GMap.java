package com.icesoft.faces.component.gmap;

import java.beans.Beans;
import java.io.IOException;
import java.util.Map;

import javax.faces.component.UICommand;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import org.w3c.dom.Element;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;

public class GMap extends UIPanel{
	public static final String COMPONENET_TYPE = "com.icesoft.faces.GMap";
    public static final String DEFAULT_RENDERER_TYPE = "com.icesoft.faces.GMapRenderer";
    private String DEFAULT_LONGITUDE = "-101.162109375";
    private String DEFAULT_LATITUDE = "56.46249048388979";
    private String longitude;
    private String latitude;
    private Integer zoomLevel;
    private Boolean locateAddress ;
    private boolean initilized = false;
    private String address;
    private String type;
    private String style = null;
    private String styleClass = null;
    private String renderedOnUserRole = null;
    private boolean jsLibraryLoaded = false;
    
    public String getRendererType() {
        return DEFAULT_RENDERER_TYPE;
    }
    
    public String getComponentType() {
        return COMPONENET_TYPE;
    }
    
	public String getLongitude() {
       if (longitude != null) {
            return longitude;
        }
        ValueBinding vb = getValueBinding("longitude");
        return vb != null ? (String) vb.getValue(getFacesContext()) : DEFAULT_LONGITUDE;
	}

	public String getLatitude() {
       if (latitude != null) {
            return latitude;
        }
        ValueBinding vb = getValueBinding("latitude");
        return vb != null ? (String) vb.getValue(getFacesContext()) : DEFAULT_LATITUDE;
	}

    public void setLongitude(String longitude) {
        this.longitude = longitude;
        //TODO need to be changed in the processUpdate. However this property is not 
        //involved in any validator or convertor so it will not harm 
        ValueBinding vb = getValueBinding("longitude");
        if (vb != null) {
            vb.setValue(getFacesContext(), longitude);
            this.longitude = null;
        }
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
        //TODO need to be changed in the processUpdate. However this property is not 
        //involved in any validator or convertor so it will not harm 
        ValueBinding vb = getValueBinding("latitude");
        if (vb != null) {
            vb.setValue(getFacesContext(), latitude);
            this.latitude = null;
        }		
	}
	
    public void decode(FacesContext facesContext) {
    	Map map = facesContext.getExternalContext().getRequestParameterMap();
    	String clientId = getClientId(facesContext);
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
        loadJsLibrary(context);
    	super.encodeBegin(context);
    	//assume it page refresh/redirect
    	if(context.getExternalContext().getRequestParameterMap() != null &&
    	        context.getExternalContext().getRequestParameterMap().size() <= 1) {
    	    initilized = false;
    	}
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
    		} else { 
    		    String latitude = getLatitude();
    		    String longitude = getLongitude();
    		    try {
    		        Float.parseFloat(latitude);
    		        Float.parseFloat(longitude);
                } catch (NumberFormatException e) {
                    latitude = DEFAULT_LATITUDE;
                    longitude = DEFAULT_LONGITUDE;
                }
                JavascriptContext.addJavascriptCall(context, 
                        "Ice.GoogleMap.getGMapWrapper('"+ getClientId(context)+
                        "').getRealGMap().setCenter(new GLatLng("+ latitude 
                        +", "+ longitude +"), "+ getZoomLevel() +");");
    		}
    	}
    	JavascriptContext.addJavascriptCall(context, 
    			"Ice.GoogleMap.setMapType('"+ getClientId(context)+"', '"+ 
    			getType() +"');");
    }
    
    private void loadJsLibrary(FacesContext context) {
        if (jsLibraryLoaded) return;
        String key = context.getCurrentInstance().getExternalContext().getInitParameter("com.icesoft.faces.gmapKey");
        if(key != null) {
            JavascriptContext.includeLib("http://maps.google.com/maps?file=api&v=2&key="+ key, FacesContext.getCurrentInstance());
            jsLibraryLoaded = true;
        } else {
            //log you must need to define googlemap key in web.xml
        }        
    }

	public int getZoomLevel() {
       if (zoomLevel != null) {
            return zoomLevel.intValue();
        }
        ValueBinding vb = getValueBinding("zoomLevel");
        return vb != null ? ((Integer) vb.getValue(getFacesContext())).intValue() : 3;
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

    /**
     * <p>Set the value of the <code>renderedOnUserRole</code> property.</p>
     */
    public void setRenderedOnUserRole(String renderedOnUserRole) {
        this.renderedOnUserRole = renderedOnUserRole;
    }

    /**
     * <p>Return the value of the <code>renderedOnUserRole</code> property.</p>
     */
    public String getRenderedOnUserRole() {
        if (renderedOnUserRole != null) {
            return renderedOnUserRole;
        }
        ValueBinding vb = getValueBinding("renderedOnUserRole");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }

    /**
     * <p>Return the value of the <code>rendered</code> property.</p>
     */
    public boolean isRendered() {
        if (!Util.isRenderedOnUserRole(this)) {
            return false;
        }
        return super.isRendered();
    }

    /**
     * <p>Set the value of the <code>style</code> property.</p>
     */
    public void setStyle(String style) {
        this.style = style;
    }

    /**
     * <p>Return the value of the <code>style</code> property.</p>
     */
    public String getStyle() {
        if (style != null) {
            return style;
        }
        ValueBinding vb = getValueBinding("style");
        return vb != null ? (String) vb.getValue(getFacesContext()) :
                "border-collapse:collapse; border-spacing:0px; padding:0px;";
    }

    /**
     * <p>Set the value of the <code>styleClass</code> property.</p>
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * <p>Return the value of the <code>styleClass</code> property.</p>
     */
    public String getStyleClass() {
        return Util.getQualifiedStyleClass(this,
                styleClass,
                CSS_DEFAULT.GMAP,
                "styleClass");
    }

    public String getMapTdStyleClass() {
        return Util.getQualifiedStyleClass(this, 
                CSS_DEFAULT.GMAP_MAP_TD);
    }    
    
    public String getTxtTdStyleClass() {
        return Util.getQualifiedStyleClass(this, 
                CSS_DEFAULT.GMAP_TXT_TD);
    }    
}

