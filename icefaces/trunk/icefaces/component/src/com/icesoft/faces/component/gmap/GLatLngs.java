package com.icesoft.faces.component.gmap;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.icesoft.faces.component.ext.UIColumns;

public class GLatLngs extends UIPanel{
	public static final String COMPONENET_TYPE = "com.icesoft.faces.GLatLngs";
	private List value;
	public GLatLngs() {
		setRendererType(null);
	}
	
    public String getComponentType() {
        return COMPONENET_TYPE;
    }
    
    public void encodeBegin(FacesContext context) throws IOException {
    	setRendererType(null);
    	Iterator it = getValue().iterator();
    	StringBuffer latLngScript = new StringBuffer();
    	while(it.hasNext()) {
	    	UIComponent kid = (UIComponent)it.next();
	    	kid.encodeBegin(context);
		    if (kid.getRendersChildren()) {
		    	kid.encodeChildren(context);
		    }
		    kid.encodeEnd(context);
		    latLngScript.append(kid.getAttributes().get("latLngScript") + 
		    		"kid-id"+ kid.getClientId(context) + ";");
    	}
	    this.getAttributes().put("latLngsScript", latLngScript);
    }

	public List getValue() {
		if (value != null) {
			return value;
	    }
	    ValueBinding vb = getValueBinding("value");
	    return vb != null ? (List) vb.getValue(getFacesContext()) : null;
	}

	public void setValue(List value) {
		this.value = value;
	}	
}
