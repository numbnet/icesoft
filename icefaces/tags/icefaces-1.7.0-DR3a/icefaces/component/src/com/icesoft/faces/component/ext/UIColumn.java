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

package com.icesoft.faces.component.ext;

import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;

import com.icesoft.faces.component.ext.taglib.Util;

// We do not need this class, but Sun Studio Creator requires it.

public class UIColumn extends javax.faces.component.UIColumn {
    public static final String COMPONENT_TYPE = "com.icesoft.faces.Column";


    /**
     * <p>Return the family for this component.</p>
     */
    public String getFamily() {
        return "javax.faces.Column";
    }

    // binding
    private String binding = null;

    public String getBinding() {
        return this.binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    // id
    private String id = null;

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }


    private String renderedOnUserRole = null;
    /**
     * <p>Set the value of the <code>renderedOnUserRole</code> property.</p>
     */
    public void setRenderedOnUserRole(String renderedOnUserRole) {
        this.renderedOnUserRole = renderedOnUserRole;
    }

    /**
     * <p>Return the value of the <code>renderedOnUserRole</code> property.</p>
     */
    public String getRenderedOnUserRole() {
        if (renderedOnUserRole != null) {
            return renderedOnUserRole;
        }
        ValueBinding vb = getValueBinding("renderedOnUserRole");
        return vb != null ? (String) vb.getValue(getFacesContext()) : null;
    }
    
    /**
     * <p>Return the value of the <code>rendered</code> property.</p>
     */
    public boolean isRendered() {
        if (!Util.isRenderedOnUserRole(this)) {
            return false;
        }
        return super.isRendered();
    }
    
    /**
     * <p>Restore the state of this component.</p>
     */
    public void restoreState(FacesContext _context, Object _state) {
        Object _values[] = (Object[]) _state;
        super.restoreState(_context, _values[0]);
        this.binding = (String) _values[1];
        this.id = (String) _values[2];
        this.renderedOnUserRole = (String) _values[3];
    }

    /**
     * <p>Save the state of this component.</p>
     */
    public Object saveState(FacesContext _context) {
        Object _values[] = new Object[4];
        _values[0] = super.saveState(_context);
        _values[1] = this.binding;
        _values[2] = this.id;
        _values[3] = this.renderedOnUserRole;
        return _values;
    }

    /**
     * The menuContext attribute is a write-only ValueBinding for receiving
     *  contextual information on which component was clicked on, and
     *  summoned the menuPopup.
     * As such, this method serves no purpose, beyond facilitating IDE's code
     *  auto-completion, and shouldn't actually be called by any Java code.
     *  
     * @return Nothing
     */
    public Object getMenuContext() {
        return null;
    }
    
    /**
     * The menuContext attribute is a write-only ValueBinding for receiving
     *  contextual information on which component was clicked on, and
     *  summoned the menuPopup.
     * As such, this method serves no purpose, beyond facilitating IDE's code
     *  auto-completion, and shouldn't actually be called by any Java code.
     *  
     * @param param Not used
     */
    public void setMenuContext(Object param) {
    }
}
