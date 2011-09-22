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
import org.icefaces.render.MandatoryResourceComponent;

import javax.el.ValueExpression;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;
import java.util.Iterator;

@MandatoryResourceComponent("org.icefaces.component.flipswitch.FlipSwitch")
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
        //if singleSubmit is true then make sure that captured Event is same as clientId
 /*       //otherwise return
        if (inputText.isSingleSubmit()){
        	  if (requestParameterMap.containsKey("ice.event.captured")){
        		  log.info("Single Submit THIS ID CAPTURED THE EVENT id="+clientId);
        	  }
        } */
        if (requestParameterMap.containsKey(clientId)) {
            String submittedString = String.valueOf(requestParameterMap.get(clientId));
            if (submittedString != null){
                Object convertedValue = this.getConvertedValue(facesContext, uiComponent, submittedString);
       //         log.info("id is ="+clientId+" convertedValue = "+convertedValue.toString());
                this.setSubmittedValue(inputText, convertedValue);
            }
                
        }
 /*       else {
        	log.info(" clientId="+clientId+" not included in request parameter map");
        }  */

    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        String clientId = uiComponent.getClientId(facesContext);
        ResponseWriter writer = facesContext.getResponseWriter();
        InputText inputText = (InputText) uiComponent;
        String baseClass = "mobi-input-text";
        String type = inputText.validateType(inputText.getType());
        String componentType = "input";
        
        if (type.equals("textarea")) {
            componentType = "textarea";
        }
        writer.startElement(componentType, uiComponent);
        boolean isNumberType = type.equals("number");
        String compId = clientId;
        if (isNumberType){
            compId+="_number";
        }
        writer.writeAttribute(HTML.ID_ATTR, compId, HTML.ID_ATTR);
        writer.writeAttribute(HTML.NAME_ATTR, compId, null);
        writer.writeAttribute("class", baseClass, null);
        String valueToRender = getStringValueToRender(facesContext, inputText);
        //do common passThrough attributes
        PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent, inputText.getCommonInputAttributeNames());
        PassThruAttributeWriter.renderBooleanAttributes(writer, uiComponent, inputText.getBooleanAttNames());
        if (type.equals("textarea")) {
            //autocomplete and list not yet implemented on mobile safari
            PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent, inputText.getTextAreaAttributeNames());
        } else if (isNumberType){
        	PassThruAttributeWriter.renderNonBooleanAttributes(writer,uiComponent, inputText.getNumberAttributeNames());
        }
        else {
            PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent, inputText.getInputtextAttributeNames());
        }
       	writer.writeAttribute("autocorrect", "off", null);
    	writer.writeAttribute("autocapitalize", "off", null);
        boolean singleSubmit = inputText.isSingleSubmit();
        if (inputText.isDisabled())
            writer.writeAttribute("disabled", "disabled", null);
        if (inputText.isReadonly())
            writer.writeAttribute("readonly", "readonly", null);
        //still need to implement styleClass
        String jsCall = "ice.se(event, '" + clientId + "');";
        if (isNumberType){
          jsCall = "mobi.input.submit(event, '"+clientId+"', this.value,"+singleSubmit+");";
        }
        if (singleSubmit){
            writer.writeAttribute("onchange", jsCall, null);
        }
        if (!componentType.equals("textarea")) {
            writer.writeAttribute(HTML.VALUE_ATTR, valueToRender, HTML.VALUE_ATTR);
        } else {
            writer.write(valueToRender);
        }
        if (isNumberType){
            writer.startElement(HTML.INPUT_ELEM, uiComponent);
            writer.writeAttribute(HTML.TYPE_ATTR, "hidden", null);
            writer.writeAttribute(HTML.ID_ATTR, clientId, null);
            writer.writeAttribute(HTML.NAME_ATTR, clientId, null);
            writer.writeAttribute(HTML.VALUE_ATTR, valueToRender,  null);
            writer.endElement(HTML.INPUT_ELEM);
        }
        writer.endElement(componentType);


    }
    
    @Override
    public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent, Object submittedValue) throws ConverterException {
        UIInput uiInput = (UIInput) uiComponent;
        String value = String.valueOf(submittedValue);
        Converter converter = uiInput.getConverter();

        //first ask the converter
        if (converter != null) {
            return converter.getAsObject(facesContext, uiInput, value);
        }
        //Try to guess
        else {
            ValueExpression ve = uiInput.getValueExpression("value");

            if (ve != null) {
                Class<?> valueType = ve.getType(facesContext.getELContext());
                Converter converterForType = facesContext.getApplication().createConverter(valueType);
  
//    log.info("trying to guess converter for id="+uiInput.getClientId()+" valueType = "+valueType.getName());
                if (converterForType != null) {
//                	  log.info(" converter Type = "+converterForType.toString());
                    return converterForType.getAsObject(facesContext, uiInput, value);
                }
                	
            }
        }

        return value;
    }


}
