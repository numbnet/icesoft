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

package com.icesoft.faces.component.menubar;

import com.icesoft.faces.context.DOMContext;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;

public class MenuItemSeparatorRenderer extends MenuItemRendererBase {

    public void encodeBegin(FacesContext facesContext, UIComponent uiComponent)
            throws IOException {
        validateParameters(facesContext, uiComponent, MenuItemBase.class);

        DOMContext domContext =
                DOMContext.getDOMContext(facesContext, uiComponent);
        if (!domContext.isInitialized()) {
            Element root = domContext.createRootElement(HTML.DIV_ELEM);
        }
        Element root = (Element) domContext.getRootNode();

        Node hr = null;
        hr = root.getFirstChild();
        if (hr == null) {
            hr = domContext.createElement("hr");
            root.appendChild(hr);
        }
    }
}
