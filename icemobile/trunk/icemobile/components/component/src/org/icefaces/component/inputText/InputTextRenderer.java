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
package org.icefaces.component.inputText;

import org.icefaces.renderkit.BaseInputRenderer;
import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.PassThruAttributeWriter;
import org.icefaces.render.MandatoryResourceComponent;

import java.util.Date;
import java.text.SimpleDateFormat;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;


import java.util.logging.Logger;

@MandatoryResourceComponent("org.icefaces.component.inputText.InputText")
public class InputTextRenderer extends BaseInputRenderer {
    private final static Logger logger = Logger.getLogger(InputTextRenderer.class.getName());

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        // The RequestParameterMap holds the values received from the browser

        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        String clientId = uiComponent.getClientId(facesContext);
        InputText inputText = (InputText) uiComponent;
        if (inputText.isDisabled() || inputText.isReadonly()) {
            return;
        }
        if (requestParameterMap.containsKey(clientId)) {
            String submittedString = String.valueOf(requestParameterMap.get(clientId));
            if (submittedString==null) {
                return;
            }
            this.setSubmittedValue(inputText, submittedString);
        }
        decodeBehaviors(facesContext, inputText);
    }


    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        String clientId = uiComponent.getClientId(facesContext);
        ResponseWriter writer = facesContext.getResponseWriter();
        InputText inputText = (InputText) uiComponent;

        String type = inputText.validateType(inputText.getType());
        String componentType = "input";

        if (type.equals("textarea")) {
            componentType = "textarea";
        }

        boolean isNumberType = type.equals("number");
        boolean isDateType = type.equals("date");
        String compId = clientId;

        writer.startElement(componentType, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, compId, HTML.ID_ATTR);
        writer.writeAttribute(HTML.NAME_ATTR, compId, null);

        StringBuilder baseClass = new StringBuilder("mobi-input-text");
        String styleClass = inputText.getStyleClass();
        if (styleClass != null) {
            baseClass.append(" ").append(styleClass);
        }
        writer.writeAttribute("class", baseClass.toString(), null);
        String valueToRender = getStringValueToRender(facesContext, inputText);
        if (valueToRender == null && isDateType){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd");
                Date aDate = new Date();
                valueToRender =  sdf.format(aDate);
        }
        //do common passThrough attributes
        PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent, inputText.getCommonInputAttributeNames());
        PassThruAttributeWriter.renderBooleanAttributes(writer, uiComponent, inputText.getBooleanAttNames());
        if (type.equals("textarea")) {
            //autocomplete and list not yet implemented on mobile safari
            PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent, inputText.getTextAreaAttributeNames());
        } else if (isNumberType) {
            PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent, inputText.getNumberAttributeNames());
        } else {
            PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent, inputText.getInputtextAttributeNames());
        }
        if (!isDateType) writer.writeAttribute("autocorrect", "off", null);
        else writer.writeAttribute("autocorrect", "on", null);
        writer.writeAttribute("autocapitalize", "off", null);
 //       boolean singleSubmit = inputText.isSingleSubmit();
        boolean readOnly = inputText.isReadonly();
        boolean disabled = inputText.isDisabled();
        if (disabled)
            writer.writeAttribute("disabled", "disabled", null);
        if (readOnly)
            writer.writeAttribute("readonly", "readonly", null);
        //still need to implement styleClass

        if (!componentType.equals("textarea")) {
            writer.writeAttribute(HTML.VALUE_ATTR, valueToRender, HTML.VALUE_ATTR);
        } else {
            writer.write(valueToRender);
        }
        //ClientBehaviors
        String event = inputText.getDefaultEventName(facesContext);
        ClientBehaviorHolder cbh = (ClientBehaviorHolder)uiComponent;
        boolean hasBehaviors = !cbh.getClientBehaviors().isEmpty();

        if (!disabled && !readOnly && hasBehaviors){
              String cbhCall = this.buildAjaxRequest(facesContext, cbh, event);
              writer.writeAttribute(event, cbhCall, null);
        }
        else if (inputText.isSingleSubmit()){
            String jsCall = "ice.se(event, '" + clientId + "');";
            writer.writeAttribute(event, jsCall, null);
        }
        writer.endElement(componentType);
    }


}
