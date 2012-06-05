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
import java.io.Serializable;

import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.icesoft.faces.context.effects.JavascriptContext;
//Currently used as GMapLayer
public class GMapLatLng extends UIPanel implements Serializable  {
	public static final String COMPONENT_TYPE = "com.icesoft.faces.GMapLatLng";
    public static final String COMPONENT_FAMILY = "com.icesoft.faces.GMapLatLng";

    private String layerType;
    private String url;
    private String options;
	
    public GMapLatLng() {
		setRendererType(null);
	}
	
	/*public GMapLatLng(String latitude, String longitude) {
		this();
		this.latitude = latitude;
		this.longitude = longitude;
	}*/

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getComponentType() {
        return COMPONENT_TYPE;
    }
    
    public void encodeBegin(FacesContext context) throws IOException {
    	setRendererType(null);
        super.encodeBegin(context);
		if (getLayerType() != null){
			JavascriptContext.addJavascriptCall(context, "Ice.GoogleMap.removeMapLayer('" + this.getParent().getClientId(context) + "', '" + getClientId(context) +"');");
			JavascriptContext.addJavascriptCall(context, "Ice.GoogleMap.addMapLayer('" + this.getParent().getClientId(context) + "', '" + getClientId(context) +
                "', '"+ getLayerType() + "', '" + getUrl() + "', \"" + getOptions() + "\");");
		}
    }
    
	public String getLayerType() {
		if (layerType != null) {
            return layerType;
        }
        ValueBinding vb = getValueBinding("layerType");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	public void setLayerType(String layerType) {
		this.layerType = layerType;
	}

	public String getUrl() {
		if (url != null) {
            return url;
        }
        ValueBinding vb = getValueBinding("url");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOptions() {
		if (options != null) {
            return options;
        }
        ValueBinding vb = getValueBinding("options");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	public void setOptions(String options) {
		this.options = options;
	}

    private transient Object values[];
    public void restoreState(FacesContext context, Object state) {

        values = (Object[])state;
        super.restoreState(context, values[0]);
        layerType = (String)values[1];
        url = (String)values[2];
        options = (String)values[3];
        }

    public Object saveState(FacesContext context) {

        if(values == null){
            values = new Object[4];
        }
        values[0] = super.saveState(context);
        values[1] = layerType;
        values[2] = url;
        values[3] = options;        
        return values;
    }

}
