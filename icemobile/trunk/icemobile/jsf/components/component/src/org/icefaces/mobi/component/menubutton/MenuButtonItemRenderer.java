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
package org.icefaces.mobi.component.menubutton;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.icefaces.mobi.component.panelconfirmation.PanelConfirmation;
import org.icefaces.mobi.component.submitnotification.SubmitNotificationRenderer;
import org.icefaces.mobi.component.menubutton.MenuButton;
import org.icefaces.mobi.component.menubutton.MenuButtonGroup;
import org.icefaces.mobi.renderkit.BaseLayoutRenderer;
import org.icefaces.mobi.renderkit.ResponseWriterWrapper;
import org.icefaces.mobi.utils.JSFUtils;

import org.icemobile.component.IMenuButton;
import org.icemobile.component.IMenuButtonGroup;
import org.icemobile.component.IMenuButtonItem;
import org.icemobile.renderkit.IResponseWriter;
import org.icemobile.renderkit.MenuButtonItemCoreRenderer;


public class MenuButtonItemRenderer extends BaseLayoutRenderer{
    private static final Logger logger = Logger.getLogger(MenuButtonRenderer.class.getName());

    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        MenuButtonItem item = (MenuButtonItem) uiComponent;
        String source = String.valueOf(facesContext.getExternalContext().getRequestParameterMap().get("ice.event.captured"));
        String clientId = item.getClientId();
        String parentId = item.getParent().getClientId();
        if (clientId.equals(source) || parentId.equals(source)) {
            try {
                if (!item.isDisabled()) {
                    uiComponent.queueEvent(new ActionEvent(uiComponent));
                    decodeBehaviors(facesContext, uiComponent);
                    
                    UIComponent parent = item.getParent();
                    if( parent instanceof MenuButtonGroup ){
                        parent = parent.getParent();
                    }
                    if( parent instanceof MenuButton ){
                        ((MenuButton)parent).setLastSelected((String)item.getValue());
                    }
                }
            } catch (Exception e) {
                logger.warning("Error queuing CommandButton event");
            }
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
             throws IOException {
        UIComponent parent = uiComponent.getParent();
        if (!(parent instanceof IMenuButton) && !(parent instanceof IMenuButtonGroup)){
            logger.warning("MenuButtonItem must have parent of MenuButton");
            return;
        }
        UIComponent uiForm = JSFUtils.findParentForm(parent);
        setValuesForCoreRendering(facesContext, uiComponent, uiForm);
        IResponseWriter writer = new ResponseWriterWrapper(facesContext.getResponseWriter());
        MenuButtonItemCoreRenderer renderer = new MenuButtonItemCoreRenderer();
        IMenuButtonItem item = (IMenuButtonItem)uiComponent;
        renderer.encodeEnd(item, writer);

    }

    private void setValuesForCoreRendering(FacesContext facesContext, UIComponent uiComponent,
                        UIComponent form) throws IOException{
        MenuButtonItem mbi = (MenuButtonItem)uiComponent;
        String clientId = uiComponent.getClientId(facesContext);
        String subNotId = mbi.getSubmitNotification();
        String panelConfId = mbi.getPanelConfirmation();
        String submitNotificationId = null;
        ClientBehaviorHolder cbh = (ClientBehaviorHolder)uiComponent;
        boolean hasBehaviors = !cbh.getClientBehaviors().isEmpty();
        if (hasBehaviors){
                String behaviors = this.encodeClientBehaviors(facesContext, cbh, "change").toString();
                behaviors = behaviors.replace("\"", "\'");
                mbi.setBehaviors(behaviors);
            }
        if (null != subNotId && subNotId.length()>0) {
            submitNotificationId = SubmitNotificationRenderer.findSubmitNotificationId(uiComponent, subNotId);
            if (null == submitNotificationId){
                //try another way as this above one is limited when finding a namingcontainer
                if (form!=null){
                    UIComponent subObj = form.findComponent(subNotId);
                    if (null!= subObj)   {
                        submitNotificationId = subObj.getClientId();
                    }
                }
            }
            if (null != submitNotificationId ){
                mbi.setSubmitNotificationId(submitNotificationId);
            } else {
                logger.warning("no submitNotification id found for id="+clientId);
            }
        }
        PanelConfirmation pc = null;
        if (null != panelConfId && panelConfId.length()>1){
            pc = (PanelConfirmation)(form.findComponent(panelConfId));
            if (null != pc){
                //has panelConfirmation and it is found
                String panelConfirmationId = pc.getClientId(facesContext);
                mbi.setPanelConfirmationId(panelConfirmationId);
            }
        }
    }

}
