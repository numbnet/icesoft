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

package org.icefaces.mobi.component.flipswitch;

import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.PassThruAttributeWriter;
import org.icefaces.mobi.component.button.CommandButton;
import org.icefaces.mobi.renderkit.CoreRenderer;
import org.icemobile.util.ClientDescriptor;
import org.icemobile.util.UserAgentInfo;
import org.icefaces.mobi.utils.JSONBuilder;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

public class FlipSwitchRenderer extends CoreRenderer {

    private static final Logger logger = Logger.getLogger(FlipSwitchRenderer.class.getName());

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        // The RequestParameterMap holds the values received from the browser
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        String clientId = uiComponent.getClientId(facesContext);
        FlipSwitch flipswitch = (FlipSwitch) uiComponent;
        if (flipswitch.isDisabled()) {
            return;
        }
        //update with hidden field
        String submittedString = String.valueOf(requestParameterMap.get(clientId + "_hidden"));
        if (submittedString != null) {
            boolean submittedValue = isChecked(submittedString);
            flipswitch.setSubmittedValue(submittedValue);
            decodeBehaviors(facesContext, flipswitch);
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        String clientId = uiComponent.getClientId(facesContext);
        ResponseWriter writer = facesContext.getResponseWriter();
        FlipSwitch flipswitch = (FlipSwitch) uiComponent;
        ClientBehaviorHolder cbh = (ClientBehaviorHolder)uiComponent;
        boolean hasBehaviors = !cbh.getClientBehaviors().isEmpty();

        writer.startElement(HTML.ANCHOR_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, HTML.NAME_ATTR);
        String styleClass = FlipSwitch.FLIPSWITCH_OFF_CLASS;
        String switchValue = String.valueOf(flipswitch.getValue());
        boolean isChecked = this.isChecked(switchValue);
        boolean disabled = flipswitch.isDisabled();
        if (isChecked) {
            styleClass = FlipSwitch.FLIPSWITCH_ON_CLASS;
        }
        writer.writeAttribute("class", styleClass + (disabled ? " disabled" : ""), null);

        PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent, flipswitch.getAttributesNames());
        PassThruAttributeWriter.renderBooleanAttributes(writer, uiComponent, flipswitch.getBooleanAttNames());
        String labelOn = flipswitch.getLabelOn();
        String labelOff = flipswitch.getLabelOff();
        boolean readonly = flipswitch.isReadonly();
        boolean disableOffline = flipswitch.isDisableOffline();
        if (!disabled && !readonly){
            StringBuilder jsCall = new StringBuilder(255);
            jsCall.append((disableOffline? "if(!navigator.onLine)return;": "")+"mobi.flipswitch.init('").append(clientId).append("',{ event: event,elVal: this,");
            jsCall.append("singleSubmit: ").append(flipswitch.isSingleSubmit());
    
            if (hasBehaviors){
                JSONBuilder jb = JSONBuilder.create();
                this.encodeClientBehaviors(facesContext, cbh, jb);
                String bh = ", "+jb.toString();
                bh = bh.replace("\"", "\'");
                jsCall.append(bh);
            }
            // Mobi-526 pass transformer hack flag
            if (isTransformerHack(facesContext)) {
                logger.finest("Transformer Prime hack active");
                jsCall.append(", transHack: 'true'");
            }
            jsCall.append("}); return false; ");
    
            writer.writeAttribute("onclick", jsCall.toString(), null);
        }
        writer.startElement(HTML.SPAN_ELEM, uiComponent);

        boolean switchVal = (Boolean) flipswitch.getValue();
        writer.writeAttribute("class", "mobi-flipswitch-txt-on" + (switchVal ? "" : " ui-btn-up-c" ), null);
        writer.write(labelOn);
        writer.endElement(HTML.SPAN_ELEM);
        writeHiddenField(uiComponent, clientId, writer, switchVal, disabled);

        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute("class", "mobi-flipswitch-txt-off" + (switchVal ? " ui-btn-up-c" : ""), null);
        writer.write(labelOff);
        writer.endElement(HTML.SPAN_ELEM);
        if( disableOffline  ){
            writer.startElement("script", null);
            writer.writeAttribute("type", "text/javascript", null);
            
            String funcName = "flipSwitchOfflineListener_" + clientId.replace(":", "_");
            writer.writeText(
                 "function " + funcName + "(){"
                    + "var elem = document.getElementById('" + clientId + "');"
                    + "if(!elem){"
                         + "window.removeEventListener('online', " + funcName + ", false);"
                         + "window.removeEventListener('offline', " + funcName + ", false);"
                    + "}"
                    + "if( navigator.onLine ){"
                        + "elem.classList.remove('mobi-button-dis');"
                        + "elem.removeAttribute('disabled');"
                    + "}"
                    + "else{"
                        + "elem.classList.add('mobi-button-dis');"
                        + "elem.setAttribute('disabled','disabled');"
                    + "}"
               + "};"
               + "ice.mobi.addListener(window,'online', " + funcName + ");"
               + "ice.mobi.addListener(window,'offline'," + funcName + ");", null);
            writer.endElement("script");
        }
        writer.endElement(HTML.ANCHOR_ELEM);

    }

    private void writeHiddenField(UIComponent uiComponent, String clientId,
                                  ResponseWriter writer, boolean switchValue, boolean disabled) throws IOException {
        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("name", clientId + "_hidden", null);
        writer.writeAttribute("id", clientId + "_hidden", null);
        writer.writeAttribute("value", switchValue, null);
        if (disabled) {
            writer.writeAttribute("disabled", "disabled", null);
        }
        writer.endElement("input");
    }

    private boolean isChecked(String hiddenValue) {
        return hiddenValue.equalsIgnoreCase("on") ||
                hiddenValue.equalsIgnoreCase("yes") ||
                hiddenValue.equalsIgnoreCase("true");
    }

    //forced converter support. It's either a boolean or string.
    @Override
    public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent,
                                    Object submittedValue) throws ConverterException {
        if (submittedValue instanceof Boolean) {
            return submittedValue;
        } else {
            return Boolean.valueOf(submittedValue.toString());
        }
    }



    /**
     * will render it's own children
     */
    public boolean getRendersChildren() {
        return true;
    }

    /**
     * MOBI-526 Check if the device is An Asus Transformer Prime for a
     * bad hack involving suppression of double clicks on flipswitch.
     * @param pageContext
     * @return
     */
    private boolean isTransformerHack(FacesContext pageContext) {
        Object request = pageContext.getExternalContext().getRequest();
        if (request instanceof HttpServletRequest) {
            HttpServletRequest hsr = (HttpServletRequest) request;
            ClientDescriptor client = ClientDescriptor.getInstance(hsr);

            String ua = client.getUserAgent();
            if (ua != null) {
                ua = ua.toLowerCase();
                return ua.contains( UserAgentInfo.TABLET_TRANSORMER_PRIME );
            }
        }
        return false;
    }
}
