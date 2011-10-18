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

package com.icesoft.faces.component.jseventlistener;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.render.Renderer;

import com.icesoft.faces.component.util.CustomComponentUtils;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;

public class JSEventListenerRenderer extends Renderer{

    public boolean getRendersChildren() {
        return true;
    }
    
    public void decode(FacesContext context, UIComponent uiComponent) {
        Map parameter = context.getExternalContext().getRequestParameterMap();
        if (parameter.containsKey(uiComponent.getClientId(context))) {
            String result = String.valueOf(parameter.get(uiComponent.getClientId(context)));
            if ("submitted".equals(result)) {
                ((JSEventListener)uiComponent).queueEvent(new ActionEvent(uiComponent));
            }
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        String events = ((JSEventListener)uiComponent).getEvents();
        String handler = ((JSEventListener)uiComponent).getHandler();
        if (handler != null)
            handler = "'"+ handler  + "'";
        if (events != null) {
                    JavascriptContext.addJavascriptCall(facesContext, "Ice.registerEventListener('"+ clientId +"','" +
                    events	+"', "+ handler +");");
        }
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement(HTML.DIV_ELEM);
    }   
    
    public void encodeChildren(FacesContext facesContext,
            UIComponent uiComponent) throws IOException {    
        if (uiComponent.getChildCount() > 0) {
           CustomComponentUtils.renderChildren(facesContext, uiComponent);
        }
    }    
}
