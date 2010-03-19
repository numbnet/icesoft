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

package com.icesoft.faces.mock.test.data;

import javax.faces.el.MethodBinding;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodNotFoundException;
import javax.faces.context.FacesContext;
import javax.faces.component.StateHolder;
import java.io.Serializable;

public class MockMethodBinding
    extends MethodBinding implements StateHolder, Serializable {
    
    private String expression;
    private boolean transientFlag;
    
    public MockMethodBinding() {
        super();
        transientFlag = false;
    }
    
    public MockMethodBinding(String expression) {
        super();
        this.expression = expression;
    }
    
    public Object invoke(FacesContext facesContext, Object[] objects) throws EvaluationException, MethodNotFoundException {
        return null;
    }

    public Class getType(FacesContext facesContext) throws MethodNotFoundException {
        return Object.class;
    }
    
    public java.lang.String getExpressionString() {
        return expression;
    }
    
    public boolean equals(Object obj) {
        if (!(obj instanceof MockMethodBinding)) {
            return false;
        }
        MockMethodBinding mmb = (MockMethodBinding) obj;
        if (expression == null && mmb.expression == null) {
            return true;
        }
        if (expression == null && mmb.expression != null) {
            return false;
        }
        if (expression != null && mmb.expression == null) {
            return false;
        }
        return expression.equals(mmb.expression);
    }
    
    public int hashCode() {
        if (expression == null) {
            return 0;
        }
        return expression.hashCode();
    }
    
    public String toString() {
        return super.toString() + "{expression: "+expression+"}";
    }
    
    public boolean isTransient() {
        return transientFlag;
    }
    
    public void setTransient(boolean t) {
        transientFlag = t;
    }
    
    public Object saveState(FacesContext context){
        Object result = null;
        if (!transientFlag) {
            result = expression;
        }
        return result;
    }

    public void restoreState(FacesContext context, Object state) {
        if (null == state) {
            return;
        }
        expression = (String) state;
    }
}
