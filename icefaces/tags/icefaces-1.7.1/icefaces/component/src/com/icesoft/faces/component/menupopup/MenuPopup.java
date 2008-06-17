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
 * 2004-2006 ICEsoft Technologies Canada, Corp. All Rights Reserved.
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
 *
 */

package com.icesoft.faces.component.menupopup;

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ContextActionEvent;
import com.icesoft.faces.component.menubar.MenuBar;
import com.icesoft.faces.component.ext.taglib.Util;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.ActionEvent;


/**
 * MenuPopup is a JSF component class representing the ICEfaces context menu popup.
 * <p>The menuPopup component extends the menuBar, to provide a menu that is
 * displayed when the user right-mouse clicks, or context-clicks, on another
 * component, which knows to invoke the menuPopup because has specified to do so
 * via its menuPopup attribute, which references the menuPopup's id.
 * <p/>
 * <p>
 * The menuPopup behaves, as is defined, the same as menuBar, except for some
 * minor differences:
 * </p>
 * 1. menuPopup does not have a visible attribute, because that would interfere
 *    with the mechanism for dynamically displaying the popup menu
 * 2. menuPopup does not have an orientation attribute, because it is always
 *    shown vertically
 * 3. menuPopup's default style class is iceMnuPop, instead of menuBar's iceMnuBar
 *
 * @author Mark Collette
 */
public class MenuPopup extends MenuBar {
    /**
     * default no args constructor
     */
    public MenuPopup() {
        super();
    }
    
    /**
     * <p>Return the value of the <code>COMPONENT_FAMILY</code> of this
     * component.</p>
     *
     * @return String component family
     */
    public String getFamily() {
        return "com.icesoft.faces.MenuPopup";
    }

    /**
     * @return String component type
     */
    public String getComponentType() {
        return "com.icesoft.faces.MenuPopup";
    }

    /* (non-Javadoc)
     * @see javax.faces.component.UIComponentBase#getRendererType()
     */
    public String getRendererType() {
        return "com.icesoft.faces.View";
    }

    /**
     * <p>Orientation is fixed to vertical</p>
     *
     * @return String orientation
     */
    public String getOrientation() {
        return ORIENTATION_VERTICAL;
    }

    /**
     * <p>Orientation is fixed to vertical</p>
     *
     * @param orient
     */
    public void setOrientation(String orient) {
    }
    
    public String getTopSubMenuStyleClass() {
        return Util.getQualifiedStyleClass(this, 
                                CSS_DEFAULT.MENU_BAR_TOP_SUB_MENU_STYLE);
    }
    
    public String getComponentRootStyle() {
        return CSS_DEFAULT.MENU_POPUP_STYLE;
    }
    
    public void queueEvent(FacesEvent e) {
        if(e instanceof ActionEvent) {
            UIComponent contextTarget = (UIComponent) getAttributes().get("contextTarget");
            Object contextValue = getAttributes().get("contextValue");
//System.out.println("MenuPopup.queueEvent()  e: " + e);
//System.out.println("MenuPopup.queueEvent()  contextTarget: " + contextTarget);
//System.out.println("MenuPopup.queueEvent()  contextValue: " + contextValue);
            getAttributes().remove("contextTarget");
            getAttributes().remove("contextValue");
            e = new ContextActionEvent(e.getComponent(), contextTarget, contextValue);
        }
        super.queueEvent(e);
    }
}

