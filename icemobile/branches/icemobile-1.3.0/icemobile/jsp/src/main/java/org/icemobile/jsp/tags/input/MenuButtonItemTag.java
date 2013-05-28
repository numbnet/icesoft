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

package org.icemobile.jsp.tags.input;

import org.icemobile.component.IMenuButtonItem;
import org.icemobile.jsp.tags.TagWriter;
import org.icemobile.renderkit.MenuButtonItemCoreRenderer;
import org.icemobile.jsp.tags.BaseSimpleTag;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;
import java.lang.Object;
import java.lang.String;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 */
public class MenuButtonItemTag extends BaseSimpleTag implements IMenuButtonItem{

    private static Logger logger = Logger.getLogger(MenuButtonItemTag.class.getName());

    private String label;
    private Object value;
    private MenuButtonItemCoreRenderer renderer;
    private TagWriter writer;
    private boolean disabled;
    private String style;
    private String panelConfirmation;
    private String submitNotification;
    private String behaviors = null;
    private boolean singleSubmit=false;

     public void doTag() throws JspTagException {
        renderer = new MenuButtonItemCoreRenderer();
        try{
            writer = new TagWriter(getContext());
            renderer.encodeEnd(this, writer);
        } catch (IOException ioe){
            throw new JspTagException(" Error with startTag of CommandButtonGroupTag");
        }
    }


    public String getStyle() {
        return this.style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

 /*   public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getClientId(){
        return this.id;
    }  */

    public String getLabel() {
        return this.label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public Object getValue() {
        return this.value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getPanelConfirmation() {
        return this.panelConfirmation;
    }

    public void setPanelConfirmation(String panelConfirmation) {
        this.panelConfirmation = panelConfirmation;
    }

    public String getSubmitNotification() {
        return submitNotification;
    }

    public void setSubmitNotification(String submitNotification) {
        this.submitNotification = submitNotification;
    }

    private boolean getSingleSubmit(){
        return false;
    }

    public String getPanelConfirmationId(){
        return this.panelConfirmation;
    }

    public String getSubmitNotificationId(){
        return this.submitNotification;
    }

    public String getBehaviors() {
        return this.behaviors;
    }
    public boolean isSingleSubmit(){
        return this.singleSubmit;
    }

    public void release(){
        super.release();
        this.writer= null;
        this.renderer=null;
    }
}
