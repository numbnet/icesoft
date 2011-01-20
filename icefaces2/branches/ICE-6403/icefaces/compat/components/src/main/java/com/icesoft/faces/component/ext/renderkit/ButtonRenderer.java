/*
 * Version: MPL 1.1
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
 */

package com.icesoft.faces.component.ext.renderkit;

import com.icesoft.faces.component.ext.HtmlCommandLink;
import com.icesoft.faces.component.ext.HtmlCommandButton;
import org.w3c.dom.Element;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import com.icesoft.faces.component.panelconfirmation.PanelConfirmation;
import com.icesoft.faces.component.panelconfirmation.PanelConfirmationRenderer;
import com.icesoft.faces.component.util.CustomComponentUtils;
import com.icesoft.util.CoreComponentUtils;

public class ButtonRenderer
        extends com.icesoft.faces.renderkit.dom_html_basic.ButtonRenderer {

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        super.decode(facesContext, uiComponent);
    	if (didThisButtonInvokeSubmit(facesContext, uiComponent)) {
        	if (CoreComponentUtils.isJavaScriptDisabled(facesContext)) {
                String panelConfirmationId = String.valueOf(uiComponent.getAttributes().get("panelConfirmation"));
                PanelConfirmation panelConfirmation = (PanelConfirmation) CoreComponentUtils
                        .findComponent(panelConfirmationId, uiComponent);
                if (panelConfirmation != null) {   
                	panelConfirmation.setShowWhenJSDisable(true);
                	facesContext.renderResponse();
                }
        	}
        }    	
    }
    
    protected void renderOnClick(UIComponent uiComponent, Element root) {
        
		UIComponent button = (UIComponent) uiComponent;
        String onclick = (String) uiComponent.getAttributes().get("onclick");
        String submitCode;
		                        
		boolean isPartialSubmit = false;
		try {
			isPartialSubmit = (boolean)((HtmlCommandButton)button).getPartialSubmit(); 
		} catch (ClassCastException e) {
			isPartialSubmit = (boolean)((HtmlCommandLink)button).getPartialSubmit();
		}

        if (isPartialSubmit) {
            submitCode = this.ICESUBMITPARTIAL + "return false;";
        } else {
            submitCode = this.ICESUBMIT + "return false;";
        }
        
        if (null != uiComponent.getAttributes().get("panelConfirmation")) {
            root.setAttribute("onclick", PanelConfirmationRenderer.renderOnClickString(
                uiComponent, combinedPassThru(onclick, submitCode)));
        } else {
            root.setAttribute("onclick", combinedPassThru(onclick, submitCode));
        }
    }

}
