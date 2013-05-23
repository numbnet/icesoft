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

import org.icemobile.component.IPanelConfirmation;
import org.icemobile.jsp.tags.BaseSimpleTag;
import org.icemobile.jsp.tags.TagWriter;
import org.icemobile.renderkit.PanelConfirmationCoreRenderer;

import javax.servlet.jsp.JspTagException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 *
 */
public class PanelConfirmationTag extends BaseSimpleTag implements IPanelConfirmation{

    private static Logger LOG = Logger.getLogger(PanelConfirmationTag.class.getName());

    private PanelConfirmationCoreRenderer renderer;
    private TagWriter writer;
    private String title;
    private String message;
    private String type;
    private String cancelLabel;
    private String acceptLabel;

     public void doTag() throws JspTagException {
        renderer = new PanelConfirmationCoreRenderer();
        try{
            writer = new TagWriter(getContext());
            renderer.encodeEnd(this, writer);
        } catch (IOException ioe){
            throw new JspTagException(" Error with startTag of CommandButtonGroupTag");
        }
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String text) {
        this.title = text;
    }

    public String getId() {
        return id;
    }

    public void setAcceptLabel(String acceptLbl) {
        this.acceptLabel = acceptLbl;
    }

    public String getAcceptLabel() {
        return acceptLabel;
    }

    public void setCancelLabel(String cancelLbl) {
        this.cancelLabel = cancelLbl;
    }

    public String getCancelLabel() {
        return this.cancelLabel;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
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


    public void release(){
        super.release();
        this.writer= null;
        this.renderer=null;
    }
}
