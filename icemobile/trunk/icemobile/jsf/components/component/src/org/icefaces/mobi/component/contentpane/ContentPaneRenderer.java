
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

import java.io.IOException;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.icefaces.mobi.api.ContentPaneController;
import org.icefaces.mobi.component.accordion.Accordion;
import org.icefaces.mobi.component.tabset.TabSet;
import org.icemobile.component.ITabPane;
import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.JSFUtils;

import org.icemobile.component.IContentPane;
import org.icemobile.renderkit.AccordionPaneCoreRenderer;
import org.icemobile.renderkit.ContentPaneCoreRenderer;
import org.icemobile.renderkit.IResponseWriter;


public class ContentPaneRenderer extends BaseLayoutRenderer {

    private static final Logger logger = Logger.getLogger(ContentPaneRenderer.class.getName());

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)throws IOException {
        UIComponent parent = uiComponent.getParent();
        IContentPane pane = (IContentPane) uiComponent;
        IResponseWriter writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        boolean amSelected = iAmSelected(facesContext, uiComponent);
        if (parent instanceof Accordion){
            //use core renderer for accordion
            AccordionPaneCoreRenderer renderer = new AccordionPaneCoreRenderer();
            renderer.encodeBegin(pane, writer, false, amSelected);
        } else if (parent instanceof TabSet){
            encodeTabSetPage(facesContext, uiComponent);
        } else {  //plain old contentStack parent
            ContentPaneCoreRenderer renderer = new ContentPaneCoreRenderer();
            renderer.encodeBegin(pane, writer, amSelected);
        }
    }

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent) throws IOException{
        ContentPane pane = (ContentPane)uiComponent;
        if (pane.isClient()){
            JSFUtils.renderChildren(facesContext, uiComponent);
        }
        else if (iAmSelected(facesContext, uiComponent)){
            JSFUtils.renderChildren(facesContext, uiComponent);
        }

    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
          throws IOException {
        IContentPane pane = (IContentPane)uiComponent;
        IResponseWriter writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        if (pane.isAccordionPane()){
            AccordionPaneCoreRenderer renderer = new AccordionPaneCoreRenderer();
            renderer.encodeEnd(pane, writer, false);
        } else {
            ContentPaneCoreRenderer crenderer = new ContentPaneCoreRenderer();
            crenderer.encodeEnd(pane, writer);
        }
    }


    private boolean iAmSelected(FacesContext facesContext, UIComponent uiComponent){
        UIComponent parent = uiComponent.getParent();
        String selectedId= null;
        if (parent instanceof ContentPaneController){
            ContentPaneController paneController = (ContentPaneController)parent;
            selectedId = paneController.getSelectedId();
            
            if (null == selectedId){
                UIComponent pComp = (UIComponent)parent;
                logger.warning("Parent controller of contentPane must have value for selectedId="+pComp.getClientId());
                return false;
            }
        }
        else {
            logger.warning("Parent must implement ContentPaneController-> has instead="+parent.getClass().getName());
            return false;
        }
        String id = uiComponent.getId();
        return (id.equals(selectedId));
    }


    private void encodeTabSetPage(FacesContext facesContext, UIComponent uiComponent)
            throws IOException{
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = uiComponent.getClientId(facesContext);
        ContentPane pane= (ContentPane)uiComponent;
        writer.startElement(HTML.DIV_ELEM, uiComponent);
        writer.writeAttribute(HTML.ID_ATTR, clientId+"_wrapper", HTML.ID_ATTR);
        String pageClass = ITabPane.TABSET_HIDDEN_PAGECLASS.toString();
        if (iAmSelected(facesContext, uiComponent)){
            pageClass = ITabPane.TABSET_ACTIVE_CONTENT_CLASS;
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



}
