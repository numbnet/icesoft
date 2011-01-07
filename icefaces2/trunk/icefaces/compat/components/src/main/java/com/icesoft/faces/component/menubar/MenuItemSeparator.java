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

import com.icesoft.faces.component.CSS_DEFAULT;
import com.icesoft.faces.component.ext.taglib.Util;

import javax.faces.context.FacesContext;


/**
 * MenuItemSeparator is a JSF component class representing an ICEfaces
 * menuItemSeparator.
 * <p/>
 * This ccomponent extends the ICEfaces MenuItemBase component.
 * <p/>
 * By default the MenuItemSeparator is rendered by the "com.icesoft.faces.View"
 * renderer type.
 *
 * @author Chris Brown
 * @version beta 1.0
 */
public class MenuItemSeparator extends MenuItemBase {

    public MenuItemSeparator() {
    }

    /* (non-Javadoc)
    * @see javax.faces.component.UIComponent#getFamily()
    */
    public String getFamily() {
        return "com.icesoft.faces.MenuNodeSeparator";
    }

    /* (non-Javadoc)
     * @see javax.faces.component.UIComponent#getRendererType()
     */
    public String getRendererType() {
        return "com.icesoft.faces.View";
    }
    
    private String styleClass;
    /**
     * <p>Set the value of the <code>styleClass</code> property.</p>
     *
     * @param styleClass
     */
    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    /**
     * <p>Return the value of the <code>styleClass</code> property.</p>
     *
     * @return String styleClass
     */
    public String getStyleClass() {
        return Util.getQualifiedStyleClass(this, 
                styleClass, 
                CSS_DEFAULT.MENU_ITEM_SEPARATOR_STYLE, 
                "styleClass");
    }

    public Object saveState(FacesContext context) {
        Object values[] = new Object[2];
        values[0] = super.saveState(context);
        values[1] = styleClass;
        return values;
    }

    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        super.restoreState(context, values[0]);
        styleClass = (String) values[1];
    }
}
