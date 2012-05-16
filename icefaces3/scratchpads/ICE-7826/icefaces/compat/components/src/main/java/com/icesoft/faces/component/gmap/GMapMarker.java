/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the
 * License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an "AS
 * IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

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
	public static final String COMPONENT_TYPE = "com.icesoft.faces.GMapMarker";
    public static final String COMPONENT_FAMILY = "com.icesoft.faces.GMapMarker";
    
	private Boolean draggable;
    private String longitude;
    private String latitude;
	private String options;
    private transient String oldLongitude;
    private transient String oldLatitude;    
    private List point = new ArrayList();
    
	public GMapMarker() {
		setRendererType(null);
	}
	 
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getComponentType() {
        return COMPONENT_TYPE;
    }
    
    public boolean getRendersChildren() {
    	return true;
    }
    public void encodeBegin(FacesContext context) throws IOException {
    	setRendererType(null);
        super.encodeBegin(context);    	
    	String currentLat = getLatitude();
    	String currentLon = getLongitude();
    	//create a marker if lat and lon defined on the component itself
    	if (currentLat != null &&  currentLon != null 
    	        && currentLat.length() > 0 && currentLon.length() > 0) {
    	    if (!currentLat.equals(oldLatitude) || 
    	            !currentLon.equals(oldLongitude)) {
    	        //to dynamic support first to remove if any
                JavascriptContext.addJavascriptCall(context, "Ice.GoogleMap.addMarker('" + this.getParent().getClientId(context) +
                        "', '"+ currentLat + "', '" + currentLon + "', \"" + getOptions() + "\");");                
    	    }
    	    oldLatitude = currentLat;
    	    oldLongitude = currentLon;
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
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	public String getLatitude() {
       if (latitude != null) {
            return latitude;
        }
        ValueBinding vb = getValueBinding("latitude");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}
	
	public String getOptions() {
       if (options != null) {
            return options;
        }
        ValueBinding vb = getValueBinding("options");
        return vb != null ? (String) vb.getValue(getFacesContext()) : "title:'default'";
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	
	public void setOptions(String options) {
		this.options = options;
	}

    private transient Object values[];
    public void restoreState(FacesContext context, Object state) {
        values = (Object[])state;
        super.restoreState(context, values[0]);
        latitude = (String)values[1];
        longitude = (String)values[2];
        draggable = (Boolean)values[3];        
        point = (List) values[4]; 
		options = (String) values[5];
    }

    public Object saveState(FacesContext context) {
        if(values == null){
            values = new Object[6];
        }
        values[0] = super.saveState(context);
        values[1] = latitude;
        values[2] = longitude;
        values[3] = draggable;        
        values[4] = point;
		values[5] = options;
        return values;
    }
    
    public boolean isRendered() {
        boolean rendered = super.isRendered();
        if (!rendered) {
            FacesContext context = getFacesContext();
            JavascriptContext.addJavascriptCall(context, 
                    "Ice.GoogleMap.removeOverlay('"+ this.getParent()
                    .getClientId(context)+"', '"+ getClientId(context)+"');");  
            oldLongitude = null;
            oldLatitude = null;
        }
        return rendered;
    }

}
