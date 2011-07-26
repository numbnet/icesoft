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

/* This code was reimplemented to eliminate dependency on Sun RI code */

package com.icesoft.faces.component.ext.taglib;

import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;

public class MethodBindingString extends javax.faces.el.MethodBinding
        implements StateHolder {
    private String stringValue;
    private boolean transient_;

    public MethodBindingString() {
        super();
    }

    /**
     * create a method binding for the string
     *
     * @param stringValue
     */
    public MethodBindingString(String stringValue) {
        this.stringValue = stringValue;
    }

    /**
     * no method invocation required for the string
     *
     * @return stringValue
     */
    public Object invoke(FacesContext facesContext, Object[] params) {
        return stringValue;
    }

    public Class getType(FacesContext facesContext) {
        return String.class;
    }

    public Object saveState(FacesContext facesContext) {
        return stringValue;
    }

    public void restoreState(FacesContext facesContext, Object state) {
        stringValue = (String) state;
    }

    public boolean isTransient() {
        return transient_;
    }

    public void setTransient(boolean transient_) {
        this.transient_ = transient_;
    }

}
