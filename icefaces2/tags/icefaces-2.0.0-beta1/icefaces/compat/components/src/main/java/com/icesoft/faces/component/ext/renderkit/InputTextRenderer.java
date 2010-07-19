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

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;

import com.icesoft.faces.component.ExtendedAttributeConstants;
import com.icesoft.faces.component.IceExtended;
import com.icesoft.faces.component.ext.HtmlInputText;
import com.icesoft.faces.component.ext.KeyEvent;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.context.effects.LocalEffectEncoder;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeWriter;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;

public class InputTextRenderer extends com.icesoft.faces.renderkit.dom_html_basic.InputTextRenderer {
    // LocalEffectEncoder takes ownership of any passthrough attributes
    private static final String[] jsEvents = LocalEffectEncoder.maskEvents(
        ExtendedAttributeConstants.getAttributes(
            ExtendedAttributeConstants.ICE_INPUTTEXT));
    private static final String[] passThruAttributes =
        ExtendedAttributeConstants.getAttributes(
            ExtendedAttributeConstants.ICE_INPUTTEXT,
            jsEvents);
    private static Map rendererJavascript;
    private static Map rendererJavascriptPartialSubmit;
    static {
        rendererJavascript = new HashMap();
        rendererJavascript.put(HTML.ONKEYPRESS_ATTR,
            DomBasicRenderer.ICESUBMIT);
        rendererJavascript.put(HTML.ONFOCUS_ATTR,
            "setFocus(this.id);");
        rendererJavascript.put(HTML.ONBLUR_ATTR,
            "setFocus('');");
        rendererJavascript.put(HTML.ONMOUSEDOWN_ATTR, "this.focus();");
        rendererJavascriptPartialSubmit = new HashMap();
        rendererJavascriptPartialSubmit.put(HTML.ONKEYPRESS_ATTR,
            DomBasicRenderer.ICESUBMIT);
        rendererJavascriptPartialSubmit.put(HTML.ONFOCUS_ATTR,
            "setFocus(this.id);");
        rendererJavascriptPartialSubmit.put(HTML.ONBLUR_ATTR,
            "setFocus('');" + DomBasicRenderer.ICESUBMITPARTIAL);
        rendererJavascriptPartialSubmit.put(HTML.ONMOUSEDOWN_ATTR, "this.focus();");        
    }
    
    protected void renderHtmlAttributes(FacesContext facesContext, ResponseWriter writer, UIComponent uiComponent)
            throws IOException{
        PassThruAttributeWriter.renderHtmlAttributes(
            writer, uiComponent, passThruAttributes);
        //renderer is responsible to write the autocomplete attribute
        Object autoComplete = ((HtmlInputText)uiComponent).getAutocomplete();
        if (autoComplete != null) {
            writer.writeAttribute(HTML.AUTOCOMPLETE_ATTR, autoComplete, null);
        }
        Map rendererJS = ((IceExtended) uiComponent).getPartialSubmit()
            ? rendererJavascriptPartialSubmit : rendererJavascript;

        LocalEffectEncoder.encode(
            facesContext, uiComponent, jsEvents, rendererJS, null, writer);
    }
    
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        super.decode(facesContext, uiComponent);
        Object focusId = facesContext.getExternalContext()
                .getRequestParameterMap().get(FormRenderer.getFocusElementId());
        if (focusId != null) {
            if (focusId.toString()
                    .equals(uiComponent.getClientId(facesContext))) {
                ((HtmlInputText) uiComponent).setFocus(true);
            } else {
                ((HtmlInputText) uiComponent).setFocus(false);
            }
        }

        if (Util.isEventSource(facesContext, uiComponent)) {
            queueEventIfEnterKeyPressed(facesContext, uiComponent);
        }
    }


    public void queueEventIfEnterKeyPressed(FacesContext facesContext,
                                            UIComponent uiComponent) {
        try {
            KeyEvent keyEvent =
                    new KeyEvent(uiComponent, facesContext.getExternalContext()
                .getRequestParameterMap());
            if (keyEvent.getKeyCode() == KeyEvent.CARRIAGE_RETURN) {
                uiComponent.queueEvent(new ActionEvent(uiComponent));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }    
}
