package org.icefaces.mobi.component.contentstack;


import org.icefaces.mobi.api.ContentPaneController;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class ContentStack extends ContentStackBase implements ContentPaneController {
//public class ContentStack extends ContentStackBase  {
     public static final String CONTENT_WRAPPER_CLASS = "mobi-content-stack ";

     private String selectedId;

     public ContentStack() {
         super();
     }
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
         String id = getCurrentId();
         UIComponent uiC = findComponent(id);
         if (uiC!=null){
             selectedId = uiC.getClientId();
         }
         //for tagHandler do I need to ensure all children are of type ContentPane here??
         if (null!=selectedId){
            return selectedId;
         } else {  // return client Id of first child
            return null;
         }
     }

    public int getChildCount(){
        return 3;
    }
}
