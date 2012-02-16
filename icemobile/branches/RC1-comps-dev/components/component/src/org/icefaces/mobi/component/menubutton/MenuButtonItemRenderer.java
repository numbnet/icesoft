package org.icefaces.mobi.component.menubutton;

import org.icefaces.mobi.component.panelconfirmation.PanelConfirmation;
import org.icefaces.mobi.component.submitnotification.SubmitNotificationRenderer;
import org.icefaces.mobi.utils.HTML;
import org.icefaces.mobi.utils.Utils;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import javax.faces.event.ActionEvent;
import org.icefaces.mobi.renderkit.BaseLayoutRenderer;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;


public class MenuButtonItemRenderer extends BaseLayoutRenderer{
       private static Logger logger = Logger.getLogger(MenuButtonRenderer.class.getName());

       public void decode(FacesContext facesContext, UIComponent uiComponent) {
        Map requestParameterMap = facesContext.getExternalContext().getRequestParameterMap();
        MenuButtonItem item = (MenuButtonItem) uiComponent;
        String source = String.valueOf(requestParameterMap.get("ice.event.captured"));
        String clientId = item.getClientId();
        String parentId = item.getParent().getClientId();
        if (clientId.equals(source) || parentId.equals(source)) {
            try {
                if (!item.isDisabled()) {
                    uiComponent.queueEvent(new ActionEvent(uiComponent));
                }
            } catch (Exception e) {
                logger.warning("Error queuing CommandButton event");
            }
        }
    }

    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
             throws IOException {
         ResponseWriter writer = facesContext.getResponseWriter();
         MenuButtonItem mbi = (MenuButtonItem)uiComponent;
         String clientId = uiComponent.getClientId(facesContext);
         boolean disabled = mbi.isDisabled();
         boolean singleSubmit = mbi.isSingleSubmit();
         ClientBehaviorHolder cbh = (ClientBehaviorHolder)uiComponent;
         boolean hasBehaviors = !cbh.getClientBehaviors().isEmpty();
         String parentId = uiComponent.getParent().getClientId();
         UIComponent parent = uiComponent.getParent();
         if (!(parent instanceof MenuButton)){
             logger.warning("MenuButtonItem must have parent of MenuButton");
             return;
         }
         MenuButton parentMenu = (MenuButton)parent;
         String subNotId = mbi.getSubmitNofification();
         String panelConfId = mbi.getPanelConfirmation();
         String submitNotificationId = null;
  //       StringBuilder builder = new StringBuilder("mobi.menubutton.initCfg('").append(parentId).append("','").append(clientId).append("',{singleSubmit: ");
        StringBuilder builder = new StringBuilder(",{singleSubmit: ").append(singleSubmit);
        builder.append(",disabled: ").append(disabled);
         if (null != subNotId) {
            submitNotificationId = SubmitNotificationRenderer.findSubmitNotificationId(uiComponent, subNotId);
            if (null != submitNotificationId ){
                builder.append(",snId: '").append(submitNotificationId).append("'");
            } else {
                logger.warning("no submitNotification id found for commandButton id="+clientId);
            }
         }
        if (null != panelConfId){
            ///would never use this with singleSubmit so always false when using with panelConfirmation
            //panelConf either has ajax request behaviors or regular ice.submit.
            if (hasBehaviors){
                String behaviors = this.encodeClientBehaviors(facesContext, cbh, "change").toString();
                behaviors = behaviors.replace("\"", "\'");
                builder.append(behaviors);
            }
            UIComponent uiForm = Utils.findParentForm(parent);
            PanelConfirmation panelConfirmation = (PanelConfirmation) (parent.findComponent(panelConfId));
            if (null==panelConfirmation){
                panelConfirmation = (PanelConfirmation)(uiForm.findComponent(panelConfId));
               // panelConfirmation = (PanelConfirmation)(uiForm.findComponent(panelConfId+"_popup"));
            }
         //   StringBuilder pcBuilder = PanelConfirmationRenderer.renderOnClickString(uiComponent, builder);
            if (null != panelConfirmation){
                //has panelConfirmation and it is found
                String panelConfirmationId = panelConfirmation.getClientId(facesContext);
                builder.append(",pcId: '").append(panelConfirmationId).append("'");
                StringBuilder noPanelConf = this.getCall(clientId, builder.toString());
                noPanelConf.append("});");
                parentMenu.addMenuItem(clientId, noPanelConf);
            } else { //no panelConfirmation found so commandButton does the job
                logger.warning("panelConfirmation of "+panelConfId+" NOT FOUND:- resorting to standard ajax form submit");
                StringBuilder noPanelConf = this.getCall(clientId, builder.toString());
                noPanelConf.append("});");
                parentMenu.addMenuItem(clientId, noPanelConf);
            }
        } else {  //no panelConfirmation requested so button does job
            StringBuilder noPanelConf = getCall(clientId, builder.toString()).append("});");
            parentMenu.addMenuItem(clientId, noPanelConf);
        }
         writer.startElement(HTML.OPTION_ELEM, uiComponent);
         writer.writeAttribute(HTML.ID_ATTR, clientId, HTML.ID_ATTR);
         writer.writeAttribute(HTML.NAME_ATTR, clientId, HTML.NAME_ATTR);
         if (mbi.isDisabled()) {
            writer.writeAttribute("disabled", "disabled", null);
         }
         writer.writeAttribute(HTML.VALUE_ATTR, mbi.getValue(), HTML.VALUE_ATTR);
         writer.write(mbi.getLabel());
         writer.endElement(HTML.OPTION_ELEM);
    }
    private StringBuilder getCall(String clientId, String builder ) {
        StringBuilder noPanelConf = new StringBuilder("'").append(clientId).append("'");
        noPanelConf.append(builder);
        return noPanelConf;
    }

}
