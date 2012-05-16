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


//IMPORTANT: Currently functioning as GMapOverlay
public class GMapControl extends UIPanel{
	public static final String COMPONENT_TYPE = "com.icesoft.faces.GMapControl";
    public static final String COMPONENT_FAMILY = "com.icesoft.faces.GMapControl";

	private String options;
	private String shape;
	private String points;
	
	
	public GMapControl() {
		setRendererType(null);
	}

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getComponentType() {
        return COMPONENT_TYPE;
    }
    
    public void encodeBegin(FacesContext context) throws IOException {
        super.encodeBegin(context);
		if (getPoints() != null){
			JavascriptContext.addJavascriptCall(context, "Ice.GoogleMap.gOverlay('" + this.getParent().getClientId(context) +
                "', '"+ getShape() + "', '" + getPoints() + "', \"" + getOptions() + "\");");
			}
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
	
	public String getShape() {
       if (shape != null) {
            return shape;
        }
        ValueBinding vb = getValueBinding("shape");
        return vb != null ? (String) vb.getValue(getFacesContext()) : "line";
	}
	
	public void setShape(String shape) {
		this.shape = shape;
	}
	
	
    private transient Object values[];
	
    public void restoreState(FacesContext context, Object state) {
        values = (Object[])state;
        super.restoreState(context, values[0]);
		options = (String) values[1];
		shape = (String) values[2];
		points = (String) values[3];		
    }

    public Object saveState(FacesContext context) {
		if(values == null){
            values = new Object[4];
        }
        values[0] = super.saveState(context);       
		values[1] = options;
		values[2] = shape;
		values[3] = points;
        return values;
    }
    
    
}
