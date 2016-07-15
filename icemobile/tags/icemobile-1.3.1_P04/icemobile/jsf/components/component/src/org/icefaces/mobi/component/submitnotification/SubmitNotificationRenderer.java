/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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
import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.JSFUtils;
import org.icemobile.component.ISubmitNotification;
import org.icemobile.renderkit.IResponseWriter;
import org.icemobile.renderkit.SubmitNotificationCoreRenderer;


public class SubmitNotificationRenderer extends BaseLayoutRenderer {
    private static final Logger logger =
            Logger.getLogger(SubmitNotificationRenderer.class.toString());
    private static final String JS_NAME = "submitnotification.js";
    private static final String JS_MIN_NAME = "submitnotification-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.submitnotification";

    @Override
    public void encodeBegin(FacesContext facesContext, UIComponent component) throws IOException {
        writeJavascriptFile(facesContext, component, JS_NAME, JS_MIN_NAME, JS_LIBRARY);
        IResponseWriter writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        SubmitNotificationCoreRenderer renderer = new SubmitNotificationCoreRenderer();
        ISubmitNotification panel = (ISubmitNotification)component;
        renderer.encodeBegin(panel, writer);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent component) throws IOException {
        IResponseWriter writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        SubmitNotificationCoreRenderer renderer = new SubmitNotificationCoreRenderer();
        ISubmitNotification panel = (ISubmitNotification)component;
        renderer.encodeEnd(panel, writer);
    }


    @Override
    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
        super.encodeChildren(facesContext, component);
    }

    @Override
    public boolean getRendersChildren() {
        return true;
    }

    public static String findSubmitNotificationId(UIComponent uiComponent, String subNotId) {
        String val = null;
        if( subNotId != null && subNotId.length() > 0 ){
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
                val = panelNotification.getClientId(facesContext);
            } 
            
        }
        return val;
    }


}
