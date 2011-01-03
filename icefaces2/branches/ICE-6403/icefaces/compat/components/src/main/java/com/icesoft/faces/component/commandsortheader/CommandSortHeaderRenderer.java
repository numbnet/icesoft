/*
 * Version: MPL 1.1
 *
 * "The contents of this file are subject to the Mozilla Public License
 * Version 1.1 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations under
 * the License.
 *
 * The Original Code is ICEfaces 1.5 open source software code, released
 * November 5, 2006. The Initial Developer of the Original Code is ICEsoft
 * Technologies Canada, Corp. Portions created by ICEsoft are Copyright (C)
 * 2004-2010 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 *
 */

package com.icesoft.faces.component.commandsortheader;

import com.icesoft.faces.component.ext.HtmlDataTable;
import com.icesoft.faces.component.ext.renderkit.CommandLinkRenderer;
import com.icesoft.faces.component.ext.taglib.Util;
import com.icesoft.faces.component.util.CustomComponentUtils;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.util.CoreComponentUtils;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import java.io.IOException;
import java.util.Map;

public class CommandSortHeaderRenderer extends CommandLinkRenderer {
    
    public void decode(FacesContext facesContext, UIComponent uiComponent) {
        validateParameters(facesContext, uiComponent, UICommand.class);
        if (CoreComponentUtils.isJavaScriptDisabled(facesContext)) {
	        String commandLinkClientId = uiComponent.getClientId(facesContext);
	        Map requestParameterMap =
	                facesContext.getExternalContext().getRequestParameterMap();
	        if (requestParameterMap.containsKey(commandLinkClientId)) {
	            // this command link caused the submit so queue an event
	            uiComponent.queueEvent(new ActionEvent(uiComponent));
	        }
        } else {
        	super.decode(facesContext, uiComponent);
        }
    }	
    /*
     *  (non-Javadoc)
     * @see javax.faces.render.Renderer#encodeEnd(javax.faces.context.FacesContext, javax.faces.component.UIComponent)
     */
    public void encodeEnd(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, null);
        //Render if user is in given role
        if (Util.isEnabledOnUserRole(uiComponent)) {
            CommandSortHeader sortHeader = (CommandSortHeader) uiComponent;
            HtmlDataTable dataTable = sortHeader.findParentDataTable();

            Node child = null;
            String value = dataTable.getSortColumn();
            DOMContext domContext =
                    DOMContext.getDOMContext(facesContext, uiComponent);
            Element root = (Element) domContext.getRootNode();
            String headerClass = sortHeader.getStyleClass();
            if (headerClass != null) {
                root.setAttribute(HTML.CLASS_ATTR, headerClass);
            }
            child = (Element)root.getFirstChild();
            if (child != null) {
                if (child.getNodeType() == 1) { //span
                    child = child.getFirstChild();
                }
                value = child.getNodeValue();
            }
            
            Element div = root;
            Element btn = null;
            if (CoreComponentUtils.isJavaScriptDisabled(facesContext)) {
            	div = (Element) domContext.createElement(HTML.DIV_ELEM);
            	btn = (Element) domContext.createElement(HTML.INPUT_ELEM);
            	div.appendChild(btn);
            	btn.setAttribute(HTML.NAME_ATTR, root.getAttribute("id"));
            	btn.setAttribute(HTML.ID_ATTR, root.getAttribute("id"));
            	btn.setAttribute(HTML.TYPE_ATTR, "submit");
            	btn.setAttribute(HTML.VALUE_ATTR, value);
            	btn.setAttribute(HTML.CLASS_ATTR, "iceCmdLnkJSDis");
            	root.getParentNode().replaceChild(div, root);
            	root = div;
            }
            
            if (sortHeader.getColumnName().equals(dataTable.getSortColumn())) {

                if (dataTable.isSortAscending()) {
                    headerClass += "Asc";
                } else {
                    headerClass += "Desc";
                }
                Element arrowDiv = domContext.createElement(HTML.DIV_ELEM);
                arrowDiv.setAttribute(HTML.CLASS_ATTR, headerClass);
                arrowDiv.setAttribute("valign", "middle");   
	        	Element table = domContext.createElement(HTML.TABLE_ELEM);
	            Element tr = domContext.createElement(HTML.TR_ELEM);
	            table.appendChild(tr);
	            Element textTd = domContext.createElement(HTML.TD_ELEM);
	            if (CoreComponentUtils.isJavaScriptDisabled(facesContext)) {	
	             	textTd.appendChild(btn); 
	            } else {
	              	textTd.appendChild(domContext.createTextNode(value));  	
	            }
	            Element arrowTd = domContext.createElement(HTML.TD_ELEM);
	            tr.appendChild(textTd);
	            tr.appendChild(arrowTd);
	            arrowTd.appendChild(arrowDiv);
	            child.setNodeValue("");
	            root.appendChild(table);    
            }
        }
        super.encodeEnd(facesContext, uiComponent);
    }
}
