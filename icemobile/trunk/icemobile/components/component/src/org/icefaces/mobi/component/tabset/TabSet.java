package org.icefaces.mobi.component.tabset;

import org.icefaces.mobi.api.ContentPaneController;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;


public class TabSet extends TabSetBase implements ContentPaneController {

     public static final StringBuilder TABSET_CONTAINER_CLASS = new StringBuilder("mobi-tabset ");
     public static final StringBuilder TABSET_TABS_CLASS = new StringBuilder("mobi-tabset-tabs ");
     public static final StringBuilder TABSET_ACTIVETAB_CLASS = new StringBuilder("activeTab ");
     public static final StringBuilder TABSET_CONTENT_CLASS = new StringBuilder("mobi-tabset-content ");
     public static final StringBuilder TABSET_VISIBLE_PAGECLASS = new StringBuilder("mobi-tabpage");
     public static final StringBuilder TABSET_HIDDEN_PAGECLASS = new StringBuilder("mobi-tabpage-hidden");

     public String getSelectedId(){
         int childCount = getChildCount();
         if (childCount== 0) {
             return null;
         }
         int index = getTabIndex();
         // String clientId = this.getClientId(FacesContext.getCurrentInstance());
         if (index< 0 || index > childCount){
             index = 0;
         }
         String id = getChildren().get(index).getId();
         UIComponent selectedComp = this.findComponent(id);
         FacesContext facesContext = FacesContext.getCurrentInstance();
         String panelClientId = selectedComp.getClientId(facesContext);
         //for tagHandler do I need to ensure all children are of type ContentPane here??
         if (null!=panelClientId){
            return panelClientId;
         } else {
            return null;
         }
     }
}
