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

package org.icemobile.jsp.tags.layout;

import org.icemobile.renderkit.SplitPaneCoreRenderer;
import org.icemobile.jsp.tags.TagWriter;
import org.icemobile.util.ClientDescriptor;

import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SplitPaneTag extends SplitPaneBaseTag {

    private static final Logger logger = Logger.getLogger(SplitPaneTag.class.getName());

    private TagWriter writer;
    private SplitPaneCoreRenderer renderer;
    private String leftStyle;
    private String rightStyle;
    private String paneClass; //for the child fragments


    public void doInitBody() throws JspException {
        //prepare for evaulation of the body just before the first body evaulation - no return action
            /* check for attributes set */
        //init default values?
    }

    private void calculatePanelWidths() {
        leftStyle = "'width:" + String.valueOf(columnDivider)+"%'";
        rightStyle = "'width:"+String.valueOf(100-columnDivider) + "%'";
        //  this.setLeftStyle("style='width:" + String.valueOf(columnDivider)+"%'");
        paneClass = SplitPaneCoreRenderer.SPLITPANE_SCROLLABLE_CSS;
        if (!scrollable){
            paneClass = SplitPaneCoreRenderer.SPLITPANE_NONSCROLL_CSS;
        }
        if (getStyleClass()!=null){
            paneClass += " "+getStyleClass();
        }
        if (getStyle()!=null){
            leftStyle+= ", "+getStyle();
            rightStyle+= ", "+getStyle();
        }
    }

    public int doStartTag() throws JspTagException{
        /* put class here */
        calculatePanelWidths();
	    renderer= getRenderer();
        try {
            writer = new TagWriter(pageContext);
            renderer.encodeBegin(this, writer);
            writer.closeOffTag();
        } catch (IOException ioe){
            throw new JspTagException(" Error with startTag of SplitPaneTag");
        }
        return EVAL_BODY_INCLUDE;
    }

    /**
    * @return
    */
    public int doEndTag() throws JspTagException{
        renderer= getRenderer();
        try {
            this.renderer.encodeEnd(this, writer);
        } catch (IOException ioe){
            throw new JspTagException(" Error with endTag of SplitPaneTag") ;
        }
        return EVAL_PAGE;
    }
    
    public SplitPaneCoreRenderer getRenderer() {
        return new SplitPaneCoreRenderer();
    }
    public void setLeftStyle(String leftstyle){
        this.leftStyle = leftstyle;
    }

    public void setRightStyle(String rightstyle) {
        this.rightStyle = rightstyle;
    }

    public String getLeftStyle(){
        return this.leftStyle;
    }

    public String getRightStyle(){
        return this.rightStyle;
    }

    public String getPaneClass() {
        return paneClass;
    }

    public String getClientId() {
        return getId();
    }

    public ClientDescriptor getClient(){
        return ClientDescriptor.getInstance(getRequest());
    }

    protected HttpServletRequest getRequest(){
        return (HttpServletRequest)pageContext.getRequest();
    }

    protected HttpServletResponse getResponse(){
        return (HttpServletResponse)pageContext.getResponse();
    }

    public void release() {
		super.release();
        renderer = null;
        leftStyle = null;
        rightStyle = null;
	}
}