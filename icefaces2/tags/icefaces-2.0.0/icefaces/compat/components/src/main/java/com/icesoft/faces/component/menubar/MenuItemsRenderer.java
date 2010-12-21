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

package com.icesoft.faces.component.menubar;

import com.icesoft.faces.renderkit.dom_html_basic.DomBasicRenderer;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.io.IOException;
import java.util.List;

public class MenuItemsRenderer extends DomBasicRenderer {

    public boolean getRendersChildren() {
        return true;
    }

    public void encodeChildren(FacesContext context, UIComponent component)
            throws IOException {
        if(!component.isRendered())
            return;
        MenuItems menuItems = (MenuItems) component;
        List children = menuItems.prepareChildren();
        renderRecursive(context, children);
    }

    private void renderRecursive(FacesContext context, List children)
            throws IOException {
        if(children == null)
            return;
        for (int i = 0; i < children.size(); i++) {
            UIComponent nextChildMenuNode = (UIComponent) children.get(i);
            if(nextChildMenuNode.isRendered())
                encodeParentAndChildren(context, nextChildMenuNode);
        }
    }
}
