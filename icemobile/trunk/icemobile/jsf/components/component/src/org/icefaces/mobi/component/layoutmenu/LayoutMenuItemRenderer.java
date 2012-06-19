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
package org.icefaces.mobi.component.layoutmenu;


import org.icefaces.mobi.component.contentpane.ContentPane;
import org.icefaces.mobi.component.contentstack.ContentStack;
import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.utils.HTML;

import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;


public class LayoutMenuItemRenderer extends BaseLayoutRenderer {
       private static Logger logger = Logger.getLogger(LayoutMenuItemRenderer.class.getName());

       public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        LayoutMenuItem item = (LayoutMenuItem) uiComponent;
        String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
        String clientId = item.getClientId();
        String parentId = item.getParent().getClientId();
        if (clientId.equals(source) || parentId.equals(source)) {
            try {
                if (!item.isDisabled()) {
                    uiComponent.queueEvent(new ActionEvent(uiComponent));
                    decodeBehaviors(facesContext, uiComponent);
                }
            } catch (Exception e) {
                logger.warning("Error queuing CommandButton event");
            }
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
             throws IOException {
         ResponseWriter writer = facesContext.getResponseWriter();
         LayoutMenuItem lmi = (LayoutMenuItem)uiComponent;
         String clientId = uiComponent.getClientId(facesContext);
         boolean disabled = lmi.isDisabled();
         boolean singleSubmit = lmi.isSingleSubmit();
         ClientBehaviorHolder cbh = (ClientBehaviorHolder)uiComponent;
     //    boolean hasBehaviors = !cbh.getClientBehaviors().isEmpty();
         String parentId = uiComponent.getParent().getClientId();
         UIComponent parent = uiComponent.getParent();
         if (!(parent instanceof LayoutMenu)){
             logger.warning("LayoutMenuItem must have parent of LayoutMenu");
             return;
         }
         LayoutMenu parentMenu = (LayoutMenu)parent;
         String contentStackId = parentMenu.getContentStackId();
         String stackClientId = null;
         boolean client = false;
         String selId = null;
         if (null!=lmi.getValue()){
             selId= (String)lmi.getValue();
         }
         UIViewRoot root = facesContext.getViewRoot();
         String selClientId = null;
         if (null == ((LayoutMenu) parent).getStackClientId()){
             UIComponent comp = root.findComponent(contentStackId);
             if (null != comp && comp instanceof ContentStack){
                 stackClientId=  comp.getClientId(facesContext);
                 ((LayoutMenu) parent).setStackClientId(stackClientId);
             }
             else{
                 logger.info("cant find stack id="+contentStackId);
             }
         }  else {
             stackClientId = ((LayoutMenu) parent).getStackClientId();
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
         writer.startElement(HTML.LI_ELEM, uiComponent);
         writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
         writer.writeAttribute(HTML.NAME_ATTR, clientId, HTML.NAME_ATTR);
         String label = lmi.getLabel();
         if (null==selId) {
             writer.writeAttribute("class", LayoutMenuItem.OUTPUTLISTITEMGROUP_CLASS, "class");
             writer.startElement(HTML.DIV_ELEM, uiComponent);
             writer.writeAttribute("class", LayoutMenuItem.OUTPUTLISTITEMDEFAULT_CLASS, "class");
             writer.write(label);
             writer.endElement(HTML.LI_ELEM);
         }else {
             if (lmi.isDisabled()){
                writer.writeAttribute("disabled", "disabled", null);
             }

             writer.writeAttribute("class", LayoutMenuItem.OUTPUTLISTITEM_CLASS, "class");
             writer.startElement(HTML.DIV_ELEM, uiComponent);
             writer.writeAttribute("class", LayoutMenuItem.OUTPUTLISTITEMDEFAULT_CLASS, "class");
             writer.startElement(HTML.ANCHOR_ELEM, uiComponent);
             //verify location of panel and get proper id of the contentPane for onclick
             // if url or target then put that in the onclick  otherwise use the value lmi.getValue()

             StringBuilder sb = new StringBuilder("mobi.layoutMenu.showContent('").append(stackClientId).append("', this");
             sb.append(",{ selectedId: '").append(lmi.getValue()).append("'");
             sb.append(",singleSubmit: ").append(lmi.isSingleSubmit());
             if (selClientId!=null){
                  sb.append(",selClientId: '").append(selClientId).append("'");
             }
             sb.append(",client: ").append(client);
             sb.append("});");
             if (stackClientId != null){
              writer.writeAttribute("onclick", sb.toString(), null);
             }
             writer.write( lmi.getLabel());
             writer.endElement(HTML.ANCHOR_ELEM);
             writer.endElement(HTML.DIV_ELEM);
         }
         writer.endElement(HTML.LI_ELEM);
    }

}
