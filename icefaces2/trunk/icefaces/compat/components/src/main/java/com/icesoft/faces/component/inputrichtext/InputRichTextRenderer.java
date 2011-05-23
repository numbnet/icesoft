/*
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License
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
 * 2004-2011 ICEsoft Technologies Canada, Corp. All Rights Reserved.
 *
 * Contributor(s): _____________________.
 */

package com.icesoft.faces.component.inputrichtext;

import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.DomBasicInputRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.util.pooling.ClientIdPool;
import org.w3c.dom.Element;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class InputRichTextRenderer extends DomBasicInputRenderer {
	
    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        String clientId = uiComponent.getClientId(facesContext);
        InputRichText inputRichText = (InputRichText) uiComponent;
        DOMContext domContext =
                DOMContext.attachDOMContext(facesContext, uiComponent);
        if (!domContext.isInitialized()) {
            Element root = domContext.createRootElement(HTML.DIV_ELEM);
            root.setAttribute(HTML.ID_ATTR, ClientIdPool.get(clientId + "container"));

            root.setAttribute(HTML.CLASS_ATTR, inputRichText.getStyleClass());
            if (inputRichText.getStyle() != null) {
                root.setAttribute(HTML.STYLE_ATTR, inputRichText.getStyle());
            }

            Element textarea= domContext.createElement(HTML.TEXTAREA_ELEM);
            textarea.setAttribute(HTML.NAME_ATTR,  ClientIdPool.get(clientId));
            textarea.setAttribute(HTML.ID_ATTR,  ClientIdPool.get(clientId));
            textarea.setAttribute(HTML.STYLE_ATTR,  "display:none;");
            Object value = inputRichText.getValue();
            if (value != null) {
            	textarea.appendChild(domContext.createTextNode(String.valueOf(value)));
            }
            root.appendChild(textarea);

            Element scrptWrpr = domContext.createElement(HTML.SPAN_ELEM);
            scrptWrpr.setAttribute(HTML.ID_ATTR, clientId+ "scrpt");
            root.getParentNode().appendChild(scrptWrpr);
            Element scrpt = domContext.createElement(HTML.SCRIPT_ELEM);
            scrpt.setAttribute(HTML.TYPE_ATTR, "text/javascript");
            scrpt.appendChild(domContext.createTextNodeUnescaped("renderEditor('"+ ClientIdPool.get(clientId) +"', '"+ inputRichText.getToolbar() +"'," +
            		"'"+ inputRichText.getLanguage()+"')"));
            scrptWrpr.appendChild(scrpt);

            domContext.stepOver();
        }
    }
}
