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

import org.icemobile.component.IButtonGroup;
import org.icemobile.jsp.tags.TagWriter;
import org.icemobile.renderkit.ButtonGroupCoreRenderer;
import org.icemobile.jsp.tags.BaseBodyTag;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.PageContext;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 *
 */
public class CommandButtonGroupTag extends BaseBodyTag implements IButtonGroup{

    private static Logger logger = Logger.getLogger(CommandButtonGroupTag.class.getName());

    private String orientation;
    private ButtonGroupCoreRenderer renderer;
    private TagWriter writer;
    private String selectedId;
    private String name;

     public int doStartTag() throws JspTagException {
        renderer = new ButtonGroupCoreRenderer();
         String attrName = this.id+"_sel";
        try{
            writer = new TagWriter(pageContext);
            if (selectedId!=null && !selectedId.equals("null") && !selectedId.trim().equals("")){
          //      System.out.println("setting selectedId="+selectedId+" into pageContet");
                pageContext.setAttribute(attrName,selectedId);
            }
            renderer.encodeBegin(this, writer);
  /*          if (pageContext.getAttribute(attrName)!=null){
                logger.info("set page attr to ="+pageContext.getAttribute(attrName));
            }*/
            writer.closeOffTag();
        } catch (IOException ioe){
            throw new JspTagException(" Error with startTag of CommandButtonGroupTag");
        }
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspTagException {
        try {

            renderer.encodeEnd(this,writer);
        }catch (IOException ioe){
            throw new JspTagException(" Error in endTag of CommandButtonGroupTag");
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

    public String getOrientation() {
        if (!(this.orientation.equals(IButtonGroup.ORIENTATION_HORIZONTAL)) && !(this.orientation.equals(IButtonGroup.ORIENTATION_VERTICAL))){
            if (logger.isLoggable(Level.FINER)) {
                logger.info("invalid orientation ="+this.orientation+" returning default = "+IButtonGroup.ORIENTATION_HORIZONTAL);
            }
            return IButtonGroup.ORIENTATION_HORIZONTAL; //default
        }
        return this.orientation;
    }

    public String getClientId(){
        return this.id;
    }

    public void setOrientation(String orient) {
        this.orientation = orient;
    }

    public String getSelectedId() {
        return selectedId;
    }

    public void setSelectedId(String selectedId) {
            this.selectedId = selectedId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void release(){
    //    System.out.println(this.id+ "commandButtonGroupTag release");
        super.release();
        this.writer= null;
        this.renderer=null;
    }
}
