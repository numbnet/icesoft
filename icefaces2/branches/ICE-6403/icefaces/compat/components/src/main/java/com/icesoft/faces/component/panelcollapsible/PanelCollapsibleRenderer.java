/*
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
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
 * Alternatively, the contents of this file may be used under the terms of
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"
 * License), in which case the provisions of the LGPL License are
 * applicable instead of those above. If you wish to allow use of your
 * version of this file only under the terms of the LGPL License and not to
 * allow others to use your version of this file under the MPL, indicate
 * your decision by deleting the provisions above and replace them with
 * the notice and other provisions required by the LGPL License. If you do
 * not delete the provisions above, a recipient may use your version of
 * this file under either the MPL or the LGPL License."
 */

package com.icesoft.faces.component.panelcollapsible;

import com.icesoft.faces.component.ExtendedAttributeConstants;
import com.icesoft.faces.component.util.CustomComponentUtils;
import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.context.effects.JavascriptContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeRenderer;
import com.icesoft.faces.util.CoreUtils;
import com.icesoft.util.CoreComponentUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;

import javax.faces.application.Application;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.Iterator;

public class PanelCollapsibleRenderer extends DomBasicRenderer {
    private static final String[] passThruAttributes =
            ExtendedAttributeConstants.getAttributes(ExtendedAttributeConstants.ICE_PANELCOLLAPSIBLE);

    private static Log log = LogFactory.getLog(PanelCollapsibleRenderer.class);


    public boolean getRendersChildren() {
        return true;
    }

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent) throws IOException {
        PanelCollapsible panelCollapsible = (PanelCollapsible) uiComponent;
        DOMContext domContext = DOMContext.attachDOMContext(facesContext, uiComponent);
        if (!domContext.isInitialized()) {
            Element rootSpan = domContext.createElement(HTML.DIV_ELEM);
            domContext.setRootNode(rootSpan);
            setRootElementId(facesContext, rootSpan, uiComponent);
        }
        Element root = (Element) domContext.getRootNode();
        PassThruAttributeRenderer.renderHtmlAttributes(facesContext, uiComponent, passThruAttributes);
        root.setAttribute(HTML.CLASS_ATTR, panelCollapsible.getStyleClass());

        //create "header" div and append to the parent, don't render any children yet
        Element header = (Element) domContext.createElement(HTML.DIV_ELEM);
        header.setAttribute(HTML.CLASS_ATTR, panelCollapsible.getHeaderClass());
        root.appendChild(header);

        //create "contents" div and append to the parent, don't render any children yet        
        Element contents = (Element) domContext.createElement(HTML.DIV_ELEM);
        contents.setAttribute(HTML.CLASS_ATTR, panelCollapsible.getContentClass());
        root.appendChild(contents);

        //add click handler if not disabled and toggleOnClick is set to true
        if (panelCollapsible.isToggleOnClick() &&
                !panelCollapsible.isDisabled()) {
			if (CoreComponentUtils.isJavaScriptDisabled(facesContext)) {
	            UIComponent form = findForm(uiComponent);
	            if (form == null) {
	                throw new FacesException("PanelCollapsible must be contained within a form");
	            } 

	            Element div = domContext.createElement(HTML.DIV_ELEM);
	            div.setAttribute(HTML.STYLE_ATTR, "position:relative;padding:0px;background-image:none;width:100%;");
	            header.setAttribute(HTML.ID_ATTR, uiComponent.getClientId(facesContext) + "hdr");
	            header.appendChild(div);
	            //this anchor should be known by the component only, so we are defining style to the component level
	            Element button = domContext.createElement(HTML.INPUT_ELEM);
	            button.setAttribute(HTML.STYLE_ATTR, 
                    "cursor:pointer; border:0px; width:100%; height:16px; background-color:transparent; background:url(" + 
                    CoreUtils.resolveResourceURL(facesContext, "/xmlhttp/css/xp/css-images/spacer.gif") +
                    "); margin:0; padding:0; font-size: 0; line-height: 0;");
	            button.setAttribute(HTML.TYPE_ATTR, "submit");
				button.setAttribute(HTML.NAME_ATTR, uiComponent.getClientId(facesContext) + "Expanded");
				button.setAttribute(HTML.VALUE_ATTR, panelCollapsible.isExpanded() ? "true" : "false" ); 				
	            div.appendChild(button);	               				
			} else {
	            Element hiddenField = domContext.createElement(HTML.INPUT_ELEM);
	            hiddenField.setAttribute(HTML.NAME_ATTR, uiComponent.getClientId(facesContext) + "Expanded");
	            hiddenField.setAttribute(HTML.TYPE_ATTR, "hidden");
	            root.appendChild(hiddenField);
	            UIComponent form = findForm(uiComponent);
	            if (form == null) {
	                throw new FacesException("PanelCollapsible must be contained within a form");
	            }
	            if (panelCollapsible.hasInitiatedSubmit(facesContext)) {
	                JavascriptContext.addJavascriptCall(facesContext,
	                        "document.getElementById('" + uiComponent.getClientId(facesContext) + "')." +
	                                "getElementsByTagName('a')[0].focus();");
	            }
	            String hiddenValue = "document.forms['" + form.getClientId(facesContext) + "']" +
	                    "['" + uiComponent.getClientId(facesContext) + "Expanded" + "'].value='";
	            header.setAttribute(HTML.ONCLICK_ATTR,
	                    hiddenValue +
	                            panelCollapsible.isExpanded() + "'; " +
	                            "iceSubmit(document.forms['" + form.getClientId(facesContext) + "'],this,event);" +
	                            hiddenValue + "'; return false;");
	            header.setAttribute(HTML.ID_ATTR, uiComponent.getClientId(facesContext) + "hdr");
	            Element div = domContext.createElement(HTML.DIV_ELEM);
	            div.setAttribute(HTML.STYLE_ATTR, "padding:0px;background-image:none;width:100%;");
	            header.appendChild(div);
	            //this anchor should be known by the component only, so we are defining style to the component level
	            Element anchor = domContext.createElement(HTML.ANCHOR_ELEM);
	            anchor.setAttribute(HTML.ONFOCUS_ATTR, "Ice.pnlClpFocus(this);");
	            anchor.setAttribute(HTML.ONBLUR_ATTR, "Ice.pnlClpBlur(this);");
	            anchor.setAttribute(HTML.STYLE_ATTR, "float:left;border:none;margin:0px;");
	            anchor.setAttribute(HTML.HREF_ATTR, "#");
	            anchor.appendChild(domContext.createTextNodeUnescaped("<img src=\"" + CoreUtils.resolveResourceURL(facesContext,
	                    "/xmlhttp/css/xp/css-images/spacer.gif") + "\"/>"));
	            div.appendChild(anchor);
			}
		}
    }


    public void encodeChildren(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, null);
        PanelCollapsible panelCollapsible = (PanelCollapsible) uiComponent;
        DOMContext domContext = DOMContext.getDOMContext(facesContext, uiComponent);

        //if headerfacet found, get the header div and render all its children
        UIComponent headerFacet = uiComponent.getFacet("header");
        if (headerFacet != null) {
            Element header = null;
            if (panelCollapsible.isToggleOnClick() &&
                    !panelCollapsible.isDisabled()) {
                header = (Element) domContext.getRootNode().getFirstChild().getFirstChild();
            } else {
                header = (Element) domContext.getRootNode().getFirstChild();
            }
            domContext.setCursorParent(header);

			if (CoreComponentUtils.isJavaScriptDisabled(facesContext)) {
				Application application = facesContext.getApplication();
				HtmlPanelGroup headerWrapper = (HtmlPanelGroup) application.createComponent(HtmlPanelGroup.COMPONENT_TYPE);
				headerWrapper.getAttributes().put(HTML.STYLE_ATTR, "background-color:transparent; height:26px; v-align:middle; position:absolute; top:0px; left:0px;");
				headerWrapper.getChildren().add(headerFacet);
				headerFacet = headerWrapper;
			}
			
            CustomComponentUtils.renderChild(facesContext, headerFacet);
        }

        //if expanded get the content div and render all its children 
        if (panelCollapsible.isExpanded()) {
            Element contents = (Element) domContext.getRootNode().getFirstChild().getNextSibling();
            domContext.setCursorParent(contents);
            if (uiComponent.getChildCount() > 0) {
                Iterator children = uiComponent.getChildren().iterator();
                while (children.hasNext()) {
                    UIComponent nextChild = (UIComponent) children.next();
                    if (nextChild.isRendered()) {
                        encodeParentAndChildren(facesContext, nextChild);
                    }
                }
            }
        }
        domContext.stepOver();
    }
}

