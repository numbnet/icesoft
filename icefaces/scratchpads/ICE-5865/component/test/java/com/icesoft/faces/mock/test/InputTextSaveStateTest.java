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
 
package com.icesoft.faces.mock.test;

import com.icesoft.faces.mock.test.container.MockTestCase;
import com.icesoft.faces.component.ext.HtmlForm;
import com.icesoft.faces.component.ext.HtmlInputText;
import com.sun.faces.application.StateManagerImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.component.UIComponent;
import javax.faces.component.UIViewRoot;
import junit.framework.Test;
import junit.framework.TestSuite;

public class InputTextSaveStateTest extends MockTestCase {

    public static Test suite() {
        return new TestSuite(InputTextSaveStateTest.class);
    }

    public void testSaveState() {

        UIViewRoot uiViewRoot = getViewHandler().createView(getFacesContext(), this.getClass().getName()+"_view_id");
        getFacesContext().setViewRoot(uiViewRoot);

        HtmlForm form = new HtmlForm();
        form.setPartialSubmit(true);
        HtmlInputText inputText = new HtmlInputText();
        inputText.setFocus(Boolean.TRUE);
        inputText.setDisabled(Boolean.TRUE);

        form.getChildren().add(inputText);
        uiViewRoot.getChildren().add(form);

        //save state
        Object state = uiViewRoot.processSaveState(getFacesContext());

        StateManagerImpl stateManager = new StateManagerImpl();
        //MockStateManager stateManager = (MockStateManager)getFacesContext().getApplication().getStateManager();
        List treeList = new ArrayList();
        invokePrivateMethod("captureChild",
                new Class[]{List.class, Integer.TYPE, UIComponent.class},
                new Object[]{treeList, 0, uiViewRoot},
                StateManagerImpl.class,
                stateManager);

        //tree
        Object[] comps = treeList.toArray();

        UIViewRoot restoreViewRoot = (UIViewRoot) invokePrivateMethod("restoreTree",
                new Class[]{Object[].class},
                new Object[]{comps}, StateManagerImpl.class,
                stateManager);
        restoreViewRoot.processRestoreState(getFacesContext(), state);

        UIComponent firstForm = (UIComponent) restoreViewRoot.getChildren().get(0);
        UIComponent first = (UIComponent) firstForm.getChildren().get(0);
        myBooleanAttribute(first, "focus", Boolean.TRUE);
        myBooleanAttribute(first, "disabled", Boolean.TRUE);

    }
    
    private void myBooleanAttribute(UIComponent first, String attributeName, boolean expectedValue) {

        Object value = first.getAttributes().get(attributeName);
        if (value != null) {
            Boolean booleanValue = (Boolean) value;
            String message = " component=" + first + " property " + attributeName + " value=" + booleanValue.toString()+" expected value="+ expectedValue;
            Logger.getLogger(InputTextSaveStateTest.class.getName()).log(Level.INFO, message);
        } else {
            String message = " component=" + first + " property " + attributeName + "=null";
            Logger.getLogger(InputTextSaveStateTest.class.getName()).log(Level.INFO, message);
        }
    }
}
