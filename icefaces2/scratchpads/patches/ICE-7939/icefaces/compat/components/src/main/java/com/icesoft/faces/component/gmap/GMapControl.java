/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package com.icesoft.faces.component.gmap;

import java.io.IOException;

import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.icesoft.faces.context.effects.JavascriptContext;

public class GMapControl extends UIPanel{
	public static final String COMPONENT_TYPE = "com.icesoft.faces.GMapControl";
    public static final String COMPONENT_FAMILY = "com.icesoft.faces.GMapControl";

	public GMapControl() {
		setRendererType(null);
	}

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getComponentType() {
        return COMPONENT_TYPE;
    }
    
    private String name;
    private String localName;
    private String position;

	public String getName() {
       if (name != null) {
            return name;
        }
        ValueBinding vb = getValueBinding("name");
        return vb != null ? (String) vb.getValue(getFacesContext()) : "GLargeMapControl";
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPosition() {
       if (position != null) {
            return position;
        }
        ValueBinding vb = getValueBinding("position");
        return vb != null ? (String) vb.getValue(getFacesContext()) : "G_ANCHOR_TOP_RIGHT";
	}

	public void setPosition(String position) {
		this.position = position;
	}
    
    public void encodeBegin(FacesContext context) throws IOException {
        super.encodeBegin(context);
        String currentName = getName();
        String mapClientId = this.getParent().getClientId(context); 
        if (!isRendered()) {
            removeControl(context, mapClientId);
            return;
        }
        if (localName != null && !localName.equalsIgnoreCase(currentName)) {
            //control name has been changed so removed the previous control
            removeControl(context, mapClientId);
        }
        localName = currentName;
        JavascriptContext.addJavascriptCall(context, 
        "Ice.GoogleMap.addControl('"+ mapClientId +"', '"+ currentName +"');");
    }
    
    private void removeControl(FacesContext facesContext, String mapId) {
        JavascriptContext.addJavascriptCall(facesContext, 
                "Ice.GoogleMap.removeControl('"+ mapId +"', '"+ localName +"');");
    }

    private transient Object values[];
    public void restoreState(FacesContext context, Object state) {
        values = (Object[])state;
        super.restoreState(context, values[0]);
        name = (String)values[1];
        localName = (String)values[2]; 
        position = (String)values[3];        
    }

    public Object saveState(FacesContext context) {

        if(values == null){
            values = new Object[4];
        }
        values[0] = super.saveState(context);
        values[1] = name;
        values[2] = localName;  
        values[3] = position;          
        return values;
    }
    
    
}
