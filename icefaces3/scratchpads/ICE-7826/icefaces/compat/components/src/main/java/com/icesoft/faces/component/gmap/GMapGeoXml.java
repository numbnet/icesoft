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

import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.icesoft.faces.context.effects.JavascriptContext;
//CURRENTLY SET UP AS GMapServices
public class GMapGeoXml extends UIPanel{
	public static final String COMPONENT_TYPE = "com.icesoft.faces.GMapGeoXml";
    public static final String COMPONENT_FAMILY = "com.icesoft.faces.GMapGeoXml";
	private String options;
	private String name;
	private String points;
    
	public GMapGeoXml() {
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
		if (getPoints() != null && getName() != null){
			JavascriptContext.addJavascriptCall(context, "Ice.GoogleMap.gService('" + this.getParent().getClientId(context) +
                "', '"+ getName() + "', '" + getPoints() + "', \"" + getOptions() + "\");");
		}
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
	
	public String getName() {
       if (name != null) {
            return name;
        }
        ValueBinding vb = getValueBinding("name");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	public void setPoints(String points) {
		this.points = points;
	}

	public String getPoints() {
       if (points != null) {
            return points;
        }
        ValueBinding vb = getValueBinding("points");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
	}

	public void setName(String name) {
		this.name = name;
	}	


    private transient Object values[];
    public void restoreState(FacesContext context, Object state) {
        values = (Object[])state;
        super.restoreState(context, values[0]); 
		options = (String) values[1];
		name = (String) values[2];
		points = (String) values[3];
    }

    public Object saveState(FacesContext context) {
        if(values == null){
            values = new Object[4];
        }
        values[0] = super.saveState(context);       
		values[1] = options;
		values[2] = name;
		values[3] = points;
        return values;
    }
    
    public boolean isRendered() {
        boolean rendered = super.isRendered();
        if (!rendered) {
            FacesContext context = getFacesContext();  
        }
        return rendered;
    }
}
