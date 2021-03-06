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

package com.icesoft.faces.component;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;

public class DisplayEvent extends FacesEvent{

    private static final long serialVersionUID = 0L;
    UIComponent target = null;
    Object contextValue = null;
    boolean visible = false;
    
    public DisplayEvent(UIComponent component) {
        super(component);
    }

    public DisplayEvent(UIComponent component, UIComponent target) {
        super(component);
        this.target = target;
    }
    
    public DisplayEvent(UIComponent component, UIComponent target, Object contextValue) {
        super(component);
        this.target = target;
        this.contextValue = contextValue;
    }

    public DisplayEvent(UIComponent component, 
                        UIComponent target, 
                        Object contextValue,
                        boolean visible) {
        super(component);
        this.target = target;
        this.contextValue = contextValue;
        this.visible = visible;
    }
    
    public UIComponent getTarget() {
        return target;
    }

    public Object getContextValue() {
        return contextValue;
    }

    public boolean isVisible() {
        return visible;
    }
    
    public boolean isAppropriateListener(FacesListener listener) {
        // TODO Auto-generated method stub
        return false;
    }


    public void processListener(FacesListener listener) {
        // TODO Auto-generated method stub
    }
}
