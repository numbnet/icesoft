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

package org.icemobile.jsp.tags;

import org.icemobile.component.IPanelPopup;
import org.icemobile.renderkit.PanelPopupCoreRenderer;


import java.io.IOException;
import java.util.logging.Logger;

import javax.servlet.jsp.JspTagException;
import org.icemobile.util.CSSUtils;

/**
 *
 */
public class PanelPopupTag extends BaseBodyTag implements IPanelPopup {

    private static Logger LOG = Logger.getLogger(PanelPopupTag.class.getName());
    private TagWriter writer;
    private PanelPopupCoreRenderer renderer;

    public int doStartTag() throws JspTagException {
    		         
  	    renderer= getRenderer();
        try {
            writer = new TagWriter(pageContext);
            renderer.encodeBegin(this, writer);
        	// popupPanel Header section
            if (headerText != null && !"".equals(headerText)) {
            	writer.startDiv();
            	writer.writeAttribute("id", id+"_title");
                writer.writeStyleClass(TITLE_CLASS + " " + CSSUtils.STYLECLASS_BAR_B);
                writer.writeText(headerText);
                writer.endElement();// Close title section
            }
           // writer.closeOffTag();
    	}
    	catch(IOException ioe){
    		LOG.severe("IOException starting panelPopup tag: " + ioe);
    	}
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspTagException {
        renderer= getRenderer();
    	try{
	       renderer.encodeEnd(this, writer);
	    }
		catch(IOException ioe){
			LOG.severe("IOException starting panelPopup tag: " + ioe);
		}
        return EVAL_PAGE;

    }


    private String headerText;
    private boolean visible;
    private boolean autoCenter;
    private String name;
    private boolean clientSide;
    private int height;
    private int width;

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isAutoCenter() {
        return autoCenter;
    }

    public void setAutoCenter(boolean autocenter) {
        this.autoCenter = autocenter;
    }

    public PanelPopupCoreRenderer getRenderer() {
        return new PanelPopupCoreRenderer();
    }

    public void setRenderer(PanelPopupCoreRenderer renderer) {
        this.renderer = renderer;
    }

    public String getHeaderText() {
        return headerText;
    }

    public void setHeaderText(String headerText) {
        this.headerText = headerText;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }

    public boolean isClientSide() {
        return true;
    }

    public void setClientSide(boolean clientSide) {
        this.clientSide = clientSide;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void release(){
 	    super.release();
		id = null;
        name=null;
		style = null;
		styleClass = null;
        renderer = null;
    }
}
