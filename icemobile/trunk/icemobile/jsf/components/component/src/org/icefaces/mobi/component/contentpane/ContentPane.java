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
package org.icefaces.mobi.component.contentpane;


import javax.el.ValueExpression;
import javax.faces.component.StateHelper;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import java.util.HashMap;
import java.util.Map;

public class ContentPane extends ContentPaneBase {
    public static final String CONTENT_BASE_CLASS = "mobi-contentpane ";
    public static final String CONTENT_HIDDEN_CLASS = "mobi-contentpane-hidden ";
    public static final String CONTENT_SINGLE_BASE_CLASS = "mobi-contentpane-single";
    public static final String CONTENT_SINGLE_HIDDEN_CLASS = "mobi-contentpane-single-hidden";
    public static final String CONTENT_SINGLE_MENUPANE_CLASS = "mobi-contentpane-single-menu-hidden";


    @Override
    public void setClient(boolean client) {
        ValueExpression ve = getValueExpression(PropertyKeys.client.name() );
        if (ve != null) {
            // map of style values per clientId
            ve.setValue(getFacesContext().getELContext(), client );
        } else {
            StateHelper sh = getStateHelper();
            if (isDisconnected(this))  {
                String defaultKey = PropertyKeys.client.name() + "_defaultValues";
                Map clientDefaults = (Map) sh.get(defaultKey);
                if (clientDefaults == null) {
                    clientDefaults = new HashMap();
                    clientDefaults.put("defValue",client);
                    sh.put(defaultKey, clientDefaults);
                }
            }
        }
    }

    @Override
    public boolean isClient() {
        java.lang.Boolean retVal = false;
        ValueExpression ve = getValueExpression( PropertyKeys.client.name() );
        if (ve != null) {
            Object o = ve.getValue( getFacesContext().getELContext() );
            if (o != null) {
                retVal = (java.lang.Boolean) o;
            }
        } else {
            StateHelper sh = getStateHelper();
            String defaultKey = PropertyKeys.client.name() + "_defaultValues";
            Map defaultValues = (Map) sh.get(defaultKey);
            if (defaultValues != null) {
                if (defaultValues.containsKey("defValue" )) {
                    retVal = (java.lang.Boolean) defaultValues.get("defValue");
                }
            }
        }
        return retVal;
    }
	private static boolean isDisconnected(UIComponent component) {
		UIComponent parent = component.getParent();
		if (parent != null && parent instanceof UIViewRoot) {
			return false;
		} else if (parent != null) {
			return isDisconnected(parent);
		} else {
			return true;
		}
	}
  }
