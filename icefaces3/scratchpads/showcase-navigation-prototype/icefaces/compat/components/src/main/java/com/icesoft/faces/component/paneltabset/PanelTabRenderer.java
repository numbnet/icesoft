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

package com.icesoft.faces.component.paneltabset;

import com.icesoft.faces.renderkit.dom_html_basic.GroupRenderer;
import com.icesoft.faces.renderkit.dom_html_basic.HTML;
import com.icesoft.faces.context.DOMContext;
import org.w3c.dom.Element;

/**
 * <p>PanelTabRenderer extends GroupRenderer and is responsible for
 * rendering the PanelTab's child components. The rendering of the
 * actual tab is done by PanelTabSetRenderer.</p>
 */
public class PanelTabRenderer extends GroupRenderer {
    
    protected Element createRootElement(DOMContext domContext) {
        // The SPAN that our superclass renders causes problems in
        //  some browsers, so we render a DIV here, to avoid that
        return domContext.createElement(HTML.DIV_ELEM);
    }
    
    protected void renderStyleAndStyleClass(
        String style, String styleClass, Element root)
    {
        // Do not render out the style or styleClass attributes to root,
        //  since PanelTab's styling is not for it as a container, but
        //  instead for the actual tabs that you click on.
    }
}
