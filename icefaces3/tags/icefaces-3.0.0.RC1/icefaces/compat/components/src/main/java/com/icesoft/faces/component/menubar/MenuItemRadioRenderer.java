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

import com.icesoft.faces.component.InvalidComponentTypeException;

import javax.faces.component.UIComponent;


public class MenuItemRadioRenderer extends MenuItemRenderer {

    protected String getIcon(UIComponent component) {
        String iconImage = null;
        if (component instanceof MenuItemRadio) {
            if (((MenuItemRadio) component).isSelected()) {
                iconImage = ((MenuItemRadio) component).getSelectedIcon();
            } else {
                iconImage = ((MenuItemRadio) component).getUnselectedIcon();
            }
        } else {
            throw new InvalidComponentTypeException("MenuItemRadio expected");
        }
        return iconImage;
    }

    protected String getTextValue(UIComponent component) {
        String text = null;
        if (component instanceof MenuItemRadio) {
            text = ((MenuItem) component).getValue().toString();
        } else {
            throw new InvalidComponentTypeException("MenuItemRadio expected");
        }
        return text;
    }
}
