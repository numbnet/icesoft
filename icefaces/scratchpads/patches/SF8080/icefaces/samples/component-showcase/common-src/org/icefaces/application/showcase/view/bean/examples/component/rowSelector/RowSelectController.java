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
package org.icefaces.application.showcase.view.bean.examples.component.rowSelector;

import org.icefaces.application.showcase.view.bean.examples.component.dataTable.DataTableBase;
import org.icefaces.application.showcase.model.entity.Employee;

import java.util.ArrayList;

import com.icesoft.faces.component.ext.RowSelectorEvent;

import javax.faces.event.ValueChangeEvent;

/**
 * <p>The RowSelectController is responsible for handling the RowSelectorEvent
 * that is fire from the rowSelector component.  This simple class keeps
 * a list of selected.  A user can also change the selection more of the
 * row Selector component. </p>
 *
 * @since 1.7
 */
public class RowSelectController extends DataTableBase {

    // list of selected employees
    private ArrayList selectedEmployees;

    // flat to indicate multiselect row enabled.
    private boolean multiRowSelect;

    public RowSelectController() {
        selectedEmployees = new ArrayList();
    }

    /**
     * SelectionListener bound to the ice:rowSelector component.  Called
     * when a row is selected in the UI.
     *
     * @param event from the ice:rowSelector component
     */
    public void rowSelectionListener(RowSelectorEvent event) {
        // clear our list, so that we can build a new one
        selectedEmployees.clear();

        // build the new selected list
        Employee employee;
        for(int i = 0, max = employees.size(); i < max; i++){
        employee = (Employee)employees.get(i);
            if (employee.isSelected()) {
                selectedEmployees.add(employee);
            }
        }
    }

    /**
     * Clear the selection list if going from multi select to single select.
     *
     * @param event jsf action event.
     */
    public void rowSelectionListener(ValueChangeEvent event) {
        // if multi select then want to make sure we clear the selected states
        if (!((Boolean) event.getNewValue()).booleanValue()) {
            selectedEmployees.clear();

            // build the new selected list
            Employee employee;
            for(int i = 0, max = employees.size(); i < max; i++){
                employee = (Employee)employees.get(i);
                employee.setSelected(false);
            }
        }
    }

    public ArrayList getSelectedEmployees() {
        return selectedEmployees;
    }

    public void setSelectedEmployees(ArrayList selectedEmployees) {
        this.selectedEmployees = selectedEmployees;
    }

    public boolean isMultiRowSelect() {
        return multiRowSelect;
    }

    /**
     * Sets the selection more of the rowSelector.
     *
     * @param multiRowSelect true indicates multi-row select and false indicates
     *                       single row selection mode.
     */
    public void setMultiRowSelect(boolean multiRowSelect) {
        this.multiRowSelect = multiRowSelect;
    }
}
