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

package com.icesoft.faces.renderkit.dom_html_basic;

import com.icesoft.faces.component.AttributeConstants;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.icesoft.faces.util.DOMUtils;

public class OutputTextRenderer extends BaseRenderer{
    private static final String[] passThruAttributes =
        AttributeConstants.getAttributes(
            AttributeConstants.H_OUTPUTTEXT);
    
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        UIOutput component = (UIOutput) uiComponent;
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        //This is not ice:outputText, so just render the value
        //this will be true for open HTML in the JSP
        if (!(component instanceof HtmlOutputText)) {
            Object value = component.getValue();
            if (value != null) {
                String svalue = String.valueOf(value);
                if (svalue.length() > 0) {
                    writer.write(svalue);
                }
            }
            return;
        }

        if (!requiresSpan(uiComponent)) return;
        Object styleClass = uiComponent.getAttributes().get("styleClass");
        
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        renderHtmlAttributes(facesContext, writer, uiComponent);

        if (styleClass != null) {
            writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
        }
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        if (!(uiComponent instanceof HtmlOutputText)) {
            return;
        }
        //it must call the super.encode to support effects and facesMessage recovery
        super.encodeEnd(facesContext, uiComponent); 
        ResponseWriter writer = facesContext.getResponseWriter();
        UIOutput component = (UIOutput) uiComponent;
        Object rawValue = component.getValue();         
        String convertedValue = DomBasicInputRenderer.converterGetAsString(
            facesContext, uiComponent, rawValue);
        boolean valueTextRequiresEscape = DOMUtils.escapeIsRequired(uiComponent);
        if (valueTextRequiresEscape) {
            convertedValue = DOMUtils.escapeAnsi(convertedValue);
        } 
        writer.write(convertedValue);
        if (!requiresSpan(uiComponent)) return;
        writer.endElement(HTML.SPAN_ELEM);
    }
    
    protected void renderHtmlAttributes(
        FacesContext facesContext, ResponseWriter writer, UIComponent uiComponent)
        throws IOException {
        PassThruAttributeWriter.renderHtmlAttributes(
            writer, uiComponent, passThruAttributes);
    }
    
    private boolean requiresSpan(UIComponent uiComponent) {
        Boolean nospan = (Boolean) uiComponent.getAttributes().get("nospan");
        if (nospan != null && nospan.booleanValue()) return false;
        String style = (String) uiComponent.getAttributes().get("style");
        String styleClass =
                (String) uiComponent.getAttributes().get("styleClass");
        String title = (String) uiComponent.getAttributes().get("title");
        String dir = (String) uiComponent.getAttributes().get("dir");
        String lang = (String) uiComponent.getAttributes().get("lang");
        if (styleClass != null
            || style != null
            || title != null
            || dir != null
            || lang != null) {
            return true;
        }
        String id = uiComponent.getId();
        if (id != null && !id.startsWith("_") && !id.startsWith("j_id")) {
            return true;
        }
        return false;
    }    
}
