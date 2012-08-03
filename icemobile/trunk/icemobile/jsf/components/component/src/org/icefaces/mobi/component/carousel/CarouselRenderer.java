/*
 * Copyright 2004-2012 ICEsoft Technologies Canada Corp.
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

package org.icefaces.mobi.component.carousel;


import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.Utils;
import org.icefaces.mobi.renderkit.BaseLayoutRenderer;

import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ValueChangeEvent;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class CarouselRenderer extends BaseLayoutRenderer {
    private static Logger logger = Logger.getLogger(CarouselRenderer.class.getName());
    private static final String JS_NAME = "carousel.js";
    private static final String JS_MIN_NAME = "carousel-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.carousel";
    private static final String JS_ISCROLL = "iscroll.js";
    private static final String JS_ISCROLL_MIN = "iscroll-min.js";
    private static final String LIB_ISCROLL = "org.icefaces.component.util";


    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map<String, String> params = facesContext.getExternalContext().getRequestParameterMap();
        Carousel carousel = (Carousel) uiComponent;
        String clientId = carousel.getClientId(facesContext);
        String hiddenField = clientId + "_hidden";
        if (params.containsKey(hiddenField)) {
            try {
                Integer selected = Integer.parseInt(params.get(hiddenField));
                int old = carousel.getSelectedItem();
                if (old != selected) {
                    carousel.setSelectedItem(selected);
                    uiComponent.queueEvent(new ValueChangeEvent(uiComponent,
                            old, selected));
                    decodeBehaviors(facesContext, carousel);
                }

            } catch (NumberFormatException e) {
                logger.log(Level.WARNING, "Error creating carousel value change event.",e);
            }
        }
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        writeJavascriptFile(facesContext, uiComponent, JS_NAME, JS_MIN_NAME, JS_LIBRARY,
                JS_ISCROLL, JS_ISCROLL_MIN, LIB_ISCROLL);
        Carousel carousel = (Carousel) uiComponent;
        writer.startElement(HTML.SPAN_ELEM, uiComponent);
        writer.writeAttribute("id", clientId, "ui");
        writer.writeAttribute("name", clientId, "name");
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId + "_carousel", HTML.ID_ATTR);
        String userDefinedClass = carousel.getStyleClass();
        String styleClass = Carousel.CAROUSEL_CLASS;
        if (userDefinedClass != null) {
            styleClass += userDefinedClass;
        }
        writer.writeAttribute("class", styleClass, "styleClass");
        if (carousel.getStyle() != null) {
            writer.writeAttribute("style", carousel.getStyle(), "style");
        }
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute("class", "mobi-carousel-scroller", null);
        writer.startElement(HTML.UL_ELEM, uiComponent);
        writer.writeAttribute("class", "mobi-carousel-list", null);

    }


    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
        //Rendering happens on encodeEnd
    }

    private void encodeCarouselList(Carousel carousel, FacesContext facesContext)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        if (carousel.getVar() != null) {
            carousel.setRowIndex(-1);
            for (int i = 0; i < carousel.getRowCount(); i++) {
                carousel.setRowIndex(i);
                writer.startElement(HTML.LI_ELEM, null);
                renderChildren(facesContext, carousel);
                writer.endElement(HTML.LI_ELEM);
            }
        } else if (facesContext.isProjectStage(ProjectStage.Development) ||
                logger.isLoggable(Level.FINER)) {
            logger.finer("Carousel must define the var and value attributes");
        }

    }



    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Carousel carousel = (Carousel) uiComponent;
        int selected = carousel.getSelectedItem();
        String savedId = carousel.getId();
        encodeCarouselList(carousel, facesContext);
        //no javascript tag for this component
        //check to ensure children are all of type OutputListItem
        writer.endElement(HTML.UL_ELEM);
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.DIV_ELEM);
        //now do the paginator for the carousel
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_list", HTML.ID_ATTR);
        writer.writeAttribute(HTML.NAME_ATTR, clientId+"_list", HTML.NAME_ATTR);
        Object prevLabel = carousel.getPreviousLabel();
        if (prevLabel !=null){
            renderPagination(facesContext, uiComponent, writer, String.valueOf(prevLabel),clientId, "prev" );
        }
        Object nextLabel = carousel.getNextLabel();
        if (nextLabel !=null ){
            renderPagination(facesContext, uiComponent, writer, String.valueOf(nextLabel),clientId, "next" );
        }
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute("class", Carousel.CAROUSEL_CURSOR_CLASS, null);
        writer.writeAttribute("style", carousel.getStyle(), null);
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute("class", Carousel.CAROUSEL_CURSOR_CURSOR_CENTER_CLASS, null);
        writer.startElement(HTML.UL_ELEM, null);
        writer.writeAttribute("class", Carousel.CAROUSEL_CURSOR_LISTCLASS, null);
        int size = carousel.getRowCount();
        if (selected > size - 1 || selected < 0) {
            selected = 0;
        }
        for (int i = 0; i < size; i++) {
            writer.startElement(HTML.LI_ELEM, null);
            if (selected == i) {
                writer.writeAttribute("class", "active", null);
            }
            writer.write(String.valueOf(i + 1));
            writer.endElement(HTML.LI_ELEM);
        }
        //do the list of dots for pagination
        writer.endElement(HTML.UL_ELEM);

        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.DIV_ELEM);
        this.encodeHiddenSelected(facesContext, clientId, selected);
        renderScript(uiComponent, facesContext, clientId);
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.SPAN_ELEM);
        ((Carousel) uiComponent).setRowIndex(-1);
    }

    private void renderPagination(FacesContext facesContext, UIComponent uiComponent, ResponseWriter writer,
                                  String value, String id,String ind) throws IOException{
        String call = "mobi.carousel.scrollTo('";
        String eventStr = Utils.isTouchEventEnabled(facesContext) ?
                TOUCH_START_EVENT : CLICK_EVENT;
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, id+"_"+ind, HTML.ID_ATTR);
        StringBuilder prevBuilder = new StringBuilder(call).append(id).append("', '").append(ind).append("'); return false");
        writer.writeAttribute(eventStr, prevBuilder.toString(), null);
        writer.write(value);
        writer.endElement(HTML.DIV_ELEM);
    }
    private void renderScript(UIComponent uiComponent, FacesContext facesContext, String clientId) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        Carousel carousel = (Carousel) uiComponent;
        ClientBehaviorHolder cbh = (ClientBehaviorHolder)uiComponent;
        boolean singleSubmit = carousel.isSingleSubmit();
        writer.startElement("span", uiComponent);
        writer.writeAttribute("id", clientId + "_script", "id");
        writer.startElement("script", null);
   //     writer.writeAttribute("id", clientId+"_script", "id");
        writer.writeAttribute("text", "text/javascript", null);
        //define mobi namespace if necessary
        StringBuilder builder = new StringBuilder(255);
        builder.append(clientId).append("',{ singleSubmit: ").append(singleSubmit);
        int hashcode = Utils.generateHashCode(carousel.getSelectedItem());
        builder.append(", hash: ").append(hashcode);
        boolean hasBehaviors = !carousel.getClientBehaviors().isEmpty();
        if (hasBehaviors){
            String behaviors = encodeClientBehaviors(facesContext, cbh, "change").toString();
            behaviors = behaviors.replace("\"", "\'");
            builder.append(behaviors);
        }
        builder.append("});");
        writer.write("mobi.carousel.loaded('"+builder.toString());
   /*     if (!Utils.isTouchEventEnabled(facesContext) && !Utils.isAndroid()) {
                writer.write("document.addEventListener('touchmove', function (e) { e.preventDefault(); }, false);");
        } */
        writer.endElement("script");
        writer.endElement("span");
    }

    /**
     * will render it's own children
     */
    public boolean getRendersChildren() {
        return true;
    }

    private void encodeHiddenSelected(FacesContext facesContext, String id, int selectedIndex) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement("input", null);
        writer.writeAttribute("id", id + "_hidden", null);
        writer.writeAttribute("name", id + "_hidden", null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("value", String.valueOf(selectedIndex), null);
        writer.endElement("input");
    }
}
