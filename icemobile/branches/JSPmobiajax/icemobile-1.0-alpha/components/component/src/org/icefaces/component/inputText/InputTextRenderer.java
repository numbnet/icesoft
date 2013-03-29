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

import org.icefaces.component.utils.BaseInputRenderer;
import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.PassThruAttributeWriter;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;


public class InputTextRenderer extends BaseInputRenderer {
    private final static Logger log = Logger.getLogger(InputTextRenderer.class.getName());


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
            if (submittedString != null)
                this.setSubmittedValue(inputText, submittedString);
        }

    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        String clientId = uiComponent.getClientId(facesContext);
        ResponseWriter writer = facesContext.getResponseWriter();
        InputText inputText = (InputText) uiComponent;
        String baseClass = "mobi-input-text";
        // Write outer div --> only for panelStack?
//	        writer.startElement(HTML.DIV_ELEM, uiComponent); //only have this if in panelStack?
//	        writer.writeAttribute(HTML.ID_ATTR, clientId + "_div", HTML.ID_ATTR);
//            writer.writeAttribute("class", divClass, null);
        String type = inputText.getType();
        String componentType = "input";
        if (type.equals("textarea")) {
            componentType = "textarea";
        }
        writer.startElement(componentType, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
        writer.writeAttribute("class", baseClass, null);
        String valueToRender = getStringValueToRender(facesContext, inputText);

        //do common passThrough attributes
        PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent, inputText.getCommonInputAttributeNames());
        PassThruAttributeWriter.renderBooleanAttributes(writer, uiComponent, inputText.getBooleanAttNames());
        if (type.equals("textarea")) {
            //autocomplete and list not yet implemented on mobile safari
            PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent, inputText.getTextAreaAttributeNames());
        } else {
            PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent, inputText.getInputtextAttributeNames());
        }
        boolean singleSubmit = inputText.isSingleSubmit();
        if (inputText.isDisabled())
            writer.writeAttribute("disabled", "disabled", null);
        if (inputText.isReadonly())
            writer.writeAttribute("readonly", "readonly", null);
        //still need to implement styleClass
        if (singleSubmit)
            writer.writeAttribute("onchange", "ice.se(event, '" + clientId + "');", null);
        if (!componentType.equals("textarea")) {
            writer.writeAttribute(HTML.VALUE_ATTR, valueToRender, HTML.VALUE_ATTR);
        } else {
            writer.write(valueToRender);
        }
        writer.endElement(componentType);


    }


}
