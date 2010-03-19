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
 
package com.icesoft.faces.mock.test.container;

import java.io.IOException;
import javax.faces.application.StateManager;
import javax.faces.component.UIViewRoot;
import javax.faces.context.FacesContext;

public class MockStateManager extends StateManager{

    @Override
    protected Object getComponentStateToSave(FacesContext context) {
        return super.getComponentStateToSave(context);
    }

    @Override
    protected Object getTreeStructureToSave(FacesContext context) {
        return super.getTreeStructureToSave(context);
    }

    @Override
    public boolean isSavingStateInClient(FacesContext context) {
        return super.isSavingStateInClient(context);
    }

    @Override
    protected void restoreComponentState(FacesContext context, UIViewRoot viewRoot, String renderKitId) {
        super.restoreComponentState(context, viewRoot, renderKitId);
    }

    @Override
    protected UIViewRoot restoreTreeStructure(FacesContext context, String viewId, String renderKitId) {
        return super.restoreTreeStructure(context, viewId, renderKitId);
    }

    @Override
    public SerializedView saveSerializedView(FacesContext context) {
        return super.saveSerializedView(context);
    }

    @Override
    public Object saveView(FacesContext context) {
        return super.saveView(context);
    }

    @Override
    public void writeState(FacesContext context, Object state) throws IOException {
        super.writeState(context, state);
    }

    @Override
    public void writeState(FacesContext context, SerializedView state) throws IOException {
        super.writeState(context, state);
    }

    @Override
    public UIViewRoot restoreView(FacesContext context, String viewId, String renderKitId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
