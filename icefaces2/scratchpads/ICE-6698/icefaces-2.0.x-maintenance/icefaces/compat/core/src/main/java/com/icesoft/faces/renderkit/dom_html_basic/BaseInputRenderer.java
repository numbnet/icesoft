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

package com.icesoft.faces.renderkit.dom_html_basic;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * @author mcollette
 * @since 1.8.1
 */
public class BaseInputRenderer extends BaseRenderer {
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        super.decode(facesContext, uiComponent);
        //readonly or disabled components are not required to submit the value
        if(DomBasicRenderer.isStatic(uiComponent)) {
            return;
        }
        String clientId = uiComponent.getClientId(facesContext);
        Map requestMap =
                facesContext.getExternalContext().getRequestParameterMap();
        if (requestMap.containsKey(clientId)) {
            String decodedValue = (String) requestMap.get(clientId);
            ((UIInput)uiComponent).setSubmittedValue(decodedValue);
        }
    }
    
    public String getValue(FacesContext facesContext, UIComponent uiComponent) {
        // for input components, get the submitted value
        if (uiComponent instanceof UIInput) {
            Object submittedValue = ((UIInput) uiComponent).getSubmittedValue();
            if (submittedValue != null && submittedValue instanceof String) {
                return (String) submittedValue;
            }
        }
        return DomBasicInputRenderer.converterGetAsString(facesContext, 
                uiComponent, ((UIInput) uiComponent).getValue());
    }
}
