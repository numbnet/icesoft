package com.icesoft.faces.component.gmap;

import java.io.IOException;

import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.icesoft.faces.context.effects.JavascriptContext;

public class GMapControl extends UIPanel{
	public static final String COMPONENET_TYPE = "com.icesoft.faces.GMapControl";

	public GMapControl() {
		setRendererType(null);
	}
	
    public String getComponentType() {
        return COMPONENET_TYPE;
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
}
