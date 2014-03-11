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

package org.icefaces.mobi.component.carousel;


import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.application.ProjectStage;
import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ValueChangeEvent;

import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icemobile.renderkit.CarouselCoreRenderer;
import org.icemobile.component.ICarousel;
import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.MobiJSFUtils;
import org.icefaces.mobi.utils.JSONBuilder;


public class CarouselRenderer extends BaseLayoutRenderer {
    private static final Logger logger = Logger.getLogger(CarouselRenderer.class.getName());
    private static final String JS_NAME = "carousel.js";
    private static final String JS_MIN_NAME = "carousel-min.js";
    private static final String JS_LIBRARY = "org.icefaces.component.carousel";


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

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        Carousel carousel = (Carousel) uiComponent;
        String clientId = uiComponent.getClientId(facesContext);
        //jsp does not write the carousel js file out so core renderer won't bother
        writeJavascriptFile(facesContext, uiComponent, JS_NAME, JS_MIN_NAME, JS_LIBRARY);
        ResponseWriterWrapper writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        CarouselCoreRenderer renderer = new CarouselCoreRenderer();
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_iSlib");
        if (!isScriptLoaded(facesContext, ICarousel.JS_ISCROLL) && 
            !MobiJSFUtils.getClientDescriptor().isIE8orLessBrowser()){
            renderer.encodeIScrollLib(carousel, writer);
            setScriptLoaded(facesContext, ICarousel.JS_ISCROLL);
        }
        writer.endElement(HTML.DIV_ELEM);
        renderer.encodeBegin(carousel, writer);
        /* writing list is part of UISeries so jsf only */
        encodeCarouselList(carousel, facesContext);
        boolean hasBehaviors = !carousel.getClientBehaviors().isEmpty();
        if (hasBehaviors){
           JSONBuilder jb = JSONBuilder.create();
           this.encodeClientBehaviors(facesContext, carousel, jb);
   // System.out.println(" jb to string="+jb.toString());
            String bh = ", "+jb.toString();
            bh = bh.replace("\"", "\'");
   // System.out.println(" behaviors for commandButton="+bh);
            carousel.setBehaviors(bh);
        } else {
            carousel.setBehaviors(null);
        }
        renderer.encodeEnd(carousel, writer);
        ((Carousel) uiComponent).setRowIndex(-1);

    }

    public void encodeChildren(FacesContext facesContext, UIComponent component) throws IOException {
        //Rendering happens on encodeBegin and encodeEnd
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

    public boolean getRendersChildren() {
        return true;
    }

}
