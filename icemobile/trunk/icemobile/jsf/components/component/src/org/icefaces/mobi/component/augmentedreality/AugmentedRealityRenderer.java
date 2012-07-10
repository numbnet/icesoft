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
package org.icefaces.mobi.component.augmentedreality;

import org.icefaces.mobi.renderkit.BaseInputRenderer;
import org.icefaces.impl.application.AuxUploadResourceHandler;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.Utils;
import org.icefaces.util.EnvUtils;

import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;


public class AugmentedRealityRenderer extends BaseInputRenderer  {

     public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
             throws IOException {
         ResponseWriter writer = facesContext.getResponseWriter();
         String clientId = uiComponent.getClientId(facesContext);
         AugmentedReality ag = (AugmentedReality)uiComponent;
         boolean isEnhanced = EnvUtils.isEnhancedBrowser(facesContext);
         boolean isAuxUpload = EnvUtils.isAuxUploadBrowser(facesContext);
         if (!isEnhanced && !isAuxUpload) {  //no container or SX, use text field
             writer.startElement(HTML.INPUT_ELEM, uiComponent);
             writer.writeAttribute(HTML.ID_ATTR, clientId, null);
             writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
             writer.endElement(HTML.INPUT_ELEM);
             return;
         }
         writer.startElement(HTML.BUTTON_ELEM, uiComponent);
         writer.writeAttribute(HTML.ID_ATTR, clientId, null);
         String buttonValue="Reality";
         if (null!=ag.getStyle()){
             String style= ag.getStyle();
             if ( style.trim().length() > 0) {
                 writer.writeAttribute(HTML.STYLE_ATTR, style, HTML.STYLE_ATTR);
             }
         }
         StringBuilder defaultClass = new StringBuilder(AugmentedReality.DEFAULT_STYLE_CLASS);
         if (null!=ag.getStyleClass()) {
             String styleClass = ag.getStyleClass();
             defaultClass.append(" ").append(styleClass);
         }
         writer.writeAttribute(HTML.CLASS_ATTR, defaultClass, HTML.CLASS_ATTR);
         String myparams = ag.getParams();
        String script;
        if (isAuxUpload)  {
            writer.writeAttribute("data-params", myparams, null);
            writer.writeAttribute("data-command", "aug", null);
            script = "ice.mobi.sx(this);";
        } else {
            script = "ice.aug( '" + clientId + "', '" + myparams + "' );";
        }
        writer.writeAttribute(HTML.ONCLICK_ATTR, script, null);
         writer.writeText(buttonValue, null);
        writer.endElement(HTML.BUTTON_ELEM);
     }

    @Override
    public void decode(FacesContext facesContext, UIComponent component) {
        Map parameterMap = facesContext.getExternalContext()
                .getRequestParameterMap();
        AugmentedReality aug = (AugmentedReality) component;
        String clientId = component.getClientId();
        Object submitted = parameterMap.get(clientId);
        if (null == submitted)  {
            Map auxMap = AuxUploadResourceHandler.getAuxRequestMap();
            submitted = auxMap.get(clientId);
        }
        if (null != submitted) {
            String submittedString = String.valueOf(submitted);
            if (submittedString != null){
                Object convertedValue = this.getConvertedValue(facesContext, component, submittedString);
                this.setSubmittedValue(aug, convertedValue);
            }

        }
    }

}
