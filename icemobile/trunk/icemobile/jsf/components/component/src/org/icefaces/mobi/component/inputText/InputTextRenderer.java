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
package org.icefaces.mobi.component.inputText;

import org.icefaces.mobi.renderkit.BaseInputRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icefaces.mobi.utils.PassThruAttributeWriter;
import org.icemobile.util.ClientDescriptor;
import org.icefaces.mobi.utils.JSONBuilder;

import java.lang.StringBuilder;
import java.util.Date;
import java.text.SimpleDateFormat;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;

public class InputTextRenderer extends BaseInputRenderer {
    
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
            decodeBehaviors(facesContext, inputText);
        }
    }


    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        String clientId = uiComponent.getClientId(facesContext);
        ResponseWriter writer = facesContext.getResponseWriter();
        InputText inputText = (InputText) uiComponent;
        
        String label = inputText.getLabel();
        if( label != null ){
            writer.startElement(HTML.LABEL_ELEM, null);
            writer.writeAttribute(HTML.ID_ATTR, inputText.getClientId()+"_lbl", null);
            writer.writeAttribute(HTML.FOR_ATTR, inputText.getClientId(), null);
            writer.writeAttribute(HTML.CLASS_ATTR, "ui-input-text", null);
            writer.writeText(label, null);
            writer.endElement(HTML.LABEL_ATTR);
        }

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

        StringBuilder baseClass = new StringBuilder("mobi-input-text ui-input-text");
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
            ClientDescriptor client = MobiJSFUtils.getClientDescriptor();
            String typeVal = (String)uiComponent.getAttributes().get("type");
            if( isDateType && client.isAndroidOS() && client.isICEmobileContainer() ){ //Android container borks date types
                typeVal = "text";
            }
            writer.writeAttribute("type", typeVal, null);
            PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent, inputText.getInputtextAttributeNames());
        }
        if (!isDateType) writer.writeAttribute("autocorrect", "off", null);
        else writer.writeAttribute("autocorrect", "on", null);
        writer.writeAttribute("autocapitalize", "off", null);
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
        ClientBehaviorHolder cbh = (ClientBehaviorHolder)uiComponent;
        boolean hasBehaviors = !cbh.getClientBehaviors().isEmpty();
        StringBuilder jsCall = new StringBuilder("ice.setFocus(null); ice.mobi.inputText.activate('");
        if (!disabled && !readOnly){
            boolean singleSubmit = inputText.isSingleSubmit();
            jsCall.append(inputText.getClientId()).append("',{");
            jsCall.append("singleSubmit:").append(inputText.isSingleSubmit());
            if (hasBehaviors){
                String bh = this.buildAjaxRequest(facesContext, cbh, "onchange");
                bh = bh.replace("\"", "\'");
                jsCall.append(",'behaviors':{'change':").append(bh).append("}});");
            } else {
                jsCall.append("});") ;
            }
            writer.writeAttribute("onchange", jsCall, null);
        }
        writer.endElement(componentType);
    }
}
