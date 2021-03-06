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

package com.icesoft.faces.component.ext;

import java.util.List;

import javax.faces.component.UIComponent;
import javax.faces.event.FacesEvent;
import javax.faces.event.FacesListener;
import javax.faces.event.PhaseId;

/**
 * Created by IntelliJ IDEA. User: rmayhew Date: Sep 5, 2006 Time: 2:29:29 PM To
 * change this template use File | Settings | File Templates.
 */
public class RowSelectorEvent extends FacesEvent {

    private int row;
    private boolean selected;
    private List selectedRows;
    public RowSelectorEvent(UIComponent uiComponent, int row,
                            boolean selected) {
        super(uiComponent);
        this.row = row;
        this.selected = selected;
    }

    public boolean isAppropriateListener(FacesListener facesListener) {
        return false;
    }

    public void processListener(FacesListener facesListener) {
    }

    public int getRow() {
        return row;
    }

    public boolean isSelected() {
        return selected;
    }

    public List getSelectedRows() {
        return selectedRows;
    }

    void setSelectedRows(List selectedRows) {
        this.selectedRows = selectedRows;
    }
}
