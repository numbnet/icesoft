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
package org.icefaces.mobi.component.contentstack;


import org.icefaces.mobi.api.ContentPaneController;

import javax.annotation.PostConstruct;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

public class ContentStack extends ContentStackBase implements ContentPaneController {
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
         return this.selectedId;
     }

     @PostConstruct
     public void init(){
        this.findMySelectedId();
     }

     public void findMySelectedId(){
        int childCount = getChildCount();
        if (childCount > 0 ){
            String id = getCurrentId();
            if (null!=id){
                UIComponent uiC = findComponent(id);
                if (null!= uiC ){
                    if (null!= uiC.getClientId()) {
                        this.selectedId = uiC.getClientId();
                    }
                    else {
                        this.selectedId = uiC.getChildren().get(0).getClientId();
                    }
                }
            }
        }
        else {
          this.selectedId= null;
        }
     }


}
