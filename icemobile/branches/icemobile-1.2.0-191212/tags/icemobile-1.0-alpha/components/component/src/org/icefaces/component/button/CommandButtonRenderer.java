/*
 * Copyright 2004-2011 ICEsoft Technologies Canada Corp. (c)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions an
 * limitations under the License.
 */
package org.icefaces.component.button;

import org.icefaces.component.utils.HTML;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CommandButtonRenderer extends Renderer {
    private static Logger logger = Logger.getLogger(CommandButtonRenderer.class.getName());

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        CommandButton commandButton = (CommandButton) uiComponent;
        String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
        String clientId = commandButton.getClientId();
        if (clientId.equals(source)) {
            try {
                if (!commandButton.isDisabled()) {
                    uiComponent.queueEvent(new ActionEvent(uiComponent));
                }
            } catch (Exception e) {
                logger.warning("Error queuing CommandButton event");
            }
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        CommandButton commandButton = (CommandButton) uiComponent;

        // root element
        writer.startElement(HTML.INPUT_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        String defaultClass = "mobi-button mobi-button-default";
        String importantClass = "mobi-button mobi-button-important";
        String backClass = "mobi-button mobi-button-back";
        String attentionClass = "mobi-button mobi-button-attention";
        String baseClass = "mobi-button";
        String type = commandButton.getType();
        if (type.equals("important")) {
            baseClass = importantClass;
        } else if (type.equals("back")) {
            baseClass = backClass;
        } else if (type.equals("attention")) {
            baseClass = attentionClass;
        } else if (type.equals("default")) {
            baseClass = defaultClass;
        } else if (logger.isLoggable(Level.FINER)) {
            logger.finer("unsupported type. default is used");
        }
        /*  mobi-button-default - regular mobile OS themed button
mobi-button-important - mimics OS's important button if relevant
mobi-button-back - mimics OS's back button if relevant
mobi-button-attention - mimics OS's attention button if relevant. */
//        Object styleClass = commandButton.getStyleClass();
//        if (null!=styleClass){
//             baseClass +=  " " + String.valueOf(styleClass);
//        }
        writer.writeAttribute(HTML.CLASS_ATTR, baseClass, null);
        String style = commandButton.getStyle();
        if (style != null && style.trim().length() > 0) {
            writer.writeAttribute(HTML.STYLE_ATTR, style, HTML.STYLE_ATTR);
        }
        writer.writeAttribute("type", "button", null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        String value = type;
        Object oVal = commandButton.getValue();
        if (null != oVal) {
            value = oVal.toString();
        }
        writer.writeAttribute(HTML.VALUE_ATTR, value, HTML.VALUE_ATTR);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        CommandButton commandButton = (CommandButton) uiComponent;
        boolean singleSubmit = commandButton.isSingleSubmit();
        if (commandButton.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", null);
        } else {
            if (singleSubmit) {
                writer.writeAttribute("onclick", "ice.se(event, '" + clientId + "');", null);
            } else {
                writer.writeAttribute("onclick", "ice.s(event, '" + clientId + "');", null);
            }
        }
        writer.endElement(HTML.INPUT_ELEM);

    }
}
