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

package com.icesoft.faces.component.selectinputtext;

import javax.faces.event.ValueChangeEvent;
import javax.faces.event.FacesListener;
import javax.faces.component.UIComponent;

/**
 * TextChangeEvent is broadcast in the APPLY_REQUEST_VALUES phase via the
 * SelectInputText's textChangeListener MethodBinding, containing the
 * SelectInputText's submittedValue as its new value.
 * 
 * It's purpose is to notify the application that the user has typed in a
 * text fragment into the SelectInputText's text input field, allowing for
 * the application to refine its selection list which will popup.
 * 
 * In the case of converted and validated values, which require a complete
 * input of text, like with a Date, the textChangeListener may call
 * FacesContext.getCurrentInstance().renderResponse() to forstall the
 * doomed validation.
 * 
 * @author Mark Collette
 * @since ICEfaces 1.7
 */
public class TextChangeEvent extends ValueChangeEvent {
    public TextChangeEvent(UIComponent comp, Object oldValue, Object newValue) {
        super(comp, oldValue, newValue);
    }

    public boolean isAppropriateListener(FacesListener facesListener) {
        return false;
    }

    public void processListener(FacesListener facesListener) {
    }
}
