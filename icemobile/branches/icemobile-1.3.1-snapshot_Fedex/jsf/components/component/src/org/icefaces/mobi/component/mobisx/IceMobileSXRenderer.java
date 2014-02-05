/*
 * Copyright 2004-2013 ICEsoft Technologies Canada Corp.
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

package org.icefaces.mobi.component.mobisx;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.icefaces.impl.application.AuxUploadSetup;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icemobile.util.ClientDescriptor;


public class IceMobileSXRenderer extends Renderer {
    private static final Logger logger = Logger.getLogger(IceMobileSXRenderer.class.getName());
    private static final String ITUNES_LINK =
            "http://itunes.apple.com/us/app/icemobile-sx/id485908934?mt=8";

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        ClientDescriptor client = MobiJSFUtils.getClientDescriptor();
        
        IceMobileSX sx = (IceMobileSX) uiComponent;
        if (client.isIOS() && !client.isSXRegistered() && !client.isICEmobileContainer()){
            writer.startElement(HTML.SPAN_ELEM, uiComponent);
            writer.startElement(HTML.INPUT_ELEM, uiComponent);
            writer.writeAttribute(HTML.TYPE_ATTR, "button", HTML.TYPE_ATTR);
            writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
            StringBuilder baseClass = new StringBuilder(IceMobileSX.IMPORTANT_STYLE_CLASS);
            String styleClass = sx.getStyleClass();
            if (styleClass != null) {
                 baseClass.append(" ").append(styleClass);
            }
            writer.writeAttribute(HTML.CLASS_ATTR, baseClass.toString(), null);
            String style = sx.getStyle();

            if (style != null && style.trim().length() > 0) {
               writer.writeAttribute(HTML.STYLE_ATTR, style, HTML.STYLE_ATTR);
            }
            String value = "Enable";
            Object oVal = sx.getValue();
            if (null != oVal) {
                value = oVal.toString();
            }
            writer.writeAttribute(HTML.VALUE_ATTR, value, HTML.VALUE_ATTR);

            String sessionIdParam = MobiJSFUtils.getSessionIdCookie(facesContext);
            String uploadURL = AuxUploadSetup.getInstance().getUploadURL();
            StringBuilder sb = new StringBuilder("mobi.registerAuxUpload('");
            sb.append(sessionIdParam).append("','").append(uploadURL).append("');");
            writer.writeAttribute(HTML.ONCLICK_ATTR, sb.toString(), HTML.ONCLICK_ATTR);

            writer.endElement(HTML.INPUT_ELEM);

            //write default text if component has no children
            if (0 == uiComponent.getChildCount())  {
                writer.startElement(HTML.ANCHOR_ELEM, uiComponent);
                writer.writeAttribute(HTML.HREF_ATTR, ITUNES_LINK, null);
                writer.writeText("ICEmobile-SX", null);
                writer.endElement(HTML.ANCHOR_ELEM);
                writer.writeText(" Surf Expander.", null);
            }

            writer.endElement(HTML.SPAN_ELEM);
        }

    }


}
