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

import org.icemobile.component.ISubmitNotification;
import org.icemobile.jsp.tags.BaseBodyTag;
import org.icemobile.jsp.tags.TagWriter;
import org.icemobile.renderkit.SubmitNotificationCoreRenderer;

import javax.servlet.jsp.JspTagException;
import java.io.IOException;
import java.util.logging.Logger;

/**
 *
 */
public class SubmitNotificationTag extends BaseBodyTag implements ISubmitNotification{

    private static Logger LOG = Logger.getLogger(SubmitNotificationTag.class.getName());

    private SubmitNotificationCoreRenderer renderer;
    private TagWriter writer;
    private String style;
    private String styleClass;
    private boolean disabled;

    public int doStartTag() throws JspTagException {
        renderer = new SubmitNotificationCoreRenderer();
        try{
            writer = new TagWriter(pageContext);
            renderer.encodeBegin(this, writer);
            writer.closeOffTag();
        } catch (IOException ioe){
            throw new JspTagException(" Error with startTag of SubmitNotificationTag");
        }
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspTagException {
        try {
            renderer.encodeEnd(this,writer);
        }catch (IOException ioe){
            throw new JspTagException(" Error in endTag of SubmitNotificationTag");
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

    public boolean isDisabled(){
        return this.disabled;
    }

    public void setDisabled(boolean disabled){
        this.disabled = disabled;
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
