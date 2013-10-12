
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
package org.icefaces.mobi.component.contentpane;

import static org.icemobile.util.HTML.CLASS_ATTR;
import static org.icemobile.util.HTML.DIV_ELEM;
import static org.icemobile.util.HTML.ID_ATTR;
import static org.icemobile.util.HTML.SPAN_ELEM;
import static org.icemobile.util.HTML.STYLE_ATTR;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.mobi.component.accordion.Accordion;
import org.icefaces.mobi.component.tabset.TabSet;
import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.JSFUtils;
import org.icemobile.util.CSSUtils;
import org.icemobile.util.ClientDescriptor;
import org.icemobile.util.Utils;


public class ContentPaneRenderer extends BaseLayoutRenderer {

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)throws IOException {
        UIComponent parent = uiComponent.getParent();
        ContentPane pane = (ContentPane) uiComponent;
        ResponseWriter writer =facesContext.getResponseWriter();
        boolean selected = pane.isSelected();
        if (parent instanceof Accordion){
            encodeAccordionPaneBegin(pane, writer, selected);
        } else if (parent instanceof TabSet){
            encodeTabSetPaneBegin(facesContext, uiComponent);
        } else {  
            encodeContentPaneBegin(pane, writer, selected);
        }
    }
    
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
          UIComponent parent = uiComponent.getParent();
          ContentPane pane = (ContentPane)uiComponent;
          ResponseWriter writer =facesContext.getResponseWriter();
          if (parent instanceof Accordion){
              encodeAccordionPaneEnd(pane, writer);
          } else if (parent instanceof TabSet){
              encodeTabSetPaneEnd(writer);
          } else {  
              encodeContentPaneEnd(writer);
          }
      }

    
    /* ****************** CONTENT STACK RENDERING ********************** */
    
    private void encodeContentPaneBegin(ContentPane pane, ResponseWriter writer, boolean selected)
        throws IOException{
        String clientId = pane.getClientId();
        writer.startElement(DIV_ELEM, pane);
        writer.writeAttribute(ID_ATTR, clientId+"_wrp", null);
        if( selected ){
            writer.writeAttribute(CLASS_ATTR, 
                    pane.isFirstPane() ? ContentPane.CONTENT_LEFT_SELECTED : ContentPane.CONTENT_SELECTED, null);
                
        }
        else{
            writer.writeAttribute(CLASS_ATTR, 
                    pane.isFirstPane() ? ContentPane.CONTENT_LEFT_HIDDEN : ContentPane.CONTENT_HIDDEN, null);
        }
        writer.writeAttribute("data-paneid", pane.getId(), null);

        writer.startElement(DIV_ELEM, pane);
        writer.writeAttribute(ID_ATTR, clientId, null);
        if (pane.getStyleClass()!=null){
            writer.writeAttribute(CLASS_ATTR, pane.getStyleClass(), null);
        }
        if (pane.getStyle()!=null){
            writer.writeAttribute(STYLE_ATTR, pane.getStyle(), null);
        }

    }
    
    public void encodeContentPaneEnd(ResponseWriter writer)
            throws IOException {
       writer.endElement(DIV_ELEM);
       writer.endElement(DIV_ELEM);
   }
    
    /* ****************** ACCORDION RENDERING ********************** */

    private void encodeAccordionPaneBegin(ContentPane pane, ResponseWriter writer, boolean selected)
    throws IOException{
        Accordion accordion = (Accordion)pane.getParent();
        String accordionId = accordion.getClientId();
        String clientId = pane.getClientId();
        boolean client = pane.isClient();
        String handleClass = "handle " + CSSUtils.STYLECLASS_BAR_B;
        String pointerClass = "pointer";
        writer.startElement(DIV_ELEM, pane);
        writer.writeAttribute(ID_ATTR, clientId+"_sect", null);
        StringBuilder styleClass = new StringBuilder("closed");
        if (selected){
            styleClass = new StringBuilder("open") ;
        }
        String userDefinedClass = pane.getStyleClass();
        if (userDefinedClass != null && userDefinedClass.length() > 0){
             handleClass+= " " + userDefinedClass;
             pointerClass+=" " + userDefinedClass;
        }
        writer.writeAttribute(CLASS_ATTR, styleClass, null);
        writer.writeAttribute(STYLE_ATTR, pane.getStyle(), null);
        writer.startElement(DIV_ELEM, pane);
        writer.writeAttribute(ID_ATTR, clientId+"_hndl", null);
        writer.writeAttribute(CLASS_ATTR, handleClass, null);
        StringBuilder args = new StringBuilder("ice.mobi.accordionController.toggleClient('");
        args.append(accordionId).append("', this, '").append(client).append("'");
        ClientDescriptor cd = accordion.getClient();
        if (Utils.isTransformerHack(cd)) {
            args.append(", true");
        }
        args.append(");");
        writer.writeAttribute(HTML.ONCLICK_ATTR, args.toString(), null);
        writer.startElement(SPAN_ELEM, pane);
        writer.writeAttribute(HTML.CLASS_ATTR, pointerClass, null);
        writer.endElement(SPAN_ELEM);
        String title = pane.getTitle();
        writer.writeText(title, null);
        writer.endElement(DIV_ELEM);
        writer.startElement(DIV_ELEM, pane);
        writer.writeAttribute(ID_ATTR, clientId+"wrp", null);
        writer.startElement(DIV_ELEM, pane);
        writer.writeAttribute(ID_ATTR, clientId, null);

    }
    
    private void encodeAccordionPaneEnd(ContentPane component, ResponseWriter writer)
            throws IOException {
       writer.endElement(DIV_ELEM);
       writer.endElement(DIV_ELEM);
       writer.endElement(DIV_ELEM);
   }

    /* ****************** TABSET RENDERING ********************** */

    private void encodeTabSetPaneBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException{
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        ContentPane pane= (ContentPane)uiComponent;
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_wrapper", HTML.ID_ATTR);
        String pageClass = ContentPane.TABSET_HIDDEN_PAGECLASS;
        if (pane.isSelected()){
            pageClass = ContentPane.TABSET_ACTIVE_CONTENT_CLASS;
        }
        writer.writeAttribute(HTML.CLASS_ATTR, pageClass, "class");
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
        if (pane.getStyle()!=null){
            writer.writeAttribute(HTML.STYLE_ATTR, pane.getStyle(), HTML.STYLE_ATTR);
        }
        if (pane.getStyleClass() !=null){
            writer.writeAttribute(HTML.CLASS_ATTR, pane.getStyleClass(), HTML.STYLE_CLASS_ATTR);
        }
    }
    
    public void encodeTabSetPaneEnd(ResponseWriter writer)
            throws IOException {
       writer.endElement(DIV_ELEM);
       writer.endElement(DIV_ELEM);
   }

    
    
    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException{
        ContentPane pane = (ContentPane)uiComponent;
        if (pane.isClient()){
            JSFUtils.renderChildren(facesContext, uiComponent);
        }
        else if (pane.isSelected()){
            JSFUtils.renderChildren(facesContext, uiComponent);
        }
    }
    






}
