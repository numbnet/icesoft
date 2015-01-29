/*
 * Copyright 2004-2014 ICEsoft Technologies Canada Corp.
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

package org.icefaces.mobi.component.contentstackmenu;


import org.icefaces.mobi.component.contentstack.ContentStack;
import org.icefaces.mobi.utils.JSFUtils;

public class ContentStackMenu extends ContentStackMenuBase {
    public static final String LAYOUTMENU_CLASS = "mobi-layoutmenu ";
    public static final String LAYOUTMENU_LIST_CLASS = "mobi-list ";

    private boolean openAccordionHandle;
    private boolean ulElementStarted = false;
    
    private ContentStack contentStack;
    
    public ContentStack getContentStack(){
        if( contentStack == null ){
            String contentStackId = getContentStackId();
            if( contentStackId != null ){
                contentStack = (ContentStack)JSFUtils.findComponent(contentStackId, this);
                if( contentStack == null ){
                    contentStack = (ContentStack)JSFUtils.findComponent(contentStackId, null);
                }
            }
        }
        return contentStack;
    }

    public boolean isOpenAccordionHandle() {
        return openAccordionHandle;
    }

    public void setOpenAccordionHandle(boolean openAccordionHandle) {
        this.openAccordionHandle = openAccordionHandle;
    }

    public boolean isUlElementStarted() {
        return ulElementStarted;
    }

    public void setUlElementStarted(boolean ulElementStarted) {
        this.ulElementStarted = ulElementStarted;
    }
}
