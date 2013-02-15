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

package org.icefaces.ace.component.pushbutton;

import java.io.IOException;
import java.lang.Integer;
import java.lang.Object;
import java.lang.String;
import java.lang.StringBuilder;
import java.util.*;
import javax.el.MethodExpression;
import javax.faces.event.ActionEvent;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionListener;
import javax.faces.render.Renderer;


import org.icefaces.ace.util.HTML;
import org.icefaces.ace.util.JSONBuilder;
import org.icefaces.ace.util.ScriptWriter;
import org.icefaces.ace.util.Utils;
import org.icefaces.util.EnvUtils;
import org.icefaces.render.MandatoryResourceComponent;
import org.icefaces.ace.renderkit.CoreRenderer;


@MandatoryResourceComponent(tagName="pushButton", value="org.icefaces.ace.component.pushbutton.PushButton")
public class PushButtonRenderer extends CoreRenderer {

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
    	Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        PushButton pushButton = (PushButton) uiComponent;
        String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
        String clientId = pushButton.getClientId();
  
        if (clientId.equals(source)) {
            try {
               if (!pushButton.isDisabled()){
                    uiComponent.queueEvent(new ActionEvent (uiComponent));
               }
            } catch (Exception e) {}
        }

        decodeBehaviors(facesContext, pushButton);
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        String yuiBaseClass= "yui-button yui-push-button ui-button ui-widget ui-state-default";
        PushButton pushButton = (PushButton) uiComponent;
        boolean ariaEnabled = EnvUtils.isAriaEnabled(facesContext);
        Integer tabindex = pushButton.getTabindex();

        if (ariaEnabled && tabindex == null) tabindex = 0;

		// root element
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);

        encodeRootStyle(writer, pushButton);

        // first span
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
		writer.writeAttribute(HTML.CLASS_ATTR, yuiBaseClass, null);

		// second span
		writer.startElement(HTML.SPAN_ELEM, uiComponent);
        if (ariaEnabled)
            encodeAriaAttributes(writer, pushButton);
		writer.writeAttribute(HTML.CLASS_ATTR, "first-child", null);

		// button element
		writer.startElement(HTML.BUTTON_ELEM, uiComponent);
		writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId+"_button", null);

        if (pushButton.isDisabled())
            writer.writeAttribute(HTML.STYLE_CLASS_ATTR, "ui-state-disabled", null);

        if (tabindex != null)
            writer.writeAttribute(HTML.TABINDEX_ATTR, tabindex, null);

        renderPassThruAttributes(facesContext, pushButton, HTML.BUTTON_ATTRS, new String[]{"style"});

        // yet another span
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writeButtonValue(writer, pushButton);
        writer.endElement(HTML.SPAN_ELEM);

        writer.endElement(HTML.BUTTON_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
    }

    private void encodeAriaAttributes(ResponseWriter writer, PushButton pushButton) throws IOException {
        Object value = pushButton.getValue();
        Object label = pushButton.getLabel();
        String valueString = value == null ? null : value.toString();
        String labelString = label == null ? null : label.toString();

        if (labelString == null) labelString = valueString;

        writer.writeAttribute(HTML.ROLE_ATTR, "button", null);

        if (labelString != null) {
            writer.writeAttribute(HTML.ARIA_DESCRIBED_BY_ATTR, labelString, null);
        } else {
            writer.writeAttribute(HTML.ARIA_DESCRIBED_BY_ATTR, "description unavailable", null);
        }

        if (pushButton.isDisabled()) {
            writer.writeAttribute(HTML.ARIA_DISABLED_ATTR, true, null);
        }
    }

    private void writeButtonValue(ResponseWriter writer, PushButton pushButton) throws IOException {
        Object buttonObject = null;
        Object valueObject = pushButton.getValue();
        String labelObject = pushButton.getLabel();

        if (valueObject != null)
            buttonObject = valueObject;
        else if (labelObject != null)
            buttonObject = labelObject;

        if (buttonObject != null)
            writer.write(buttonObject.toString());
    }

    private void encodeRootStyle(ResponseWriter writer, PushButton pushButton) throws IOException {
        String rootStyle = "ice-pushbutton";
        String styleClass = pushButton.getStyleClass();
        String style = pushButton.getStyle();

        if (styleClass != null)
            rootStyle = " " + styleClass;

        if (style != null)
            writer.writeAttribute(HTML.STYLE_ATTR, style, null);

        writer.writeAttribute(HTML.CLASS_ATTR, rootStyle, null);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
		String clientId = uiComponent.getClientId(facesContext);
		PushButton pushButton = (PushButton) uiComponent;

        encodeScript(facesContext, pushButton, clientId);
            
        writer.endElement(HTML.DIV_ELEM);
    }

    private void encodeScript(FacesContext facesContext, PushButton pushButton, String clientId) throws IOException {;
        List<UIParameter> uiParams = Utils.captureParameters(pushButton);
        JSONBuilder json = JSONBuilder.create()
                                      .beginFunction("ice.ace.create")
                                      .item("pushbutton")
                                      .beginArray()
                                      .item(clientId)
                                      .beginMap();

        if (pushButton.isDisabled())
            json.entry("disabled", true);

        if (hasListener(pushButton))
            json.entry("fullSubmit", true);

        encodeClientBehaviors(facesContext, pushButton, json);

        if (uiParams != null) {
            json.beginMap("uiParams");
            for (UIParameter p : uiParams)
                json.entry(p.getName(), p.getValue().toString());
            json.endMap();
        }

        json.endMap().endArray().endFunction();

        ScriptWriter.insertScript(facesContext, pushButton, json.toString());
    }

    private boolean hasListener(PushButton pushButton) {
        MethodExpression actionExpression = pushButton.getActionExpression();
        ActionListener[] actionListeners = pushButton.getActionListeners();
        return (actionExpression != null || actionListeners.length > 0);
    }
}
