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

package org.icefaces.component.carousel;


import org.icefaces.component.list.OutputListItem;
import org.icefaces.component.utils.HTML;
import org.icefaces.component.utils.Utils;
import org.icefaces.util.EnvUtils;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import javax.faces.render.Renderer;

import java.io.IOException;
import java.util.List;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;


public class CarouselRenderer extends Renderer {
    private static Logger logger = Logger.getLogger(CarouselRenderer.class.getName());
    
    public void decode(FacesContext facesContext, UIComponent uiComponent){
    	Map<String,String> params = facesContext.getExternalContext().getRequestParameterMap();
    	Carousel carousel = (Carousel)uiComponent;
    	String clientId = carousel.getClientId(facesContext);
    	String hiddenField = clientId+"_hidden";
    	if (params.containsKey(hiddenField)){
    		int selected = Integer.parseInt(params.get(hiddenField));
    		logger.info("DECODE selected is="+selected);
    		carousel.setSelectedItem(selected);
    	}
    }
      
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Carousel carousel = (Carousel) uiComponent;
        //check to ensure children are all of type OutputListItem
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        String userDefinedClass = carousel.getStyleClass();
        String styleClass = Carousel.CAROUSEL_CLASS;
        if (userDefinedClass!=null){
        	 styleClass+=  userDefinedClass;
        }    
        writer.writeAttribute("class", styleClass, "styleClass");
		if(carousel.getStyle() != null) {
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
          throws IOException{
    	ResponseWriter writer = facesContext.getResponseWriter();
		if (carousel.getVar() !=null){
			carousel.setRowIndex(-1);
			for (int i=0; i< carousel.getRowCount(); i++){
				carousel.setRowIndex(i);
				writeListWrapper(carousel, writer);
				renderChildren(facesContext, carousel);
				writer.endElement(HTML.LI_ELEM);
			}
		}else {
			logger.info("Must have var for carousel items");
		}
		//set count attribute
		if (carousel.getRowCount() > -1){
			carousel.setCount(carousel.getRowCount());
		}
		
	}

	private void renderChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException {
		Iterator iterator = uiComponent.getChildren().iterator();
		while (iterator.hasNext()) {
			UIComponent child = (UIComponent) iterator.next();
			renderChild(facesContext, child);
		}
	}

	private void renderChild(FacesContext facesContext, UIComponent child) throws IOException {
		if (!child.isRendered()) {
			return;
		}
		//do we have to worry about encodeAll method???
		child.encodeBegin(facesContext);		
		if (child.getRendersChildren()) {
			child.encodeChildren(facesContext);
		} else {
			renderChildren(facesContext, child);
		}
		child.encodeEnd(facesContext);
	}
	private void writeListWrapper(Carousel carousel, ResponseWriter writer)
			throws IOException {
		writer.startElement(HTML.LI_ELEM, null);
		String itemStyleClass = Carousel.CAROUSEL_ITEM_CLASS;
	}

	public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
    throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        Carousel carousel = (Carousel) uiComponent;
		encodeCarouselList(carousel, facesContext);
         //no javascript tag for this component
        //check to ensure children are all of type OutputListItem
        writer.endElement(HTML.UL_ELEM);
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.DIV_ELEM);
        //now do the paginator for the carousel
        writer.startElement(HTML.DIV_ELEM, null);
        writer.writeAttribute("class", carousel.CAROUSEL_CURSOR_CLASS, null);
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute("class", "mobi-carousel-cursor-center", null);
        writer.startElement(HTML.UL_ELEM, null);
        writer.writeAttribute("class", carousel.CAROUSEL_CURSOR_LISTCLASS, null);
        int selected = carousel.getSelectedItem();
        int size = carousel.getRowCount();
        if (selected > size-1 || selected < 0){
        	selected = 0;
        }
        for (int i=0; i< size; i++){
        	writer.startElement(HTML.LI_ELEM, null);
        	if (selected == i){
        		writer.writeAttribute("class", "active", null);
        	}
        	writer.write(String.valueOf(i+1));
        	writer.endElement(HTML.LI_ELEM);
        }
        //do the list of dots for pagination
        writer.endElement(HTML.UL_ELEM);
        writer.endElement(HTML.DIV_ELEM);
        writer.endElement(HTML.DIV_ELEM);
        this.encodeHiddenSelected(facesContext, clientId, selected);
        renderScript(carousel, facesContext, clientId);
     }
    private void renderScript(Carousel carousel, FacesContext facesContext, String clientId) throws IOException {
        ResponseWriter writer= facesContext.getResponseWriter();
        writer.startElement("script", null);
        writer.writeAttribute("text", "text/javascript", null);
        //define mobi namespace if necessary
        writer.write("if (!window['mobi']) {"+
         " window.mobi = {};}\n");
        writer.write("ice.onLoad(function() { "+
            "mobi.carousel.loaded('"+clientId+"');"+
                 "});\n");
        writer.write("ice.onAfterUpdate(function() { "+
             "mobi.carousel.loaded('"+clientId+"');"+
                "});\n");
        writer.write("supportsOrientationChange = 'onorientationchange' in window," +
            "orientationEvent = supportsOrientationChange ? 'orientationchange' : 'resize';");	
//        writer.write("window.addEventListener(orientationEvent, function() {"+
//	          "  setTimeout(function () { "+
//	          "       mobi.carousel.carousels.refresh(); "+
//	          "  }, 100); "+
//	       " }, false);");
        writer.endElement("script");
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
		writer.writeAttribute("id", id+"_hidden", null);
		writer.writeAttribute("name", id+"_hidden", null);
		writer.writeAttribute("type", "hidden", null);
		writer.writeAttribute("value", String.valueOf(selectedIndex), null);
		writer.endElement("input");
	}
}
