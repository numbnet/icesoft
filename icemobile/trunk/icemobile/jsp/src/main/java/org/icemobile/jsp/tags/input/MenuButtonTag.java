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

import org.icemobile.component.IMenuButton;
import org.icemobile.jsp.tags.TagWriter;
import org.icemobile.renderkit.MenuButtonCoreRenderer;
import org.icemobile.jsp.tags.BaseBodyTag;
import javax.servlet.jsp.JspTagException;
import java.io.IOException;
import java.lang.String;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 */
public class MenuButtonTag extends BaseBodyTag implements IMenuButton{

    private static Logger logger = Logger.getLogger(MenuButtonTag.class.getName());

    private MenuButtonCoreRenderer renderer;
    private TagWriter writer;
    private String name;
    private String style;
    private String styleClass;
    private boolean disabled;
    private String buttonLabel;
    private String selectTitle;
    private String selectedValue;

     public int doStartTag() throws JspTagException {
        renderer = new MenuButtonCoreRenderer();
        try{
            writer = new TagWriter(pageContext);
            renderer.encodeBegin(this, writer);
            writer.closeOffTag();
            if (this.selectTitle==null || this.selectTitle.trim()
                .equals("")){
                this.selectTitle =  "Select";
            }
            renderer.encodeSelectTitle(this, writer);

        } catch (IOException ioe){
            throw new JspTagException(" Error with startTag of MenuButtonTag");
        }
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspTagException {
        try {
            renderer.encodeEnd(this,writer);
        }catch (IOException ioe){
            throw new JspTagException(" Error in endTag of MenuButtonTag");
        }
        return SKIP_BODY;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getClientId(){
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelectTitle() {
        return this.selectTitle;
    }

    public void setSelectTitle(String selectTitle) {
        this.selectTitle = selectTitle;
    }

    public String getButtonLabel() {
        return this.buttonLabel;
    }

    public void setButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
    }

    public boolean isDisabled() {
        return this.disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getSelectedValue() {
        return this.selectedValue;
    }

    public void setSelectedValue(String selectedValue) {
        this.selectedValue = selectedValue;
    }

    public void release(){
        this.selectedValue=null;
        super.release();
        this.writer= null;
        this.renderer=null;
    }

    public String getLastSelected() {
        return null;
    }
}
