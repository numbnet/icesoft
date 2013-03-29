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

package org.icefaces.component.sliderentry;

import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.PassThruAttributeWriter;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ValueChangeEvent;
import javax.faces.render.Renderer;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * The sliderRender renders following elements:
 * 1. A div with a client id(e.g.)
 * <div id="xxx" />
 * which will be used by the YUI slider, as a slider holder
 * <p/>
 * In addition to the rendering the renderer performs decode as well. This component
 * doesn't use a hidden field for it value instead takes advantage of param support of JSF2
 */

public class SliderEntryRenderer extends Renderer {

    List<UIParameter> uiParamChildren;
    private final static Logger log = Logger.getLogger(SliderEntryRenderer.class.getName());

    // The decode method, in the renderer, is responsible for taking the values
    //  that have been submitted from the browser, and seeing if they correspond
    //  to this particular component, and also for the correct row(s) of any
    //  UIData container(s). So if there are 5 rows, and the user used the 3rd
    //  row's slider, this slider will decode 5 times, but should only be affected
    //  the 3rd time. Once it find that there is data relevant to it, it then
    //  extracts that data, and passes it to the component.
    //  There is another case where we perform decode, its when the singleSubmit is set 
    //  to false, the reason behind is that the source of event can be any component. 
    //  So we have to respect the change in value, however we queue event only 
    //  if there is a change in value
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        // The RequestParameterMap holds the values received from the browser
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        String clientId = uiComponent.getClientId(facesContext);
        log.info("SLIDER DECODE");
        Object sliderValue = 50;
        SliderEntry slider = (SliderEntry) uiComponent;
        boolean foundInMap = false;
        if (requestParameterMap.containsKey(clientId)) {
            log.info("found client in param map");
            if (!slider.isSingleSubmit()) {
                foundInMap = true;
            }
        }
        if (requestParameterMap.containsKey(clientId + "_div")) {
            log.info("Found id=" + clientId + "_div in req param map");
        }
        if (requestParameterMap.containsKey(clientId + "_hidden")) {
            log.info("found id=" + clientId + "hidden in req param map");
        }

        //"ice.event.captured" should be holding the event source id
        if (requestParameterMap.containsKey("ice.event.captured")
                || foundInMap) {

            String source = String.valueOf(requestParameterMap.get("ice.event.captured"));

            if ("ice.ser".equals(requestParameterMap.get("ice.submit.type"))) {
                facesContext.renderResponse();
                return;
            }
            sliderValue = requestParameterMap.get(clientId);
            if (sliderValue == null) return;
            int submittedValue = 0;
            try {
                submittedValue = Integer.valueOf(sliderValue.toString());
                log.info("Decoded slider value [id:value] [" + clientId + ":" + sliderValue + "]");
            } catch (NumberFormatException nfe) {
                log.warning("NumberFormatException  Decoding value for [id:value] [" + clientId + ":" + sliderValue + "]");
            } catch (NullPointerException npe) {
                log.warning("NullPointerException  Decoding value for [id:value] [" + clientId + ":" + sliderValue + "]");
            }

            //If I am a source of event?
            if (clientId.equals(source) || foundInMap) {
                try {
                    int value = slider.getValue();
                    //if there is a value change, queue a valueChangeEvent    
                    if (value != submittedValue) {
                        uiComponent.queueEvent(new ValueChangeEvent(uiComponent,
                                new Integer(value), new Integer(submittedValue)));
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // The encodeBegin method, in the renderer, is responsible for rendering
    //  the html markup, as well as the javacript necessary for initialising
    //  the YUI javascript object, in the browser. Typically the encodeEnd(-)
    //  method and possibly the encodeChildren(-) method would be used too,
    //  but we've put all the rendering here, in this one method.
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        String clientId = uiComponent.getClientId(facesContext);
        ResponseWriter writer = facesContext.getResponseWriter();
        SliderEntry slider = (SliderEntry) uiComponent;

        // capture any children UIParameter (f:param) parameters.
//        uiParamChildren = Utils.captureParameters( slider );

        // Write outer div
        //prototype for jquery mobile
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute("data-role", "fieldcontain", null);
        String label = "noLabel";
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_div", HTML.ID_ATTR);
        //       writer.writeAttribute("name", clientId + "_div", null);

        if (slider.getLabel() != null) {
            label = slider.getLabel();
            writer.startElement("label", uiComponent);
            writer.writeAttribute("for", clientId, null);
            writer.writeText(label, null);
            writer.endElement("label");
        }
        writer.startElement("input", uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        writer.writeAttribute("type", "range", null);
        // if axis is y or vertical then set style to vertical 
        String axis = slider.getAxis();
        if (axis.equals("y")) {
            writer.writeAttribute("class", "mobi-slider-vertical", null);
        } else if (axis.equals("custom")) {
            writer.writeAttribute("class", "mobi-slider-custom", null);
        }
        //just use default of horizontal slider if it's neither of these
        boolean singleSubmit = slider.isSingleSubmit();
        if (slider.isDisabled())
            writer.writeAttribute("disabled", "disabled", null);
        if (slider.isReadonly())
            writer.writeAttribute("readonly", "readonly", null);
        int value = 0;
        try {
            value = slider.getValue();
        } catch (Exception e) {
            log.info("exception getting slider value");
            //any exception on getting value keep it at zero and don't need converters if only an int
        }
        // If the application has specified a string of CSS style(s), output it 
        PassThruAttributeWriter.renderNonBooleanAttributes(writer, uiComponent, slider.getAttributesNames());
        //   if (singleSubmit) writer.writeAttribute("onchange", "ice.se(event, '" + clientId + "_div');", null);
        writer.writeAttribute("onchange", "ice.mobi.submit('" + clientId + "', " + singleSubmit + ");", null);
        //	    PassThruAttributeWriter.renderBooleanAttributes(writer, uiComponent, slider.getBooleanAttNames());
        writer.endElement("input");
        writer.startElement("input", uiComponent);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("name", clientId + "_hidden", null);
        writer.writeAttribute("id", clientId + "_hidden", null);
        writer.writeAttribute("value", slider.getValue(), null);
        writer.endElement("input");
        writer.endElement(HTML.DIV_ELEM);

    }
}
