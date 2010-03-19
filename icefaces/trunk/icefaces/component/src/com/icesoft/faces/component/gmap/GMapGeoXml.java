/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * "The contents of this file are subject to the Mozilla Public License
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
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */

package com.icesoft.faces.component.gmap;

import java.io.IOException;

import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.icesoft.faces.context.effects.JavascriptContext;

public class GMapGeoXml extends UIPanel{
	public static final String COMPONENT_TYPE = "com.icesoft.faces.GMapGeoXml";
    public static final String COMPONENT_FAMILY = "com.icesoft.faces.GMapGeoXml";
	private String url;
	
	public GMapGeoXml() {
		setRendererType(null);
	}

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getComponentType() {
        return COMPONENT_TYPE;
    }
    
    public void encodeBegin(FacesContext context) throws IOException {
        setRendererType(null);
        super.encodeBegin(context);        
        if (!isRendered()) {
            JavascriptContext.addJavascriptCall(context, "Ice.GoogleMap.removeOverlay('"+ this.getParent().getClientId(context)+"', '"+ this.getClientId(context) +"');");
        } else {
            JavascriptContext.addJavascriptCall(context, "Ice.GoogleMap.addOverlay('"+ this.getParent().getClientId(context)+"', '"+ this.getClientId(context) +"', 'new GGeoXml(\""+ getUrl() +"\")');");
        }
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

    private transient Object values[];
    public void restoreState(FacesContext context, Object state) {
        values = (Object[])state;
        super.restoreState(context, values[0]);
        url = (String)values[1];
    }

    public Object saveState(FacesContext context) {

        if(values == null){
            values = new Object[2];
        }
        values[0] = super.saveState(context);
        values[1] = url;
        return values;
    }
        
        
    
}
