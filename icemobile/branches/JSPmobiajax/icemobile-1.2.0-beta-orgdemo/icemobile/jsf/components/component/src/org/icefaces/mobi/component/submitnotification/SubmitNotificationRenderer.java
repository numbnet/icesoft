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
package org.icefaces.mobi.component.submitnotification;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.JSFUtils;


public class SubmitNotificationRenderer extends BaseLayoutRenderer {
    private static final Logger logger =
            Logger.getLogger(SubmitNotificationRenderer.class.toString());
    private static final String JS_NAME = "submitnotification.js";
    private static final String JS_MIN_NAME = "submitnotification-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.submitnotification";

    @Override
    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
        writeJavascriptFile(facesContext, component, JS_NAME, JS_MIN_NAME, JS_LIBRARY);
        encodeMarkup(facesContext, component);
       // encodeScript(facesContext, component);
    }

    protected void encodeMarkup(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        SubmitNotification panelNotify = (SubmitNotification) uiComponent;
        String clientId = panelNotify.getClientId(facesContext);
        StringBuilder popupBaseClass = new StringBuilder(SubmitNotification.CONTAINER_HIDE_CLASS);
        // div that is use to hide/show the popup screen black out--will manipulate using js
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_bg", HTML.ID_ATTR);
        writer.writeAttribute(HTML.CLASS_ATTR, SubmitNotification.BLACKOUT_PNL_HIDE_CLASS, HTML.CLASS_ATTR);
        writer.endElement(HTML.DIV_ELEM);
        // panel
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_popup", HTML.ID_ATTR);
        writer.writeAttribute("class", popupBaseClass.toString(), "class");
        writer.writeAttribute("style", panelNotify.getStyle(), "style");
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_popup_inner", HTML.ID_ATTR);
        renderChildren(facesContext, panelNotify);
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.DIV_ELEM);
    }


    @Override
    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
        //Rendering happens on encodeEnd
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    public static String findSubmitNotificationId(UIComponent uiComponent, String subNotId) {
        SubmitNotification panelNotification = (SubmitNotification) uiComponent.findComponent(subNotId);
        if (panelNotification ==null){
            //try again incase it's within a datatable or some other naming container and start with form.
            UIComponent uiForm = JSFUtils.findParentForm(uiComponent);
            if (uiForm !=null){
                panelNotification = (SubmitNotification)(uiForm.findComponent(subNotId));
            }
        }
        if (panelNotification != null) {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            String panelNotificationId = panelNotification.getClientId(facesContext);
            String sb = new String(panelNotificationId);
            return sb;
        } else {
            return null;
        }
    }


}
