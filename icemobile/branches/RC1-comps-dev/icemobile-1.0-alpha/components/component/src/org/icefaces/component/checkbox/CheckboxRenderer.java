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

package org.icefaces.component.checkbox;


import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.JSONBuilder;
import org.icefaces.component.utils.ScriptWriter;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.ConverterException;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.Map;


public class CheckboxRenderer extends Renderer {

    public CheckboxRenderer() {
        super();
    }

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        Checkbox checkbox = (Checkbox) uiComponent;
//        String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
        String clientId = uiComponent.getClientId();
        //update with hidden field
        String hiddenValue = String.valueOf(requestParameterMap.get(clientId + "_hidden"));
        boolean submittedValue = isChecked(hiddenValue);
        checkbox.setSubmittedValue(submittedValue);
    }


    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Checkbox checkbox = (Checkbox) uiComponent;

        // root element
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, null);
        writer.writeAttribute(HTML.CLASS_ATTR, "ice-Checkbox", null);

        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_span", null);
        Object styleClass = checkbox.getStyleClass();
        if (null != styleClass) {
            String baseClass = String.valueOf(styleClass);
            writer.writeAttribute(HTML.STYLE_CLASS_ATTR, baseClass, null);
        }

        String style = checkbox.getStyle();
        if (style != null && style.trim().length() > 0) {
            writer.writeAttribute(HTML.STYLE_ATTR, style, HTML.STYLE_ATTR);
        }
        // first child
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute(HTML.CLASS_ATTR, "first-child", null);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_s2", null);
        //labelling  should be label images are skinned

//        String label = this.findCheckboxLabel(checkbox);


        // button element
        writer.startElement(HTML.BUTTON_ELEM, uiComponent);
        writer.writeAttribute(HTML.TYPE_ATTR, "button", null);
        writer.writeAttribute(HTML.NAME_ATTR, clientId + "_button", null);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_button", null);
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Checkbox checkbox = (Checkbox) uiComponent;
        String label = this.findCheckboxLabel(checkbox);

        writer.endElement(HTML.BUTTON_ELEM);
        writer.endElement(HTML.SPAN_ELEM);

        writer.endElement(HTML.SPAN_ELEM);
        //hidden input for single submit=false

        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("name", clientId + "_hidden", null);
        writer.writeAttribute("id", clientId + "_hidden", null);
        writer.writeAttribute("value", checkbox.getValue(), null);
        writer.endElement("input");

        // js call using JSONBuilder utility ICE-5831 and ScriptWriter ICE-5830
        //note that ScriptWriter takes care of the span tag surrounding the script
        String boxValue = String.valueOf(checkbox.getValue());
        boolean isChecked = this.isChecked(boxValue);

        StringBuilder sb = new StringBuilder();
        sb.append(checkbox.getStyle()).
                append(checkbox.getStyleClass());

        String builder = "";
        JSONBuilder.create().beginMap().entry("nothing", label).toString();

        builder = JSONBuilder.create().beginMap().
                entry("type", "checkbox").
                entry("checked", isChecked).
                entry("disabled", checkbox.isDisabled()).
                entry("tabindex", checkbox.getTabindex()).
                entry("label", label).endMap().toString();


        JSONBuilder jBuild = JSONBuilder.create().
                beginMap().
                entry("singleSubmit", checkbox.isSingleSubmit()).
                entry("hashCode", sb.toString().hashCode()).
                entry("ariaEnabled", EnvUtils.isAriaEnabled(facesContext));

        String params = "'" + clientId + "'," +
                builder
                + "," + jBuild.endMap().toString();
        //        System.out.println("params = " + params);

        String finalScript = "ice.mobi.Checkbox(" + params + ");";
        ScriptWriter.insertScript(facesContext, uiComponent, finalScript);

        writer.endElement(HTML.DIV_ELEM);
    }

    private String findCheckboxLabel(Checkbox checkbox) {
        String label = "";
        String checkLabel = checkbox.getLabel();
        if (null != checkLabel && !checkLabel.equals("")) {
            label = checkLabel;
        }
        return label;
    }


    /**
     * support similar return values as jsf component
     * so can use strings true/false, on/off, yes/no to
     * support older browsers
     *
     * @param hiddenValue
     * @return
     */
    private boolean isChecked(String hiddenValue) {
        return hiddenValue.equalsIgnoreCase("on") ||
                hiddenValue.equalsIgnoreCase("yes") ||
                hiddenValue.equalsIgnoreCase("true");
    }

    //forced converter support. It's either a boolean or string.   
    @Override
    public Object getConvertedValue(FacesContext facesContext, UIComponent uiComponent,
                                    Object submittedValue) throws ConverterException {
        if (submittedValue instanceof Boolean) return submittedValue;
        else return Boolean.valueOf(submittedValue.toString());
    }

}
