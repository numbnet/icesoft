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

package com.icesoft.faces.component.outputresource;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.w3c.dom.Element;

import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicInputRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.renderkit.dom_html_basic.PassThruAttributeRenderer;

public class OutputResourceRenderer extends DomBasicInputRenderer {

	protected static final String CONTAINER_DIV_SUFFIX = "_cont";

	public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
			throws IOException {

		String clientId = uiComponent.getClientId(facesContext);
		OutputResource outputResource = (OutputResource) uiComponent;
		if( outputResource.getResource() != null ){
			DOMContext domContext = DOMContext.attachDOMContext(facesContext,
					uiComponent);
			if (!domContext.isInitialized()) {
				domContext.createRootElement(HTML.DIV_ELEM);
			}

			Element root = (Element) domContext.getRootNode();
			root.setAttribute(HTML.ID_ATTR, uiComponent.getClientId(facesContext)
					+ CONTAINER_DIV_SUFFIX);
			domContext.setCursorParent(root);
			
			String style = outputResource.getStyle();
			String styleClass = outputResource.getStyleClass();
			
			Element resource = null;
		        		
			if( OutputResource.TYPE_BUTTON.equals(outputResource.getType())){
				resource = domContext.createElement(HTML.INPUT_ELEM);
				resource.setAttribute(HTML.TYPE_ATTR, "button");
				resource.setAttribute(HTML.VALUE_ATTR, outputResource.getLabel());
				resource.setAttribute(HTML.ONCLICK_ATTR, "window.open('" + outputResource.getPath() + "');");
			}
			else{
				resource = domContext.createElement(HTML.ANCHOR_ELEM);
				resource.setAttribute(HTML.HREF_ATTR, outputResource.getPath());
                PassThruAttributeRenderer.renderNonBooleanHtmlAttributes(uiComponent, resource, new String[]{"target"});

				if( outputResource.getImage() != null ){
					Element img = domContext.createElement(HTML.IMG_ELEM);
					String image = outputResource.getImage();
					if (image != null) {
    					img.setAttribute(HTML.SRC_ATTR, facesContext.
    					        getApplication().getViewHandler()
    	                        .getResourceURL(facesContext, image));
					}
					resource.appendChild(img);
					img.setAttribute(HTML.ALT_ATTR, outputResource.getLabel());
				}
				else{
					resource.appendChild(domContext.createTextNode(outputResource
							.getLabel()));
				}
				
			}
			resource.setAttribute(HTML.ID_ATTR, clientId);
			root.appendChild(resource);
			if( style != null ){
				resource.setAttribute(HTML.STYLE_ATTR, style);
			}
			if (styleClass != null) {
				resource.setAttribute("class", styleClass);
	        }
			
			domContext.stepOver();
		}
		
	}

}
