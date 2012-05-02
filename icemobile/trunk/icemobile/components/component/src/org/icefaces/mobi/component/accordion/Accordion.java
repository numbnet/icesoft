package org.icefaces.mobi.component.accordion;


import org.icefaces.mobi.api.ContentPaneController;

import javax.faces.component.UIComponent;
import javax.faces.component.behavior.FacesBehavior;
import javax.faces.context.FacesContext;

public class Accordion extends AccordionBase implements ContentPaneController {
     public static final String ACCORDION_CLASS = "mobi-accordion";
     public static final String ACCORDION_RIGHT_POINTING_TRIANGLE = "&#9654;";
     public static final String ACCORDION_RIGHT_POINTING_POINTER= "&#9658;";
     public static final String ACCORDION_LEFT_POINTING_TRIANGLE = "&#9664;";
     public static final String ACCORDION_LEFT_POINTING_POINTER= "&#9668;";
     /**
     * method is required by ContentPaneController interface
     * returns null if their are no children of type contentPane or no children at all.
     * If activeIndex is outside of the range of 0 -> number of children -1, then the default
     * valid value is the first child.
     * @return
     */

     public String getSelectedId(){
         int childCount = getChildCount();
         if (childCount== 0) {
             return null;
         }
         int index = getActiveIndex();
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
