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
package org.icefaces.mobi.component.contentmenuitem;


import org.icefaces.mobi.component.contentpane.ContentPane;
import org.icefaces.mobi.component.contentstack.ContentStack;
import org.icefaces.mobi.component.contentnavbar.ContentNavBar;
import org.icefaces.mobi.component.contentstackmenu.*;
import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
//import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;


public class ContentMenuItemRenderer extends BaseLayoutRenderer {
       private static Logger logger = Logger.getLogger(ContentMenuItemRenderer.class.getName());

       public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        ContentMenuItem item = (ContentMenuItem) uiComponent;
        String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
        String clientId = item.getClientId();
        String parentId = item.getParent().getClientId();
        if (clientId.equals(source) || parentId.equals(source)) {
            try {
                if (!item.isDisabled()) {
                    uiComponent.queueEvent(new ActionEvent(uiComponent));
                //    decodeBehaviors(facesContext, uiComponent);
                }
            } catch (Exception e) {
                logger.warning("Error queuing CommandButton event");
            }
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
             throws IOException {
         UIComponent parent = uiComponent.getParent();
         if (!(parent instanceof ContentStackMenu) && !(parent instanceof ContentNavBar)){
             logger.warning("ContentMenuItem must have parent of ContentStackMenu or ContentNavBa");
             return;
         }
         if (parent instanceof ContentStackMenu){
             renderItemAsList(parent, facesContext, uiComponent);
         }
         if (parent instanceof ContentNavBar){
             renderItemAsButton(parent, facesContext, uiComponent);
         }
    }

    private void renderItemAsButton(UIComponent parent, FacesContext facesContext, UIComponent uiComponent)
        throws IOException {
        ContentNavBar parentMenu = (ContentNavBar)parent;
        ContentMenuItem item = (ContentMenuItem)uiComponent;
        ResponseWriter writer = facesContext.getResponseWriter();
        String clientId = item.getClientId(facesContext);
        String stackClientId = null;
        StringBuilder menubuttonClass = new StringBuilder(ContentNavBar.CONTENTNAVBAR_BUTTON_MENU_CLASS);
        StringBuilder buttonClass = new StringBuilder (ContentNavBar.CONTENTNAVBAR_BUTTON_CLASS);
        UIComponent stack = findParentContentStack(uiComponent);
        // user specified style class
        String userDefinedClass = item.getStyleClass();
        if (userDefinedClass != null && userDefinedClass.length() > 0){
            buttonClass.append(" ").append(userDefinedClass);
            menubuttonClass.append(" ").append(userDefinedClass);
        }
        if (item.isDisabled()){
            writer.writeAttribute("disabled", "disabled", null);
        }
        if (item.getUrl() != null){
            writer.writeAttribute("href", getResourceURL(facesContext,item.getUrl()), null);
        } else {
            if (stack!=null){
                if (item.getValue() !=null){
                    stackClientId = stack.getClientId(facesContext);
                    String valId = String.valueOf(item.getValue());
                    UIComponent pane = stack.findComponent(valId);
                    writer.startElement(HTML.ANCHOR_ELEM, uiComponent);
                    writer.writeAttribute("class",menubuttonClass , "class");
                    StringBuilder sb = new StringBuilder("mobi.layoutMenu.showContent('").append(stackClientId);
                    sb.append("', this");
                    sb.append(",{ selectedId: '").append(item.getValue()).append("'");
                    sb.append(",singleSubmit: ").append(item.isSingleSubmit());
                    if (pane!=null){
                        String paneId = pane.getClientId(facesContext);
                        sb.append(",selClientId: '").append(paneId).append("'");
                        ContentPane cp = (ContentPane)pane;
                        sb.append(",client: ").append(cp.isClient());
                    }
                    sb.append("});");
                    if ( !item.isDisabled()){
                          writer.writeAttribute("onclick", sb.toString(), null);
                    }
                    writer.write(item.getLabel());
                writer.endElement(HTML.ANCHOR_ELEM);
                }
            }
        }
    }

    private void renderItemAsList(UIComponent parent, FacesContext facesContext, UIComponent uiComponent)
                    throws IOException{
         ContentStackMenu parentMenu = (ContentStackMenu)parent;
         ContentMenuItem lmi = (ContentMenuItem)uiComponent;
         ResponseWriter writer = facesContext.getResponseWriter();
         String clientId = lmi.getClientId(facesContext);
         String contentStackId = parentMenu.getContentStackId();
         String stackClientId = null;
         boolean client = false;
         String selId = null;
         if (null!=lmi.getValue()){
             selId= (String)lmi.getValue();
         }
         UIViewRoot root = facesContext.getViewRoot();
         String selClientId = null;
         if (null == ((ContentStackMenu) parent).getStackClientId()){
             UIComponent comp = root.findComponent(contentStackId);
             if (null != comp && comp instanceof ContentStack){
                 stackClientId=  comp.getClientId(facesContext);
                 ((ContentStackMenu) parent).setStackClientId(stackClientId);
             }
             else{
                 logger.info("cant find stack id="+contentStackId);
             }
         }  else {
             stackClientId = ((ContentStackMenu) parent).getStackClientId();
         }
                          //find the clientId of the selected Pane
         if (selId !=null){
             UIComponent comp = root.findComponent(contentStackId);
             UIComponent pane = comp.findComponent(selId);
             if (pane!=null ){
                 selClientId= pane.getClientId(facesContext);
                 client = ((ContentPane)pane).isClient();
             }
         }
         String icon = lmi.getIcon();
         writer.startElement(HTML.LI_ELEM, uiComponent);
         writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
         writer.writeAttribute(HTML.NAME_ATTR, clientId, HTML.NAME_ATTR);
         String label = lmi.getLabel();
         if (null==selId) {
             writer.writeAttribute("class", ContentMenuItem.OUTPUTLISTITEMGROUP_CLASS, "class");
             writer.startElement(HTML.DIV_ELEM, uiComponent);
             writer.writeAttribute("class", ContentMenuItem.OUTPUTLISTITEMDEFAULT_CLASS, "class");
             writer.write(label);
             writer.endElement(HTML.LI_ELEM);
         }else {
             writer.writeAttribute("class", ContentMenuItem.OUTPUTLISTITEM_CLASS, "class");
             writer.startElement(HTML.DIV_ELEM, uiComponent);
             writer.writeAttribute("class", ContentMenuItem.OUTPUTLISTITEMDEFAULT_CLASS, "class");
             writer.startElement(HTML.ANCHOR_ELEM, uiComponent);
             //verify location of panel and get proper id of the contentPane for onclick
             // if url or target then put that in the onclick  otherwise use the value lmi.getValue()
             if (lmi.isDisabled()){
                writer.writeAttribute("disabled", "disabled", null);
             }
             if (lmi.getUrl() != null){
                 writer.writeAttribute("href", getResourceURL(facesContext,lmi.getUrl()), null);
             }
             else {
                 StringBuilder sb = new StringBuilder("mobi.layoutMenu.showContent('").append(stackClientId).append("', this");
                 sb.append(",{ selectedId: '").append(lmi.getValue()).append("'");
                 sb.append(",singleSubmit: ").append(lmi.isSingleSubmit());
                 if (selClientId!=null){
                      sb.append(",selClientId: '").append(selClientId).append("'");
                 }
                 sb.append(",client: ").append(client);
                 sb.append("});");
                 if (stackClientId != null && !lmi.isDisabled()){
                  writer.writeAttribute("onclick", sb.toString(), null);
                 }
             }
             if (icon !=null){

             }
             writer.write( lmi.getLabel());
             writer.endElement(HTML.ANCHOR_ELEM);
             writer.endElement(HTML.DIV_ELEM);
         }
         writer.endElement(HTML.LI_ELEM);
    }


}
