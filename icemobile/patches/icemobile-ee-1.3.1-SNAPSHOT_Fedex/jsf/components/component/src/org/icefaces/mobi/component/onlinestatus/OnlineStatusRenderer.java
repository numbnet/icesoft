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
package org.icefaces.mobi.component.onlinestatus;

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.render.Renderer;

import org.icefaces.mobi.utils.HTML;

public class OnlineStatusRenderer extends Renderer {
    private static final Logger logger = Logger.getLogger(OnlineStatusRenderer.class.getName());

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        OnlineStatus comp = (OnlineStatus)uiComponent;
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        writer.startElement("i", uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        
        String styleClass = OnlineStatus.ONLINE_STYLECLASS;
        String userClass = comp.getStyleClass();
        if( userClass != null && userClass.length() > 0 ){
            styleClass += " " + userClass;
        }
        writer.writeAttribute(HTML.CLASS_ATTR, styleClass, null);
        
        String style = comp.getStyle();
        if( style != null && style.length() > 0 ){
            writer.writeAttribute(HTML.STYLE_ATTR, style, null);
        }
        
        writer.endElement("i");
        
        String onOnline = comp.getOnonline();
        String onOffline = comp.getOnoffline();
        
        writer.startElement("script", null);
        writer.writeAttribute("type", "text/javascript", null);
        String script = "ice.mobi.onlineStatus.initClient('" + clientId 
                + "', {onlineStyleClass: '" + OnlineStatus.ONLINE_STYLECLASS + "',"
                + " offlineStyleClass: '" + OnlineStatus.OFFLINE_STYLECLASS + "'";
        
        if( onOnline != null && onOnline.length() > 0 ){
            script += ",onOnline: onOnline_" + clientId;
        }
        
        if( onOffline != null && onOffline.length() > 0 ){
            script += ",onOffline: onOffline_" + clientId;
        }
        script += "});";
        if( onOnline != null && onOnline.length() > 0 ){
            script += "function onOnline_" + clientId + "(event, elem){"
                       + onOnline
                   + "};";
        }
        if( onOffline != null && onOffline.length() > 0 ){
            script += "function onOffline_" + clientId + "(event, elem){"
                       + onOffline
                   + "};";
        }
        writer.write(script);
        writer.endElement("script");
    }

}
