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
package org.icefaces.component.tab;

import javax.servlet.jsp.JspException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.webapp.UIComponentTag;
import com.icesoft.util.pooling.ELPool;

/**
 * <p>Auto-generated component tag class.
 * Do <strong>NOT</strong> modify; all changes
 * <strong>will</strong> be lost!</p>
 */

public class TabsTag extends UIComponentTag {

    /**
     * <p>Return the requested component type.</p>
     */
    public String getComponentType() {
        return "com.icesoft.faces.Tabs";
    }

    /**
     * <p>Return the requested renderer type.</p>
     */
    public String getRendererType() {
        return null;
    }

    /**
     * <p>Release any allocated tag handler attributes.</p>
     */
    public void release() {
        super.release();
        value = null;
        var = null;
        rows = null;
        first = null;        
    }

    /**
     * <p>Transfer tag attributes to component properties.</p>
     */
    protected void setProperties(UIComponent _component) {
        try{
            super.setProperties(_component);
            if (value != null) {
                if (isValueReference(value)) {
                    ValueBinding _vb = getFacesContext().getApplication().createValueBinding(ELPool.get(value));
                    _component.setValueBinding("value", _vb);
                } else {
                    _component.getAttributes().put("value", value);
                }
            }
            if (var != null) {
                if (isValueReference(var)) {
                    ValueBinding _vb = getFacesContext().getApplication().createValueBinding(ELPool.get(var));
                    _component.setValueBinding("var", _vb);
                } else {
                    _component.getAttributes().put("var", var);
                }
            }
            
            if (rows != null) {
                if (isValueReference(rows)) {
                    ValueBinding _vb = getFacesContext().getApplication().createValueBinding(ELPool.get(rows));
                    _component.setValueBinding("rows", _vb);
                } else {
                    _component.getAttributes().put("rows", new Integer(rows));
                }
            }
            if (first != null) {
                if (isValueReference(first)) {
                    ValueBinding _vb = getFacesContext().getApplication().createValueBinding(ELPool.get(first));
                    _component.setValueBinding("first", _vb);
                } else {
                    _component.getAttributes().put("first", first);
                }
            }             
        }catch(Exception e1){e1.printStackTrace();throw new RuntimeException(e1);}
    }

    // renderedOnUserRole
    private String value = null;
    public void setValue(String value) {
        this.value = value;
    }

    private String var = null;
    public void setVar(String var) {
        this.var = var;
    }
    
    private String rows = null;
    public void setRows(String rows) {
        this.rows = rows;
    }
    
    private String first = null;
    public void setFirst(String first) {
        this.first = first;
    }        
    private static Class actionArgs[] = new Class[0];
    private static Class actionListenerArgs[] = { javax.faces.event.ActionEvent.class };
    private static Class validatorArgs[] = { FacesContext.class, UIComponent.class, Object.class };
    private static Class valueChangeListenerArgs[] = { javax.faces.event.ValueChangeEvent.class };


    // 
    // Methods From TagSupport
    // 

    public int doStartTag() throws JspException {
        int rc = 0;
        try {
            rc = super.doStartTag();
        } catch (JspException e) {
            throw e;
        } catch (Throwable t) {
            throw new JspException(t);
        }
        return rc;
    }


    public int doEndTag() throws JspException {
        int rc = 0;
        try {
            rc = super.doEndTag();
        } catch (JspException e) {
            throw e;
        } catch (Throwable t) {
            throw new JspException(t);
        }
        return rc;
    }

}
