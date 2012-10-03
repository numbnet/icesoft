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

package org.icemobile.jsp.tags;

import static org.icemobile.util.HTML.*;

import java.io.IOException;
import java.util.Stack;
import java.util.logging.Logger;

import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.TagSupport;

/**
 *
 */
public class PanelPopupTag extends TagSupport {

    private static Logger LOG = Logger.getLogger(PanelPopupTag.class.getName());
    public static final String HIDDEN_CONTAINER_CLASS = "mobi-panelpopup-container-hide ";
    public static final String BLACKOUT_PNL_HIDDEN_CLASS = "mobi-panelpopup-bg-hide ";
    public static final String CONTAINER_CLASS = "mobi-panelpopup-container ";
    public static final String BLACKOUT_PNL_CLASS = "mobi-panelpopup-bg ";
    public static final String TITLE_CLASS = "mobi-date-title-container ";

    public int doStartTag() throws JspTagException {
    	
    	try{
    		         
            TagWriter writer = new TagWriter(pageContext);
            // popup background
            writer.startDiv();
            writer.writeAttribute("id", id+"_bg");
            writer.writeStyleClass(BLACKOUT_PNL_HIDDEN_CLASS);
            writer.endElement();
            
        	// popup container section
        	writer.startDiv();
        	writer.writeAttribute("id", id+"_popup");
        	writer.writeStyleClassWithBase(styleClass, CONTAINER_CLASS);
        	
        	// popupPanel Title section
            if (title != null && !"".equals(title)) {
            	writer.startDiv();
            	writer.writeAttribute("id", id+"_title");
                writer.writeStyleClass(TITLE_CLASS);
                writer.writeTextNode(title);

                // Only do a close button if configured.
                if (isAutoCloseButton()) {
                	writer.startElement(INPUT_ELEM);
                	writer.writeAttribute(TYPE_ATTR, "button");
                	if (closeButtonLabel != null && !"".equals(closeButtonLabel)) {
                		writer.writeAttribute(VALUE_ATTR, closeButtonLabel);
                    } else {
                    	writer.writeAttribute(VALUE_ATTR, "Close");
                    }
                	writer.writeAttribute(ONCLICK_ATTR, "ice.mobi.panelpopup.close('"+id+"');");
                	writer.endElement();
                }
                writer.endElement();// Close title section
            }
    	}
    	catch(IOException ioe){
    		LOG.severe("IOException starting panelPopup tag: " + ioe);
    	}
        // Allow children to render. Container DIV is open
        return EVAL_BODY_INCLUDE;
    }

    public int doEndTag() throws JspTagException {
    	try{
	    	Stack<String> initialContext = new Stack<String>();
	    	initialContext.push(DIV_ELEM);
	    	TagWriter writer = new TagWriter(pageContext,initialContext);
	    	if (name == null || "".equals(name)) {
	            LOG.warning("No Name attribute for PanelPopup for value submission");
	        } else {
	        	writer.startElement(INPUT_ELEM);
	        	writer.writeAttribute(TYPE_ATTR, "hidden");
	        	writer.writeAttribute(ID_ATTR, id+"_hidden");
	        	writer.writeAttribute(NAME_ATTR, name);
	        	writer.endElement();
	        }
	    	writer.endElement();
	    	
	    	encodeScript(writer);
	    }
		catch(IOException ioe){
			LOG.severe("IOException starting panelPopup tag: " + ioe);
		}
        return EVAL_PAGE;

    }

    public void encodeScript(TagWriter writer) throws IOException{
    	writer.startElement(SPAN_ELEM);
    	writer.writeAttribute(ID_ATTR, id+"_srcSpan");
    	
    	writer.startElement(SCRIPT_ELEM);
    	writer.writeAttribute(TYPE_ATTR, "text/javascript");
    	writer.writeAttribute(ID_ATTR, id+"_script");
    	
    	writer.writeTextNode("ice.mobi.panelpopup.init('"+id+"',{visible:"+visible
    			+",autocenter:"+autocenter+"});");
    	writer.endElement();
    	writer.endElement();
    }


    private String id;
    private String name;
    private String style;
    private String styleClass;
    private String title;
    private boolean visible;
    private boolean autocenter;
    private boolean autoCloseButton;
    private String closeButtonLabel;

    public String getStyle() {
        return style;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStyleClass() {
        return styleClass;
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isAutocenter() {
        return autocenter;
    }

    public void setAutocenter(boolean autocenter) {
        this.autocenter = autocenter;
    }

    public boolean isAutoCloseButton() {
        return autoCloseButton;
    }

    public void setAutoCloseButton(boolean autoCloseButton) {
        this.autoCloseButton = autoCloseButton;
    }

    public String getCloseButtonLabel() {
        return closeButtonLabel;
    }

    public void setCloseButtonLabel(String closeButtonLabel) {
        this.closeButtonLabel = closeButtonLabel;
    }
}
